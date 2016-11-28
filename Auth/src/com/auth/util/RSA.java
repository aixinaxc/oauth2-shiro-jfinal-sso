package com.auth.util;

import com.qiniu.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;



/**
 * RSA加解密
 * Created by homer chang on 2015/12/1.
 */
public class RSA {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String aString = "130723199011300037";
		try {
			String bString = encryptByPrivateKey(aString);
			System.out.println("私钥加密" + bString);
			System.out.println("公钥解密" + decryptByPublic(bString));

			String cString = encryptByPublic(aString);
			System.out.println("公钥加密" + cString);
			System.out.println("私钥加密" + decryptByPrivate(cString));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static final String RSA_PUBLICE = 
			"";
	private static final String RSA_PRIVATE = 
			"";

	private static final String ALGORITHM = "RSA";

	/**
	 * 得到公钥
	 * 
	 * @param algorithm
	 * @param bysKey
	 * @return
	 */
	private static PublicKey getPublicKeyFromX509(String algorithm,
			String bysKey) throws NoSuchAlgorithmException, Exception {
		byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePublic(x509);
	}

	private static PrivateKey getrivateKeyFromPkcs8(String algorithm,
			String bysKey) throws NoSuchAlgorithmException, Exception {
		byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
		// X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
		PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePrivate(pkcs8);
	}

	/**
	 * 私钥加密
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String content) throws Exception {

		try {
			PrivateKey privatekey = null;
			BufferedReader br;
			try {
				//String a = PathKit.getWebRootPath()+"/WEB-INF/runtime/key/private_key.pem";
				/*br = new BufferedReader(new FileReader(
						"/data/wwwroot/open.doctordabai.com/api/components/key/private_key.pem"));
				String s = br.readLine();
				String str = "";
				s = br.readLine();
				while (!"-----END PRIVATE KEY-----".equals(s)) {
					str += s + "\r";
					s = br.readLine();
				}*/
				//System.out.println(str);
				privatekey = getrivateKeyFromPkcs8(ALGORITHM, RSA_PRIVATE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, privatekey);

			byte plaintext[] = content.getBytes("UTF-8");
			byte[] output = cipher.doFinal(plaintext);

			String s = new String(Base64.encode(output, Base64.DEFAULT));

			return s;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用私钥钥解密
	 * 
	 * @param content
	 *            密文
	 * @param
	 * @return 解密后的字符串
	 */
	public static String decryptByPrivate(String content) {
		try {
			PrivateKey privatekey = null;
			BufferedReader br;
			try {
				/*br = new BufferedReader(new FileReader(
						"/data/wwwroot/open.doctordabai.com/api/components/key/private_key.pem"));
				String s = br.readLine();
				String str = "";
				s = br.readLine();
				while (!"-----END PRIVATE KEY-----".equals(s)) {
					str += s + "\r";
					s = br.readLine();
				}*/
				// System.out.println(str);
				privatekey = getrivateKeyFromPkcs8(ALGORITHM, RSA_PRIVATE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privatekey);
			InputStream ins = new ByteArrayInputStream(Base64.decode(content,
					Base64.DEFAULT));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(cipher.doFinal(block));
			}
			return new String(writer.toByteArray(), "utf-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用公钥加密
	 * 
	 * @param content
	 * @param
	 * @return
	 */
	public static String encryptByPublic(String content) {
		try {

			PublicKey pubkey = null;
			BufferedReader br;
			try {
				/*br = new BufferedReader(new FileReader(
						"/data/wwwroot/open.doctordabai.com/api/components/key/rsa_public_key.pem"));
				String s = br.readLine();
				String str = "";
				s = br.readLine();
				while (!"-----END PUBLIC KEY-----".equals(s)) {
					str += s + "\r";
					s = br.readLine();
				}*/
				// System.out.println(str);
				pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);

			byte plaintext[] = content.getBytes("UTF-8");
			byte[] output = cipher.doFinal(plaintext);

			String s = new String(Base64.encode(output, Base64.DEFAULT));

			return s;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用公钥解密
	 * 
	 * @param content
	 *            密文
	 * @param
	 * @return 解密后的字符串
	 */
	public static String decryptByPublic(String content) {
		try {
			PublicKey pubkey = null;
			BufferedReader br;
			try {
				/*
				  br = new BufferedReader(new
				  FileReader("/data/wwwroot/open.doctordabai.com/api/components/key/rsa_public_key.pem"
				  )); String s = br.readLine(); String str = ""; s =
				  br.readLine(); while (!"-----END PUBLIC KEY-----".equals(s)){
				 str += s + "\r"; s = br.readLine(); }
				 */
				// System.out.println(str);
				pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, pubkey);
			InputStream ins = new ByteArrayInputStream(Base64.decode(content,
					Base64.DEFAULT));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(cipher.doFinal(block));
			}
			return new String(writer.toByteArray(), "utf-8");
		} catch (Exception e) {
			return null;
		}
	}
}
