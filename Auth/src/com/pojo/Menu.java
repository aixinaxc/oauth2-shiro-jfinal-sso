package com.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单bean
 * 
 * 
 */
public class Menu implements java.io.Serializable {
	public static final long serialVersionUID = 2592596759941016872L;


	public String id;
	public String pId;
	public String name;
	public String url;
	public String iconCls;
	public List<Menu> children = new ArrayList<Menu>();
	public boolean authority;

	/**
	 * 菜单基本内容
	 * @param name
	 * @param icon
	 * @param url
	 * @param authority
	 */
	public Menu(String id,String pId, String name, String icon, String url) {
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.iconCls = icon;
		this.url = url;
	}
	
	
	

	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getpId() {
		return pId;
	}




	public void setpId(String pId) {
		this.pId = pId;
	}




	public boolean isAuthority() {
		return authority;
	}




	public void setAuthority(boolean authority) {
		this.authority = authority;
	}




	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
}