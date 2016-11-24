package com.auth.util;

import java.util.UUID;


/**
 * 生成不带-的uuid
 */
public class MyUUID {
	public static String getUUID() {  
        UUID uuid = UUID.randomUUID();  
        String str = uuid.toString();
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }
}
