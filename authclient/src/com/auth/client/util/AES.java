package com.auth.client.util;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密
 * Created by homer chang on 2015/12/14.
 */
public class AES {

    public static String encrypt(String data1,String key1) throws Exception {
        try {
            String data = data1;
            String key = key1;
            char iv1[] = {'\u0000','\u0000','\u0000','\u0000','\u0000','\u0000',
                    '\u0000','\u0000','\u0000','\u0000',
                    '\u0000','\u0000','\u0000',
                    '\u0000','\u0000','\u0000'};
            String iv = String.valueOf(iv1);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return new BASE64Encoder().encode(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String desEncrypt(String data,String key) throws Exception {
        try
        {
            //data = "bRXFvNM/oEroDU38IVCUCA==";
            //key = "1234567812345678";
            char iv1[] = {'\u0000','\u0000','\u0000','\u0000','\u0000','\u0000',
                    '\u0000','\u0000','\u0000','\u0000',
                    '\u0000','\u0000','\u0000',
                    '\u0000','\u0000','\u0000'};
            String iv = String.valueOf(iv1);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            
            byte[] original = cipher.doFinal(encrypted1);
            
           int a =original.length-16;
            for(int i=original.length;i>(original.length-16);i--){
            	if(original[i-1] == 0){
            		original[i-1] = 32;
            	}
            }
            String originalString = new String(original);
            return originalString;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
