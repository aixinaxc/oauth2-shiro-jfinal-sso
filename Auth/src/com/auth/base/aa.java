package com.auth.base;

import com.auth.util.MD5Unit;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Created by anxingchen on 2016/11/9.
 */
public class aa {
    public static void main(String args[]) {
        SimpleHash hash = new SimpleHash("md5", "123456", "123456", 2);
        String encodedPassword = hash.toHex();

        String pwdString = "123456";
        pwdString = MD5Unit.GetMD5Code(pwdString + "123456");
        pwdString = MD5Unit.GetMD5Code(pwdString);


        System.out.println(encodedPassword+"========================"+pwdString);
    }
}
