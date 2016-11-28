package com.auth.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 通过accestoken验证登录
 * Created by anxingchen on 2016/11/24.
 */
public class Oauth2SsoToken implements AuthenticationToken {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Oauth2SsoToken(){

    }

    public Oauth2SsoToken(String accessToken){
        this.accessToken = accessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.accessToken;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
