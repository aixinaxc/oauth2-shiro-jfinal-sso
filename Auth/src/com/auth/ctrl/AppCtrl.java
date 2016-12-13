package com.auth.ctrl;

import com.alibaba.fastjson.JSON;
import com.auth.base.MyConstant;
import com.auth.util.Empty;
import com.auth.util.NewPage;
import com.auth.util.Transcoding;
import com.jfinal.plugin.activerecord.Page;
import com.model.App;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.util.List;

/**软件Ctrl
 * Created by homer chang on 2016/11/3.
 */
public class AppCtrl extends BaseCtrl {

    /**
     * 跳转软件列表页面
     */
    @RequiresPermissions("/app/list")
    public void list(){
        render(MyConstant.VIEW_STATIC_ROOT + "app/list.html");
    }

    /**
     * list列表数据
     */
    @RequiresPermissions("/app/list")
    public void listdata(){
        int pageNumber = Empty.isEmpty(getPara("pageNumber")) ? MyConstant.PAGE_NUM
                : getParaToInt("pageNumber");

        int pageSize = Empty.isEmpty(getPara("pageSize")) ? MyConstant.PAGE_SIZE
                : getParaToInt("pageSize");
        pageNumber = pageNumber/pageSize+1;
        String keyword = Empty.isEmpty(getPara("keyword")) ? ""
                : getPara("keyword");
        String keywordsql = "";
        if(null!=keyword&&!"".equals(keyword)){
            if(MyConstant.IS_TEANCODING){
                keyword = Transcoding.Transcoding(keyword);
            }
            keywordsql = " WHERE app_name LIKE '%"+keyword.trim()+"%' ";
        }
        Page<App> page = App.dao.list(pageNumber, pageSize, keywordsql);
        if(page != null && page.getList().size()>0 ){
            renderJson(NewPage.newPage(page));
        } else{
            renderJson("result","fail");
        }
    }


    @RequiresPermissions("/app/save")
    public void save(){
        App app = getModel(App.class);
        boolean is_success = false;
        if(app != null){
            is_success = App.dao.save(app);
        }
        if(is_success){
            renderJson("result","success");
        }else{
            renderJson("result","fail");
        }
    }

    @RequiresPermissions("/app/delete")
    public void delete(){
        List<App> data = JSON.parseArray(getPara("data"),App.class);
        for(int i=0;i<data.size();i++){
            App.dao.delete(data);
        }
        renderJson("result","success");
    }




}
