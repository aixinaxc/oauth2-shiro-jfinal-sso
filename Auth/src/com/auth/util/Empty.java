package com.auth.util;

public class Empty {
	/**
	 * 判断是否为空是返回true，不是返回false
	 * 
	 * @param val
	 * @return 为空返回true
	 */
	public static boolean isEmpty(String val) {
		if (null == val || "".equals(val.trim()) || "nil".equals(val.trim())) {
			return true;
		}
		return false;
	}
	
	
	
	
}
