package com.eluolang.physical.util;

import java.util.HashMap;
import java.util.Map;

public class IdCardUtil {
    public static void main(String[] args) {
        System.out.println(identificationString("350128200802220119"));
    }

    public static String isSex(String idCard) {
        String sex = String.valueOf(idCard.charAt(16));
        if (Integer.parseInt(sex) % 2 == 0) {
            sex = "2";
        } else {
            sex = "1";
        }
        return sex;
    }

    public static String getBirth(String idCard) {
        String year = String.valueOf(idCard.substring(6,10));
        String moth = String.valueOf(idCard.substring(10, 12));
        String day = String.valueOf(idCard.substring(12, 14));
        String birth = year + "-" + moth + "-" + day;
        return birth;
    }

    public static Map<String, String> getIdCardInfo(String idCard) {
        Map<String, String> idCardInfo = new HashMap<>();
        idCardInfo.put("sex", isSex(idCard));
        idCardInfo.put("birth", getBirth(idCard));
        return idCardInfo;
    }

    //截取前6位和后4位
    public static String identificationString(String idCard) {
        String front = idCard.substring(0, 6);//ǰ
        String queen = idCard.substring(14, 18);//��
        return front + queen;
    }

}
