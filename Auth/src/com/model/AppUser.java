package com.model;

import com.auth.base.MyConstant;
import com.model.base.BaseAppUser;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class AppUser extends BaseAppUser<AppUser> {
	public static final AppUser dao = new AppUser();

	/**
	 *  判断用户是否授权该系统
	 * @param openId 传入openID,即userid
	 * @return 授权返回true
	 */
	public boolean isUserApp(String openId){
		String sql = "WHERE * FROM "+ MyConstant.TABLE_PREFIX +"user_app WHERE user_id = ?";
		return dao.findFirst(sql,openId)!=null;
	}



}
