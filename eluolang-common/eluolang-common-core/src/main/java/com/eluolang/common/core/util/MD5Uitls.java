package com.eluolang.common.core.util;



import com.eluolang.common.core.constant.SystemStatus;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 *
 * @author suziwei
 * @date 2020/8/27
 */
public class MD5Uitls
{
    static final char[] HEX_DIGITS = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    static final char[] HEX_DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7','8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     *   对字符串 MD5 无盐值加密
     *
     * @param plainText 传入要加密的字符串
     * @return MD5加密后生成32位(小写字母+数字)字符串，异常返回 444
     */
    public static String MD5Lower(String plainText)
    {
        if (plainText==null){
            return SystemStatus.ERROR;
        }
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            return new BigInteger(1,  md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     *  对字符串 MD5 加密
     *
     * @param plainText 传入要加密的字符串
     * @return MD5加密后生成32位(大写字母+数字)字符串，异常返回444
     */
    public static String MD5Upper(String plainText)
    {
        if (plainText==null){
            return SystemStatus.ERROR;
        }
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = mdResult[i];
                // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                // 取字节中低 4 位的数字转换
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *   对字符串 MD5 加盐值加密
     *
     * @param plainText 传入要加密的字符串
     * @param saltValue 传入要加的盐值
     * @return MD5加密后生成32位(小写字母+数字)字符串，异常返回444
     */
    public static String MD5Lower(String plainText, String saltValue)
    {
        if (plainText==null){
            return SystemStatus.ERROR;
        }
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            md.update(saltValue.getBytes());

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            return new BigInteger(1,  md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *   对字符串 MD5 加盐值加密
     *
     * @param plainText
     * 		传入要加密的字符串
     * @param saltValue
     * 		传入要加的盐值
     * @return
     *  	MD5加密后生成32位(大写字母+数字)字符串，异常返回444
     */
    public static String MD5Upper(String plainText, String saltValue)
    {
        if (plainText==null){
            return SystemStatus.ERROR;
        }
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            md.update(saltValue.getBytes());

            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = mdResult[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  MD5加密后生成32位(小写字母+数字)字符串
     *    同 MD5Lower() 一样
     */
    public final static String MD5(String plainText)
    {
        if (plainText==null){
            return SystemStatus.ERROR;
        }
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");

            mdTemp.update(plainText.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS_LOWER[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS_LOWER[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *  校验MD5码
     *
     * @param text 要校验的字符串
     * @param md5 md5值
     * @return 校验结果
     */
    public static boolean valid(String text, String md5)
    {
        if (text==null||md5==null){
            return false;
        }
        return md5.equals(MD5(text)) || md5.equals(MD5(text).toUpperCase());
    }

    public static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        return resultString;
    }
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static void main(String[] args) {
        String pwd = MD5Lower("123456","system");
        System.out.println(pwd);
    }
}
