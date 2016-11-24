package com.pojo;

/**
 * ztree 基本model
 * 
 * @author 12
 * 
 */
public class ZTree implements java.io.Serializable {

	private static final long serialVersionUID = 6625307952110627894L;
	public String id;
	public String pId;
	public String name;
	public boolean open = true;
	public boolean checked = false;
	public boolean chkDisabled = false;

	public ZTree(String id,String pid , String name) {

		this.id = id;
		this.pId = pid;
		this.name = name;
		

	}

	public void setDisCheck(boolean b) {

		if (b) {
			checked = true;
			chkDisabled = true;
		} else {
			chkDisabled = false;
			checked = false;
		}

	}

}
