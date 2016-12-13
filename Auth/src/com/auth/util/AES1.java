package com.auth.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AES1 {
	// /** 算法/模式/填充 **/
	 private static final String CipherMode ="AES/CBC/PKCS5Padding";

	 // /** 创建密钥 **/
	 private static SecretKeySpec createKey(String key) {
	 byte[] data = null;
	 if (key == null) {
	 key ="";
	}
	 StringBuffer sb = new StringBuffer(16);
	sb.append(key);
	 while (sb.length() < 16) {
	sb.append("0");
	}
	 if (sb.length() > 16) {
	sb.setLength(16);
	}

	 try {
	 data = sb.toString().getBytes("UTF-8");
	 } catch (UnsupportedEncodingException e) {
	e.printStackTrace();
	}
	 return new SecretKeySpec(data,"AES");
	}

	 private static IvParameterSpec createIV(String password) {
	 byte[] data = null;
	 if (password == null) {
	 password ="";
	}
	 StringBuffer sb = new StringBuffer(16);
	sb.append(password);
	 while (sb.length() < 16) {
	sb.append("0");
	}
	 if (sb.length() > 16) {
	sb.setLength(16);
	}

	 try {
	 data = sb.toString().getBytes("UTF-8");
	 } catch (UnsupportedEncodingException e) {
	e.printStackTrace();
	}
	 return new IvParameterSpec(data);
	}

	 // /** 加密字节数据 **/
	 public static byte[] encrypt(byte[] content, String password, String iv) {
	 try {
	 SecretKeySpec key = createKey(password);
	 Cipher cipher = Cipher.getInstance(CipherMode);
	 cipher.init(Cipher.ENCRYPT_MODE, key, createIV(iv));
	 byte[] result = cipher.doFinal(content);
	 return result;
	 } catch (Exception e) {
	e.printStackTrace();
	}
	 return null;
	}

	 // /** 加密(结果为16进制字符串) **/
	 public static String encrypt(String content, String password, String iv) {
	 byte[] data = null;
	 try {
	 data = content.getBytes("UTF-8");
	 } catch (Exception e) {
	e.printStackTrace();
	}
	 data = encrypt(data, password, iv);
	 String result = new Base64().encodeToString(data);
	 return result;
	}

	 // /** 解密字节数组 **/
	 public static byte[] decrypt(byte[] content, String password, String iv) {
	 try {
	 SecretKeySpec key = createKey(password);
	 Cipher cipher = Cipher.getInstance(CipherMode);
	 cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
	 byte[] result = cipher.doFinal(content);
	 return result;
	 } catch (Exception e) {
	e.printStackTrace();
	}
	 return null;
	}

	 // /** 解密 **/
	 public static String decrypt(String content, String password, String iv) {
	 byte[] data = null;
	 try {
	 data =  new Base64().decode(content);//先用base64解密
	 } catch (Exception e) {
	e.printStackTrace();
	}
	 data = decrypt(data, password, iv);
	 if (data == null)
	 return null;
	 String result = null;
	 try {
	 result = new String(data,"UTF-8");
	 } catch (UnsupportedEncodingException e) {
	e.printStackTrace();
	}
	 return result;
	}

	public static void main(String args[]) {
		String data = "x85pIkZp1M7+kbnMKcetFvqxCD/6ME5vtrcuElMZUpRh5vn5zPuBkde4o3XW5TU7lY+sV1btGXADAMHl/ykTvUAhD0SOPAu96JBOUhK0FyhU7YuHwo2xzW6TSyOB09qjCoAe1cEknP0qNG9DD0MYYuw2Gm+KPZ4ep1h52mNQJU+mmLGI8Pfms6LJfpaZf5lE++fAvbJZku9T4FhBS7xnCbr8dioGwqKsf0x9rcM33H5eBOETV1pr7rGas3ra0gp+1bY56QM2/VY2xozmeUHhVERcOb+Uo29XK3VlToRrPC/yIKwYYacLXwybwvKf9rc3BDEwY3wnL9fHWkS1KC9zITzW0KrN/8KoSPjsDexPlIyZHhnS/J1tW3br+iW9Inp4hTPADJGZpcDZllGr5SPuL6E+5PnXUECs6l45gqxszBcF49hp/JA0MDgQTvt3o5tJgyThcS1B3XwcqTxJMszZLqnwoIcXlTIJbZx9I6TlUyV8KLMv8Syn5GYj5HYg9Cr6";
        String l = "大家好";
		String key = "BOXFIKWLBMOXUGTQ";
		System.out.println(encrypt(l,key,""));
		System.out.println(decrypt(data,key,""));
	}




}
