package com.auth.client.util;

import java.io.UnsupportedEncodingException;

/**
 * 转码
 * @author anxingchen
 * @date 2016年8月22日 下午2:52:20
 */
public class Transcoding {
	        /**
	         * iso8859_1转utf-8
	         * @param keyword
	         * @return
	         */
            public static String Transcoding(String keyword){
            	try {
    				keyword = new String(keyword.toString().getBytes("iso8859_1"),"utf-8");
    			} catch (UnsupportedEncodingException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				return "";
    			}
            	return keyword;
            }
}
