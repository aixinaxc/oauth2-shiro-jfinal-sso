package com.auth.ctrl;

import com.alibaba.fastjson.JSON;
import com.auth.base.MyConstant;
import com.auth.util.Empty;
import com.auth.util.NewPage;
import com.auth.util.Transcoding;
import com.model.Res;
import com.pojo.ResPojo;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.util.List;

/** 资源ctrl
 * Created by homer chang on 2016/11/3.
 */
public class ResCtrl extends BaseCtrl {
    @RequiresPermissions("/res/list")
    public void list(){
        //Log.dao.save(getRequest(), "用户进入资源管理列表界面");
        render(MyConstant.VIEW_STATIC_ROOT + "res/list.html");
    }
    @RequiresPermissions("/res/list")
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
            keywordsql = " WHERE res_name LIKE '%"+keyword.trim()+"%' " +
                    " and ";
        }
        com.jfinal.plugin.activerecord.Page<Res> page = Res.dao.list(pageNumber, pageSize, keywordsql);
        if(page != null && page.getList().size()>0 ){
            renderJson(NewPage.newPage(page));
        } else{
            renderJson("result","fail");
        }
    }

    @RequiresPermissions("/res/list")
    public void pidList(){
        renderJson(Res.dao.pidList());
    }


    @RequiresPermissions("/res/save")
    public void save(){
        Res res = getModel(Res.class);
        boolean is_success = false;
        if(res != null){
            is_success = Res.dao.save(res);
        }
        if(is_success){
            renderJson("result","success");
        }else{
            renderJson("result","fail");
        }
    }

    @RequiresPermissions("/res/delete")
    public void delete(){
        List<ResPojo> data = JSON.parseArray(getPara("data").toString(),ResPojo.class);
        for(int i=0;i<data.size();i++){
            Res.dao.deleteById(data.get(i).res_id);
        }
        renderJson("result","success");
    }
}
