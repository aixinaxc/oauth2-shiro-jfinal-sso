package com.auth.util;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
/**
 * 重新构建分页返回数据，用于后台分页
 * @author anxingchen
 * @date 2016年9月20日 上午9:56:58
 */
public class NewPage {
	 public static String newPage(Page page){
		 JSONObject json = new JSONObject();
		 json.put("rows", page.getList());
		 json.put("total", page.getTotalRow());
		 return json.toJSONString();
	 }
}
