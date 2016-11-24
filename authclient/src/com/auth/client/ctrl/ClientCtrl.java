package com.auth.client.ctrl;


import com.alibaba.fastjson.JSON;
import com.auth.client.pojo.OauthPojo;
import com.auth.client.util.AuthClient;
import com.auth.client.util.CookieUtil;
import com.auth.client.util.Empty;
import com.jfinal.core.Controller;


/** OAUTH2
 * Created by homer chang on 2016/11/4.
 */
public class ClientCtrl extends Controller {

    /**
     * 访问认证服务器，获得code
     */
    public void index(){
        AuthClient.init("1","1","http://127.0.0.1");
        String uri = AuthClient.getcode("","");
        redirect(uri);
    }


    /**
     * 通过code授权码，获得服务端返回的access_token
     */
    public void auth(){
        String code = getPara(AuthClient.CODE);
        String userId = getPara(AuthClient.OPEN_ID);
        if(Empty.isEmpty(code) || Empty.isEmpty(userId)){

            return;
        }
       OauthPojo oauthPojo =  AuthClient.getAccessToken(code,userId);
       if(oauthPojo == null){

          return;
       }
       //User user= User.dao.findById(userId);
        //保存cookie到浏览器
        CookieUtil.ticket(JSON.toJSONString(oauthPojo),getRequest(),getResponse());


    }


    public void authTicket(){
        OauthPojo oauthPojo = CookieUtil.desTicket(getRequest());
        //需加回调地址
        OauthPojo newOP = AuthClient.authTicket(oauthPojo);
        //redirect("http://127.0.0.1"+"/client/authTicket?accessToken="+oauthPojo.getAccessToken());
    }


}
