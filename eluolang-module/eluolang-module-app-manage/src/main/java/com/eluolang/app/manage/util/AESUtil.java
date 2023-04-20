package com.eluolang.app.manage.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * 
 */
public class AESUtil {

    /**
     *
     * @param content
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key, String iv) throws Exception {
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec ivParam = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParam);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return new BASE64Encoder().encode(encrypted);
    }

    /**
     *
     * @param content
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String key, String iv) throws Exception {
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding ");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec ivParam = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParam);
        //先用base64解密
        byte[] encrypted = new BASE64Decoder().decodeBuffer(content);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }
	
	
    public static void main(String[] args) throws Exception {
        // js 实现可参考  https://www.jb51.net/article/89647.htm
        String iv = "abcdefghijklmnop";
        String encrypt = AESUtil.encrypt("18867120995", "7E1%Dv96T1Rkn5x7", iv);
        System.out.println(encrypt);
        String decrypt = AESUtil.decrypt(encrypt, "7E1%Dv96T1Rkn5x7", iv);
        System.out.println(decrypt);
    }
}
