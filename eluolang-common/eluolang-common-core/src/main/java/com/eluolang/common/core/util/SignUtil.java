package com.eluolang.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * @author dengrunsen
 * @date 2022年11月15日 15:26
 */
public class SignUtil {
    private static final Logger log = LoggerFactory.getLogger(SignUtil.class);
    /**
     * 使用hashSet默认排序后直接进行md5加密
     * @param treeSet,入参
     * @return 返回加密值
     */
    public static String getSign(TreeSet<String> treeSet) {
        return encodePassword(treeSet.toString());
    }

    /**
     * Md5加密
     */
    public static String encodePassword(String pass) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(pass.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("md5:", e);
        }
        StringBuilder md5StrBuff = new StringBuilder();
        if (messageDigest != null) {
            byte[] byteArray = messageDigest.digest();
            for (byte aByteArray : byteArray) {
                if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
                }
            }
        }
        return md5StrBuff.toString();
    }
    public static void main(String[] args) {
        Long date= new Date().getTime();
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("1663840641166");
        treeSet.add("18867118866");
        treeSet.add("wolaipass11");
    System.out.println(treeSet);
        String sign = SignUtil.getSign(treeSet);
    System.out.println(sign);
    }
}
