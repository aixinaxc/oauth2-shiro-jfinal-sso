package com.auth.ctrl;

import com.auth.base.BaseConfig;
import com.auth.base.MyConstant;
import com.auth.util.Empty;
import com.auth.util.Oauth2Util;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.render.JsonRender;
import com.model.AppUser;
import com.model.User;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.servlet.http.HttpServletResponse;

/** OAUTH2认证（现在只支持code码模式和password模式）
 * Created by anxingchen on 2016/11/2.
 */
public class AuthCtrl extends BaseCtrl{

    //private Subject subject = SecurityUtils.getSubject();
    private Cache codeCache = Redis.use("auth");

    /**
     * 验证并跳转到授权页面,授权后返回code码
     */
    public void auth() {
        // 构建OAuth 授权请求
        OAuthAuthzRequest oauthRequest;
        try {

            oauthRequest = new OAuthAuthzRequest(getRequest());
            if(!Oauth2Util.getOauth2Util().is_app_id(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID))){
                authError("无效的客户端client_id或client_secret");
                return;
            }

            // 检查重定向url
            String redirectURI = oauthRequest
                    .getParam(OAuth.OAUTH_REDIRECT_URI);
            if(Empty.isEmpty(redirectURI)){
                authError("回调地址错误");
                return;
            }

            // responseType目前仅支持CODE
            String responseType = oauthRequest
                    .getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if(!responseType.equals(ResponseType.CODE.toString())){
                authError("模式错误");
                return;
            }

            if (!Oauth2Util.getOauth2Util().is_res_user(oauthRequest.getParam(OAuth.OAUTH_USERNAME),oauthRequest.getParam(OAuth.OAUTH_PASSWORD))) {
                setAttr("unAuth",true);
                setAttr("client_id",oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID));
                setAttr("client_secret",oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET));
                setAttr("response_type",oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE));
                setAttr("redirect_uri",oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI));
                render(MyConstant.VIEW_STATIC_ROOT + "auth/auth_login.html");
                return;
            }


            // 生成授权码
            String authorizationCode ;

            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
                    new MD5Generator());
            authorizationCode = oauthIssuerImpl.authorizationCode();//授权码

            //保存code授权码到redis
            codeCache.setex("code:"+authorizationCode,BaseConfig.CODE_EXPIRE, authorizationCode);


            User user = User.dao.getUser(oauthRequest.getParam(OAuth.OAUTH_USERNAME),oauthRequest.getParam(OAuth.OAUTH_PASSWORD));

            if(user==null){
                authError("用户错误");
                return;
            }

            // 进行OAuth响应构建
            // 设置授权码
            OAuthResponse r = OAuthASResponse
                    .authorizationResponse(getRequest(),
                            HttpServletResponse.SC_FOUND)
                    .setCode(authorizationCode)
                    .setExpiresIn(BaseConfig.CODE_EXPIRE+"")
                    .setParam(BaseConfig.OPEN_ID, user.getUserId())
                    .location(redirectURI+BaseConfig.REDIRECT_URI)
                    .setParam("state",oauthRequest.getParam(OAuth.OAUTH_STATE))
                    .buildQueryMessage();

            //返回授权码
            redirect(r.getLocationUri(),true);

        } catch (OAuthSystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 返回access_token
     */
    public void accessToken() {
        // 构建OAuth请求

        // 构建OAuth 授权请求
        //OAuthAuthzRequest oauthRequest;
        OAuthTokenRequest oauthRequest;
        try {
            oauthRequest = new OAuthTokenRequest(getRequest());
            //oauthRequest = new OAuthAuthzRequest(getRequest());
            if(!Oauth2Util.getOauth2Util().is_res_app(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID),oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET))){
                authError("无效的客户端client_id或client_secret");
                return;
            }

            // 检查重定向url
            String redirectURI = oauthRequest
                    .getParam(OAuth.OAUTH_REDIRECT_URI);

            if(Empty.isEmpty(redirectURI)){
                authError("回调地址错误");
                return;
            }

            // 检查验证类型，此处只检查AUTHORIZATION_CODE,PASSWORD
            String userId ;
            if(oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
                    GrantType.AUTHORIZATION_CODE.toString())){
                // 获得连接端发送来的code授权码
                String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);

                //判断code授权码是否存在
                if (Empty.isEmpty(authCode) || Empty.isEmpty(codeCache.get("code:"+authCode))) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("授权码错误").buildJSONMessage();
                    getResponse().setStatus(response.getResponseStatus());
                    renderJson(response.getBody());
                    return;
                }
                codeCache.del("code:"+authCode);
                userId = oauthRequest.getParam(BaseConfig.OPEN_ID);
            }else if(oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
                    GrantType.PASSWORD.toString())){
                //检查用户是否存在
                if (!Oauth2Util.getOauth2Util().is_res_user(oauthRequest.getParam(OAuth.OAUTH_USERNAME),oauthRequest.getParam(OAuth.OAUTH_PASSWORD))) {
                    authError("用户错误");
                    return;
                }
                userId = User.dao.getUser(oauthRequest.getParam(OAuth.OAUTH_USERNAME),oauthRequest.getParam(OAuth.OAUTH_PASSWORD)).getUserId();

            }else{
                authError("授权模式错误");
                return;
            }

            // 生成Access Token
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
                    new MD5Generator());
            String accessToken = oauthIssuerImpl.accessToken();
            String refreshToken = oauthIssuerImpl.refreshToken();

            //缓存accessToken和refreshToken到redis
            codeCache.setex("accessToken:"+accessToken,BaseConfig.REFRESH_TOKEN,accessToken);
            codeCache.setex("refreshToken:"+refreshToken,BaseConfig.REFRESH_TOKEN,refreshToken);

            // 生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setExpiresIn(BaseConfig.REFRESH_TOKEN+"")
                    .setParam(BaseConfig.OPEN_ID, userId)
                    .buildJSONMessage();
            getResponse().setStatus(response.getResponseStatus());
            render(new JsonRender(response.getBody()).forIE());



        } catch (OAuthSystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 更新令牌方法
     */
    public void refreshToken(){
        // 构建OAuth 授权请求
        OAuthTokenRequest oauthRequest;

        try {
            oauthRequest = new OAuthTokenRequest(getRequest());
            if(!Oauth2Util.getOauth2Util().is_res_app(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID),oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET))){
                authError("无效的客户端client_id或client_secret");
                return;
            }

            if (!oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
                    GrantType.REFRESH_TOKEN.toString())) {
                authError("授权模式错误");
                return;
            }

            String refreshTokenold = oauthRequest.getParam(OAuth.OAUTH_REFRESH_TOKEN);

            if(Empty.isEmpty(refreshTokenold) || Empty.isEmpty(codeCache.get("refreshToken:"+refreshTokenold))){
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_REQUEST)
                        .setErrorDescription("更新令牌错误").buildJSONMessage();
                getResponse().setStatus(response.getResponseStatus());
                renderJson(response.getBody());
            }

            // 生成Access Token
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
                    new MD5Generator());
             String accessToken = oauthIssuerImpl.accessToken();
             String refreshToken = oauthIssuerImpl.refreshToken();
            //boolean a = Oauth2.dao.save(accessToken, refreshToken);

            //缓存accessToken和refreshToken到redis
            codeCache.setex("accessToken:"+accessToken,BaseConfig.REFRESH_TOKEN,accessToken);
            codeCache.setex("refreshToken:"+refreshToken,BaseConfig.REFRESH_TOKEN,refreshToken);
            // 生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setExpiresIn(BaseConfig.REFRESH_TOKEN+"")
                    .setParam(BaseConfig.OPEN_ID, oauthRequest.getParam(OAuth.OAUTH_CODE))
                    .buildJSONMessage();
            getResponse().setStatus(response.getResponseStatus());
            renderJson(response.getBody());

        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }
    }


    /**
     * 验证票据信息
     */
    public void ticket(){
        // 构建OAuth 授权请求
        OAuthTokenRequest oauthRequest;
        try {
            oauthRequest = new OAuthTokenRequest(getRequest());
            //检验连接端信息
            if(!Oauth2Util.getOauth2Util().is_res_app(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID),oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET))){
                authError("无效的客户端client_id或client_secret");
                return;
            }

            //检验票据信息
            String refreshToken = oauthRequest.getParam(OAuth.OAUTH_REFRESH_TOKEN);
            String openid = oauthRequest.getParam(BaseConfig.OPEN_ID);
            if(Empty.isEmpty(refreshToken) || Empty.isEmpty(codeCache.get("refreshToken:"+refreshToken))
                    || Empty.isEmpty(openid) || User.dao.findById(openid)==null){
                authError("无效的票据信息");
                return;
            }

            //检验该用户是否授权此系统

            if(!AppUser.dao.isUserApp(openid)){
                //没有授权
                setAttr("unAuth",false);
                setAttr("client_id",oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID));
                setAttr("client_secret",oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET));
                setAttr("refresh_token",oauthRequest.getParam(OAuth.OAUTH_REFRESH_TOKEN));
                setAttr("username",oauthRequest.getParam(BaseConfig.USERNAME));
                setAttr("user_icon",oauthRequest.getParam(BaseConfig.USERICON));
                render(MyConstant.VIEW_STATIC_ROOT + "auth/unauth.html");
                return;
            }

            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setParam(BaseConfig.OPEN_ID, oauthRequest.getParam(OAuth.OAUTH_CODE))
                    .setParam(BaseConfig.USERNAME,oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID))
                    .setParam(BaseConfig.USERICON,oauthRequest.getParam(BaseConfig.USERICON))
                    .buildJSONMessage();
            getResponse().setStatus(response.getResponseStatus());
            renderJson(response.getBody());


        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }

    }

    /**
     * 请求错误返回数据
     * @param errorMsg 错误信息
     */
    private void authError(String errorMsg){
        OAuthResponse oAuthResponse;
        try {
            oAuthResponse = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                    .setErrorDescription(errorMsg)
                    .buildJSONMessage();

            //返回错误页面
            getResponse().setStatus(oAuthResponse.getResponseStatus());
            renderJson(oAuthResponse.getBody());

        } catch (OAuthSystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
