package com.auth.base;

/** Oauth2基础配置类
 * Created by 安星辰 on 2016/11/8.
 */
public class BaseConfig {
    //OAUTH2基础配置
    public final static int CODE_EXPIRE = 60;//单位秒
    public final static int REFRESH_TOKEN = 60*60*24;//单位秒
    public final static String REDIRECT_URI = "/client/auth";

    public final static String OPEN_ID = "openId";//保存用户的userid
    public final static String USERNAME = "username";//用户名
    public final static String USERICON = "usericon";//用户头像



    //SHIRO权限管理配置
    public static int SESSION_EXPIRE = 60*60*24;//单位秒




}
