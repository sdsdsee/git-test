package com.eluolang.common.core.util;


import com.eluolang.common.core.constant.SystemStatus;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;

/**
 * 字符串工具类
 *
 * @author suziwei
 * @date 2020/8/24
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * 求校验和的算法
     *
     * @param b   需要求校验和的字节数组
     * @param len 需要取到的长度
     * @return 两个字节的结果
     */
    public static short checkSum(byte[] b, int len) {
        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum = sum + b[i];
        }
        //超过了255，使用补码（补码 = 原码取反 + 1）
        if (sum > 0xffff) {
            sum = ~sum;
            sum = sum + 1;
        }
        System.out.println();
        return (short) (sum & 0xffff);
    }

    /**
     * 数字前面自动补零
     * @param number 数字
     * @return
     */
    public static String geFourNumber(int number){
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(2);
        formatter.setGroupingUsed(false);
        return formatter.format(number);
    }

    /**
     * 将json数据中的Unicode编码转换为中文
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    /**
     * 获取参数不为空值
     *
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     *
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return SystemStatus.ERROR;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return SystemStatus.ERROR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return SystemStatus.ERROR;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return SystemStatus.ERROR;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 下划线转驼峰命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return SystemStatus.ERROR;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            return SystemStatus.ERROR;
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return SystemStatus.ERROR;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 测量字符串字节数长度 包含中文汉子 英文字符
     *
     * @param value 字符串
     * @return 长度
     */
    public static int String_length(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 3;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }


    /**
     * 16进制转10进制
     *
     * @param str
     * @param count
     * @return
     */
    public static String bytes2hex03(String str, int count) {
        if (str == null) {
            return SystemStatus.ERROR;
        }
        return splicingZero(String.valueOf(Integer.parseInt(str, count)), 4);
    }

    /**
     * 10进制转16进制
     *
     * @param num
     * @param totalLenght 需要的字符串总长度
     * @return
     */
    public static String toHex(int num, int totalLenght) {
        if (num == 0 && totalLenght == 0) {
            return SystemStatus.ERROR;
        }
        String str = Integer.toHexString(num);
        str = splicingZero(str, totalLenght);
        return str;
    }

    /**
     * 10进制转16进制
     * 2字节
     *
     * @param num
     * @param totalLenght 需要的字符串总长度
     * @return
     */
    public static String toHexBySum(int num, int totalLenght) {
        if (num == 0 && totalLenght == 0) {
            return SystemStatus.ERROR;
        }
        String str = Integer.toHexString(num);
        BigInteger bigInteger = new BigInteger(str, 16);
        str = Integer.toHexString(bigInteger.intValue());
        str = splicingZero(str, totalLenght);
        return str.substring(str.length() - 4);
    }

    public static String toHex(long num, int totalLenght) {
        String str = Long.toHexString(num);
        str = splicingZero(str, totalLenght);
        return str;
    }

    /**
     * 字符串前面补零操作
     *
     * @param str         字符串本体
     * @param totalLenght 需要的字符串总长度
     * @return
     */
    public static String splicingZero(String str, int totalLenght) {
        if (str == null) {
            return SystemStatus.ERROR;
        }
        int strLenght = str.length();
        String strReturn = str;
        for (int i = 0; i < totalLenght - strLenght; i++) {
            strReturn = "0" + strReturn;
        }
        return strReturn;
    }

    /**
     * 字符串转换为16进制
     *
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bytes = str.getBytes();
        int bit;
        for (int i = 0; i < bytes.length; i++) {
            bit = (bytes[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bytes[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 16进制转换为字符串
     *
     * @param hexStr
     * @return
     */
    public static String hexStr2str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * 将字节数组转换成object
     *
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object convertToObj(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        bis.close();
        return obj;
    }

    /**
     * 将object转换成字节数组
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] convertToByte(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.flush();
        byte[] bytes = bos.toByteArray();
        oos.close();
        return bytes;
    }

    /**
     * 截取指定长度的字节数组
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    /**
     * 字节数组转换成字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToString(byte[] bytes) {
        return new String(bytes, 0, bytes.length);
    }


    /**
     * 按字节校验和
     *
     * @param msg
     * @return
     */
    public static short sumCheck(byte[] msg, int l) {
        int sum = 0;
        int index = 0;
        int length = msg.length;
        int abc = 0;
        while (length > 1) {
            sum = sum + (Byte.toUnsignedInt(msg[index]) << 8) + (Byte.toUnsignedInt(msg[index + 1]));
            index = index + 2;
            length = length - 2;
        }
        if (length > 0) {
            sum += Byte.toUnsignedInt(msg[index]);
        }
        sum = ((sum >> 16)) + ((sum & 0xffff));
        //sum += (sum >> 16);
        return (short) ~sum;
    }

    public static short sumCheck1(byte[] msg, int l) {
        int sum = 0;
        int index = 0;
        int length = msg.length;
        int abc = 0;
        while (length > 1) {
            sum = sum + (Byte.toUnsignedInt(msg[index]) << 8) + (Byte.toUnsignedInt(msg[index + 1]));
            index = index + 2;
            length = length - 2;
        }
        if (length > 0) {
            sum += Byte.toUnsignedInt(msg[index]);
        }
        sum = ((sum >> 16)) + ((sum & 0xffff));
        //sum += (sum >> 16);
        return (short) ~sum;
    }


    /**
     * short类型转换成数组
     *
     * @param number
     * @return
     */
    public static byte[] shortToByte(short number) {
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (shortBuf.length - 1 - i) * 8;
            shortBuf[i] = (byte) ((number >>> offset) & 0xff);
        }
        return shortBuf;
//        int temp = number;
//        byte[] bytes = new byte[2];
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = new Integer(0xff).byteValue();
//            //将最低位保存在最低为 //向右移8位
//            temp = temp >> 8;
//        }
//        bytes[0] = (byte) (number >> 8);
//        bytes[1] = (byte) (number >> 8);
//        return bytes;
    }

    /**
     * int转byte
     *
     * @param number
     * @return
     */
    public static byte[] intToByte(int number) {
        byte[] result = new byte[4];
        result[0] = (byte) ((number >> 24) & 0xff);
        result[1] = (byte) ((number >> 16) & 0xff);
        result[2] = (byte) ((number >> 8) & 0xff);
        result[3] = (byte) (number & 0xff);
        return result;
    }

    /**
     * )
     * byte转String
     *
     * @param buf
     * @param offset
     * @return
     */
    public static String byte2String(byte[] buf, int offset) {
        int pos = offset;
        for (; offset < buf.length; offset++) {
            if (buf[offset] == 0) {
                break;
            }
        }

        if (offset > pos) {
            offset--;
        } else if (offset == pos) {
            return "";
        }
        int len = (offset - pos) + 1;
        byte[] bb = new byte[len];
        System.arraycopy(buf, pos, bb, 0, len);
        String str = new String(bb);
        return str;
    }


    /**
     * byte转Long
     *
     * @param b
     * @return
     */
    public static long byte2long(byte b[]) {
        return (long) b[7] & 255L | ((long) b[6] & 255L) << 8 | ((long) b[5] & 255L) << 16 | ((long) b[4] & 255L) << 24 | ((long) b[3] & 255L) << 32 | ((long) b[2] & 255L) << 40 | ((long) b[1] & 255L) << 48 | (long) b[0] << 56;
    }


    /**
     * byte转Int
     *
     * @param b
     * @return
     */
    public static int byte2int(byte[] b) {
        int number = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            number += (b[i] & 0x000000FF) << shift;
        }
        return number;
    }

    /**
     * byte转short
     *
     * @param b
     * @return
     */
    public static int byte2short(byte[] b) {
        short number = 0;
        short number0 = (short) (b[0] & 0xff);
        short number1 = (short) (b[1] & 0xff);
        number1 <<= 8;
        number = (short) (number0 | number1);
        return number;
    }

    /**
     * 合并两个字节数组
     *
     * @param b1
     * @param b2
     * @return
     */
    public static byte[] byteMerger(byte[] b1, byte[] b2) {
        byte[] b3 = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, b3, 0, b1.length);
        System.arraycopy(b2, 0, b3, b1.length, b2.length);
        return b3;
    }

    /**
     * Title: toBytes
     * Description:序列化对象
     *
     * @throws Exception
     * @author zhengzx
     */
    public static byte[] toBytes(Object out) throws Exception {
        //用于序列化后存储对象
        ByteArrayOutputStream byteArrayOutputStream = null;
        //java序列化API
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            //将out对象进行序列化
            objectOutputStream.writeObject(out);
            //测试验证输入（获取字节数组）
            byte[] bs = byteArrayOutputStream.toByteArray();
            //将数组转化为字符串输入
            //System.out.println(Arrays.toString(bs));
            return bs;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭最外层的流（内部流会自动关闭）
            objectOutputStream.close();
        }
        return null;
    }

    /*
     * Title: toPlay
     * Description:反序列化对象
     * @author zhengzx
     * @throws Exception
     */
    public static Object toObject(byte[] bs) throws Exception {
        //创建存放二进制数据的API
        ByteArrayInputStream byteArrayInputStream = null;
        //创建反序列化对象
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bs);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);

            //校验测试
//            Player player = (Player) objectInputStream.readObject();
//            System.out.println(player.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            objectInputStream.close();
        }
        return objectInputStream.readObject();
    }

    private static String toHexUtil(int n) {
        String rt = "";
        switch (n) {
            case 10:
                rt += "A";
                break;
            case 11:
                rt += "B";
                break;
            case 12:
                rt += "C";
                break;
            case 13:
                rt += "D";
                break;
            case 14:
                rt += "E";
                break;
            case 15:
                rt += "F";
                break;
            default:
                rt += n;
        }
        return rt;
    }

    public static String toHex(int n) {
        StringBuilder sb = new StringBuilder();
        if (n / 16 == 0) {
            return toHexUtil(n);
        } else {
            String t = toHex(n / 16);
            int nn = n % 16;
            sb.append(t).append(toHexUtil(nn));
        }
        return sb.toString();
    }

    public static String parseAscii(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (int i = 0; i < bs.length; i++) {
            sb.append(toHex(bs[i]));
        }
        return sb.toString();
    }

    public static String byteToBase64(byte[] bytes) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String base64 = base64Encoder.encode(bytes);
        return base64;
    }

    public static byte[] base64ToByte(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }

    /**
     * 16进制字符串转数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);

        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    /**
     * 字节数组转化为16进制字符串
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString1(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 字符串转化为字节数组
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes1(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d;
        //转换的时候应该注意的是单双数的情况，网上很多都只处理了双数的情况，也就是默认刚好填满，这样16进制字符串长度是单数的话会丢失掉一个字符 因为length/2 是舍去余数的
        if (hexString.length() % 2 != 0) {// 16进制字符串长度是单数
            length = length + 1;
            d = new byte[length];
            // 这里把byte数组从后往前填，字符串也是翻转着填的，最后会空出byte数组的第一个（用来填充我们单出来的那个字符）
            for (int i = length - 1; i > 0; i--) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) | charToByte(hexChars[pos - 1]) << 4);
            }
            d[0] = charToByte(hexChars[0]);
        } else {// 双数情况
            d = new byte[length];
            for (int i = 0; i < length; i++) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    /**
     * 十进制转换为十六进制字符串
     *
     * @param algorism int 十进制的数字
     * @return String 对应的十六进制字符串
     */
    public static String algorismToHEXString(int algorism) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;

        }
        result = result.toUpperCase();

        return result;
    }

    public static String yihuo(String accountNumber, Integer lastDeviceId) {
        StringBuilder sb = new StringBuilder();
        String c = algorismToHEXString(lastDeviceId);
        byte[] d = hexStringToBytes1(str2HexStr(accountNumber));
        for (int i = 0; i < d.length; i++) {
            Long e = hex2decimal(String.valueOf(c));
            String f = demical2Hex(d[i] ^ e);
            sb.append(f);
        }
        return sb.toString();
    }

    /**
     * 16进制转10进制
     *
     * @param hex
     * @return
     */
    public static Long hex2decimal(String hex) {
        return Long.valueOf(hex, 16);

    }

    /**
     * 10进制转16进制
     *
     * @param i
     * @return
     */
    public static String demical2Hex(Long i) {
        String s = Long.toHexString(i);
        return s;
    }

    /**
     * 16进制每俩位取反后转为十进制
     * @param hex
     * @return
     */
    public static Long hexDesToDemical(String hex) {
        StringBuffer buffer = new StringBuffer();
        for (int i = hex.length(); i > 0; i--) {
            if (i - 2 >= 0) {
                String bb = hex.substring(i-2,i);
                buffer.append(bb);
            }
            i--;
        }
        return hex2decimal(buffer.toString());
    }

    public static void main(String[] args) {

//        System.out.println("0x".concat(String.format("%02x",10)));
//        System.out.println(algorismToHEXString(10));
//        System.out.println(hexStringToBytes1(str2HexStr("test@192.168.110.200")));
//        Integer a = hex2decimal("74");
//        Integer b = hex2decimal("0A");
//        System.out.println(a+""+b);
//        System.out.println(a^b);
        System.out.println(yihuo("test@192.168.110.200", 10));
//        System.out.println(demical2Hex(162));
        System.out.println(demical2Hex(hex2decimal("B97A8800")));
        System.out.println(isEmpty("null"));
    }

}