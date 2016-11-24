package com.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRoleRes<M extends BaseRoleRes<M>> extends Model<M> implements IBean {

	public void setRoleId(String roleId) {
		set("role_id", roleId);
	}

	public String getRoleId() {
		return get("role_id");
	}

	public void setResId(String resId) {
		set("res_id", resId);
	}

	public String getResId() {
		return get("res_id");
	}

}
