package com.eluolang.common.core.util;

import com.eluolang.common.core.pojo.EllExamineeUser;
import com.eluolang.common.core.pojo.EllUser;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;
import java.util.stream.Collectors;

public class GroupingUtil {
    public static Map<Integer, List<EllUser>> sexGroup(List<EllUser> ellUserList){
        Map<Integer, List<EllUser>> collect = ellUserList.stream().collect(Collectors.groupingBy(EllUser::getUserSex));
        return collect;
    }
    public static String randomTestNumber(Integer sex){
        String date = String.valueOf(DateUtils.getCurrentTime());
//        int len = Integer.toString(count).length();
        String str= date + sex + RandomNumberGenerator.generateNumber();
        return str;
    }
    public static void main(String[] args){
//        Integer num = 50;
//        int len = Integer.toString(num).length();
//        String str=String.format("%0"+len+"d",1);
//        System.out.println(randomTestNumber(50,1,1));
        List<EllUser> ellUsers = new ArrayList<>();
        for (int i = 0; i < 59; i++) {
            EllUser ellUser = new EllUser();
            ellUser.setUserName(String.valueOf(i));
            ellUser.setUserSex(1);
            ellUsers.add(ellUser);
        }
//        EllUser ellUser = new EllUser();
//        ellUser.setUserName("1111");
//        ellUser.setUserSex(1);
//        EllUser ellUser1 = new EllUser();
//        ellUser1.setUserName("2222");
//        ellUser1.setUserSex(2);
//        EllUser ellUser2 = new EllUser();
//        ellUser2.setUserName("3333");
//        ellUser2.setUserSex(2);
//        EllUser ellUser6 = new EllUser();
//        ellUser6.setUserName("4444");
//        ellUser6.setUserSex(2);
//        EllUser ellUser7 = new EllUser();
//        ellUser7.setUserName("5555");
//        ellUser7.setUserSex(2);
//        EllUser ellUser8 = new EllUser();
//        ellUser8.setUserName("6666");
//        ellUser8.setUserSex(2);
//        ellUsers.add(ellUser2);
//        ellUsers.add(ellUser6);
//        ellUsers.add(ellUser7);
//        ellUsers.add(ellUser8);
//        ellUsers.add(ellUser1);

        System.out.println("打乱前："+ellUsers);
        Collections.shuffle(ellUsers);
        System.out.println("打乱后："+ellUsers);
//        Map<Integer, List<EllUser>> map = sexGroup(ellUsers);
//        List<EllUser> ellUsers1 = new ArrayList<>();
//        List<EllUser> ellUsers2 = new ArrayList<>();
//        for (Integer key:map.keySet()) {
//            if (key == 1){
//                for (int i = 0; i < map.get(key).size(); i++) {
//                    ellUsers1.add(map.get(key).get(i));
//                }
//            }else {
//                for (int i = 0; i < map.get(key).size(); i++) {
//                    ellUsers2.add(map.get(key).get(i));
//                }
//            }
//        }
        List<List<EllUser>> test = test(ellUsers, 40);
        int sort1 = ((test.size()-1)*40)+test.get(test.size()-1).size();
        for (int i = 0; i < test.size(); i++) {
            for (int j = 0; j < test.get(i).size(); j++) {
                EllExamineeUser ellExamineeUser = new EllExamineeUser();
                ellExamineeUser.setGroupId("51");
                ellExamineeUser.setId("feew");
                ellExamineeUser.setStuId(test.get(i).get(j).getId());
                ellExamineeUser.setTestNumber("ewfwegeg");
                System.out.println("考生：" + ellExamineeUser);
            }
        }
        System.out.println("result:" + test);
    }

    public static List test(List list, Integer n) {//n代表每组多少人
        // 求余数
        int yushu = list.size() % n;
        // 求分组数
        int count0 = list.size() / n;
        boolean flag = false;
        if(yushu==0){
            flag=true;
        }

        List data = new ArrayList<>();
        // 获取多余list
        List sub = new ArrayList(list.subList(list.size() - yushu , list.size()));
        // 遍历list到余数前
        for (int i = 0; i < count0+1; i++) {
            List ls;
            if(flag){
                if(i==count0){
                    break;
                }
                ls = new ArrayList(list.subList(i * n, i * n + n));
                // 将余出的数据加入分组
                if (i < sub.size()) {
                    ls.add(sub.get(i));
                }
            }else{
                int endex = i * n + n;
                if(endex>=list.size()){
                    endex=list.size();
                }
                ls = new ArrayList(list.subList(i * n, endex));
            }
            data.add(ls);
        }
        return data;
    }

}
