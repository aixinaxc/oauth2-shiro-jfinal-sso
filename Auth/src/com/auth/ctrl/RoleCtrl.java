package com.auth.ctrl;

import com.alibaba.fastjson.JSON;
import com.auth.base.MyConstant;
import com.auth.util.Empty;
import com.auth.util.NewPage;
import com.auth.util.Transcoding;
import com.jfinal.plugin.activerecord.Page;
import com.model.Res;
import com.model.Role;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * Created by homer chang on 2016/11/3.
 */
public class RoleCtrl extends BaseCtrl {
    @RequiresPermissions("/role/list")
    public void list() {
        //Log.dao.save(getRequest(), "用户进入角色管理列表界面");
        render(MyConstant.VIEW_STATIC_ROOT + "role/list.html");
    }

    @RequiresPermissions("/role/list")
    public void listdata() {
        Subject subject = SecurityUtils.getSubject();
        // User user = (User)subject.getSession().getAttribute("user");
        int pageNumber = Empty.isEmpty(getPara("pageNumber")) ? MyConstant.PAGE_NUM
                : getParaToInt("pageNumber");

        int pageSize = Empty.isEmpty(getPara("pageSize")) ? MyConstant.PAGE_SIZE
                : getParaToInt("pageSize");
        pageNumber = pageNumber / pageSize + 1;
        String keyword = Empty.isEmpty(getPara("keyword")) ? ""
                : getPara("keyword");
        String keywordsql = "";
        if (null != keyword && !"".equals(keyword)) {
            if (MyConstant.IS_TEANCODING) {
                keyword = Transcoding.Transcoding(keyword);
            }
            keywordsql = "WHERE role_name LIKE '%" + keyword.trim() + "%' ";
        }
        Page<Role> page = Role.dao.list(pageNumber, pageSize, keyword);
        if (page != null || page.getList().size() > 0) {
            renderJson(NewPage.newPage(page));
        }
    }

    @RequiresPermissions("/role/save")
    public void save() {
        Role role = getModel(Role.class);
        String resIds = getPara("res_ids");
        boolean is_success = Role.dao.save(role, resIds);
        if (is_success) {
            renderJson("result", "success");
        } else {
            renderJson("result", "fail");
        }
    }

    @RequiresPermissions("/role/delete")
    public void delete() {
        List<Role> rolePojo = JSON.parseArray(getPara("data").toString(),
                Role.class);
        boolean is_success = Role.dao.delete(rolePojo);
        if (is_success) {
            renderJson("result", "success");
        } else {
            renderJson("result", "fail");
        }
    }

    /**
     * 返回权限资源json
     */
    @RequiresPermissions("/role/list")
    public void ress() {
        String id = getPara("resId");
        String ress = JSON.toJSONString(Res.dao.getResById(id));
        renderJson(ress);
    }
}
