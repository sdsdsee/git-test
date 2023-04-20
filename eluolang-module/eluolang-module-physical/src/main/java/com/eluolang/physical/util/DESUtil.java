package com.eluolang.physical.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;


public class DESUtil {

    /*
     * 生成密钥
     */
    public static String initKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        //指定keysize
        keyGenerator.init(56);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] bytesKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytesKey);
    }


    /*
     * DES 加密
     */
    public static String encrypt(String data, byte[] key) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        Key convertSecretKey = factory.generateSecret(desKeySpec);

        //加密
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
        byte[] resultBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        System.out.println("jdk des encrypt : " + Base64.getEncoder().encodeToString(resultBytes));
        return Base64.getEncoder().encodeToString(resultBytes);
    }


    /*
     * DES 解密
     */
    public static String decrypt(String data, byte[] key) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        Key convertSecretKey = factory.generateSecret(desKeySpec);
        //加密
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        //解密
        cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
        byte[] resultBytes = cipher.doFinal(Base64.getDecoder().decode(data));
        System.out.println("jdk des decrypt : " + new String(resultBytes));
        return new String(resultBytes);
    }

    //Test
    public static void main(String[] args) throws Exception {
//        System.out.println(initKey());
//        System.out.println("加密"+encrypt("123456", "5dc4685175314adf".getBytes(StandardCharsets.UTF_8)));
        System.out.println("解密"+decrypt("wDbtSFz831s/OCFMffIHq9so4i0gl6EfGSfcUmgdIVflZLdtH6l8c6gZujkqmavg", "3de902b3".getBytes()));

    }
}