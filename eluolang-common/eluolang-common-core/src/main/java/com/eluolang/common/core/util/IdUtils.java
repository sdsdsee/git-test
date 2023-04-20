package com.eluolang.common.core.util;

import com.eluolang.common.core.text.UUID;

/**
 * ID生成类
 *
 * @author suziwei
 * @date 2020/8/24
 */
public class IdUtils {
    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID() {
        String nowTime = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.fastUUID().toString().substring(0, 7);
        return nowTime + uuid;
    }

    /**
     * 生成版本文件id
     * @return
     */
    public static String getVersionId() {
        String nowTime = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.fastUUID().toString().substring(0, 11);
        return nowTime + uuid;
    }

    public static void main(String[] args) {
        System.out.println(fastSimpleUUID());
		String genHouseHoldIdx = genHexId(255);
		System.out.println("genHouseHoldIdx is "+simpleUUID());

    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }

    /**
     * 将10进制的值转换为16进制，总长度为8位，不足8位前面补零
     * @param src 待转换的10进制值
     * @return 长度为8的16进制值
     */
    public static String genHexId(Integer src) {
    	String result = "";
    	String hexString = Integer.toHexString(src);
    	for (int i = hexString.length(); i < 8; i++) {
    		result += 0;
		}
    	result += hexString;
    	return result;
    }
//    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10000000; i++) {
//            list.add(genRandomNum());
//        }
//        Set<String> stringSet = new  HashSet<String>();
//        stringSet.addAll(list);
//        System.out.println(list.size());
//        System.out.println(stringSet.size());
//    }
//    public static String[] chars = new String[]{"A","B","C","D","E","F","G","H","I","0"
//            ,"1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i"};
//    public static String genRandomNum(){
//        StringBuffer sb = new StringBuffer();
//        String uuid = UUID.randomUUID().toString().replace("-","");
//        for (int i = 0; i < 8 ; i++) {
//            String str = uuid.substring(i*4,i*4+4);
//            int x = Integer.parseInt(str,16);
//            sb.append(chars[x % 28]);
//        }
//        return sb.toString();
//    }
}
