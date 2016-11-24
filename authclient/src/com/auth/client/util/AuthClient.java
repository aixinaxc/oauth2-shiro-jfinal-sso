package com.auth.client.util;


import com.auth.client.pojo.OauthPojo;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.common.OAuth;
import org.apache.oltu.oauth2.client.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.client.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.client.common.message.types.GrantType;
import org.apache.oltu.oauth2.client.common.message.types.ResponseType;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;

/**
 * OAUTH2连接端方法
 * Created by anxingchen on 2016/11/8.
 */
public class AuthClient {

    //配置OAUTH2连接端基本信息
    private static String CLIENT_ID = "";//appid
    private static String CLIENT_SECRET = "";//appkey
    private static String REDIRECT_URI = "";//回调地址

    //配置Oauth2服务端连接地址
    private final static String BASE_URL = "http://192.168.2.103:8080";
    private final static String CODE_URL =  BASE_URL + "/auth/auth";//服务端认证地址
    private final static String ACCESS_TOKEN_URL = BASE_URL + "/auth/accessToken";//获得accessToken地址
    private final static String REFRESH_TOKEN = "/auth/refreshToken";//更新accessToken地址

    //配置sso基本信息
    private final static String USERNAME = "username";//用户名
    private final static String USERICON = "usericon";//用户图标
    private final static String SSO_AUTH_TICKET = BASE_URL + "/auth/ticket";//票据验证地址


    //对外调用字段
    public final static String CODE = "code";//code授权码
    public final static String OPEN_ID = "openId";//保存用户userid




    /**
     * 初始化Oauth，获得appid，appkey,redIrectURI,均为必填
     * @param clientId appid
     * @param clientSecret appkey
     * @param redIrectURI redIrectURI
     */
    public static void init(String clientId,String clientSecret,String redIrectURI){
        CLIENT_ID = clientId;
        CLIENT_SECRET = clientSecret;
        REDIRECT_URI = redIrectURI;
    }



    /**
     *获得验证码
     * @param scope 即权限范围
     * @param state 固定值
     */
    public static String  getcode(String scope,String state){
        // 认证服务器返回授权码,根据授权码,客户端ID,客户端密匙,组装OAuthClientRequest,前往服务端获取访问令牌
        String uri;
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .authorizationLocation(CODE_URL)
                    .setClientId(CLIENT_ID)
                    .setRedirectURI(REDIRECT_URI)
                    .setResponseType(ResponseType.CODE.toString())
                    .setScope(scope)
                    .setState(state)
                    .buildQueryMessage();
            uri = request.getLocationUri();

        } catch (OAuthSystemException e) {
            e.printStackTrace();
            uri = "";
        }
        return uri;

    }


    /**
     * 授权码模式调用方法
     * @param code 授权码
     * @param userId 用户id，即openid
     * @return OauthPojo
     */
    public static OauthPojo getAccessToken(String code,String userId){
        return getAccessToken(code,userId,GrantType.AUTHORIZATION_CODE,"","");
    }

    /**
     * 密码模式
     * @param username 用户名
     * @param pwd 密码
     * @param grantType 模式,只能填GrantType.PASSWORD
     * @return OauthPojo
     */
    public static OauthPojo getAccessToken(String username,String pwd,GrantType grantType){
        return getAccessToken("","",GrantType.PASSWORD,username,pwd);
    }


    /**
     * 通过code,获得accessToken(code码模式和password模式，在密码模式下code添"")
     * @param code  验证码
     * @param userId  用户id
     * @param grantType  验证模式
     * @param username  用户名
     * @param pwd  用户密码
     * @return  OauthPojo 需要做判空处理
     */
    private static OauthPojo getAccessToken(String code,String userId,GrantType grantType,String username,String pwd){

        OauthPojo oauthPojo;
        // 认证服务器返回授权码,根据授权码,客户端ID,客户端密匙,组装OAuthClientRequest,前往服务端获取访问令牌
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        try {
            //组装请求access_token的请求数据
            OAuthClientRequest accessTokenRequest ;
            if(grantType.equals(GrantType.AUTHORIZATION_CODE)){
                if(Empty.isEmpty(code) || Empty.isEmpty(userId)){
                    return null;
                }
                accessTokenRequest = OAuthClientRequest
                        .tokenLocation(ACCESS_TOKEN_URL)
                        .setGrantType(grantType)
                        .setClientId(CLIENT_ID)
                        .setClientSecret(CLIENT_SECRET)
                        .setRedirectURI(REDIRECT_URI)
                        .setParameter(OPEN_ID,userId)
                        .setCode(code)
                        .buildQueryMessage();
            }else if(grantType.equals(GrantType.PASSWORD)){
                if(Empty.isEmpty(username) || Empty.isEmpty(pwd)){
                    return null;
                }
                accessTokenRequest = OAuthClientRequest
                        .tokenLocation(ACCESS_TOKEN_URL)
                        .setGrantType(grantType)
                        .setClientId(CLIENT_ID)
                        .setClientSecret(CLIENT_SECRET)
                        .setRedirectURI(REDIRECT_URI)
                        .setParameter(OPEN_ID,userId)
                        .setUsername(username)
                        .setPassword(pwd)
                        .setCode(code)
                        .buildQueryMessage();
            }else{
                return null;
            }

            //获得access_token的请求数据
            OAuthAccessTokenResponse oAuthResponse;

            oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
            oauthPojo = addOauthPoji(oAuthResponse);



        } catch (OAuthSystemException e) {
            e.printStackTrace();
            oauthPojo = null;
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            oauthPojo = null;
        }
        return oauthPojo;
    }


    /**
     * 通过refreshToken,获得accessToken
     * @param refreshToken  更新令牌
     * @return  OauthPojo 需要做判空处理
     */
    public static OauthPojo refreshToken(String refreshToken){
        if(Empty.isEmpty(refreshToken)){
            return null;
        }
        OauthPojo oauthPojo;
        // 认证服务器返回授权码,根据授权码,客户端ID,客户端密匙,组装OAuthClientRequest,前往服务端获取访问令牌
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        try {
            //组装请求access_token的请求数据
            OAuthClientRequest accessTokenRequest = OAuthClientRequest
                    .tokenLocation(REFRESH_TOKEN)
                    .setGrantType(GrantType.REFRESH_TOKEN)
                    .setClientId(CLIENT_ID)
                    .setClientSecret(CLIENT_SECRET)
                    .setRedirectURI(REDIRECT_URI)
                    .setRefreshToken(refreshToken)
                    .buildQueryMessage();

            //获得access_token的请求数据
            OAuthAccessTokenResponse oAuthResponse;

            oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
            oauthPojo = addOauthPoji(oAuthResponse);



        } catch (OAuthSystemException e) {
            e.printStackTrace();
            oauthPojo = null;
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            oauthPojo = null;
        }
        return oauthPojo;
    }


    /**
     * 通过票据获得验证信息
     * @param oauthPojoCookie sso的验证信息
     * @return OauthPojo
     */
    public static OauthPojo authTicket(OauthPojo oauthPojoCookie){
        if(oauthPojoCookie==null){
            return null;
        }
        OauthPojo oauthPojo;
        // 认证服务器返回授权码,根据授权码,客户端ID,客户端密匙,组装OAuthClientRequest,前往服务端获取访问令牌
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        try {
            //组装请求access_token的请求数据
            OAuthClientRequest accessTokenRequest = OAuthClientRequest
                    .tokenLocation(SSO_AUTH_TICKET)
                    .setClientId(CLIENT_ID)
                    .setClientSecret(CLIENT_SECRET)
                    .setRefreshToken(oauthPojoCookie.getRefreshToken())
                    .setParameter(OPEN_ID,oauthPojoCookie.getOpenId())
                    .setParameter(USERNAME,oauthPojoCookie.getUsername())
                    .setParameter(USERICON,oauthPojoCookie.getUsericon())
                    .buildQueryMessage();

            //获得access_token的请求数据
            OAuthAccessTokenResponse oAuthResponse;

            oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);

            oauthPojo = addOauthPoji(oAuthResponse);

        } catch (OAuthSystemException e) {
            e.printStackTrace();
            oauthPojo = null;
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            oauthPojo = null;
        }
        return oauthPojo;
    }



    /**
     * 把服务端返回数据填充到OauthPojo
     * @param oAuthResponse  OAuthAccessTokenResponse
     * @return  OauthPojo
     */
    private static OauthPojo addOauthPoji(OAuthAccessTokenResponse oAuthResponse){
        OauthPojo oauthPojo;
        String accessToken = oAuthResponse.getAccessToken();
        String refreshToken = oAuthResponse.getRefreshToken();
        Long   expiresIn = oAuthResponse.getExpiresIn();
        String scope = oAuthResponse.getScope();
        String openId = oAuthResponse.getParam(OPEN_ID);

        //如果得到accessToken和refreshToken
        if(!Empty.isEmpty(accessToken) && !Empty.isEmpty(refreshToken) && !Empty.isEmpty(openId)){
            oauthPojo = new OauthPojo();
            oauthPojo.setAccessToken(accessToken);
            oauthPojo.setRefreshToken(refreshToken);
            oauthPojo.setExpiresIn(expiresIn+"");
            oauthPojo.setScope(scope);
            oauthPojo.setOpenId(openId);
        }else{
            oauthPojo = null;
        }
        return oauthPojo;
    }


}
