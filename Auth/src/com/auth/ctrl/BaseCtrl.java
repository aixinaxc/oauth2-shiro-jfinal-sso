package com.auth.ctrl;

import com.jfinal.core.Controller;
import com.model.Res;
import com.model.User;
import com.pojo.Menu;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.List;

/** 基础ctrl主要处理全局设置
 * Created by homer chang on 2016/11/1.
 */
public class BaseCtrl extends Controller {
    Subject subject = SecurityUtils.getSubject();

    public void render(String render){

        super.render(render);
    }



}
