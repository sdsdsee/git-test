package com.eluolang.playground.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年10月20日 10:53
 */
public class MileageUtil {
    public static List<String> Mileage(Integer mileage){
        List<String> list = new ArrayList<>();
        list.add("0");
        Integer yu = mileage%1000;
        if (yu != 0){
            Integer num = mileage - yu;
            if (num != 0){
//                list.add(String.valueOf(yu));
//            }else {
                Integer count = num / 1000;
                for (int i = 0; i < count; i++){
                    list.add(String.valueOf((i+1)*1000));
//                    if (i+1 == count){
//                        list.add(String.valueOf((i+1)*1000+yu));
//                    }
                }
            }
        }else {
            Integer count = mileage / 1000;
            for (int i = 0; i < count; i++){
                list.add(String.valueOf((i+1)*1000));
            }
        }
        return list;
    }
}
