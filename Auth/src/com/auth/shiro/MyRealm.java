package com.auth.shiro;

import com.auth.util.Empty;
import com.model.Res;
import com.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

/**
 * Created by homer chang on 2016/11/1.
 */
public class MyRealm extends AuthorizingRealm {
    public String getName() {
        return "MyRealm";
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String)principalCollection.fromRealm(getName()).iterator().next();
        SimpleAuthorizationInfo simpleAuthorizationInfo  = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(Res.dao.findPermissionsByUser(username));

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String)authenticationToken.getPrincipal(); //得到用户名
        String password = new String((char[])authenticationToken.getCredentials()); //得到密码



        Subject subject = SecurityUtils.getSubject();


        User user = User.dao.findUserByName(username);
        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        if("2".equals(user.getUserStatus())) {
            throw new LockedAccountException(); //帐号锁定
        }


        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
        //用户采用用户名，密码登录
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUserUsername(), //用户名
                user.getUserPwd(), //密码
                getName()  //realm name
        );
        if(!Empty.isEmpty(authenticationInfo.getPrincipals().toString())){
            subject.getSession().setAttribute("user", user);
            return authenticationInfo;
        }

        return null;
    }
}
