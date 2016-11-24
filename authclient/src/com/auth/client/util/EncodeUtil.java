package com.auth.client.util;


import java.io.UnsupportedEncodingException;

/**
 * 编码处理
 */
public class EncodeUtil {
	/**
	 * 获得传入字符串的编码
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String[] encode = {"GB2312", "ISO-8859-1", "UTF-8", "GBK"};
		String s = "";
		try {
			for (int i = 0; i < encode.length; i++) {
				if (str.equals(new String(str.getBytes(encode[i]), encode[i]))) {
					s = encode[i];
					break;
				}
			}
		} catch (Exception exception) {
			s = "";
		}
		return s;
	}

	/**
	 * 把传入字符串的编码转为utf8
	 * @param keyword
	 * @return
	 */
    public static String TranscodingUTF8(String keyword){
		try {
			keyword = new String(keyword.toString().getBytes(getEncoding(keyword)),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    	return keyword;
	}


}
