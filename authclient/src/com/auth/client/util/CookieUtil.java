package com.auth.client.util;



import com.alibaba.fastjson.JSON;
import com.auth.client.pojo.OauthPojo;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 对SSO登录cookie的操作，需要配置存在时间，加解密的密码，如果系统退出，需要主动清除该cookie信息
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


    /**
     *  解析票据cookie
     * @param request HttpServletRequest
     * @return 返回cookie的value，是OauthPojo的对象
     */
    public static OauthPojo desTicket(HttpServletRequest request){
        String name = "";
        OauthPojo oauthPojo = new OauthPojo();
        for(int i=0;i<request.getCookies().length;i++){
            if(T_NAME.equals(request.getCookies()[i].getName())){
                name = request.getCookies()[i].getValue();
            }
        }
        try {
            name = AES.desEncrypt(name,T_PWD);
            oauthPojo = JSON.parseObject(name,OauthPojo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oauthPojo;
    }




}
