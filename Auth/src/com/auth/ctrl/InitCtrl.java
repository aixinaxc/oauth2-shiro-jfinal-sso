package com.auth.ctrl;

import com.auth.base.ErrorMsg;
import com.auth.base.MyConstant;
import com.auth.util.Empty;
import com.auth.util.MD5Unit;
import com.model.Res;
import com.model.User;
import com.pojo.Menu;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;

import java.util.ArrayList;
import java.util.List;

/** 程序入口，主要处理登录，退出
 * Created by homer chang on 2016/11/1.
 */
public class InitCtrl extends BaseCtrl {

    public void index(){
        redirect("/login");
    }

    public void login(){
        Subject  subject = SecurityUtils.getSubject();
        String error_message = "";
        String username = getPara("username");
        String password = getPara("password");
        if(!Empty.isEmpty(username) && !Empty.isEmpty(password) && !subject.isAuthenticated()){
            try {
                User user = User.dao.findUserByName(username);
                String md5 = MD5Unit.GetMD5Code(password) + user.getUserSlat();
                md5 = MD5Unit.GetMD5Code(md5);
                UsernamePasswordToken token = new UsernamePasswordToken(username, md5);
                token.setRememberMe(true);
                subject.login(token);

            } catch (AuthenticationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                error_message = ErrorMsg.USER_PWD_ERROR;
            }
        }
        if(subject.isAuthenticated()){

            User user = (User)subject.getSession().getAttribute("user");
            List<Res> list =  Res.dao.Menu(user.getUserId());

            //填充生成菜单
            if(list != null){
                List<Menu> menus = new ArrayList();
                //生成一级菜单
                for(int i=0;i<list.size();i++){
                    if("0".endsWith(list.get(i).getResPid())){
                        Menu menu = new Menu(list.get(i).getResId(),list.get(i).getResPid(),list.get(i).getResName(), list.get(i).getResIcon(),list.get(i).getResUrl());
                        menus.add(menu);
                    }
                }
                //生成二级菜单
                for(int j=0;j<menus.size();j++){
                    List<Menu> children = new ArrayList();
                    for(int k=0;k<list.size();k++){
                        if(menus.get(j).getId().equals(list.get(k).getResPid())){
                            Menu menu = new Menu(list.get(k).getResId(),list.get(k).getResPid(),list.get(k).getResName(), list.get(k).getResIcon(),list.get(k).getResUrl());
                            children.add(menu);
                        }
                    }
                    menus.get(j).setChildren(children);
                }
                setAttr("menu", menus);
                setAttr("user", user);
            }else{
                setAttr("menu", "");
                setAttr("user", "");
            }
            render(MyConstant.VIEW_STATIC_ROOT + "layout/layout.html");
        }else{
            setAttr("error_message",error_message);
            render(MyConstant.VIEW_STATIC_ROOT + "auth/login.html");
        }

    }

    public void logout(){
        Subject subject = SecurityUtils.getSubject();

        subject.logout();

        setAttr("error_message","");
        render(MyConstant.VIEW_STATIC_ROOT + "auth/login.html");//跳转到登录页面
    }

    @RequiresPermissions("/home")
    public void home(){

        render(MyConstant.VIEW_STATIC_ROOT + "auth/home.html");
    }

}
