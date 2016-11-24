package com.auth.util;



import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 生成cookie
 * Created by anxingchen on 2016/11/10.
 */
public class CookieUtil {
    //配置SSO票据信息
    public static String T_NAME = "db_t";
    public static int T_TIME = 60*60;
    public static String T_PWD = "123456";

    /**
     * 生成票据cookie，用于sso登录
     * @param value cookie值，保存accesstoken等信息
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void ticket(String value,HttpServletRequest request, HttpServletResponse response){
        try {
            String newValue = AES.encrypt(value, T_PWD);
            Cookie cookie = new SimpleCookie();
            cookie.setName(T_NAME);
            cookie.setValue(newValue);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(T_TIME);
            cookie.saveTo(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
