package com.eluolang.app.manage.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RSAUtils {
    protected static final Log log = LogFactory.getLog(RSAUtils.class);
    private static String KEY_RSA_TYPE = "RSA";
    private static String KEY_RSA_TYPE_ALL = "RSA/ECB/PKCS1Padding";
    private static int KEY_SIZE = 1024;//JDK方式RSA加密最大只有1024位
    private static int ENCODE_PART_SIZE = KEY_SIZE / 8;
    public static final String PUBLIC_KEY_NAME = "public";
    public static final String PRIVATE_KEY_NAME = "private";


    /**
     * 创建公钥秘钥
     *
     * @return
     */
    public static Map<String, String> createRSAKeys() {
        Map<String, String> keyPairMap = new HashMap<>();//里面存放公私秘钥的Base64位加密
        try {//获得对象 KeyPairGenerator 参数 RSA 1024个字节
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_RSA_TYPE);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            //通过对象 KeyPairGenerator 获取对象KeyPair
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            //获取公钥秘钥
            String publicKeyValue = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            String privateKeyValue = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());

            //存入公钥秘钥，以便以后获取
            keyPairMap.put(PUBLIC_KEY_NAME, publicKeyValue);
            keyPairMap.put(PRIVATE_KEY_NAME, privateKeyValue);
        } catch (NoSuchAlgorithmException e) {
            log.error("当前JDK版本没找到RSA加密算法！");
            e.printStackTrace();
        }
        return keyPairMap;
    }

    /**
     * 公钥加密
     * 描述：
     * 1字节 = 8位；
     * 最大加密长度如 1024位私钥时，最大加密长度为 128-11 = 117字节，不管多长数据，加密出来都是 128 字节长度。
     *
     * @param sourceStr
     * @param publicKeyBase64Str
     * @return
     */
    public static String encode(String sourceStr, String publicKeyBase64Str) {
        byte[] publicBytes = Base64.decodeBase64(publicKeyBase64Str);
        //公钥加密
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicBytes);
        List<byte[]> alreadyEncodeListData = new LinkedList<>();

        int maxEncodeSize = ENCODE_PART_SIZE - 11;
        String encodeBase64Result = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(KEY_RSA_TYPE_ALL);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] sourceBytes = sourceStr.getBytes("utf-8");
            int sourceLen = sourceBytes.length;
            for (int i = 0; i < sourceLen; i += maxEncodeSize) {
                int curPosition = sourceLen - i;
                int tempLen = curPosition;
                if (curPosition > maxEncodeSize) {
                    tempLen = maxEncodeSize;
                }
                byte[] tempBytes = new byte[tempLen];//待加密分段数据
                System.arraycopy(sourceBytes, i, tempBytes, 0, tempLen);
                byte[] tempAlreadyEncodeData = cipher.doFinal(tempBytes);
                alreadyEncodeListData.add(tempAlreadyEncodeData);
            }
            int partLen = alreadyEncodeListData.size();//加密次数

            int allEncodeLen = partLen * ENCODE_PART_SIZE;
            byte[] encodeData = new byte[allEncodeLen];//存放所有RSA分段加密数据
            for (int i = 0; i < partLen; i++) {
                byte[] tempByteList = alreadyEncodeListData.get(i);
                System.arraycopy(tempByteList, 0, encodeData, i * ENCODE_PART_SIZE, ENCODE_PART_SIZE);
            }
            encodeBase64Result = Base64.encodeBase64String(encodeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeBase64Result;
    }

    /**
     * 私钥解密
     *
     * @param sourceBase64RSA
     * @param privateKeyBase64Str
     */
    public static String decode(String sourceBase64RSA, String privateKeyBase64Str) {
        log.info("开始用私钥解密");
        byte[] privateBytes = Base64.decodeBase64(privateKeyBase64Str);
        byte[] encodeSource = Base64.decodeBase64(sourceBase64RSA);
        int encodePartLen = encodeSource.length / ENCODE_PART_SIZE;
        List<byte[]> decodeListData = new LinkedList<>();//所有解密数据
        String decodeStrResult = null;
        //私钥解密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(KEY_RSA_TYPE_ALL);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int allDecodeByteLen = 0;//初始化所有被解密数据长度
            for (int i = 0; i < encodePartLen; i++) {
                byte[] tempEncodedData = new byte[ENCODE_PART_SIZE];
                System.arraycopy(encodeSource, i * ENCODE_PART_SIZE, tempEncodedData, 0, ENCODE_PART_SIZE);
                byte[] decodePartData = cipher.doFinal(tempEncodedData);
                decodeListData.add(decodePartData);
                allDecodeByteLen += decodePartData.length;
            }
            byte[] decodeResultBytes = new byte[allDecodeByteLen];
            for (int i = 0, curPosition = 0; i < encodePartLen; i++) {
                byte[] tempSorceBytes = decodeListData.get(i);
                int tempSourceBytesLen = tempSorceBytes.length;
                System.arraycopy(tempSorceBytes, 0, decodeResultBytes, curPosition, tempSourceBytesLen);
                curPosition += tempSourceBytesLen;
            }
            decodeStrResult = new String(decodeResultBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodeStrResult;
    }

    public static void main(String[] args) {
        Map<String, String> map=createRSAKeys();
        String ss="510724200204101213";
        System.out.println(map.get("public"));
        System.out.println(map.get("private"));
        String JIA=encode(ss,map.get("public"));
        String JIE=decode("L9Mcw8KiVffSfc8OubbbpjpkGNul7pKXOqH++l8MN+REBQgrYWpZTUpLlWr/lV1aIUSFQmbe9i5CUCiJhSo1hYZTOWrViKWGcQLTGMA9gZiXbevDBc6EMC08QKzoL24wIFpUPTVx9RYgbjbvSBtABM/OzNxeWaZKzBX+FM6mz7o=","MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAId2xYybWU020gl66UpF3nIeIAfPSFZzNI3BpPYElBHFjz3YnBPvD0hKGDVOw0OvQHuOi1Ii1OTj7H1p8/RCLUzqWjLBiZYg3Djnwza8QCeg6v9sB3UQTvgZeIDSL7ryH8kjKspoNcxP3IQPOJu92OmJ89xHa2WjQaHq7WVJsBzJAgMBAAECgYAOMJnW9brwBKsRxSdE2XbIu3EdgFAShjTeGIfAIZClH+22S8dlqygNqX1JsDtyDpyRlMfxTabBZ0KxjHS6hGgcPHg3dsmSl2rMTCZ+g3x+g1CTTEFvU2poghJgvi3MGXMsD/4OqAhctkems6Py4lLopLh4LIruWxjVQT916HGNEQJBALvoOVxDpeaqBfX+rmcId751CCV7lGOM6CQ3RCcKKOVJQrg2vdWaPQQhMtxOdHuxYFEmlCs7bkMwb5gZAoKkUu0CQQC4jX9Eyq3GnjQHGw8tKsjW0qXDva0bhWSzoKupctOF8p+CGzej1dEzoOD7B3G3s7/6cqHUsoZFlXQGzgDbIOnNAkAXxN5Mn3oC3Yr3fQnOmrGa8+7d3u38UbyjqCIE1jIqHsRDzRjiRipltVVbrMjrRJqsaTbg6Rmfgib7lF5O7D5ZAkBto3IWVipCsr3J8uNjO+Ww6decn9R1YxveMveIZTUQgIEOdEjrxhzQhSTjPRrrvZexm+RUTfLC5/TUDhIoHDeJAkAum6U4iZ1Odsad3wvndI+D3pYou3LEteKzgsOdisqjFC5kwr9x3u/Hrj2NunBmF6rlcZ+WNwNoBnKJzSHQB4kb");
        System.out.println(JIA);
        System.out.println(JIE);
    }
}
 