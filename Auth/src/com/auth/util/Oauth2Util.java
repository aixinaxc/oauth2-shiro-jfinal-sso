package com.auth.util;

import com.model.App;
import com.model.User;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.OAuth;

/** Oauth2工具类
 * Created by anxingchen on 2016/11/9.
 */
public class Oauth2Util {
    private static Oauth2Util oauth2Util = new Oauth2Util();
    private Oauth2Util(){}
    public static Oauth2Util getOauth2Util(){
        return oauth2Util;
    }
    /**
     * 判断是否注册app
     * @param client_id 输入client_id
     * @return true为有app
     */
    public boolean is_app_id(String client_id){
        boolean is = true;
        if(Empty.isEmpty(client_id) || !App.dao.isHasAPP(client_id)){
            is = false;
        }
        return is;
    }


    /**
     * 检查client_id和client_secret
     * @param client_id 输入client_id
     * @param client_secret 输入client_secret
     * @return true为有app
     */
    public boolean is_res_app(String client_id,String client_secret) {
        boolean is = true;
        // 检查client_id和client_secret
        if (Empty.isEmpty(client_id)
                || Empty.isEmpty(client_secret)
                || !App.dao.isRegister(client_id, client_secret)) {
            is = false;
        }
        return is;
    }

    /**
     * 检查用户是否正确
     *
     * @param name 用户名
     *  @param pwd 用户名
     * @return  返回true为有用户
     */
    public boolean is_res_user(String name,String pwd) {
        boolean is = true;
        // 检查用户是否正确
        if (Empty.isEmpty(name) || Empty.isEmpty(pwd) ||  !User.dao.isRegisterUser(name, pwd)) {
            is = false;
        }
        return is;
    }





}
