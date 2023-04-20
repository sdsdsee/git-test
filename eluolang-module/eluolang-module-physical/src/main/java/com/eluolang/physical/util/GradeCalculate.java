package com.eluolang.physical.util;

import java.text.ParseException;

public class GradeCalculate {
    public static int gradeNumber(String enTime, int enGrade) {
        //判断有没有入学时间
        if (enTime == null) {
            return -1;
        }
        //计算年级
        //去掉时间的分隔符
        //入学时间
        String[] enTimes = enTime.split("-");
        StringBuffer stringBuffer = new StringBuffer(enTimes[0] + enTimes[1]);
        int year = 0;
        //判断是不是学生-1不是学生
        if (enGrade != -1) {
            String[] nowTime = new String[0];
            try {
                nowTime = CreateTime.timeYear().split("-");
//                nowTime = "2022-09".split("-");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            StringBuffer stringBufferNow = new StringBuffer(nowTime[0] + nowTime[1]);
            System.out.println(enTime);
            year = (Integer.parseInt(nowTime[0]) - Integer.parseInt(enTimes[0]));
            //清空
            stringBufferNow.setLength(0);
            stringBuffer.setLength(0);
            //默认
            enGrade = enGrade + year;
            //升级月份
            int upgradePoints = 8;
            //入学时间大于8月就减去一年
            if (year > 0 && Integer.parseInt(enTimes[1]) >= upgradePoints) {
                enGrade = enGrade - 1;
            }
            //只要现在的时间大于8月就加1年级，首先不是今年
            if (year > 0 && Integer.parseInt(nowTime[1]) >= upgradePoints) {
                enGrade = enGrade + 1;
            }
            //今年入学的情况入学时间小于8月的并且现在时间大于8月
            if (year == 0 && (Integer.parseInt(nowTime[1])) >= upgradePoints && Integer.parseInt(enTimes[1]) < upgradePoints) {
                enGrade = enGrade + 1;
            }
        }
        int number = 0;
        switch (enGrade) {
            case -1:
                number = 0;
                break;
            case 1:
                number = 11;
                break;
            case 2:
                number = 12;
                break;
            case 3:
                number = 13;
                break;
            case 4:
                number = 14;
                break;
            case 5:
                number = 15;
                break;
            case 6:
                number = 16;
                break;
            case 7:
                number = 21;
                break;
            case 8:
                number = 22;
                break;
            case 9:
                number = 23;
                break;
            case 10:
                number = 31;
                break;
            case 11:
                number = 32;
                break;
            case 12:
                number = 33;
                break;
            case 13:
                number = 41;
                break;
            case 14:
                number = 42;
                break;
            case 15:
                number = 43;
                break;
            case 16:
                number = 44;
                break;
        }
        return number;
    }

    public static int gradeCode(String grade) {
        int code = 11;
        switch (grade) {
            case "一年级":
                code = 11;
                break;
            case "二年级":
                code = 12;
                break;
            case "三年级":
                code = 13;
                break;
            case "四年级":
                code = 14;
                break;
            case "五年级":
                code = 15;
                break;
            case "六年级":
                code = 16;
                break;
            case "初一":
                code = 21;
                break;
            case "初二":
                code = 22;
                break;
            case "初三":
                code = 23;
                break;
            case "高一":
                code = 31;
                break;
            case "高二":
                code = 32;
                break;
            case "高三":
                code = 33;
                break;
            case "大一":
                code = 41;
                break;
            case "大二":
                code = 42;
                break;
            case "大三":
                code = 43;
                break;
            case "大四":
                code = 44;
                break;


        }
        return code;
    }

    public static void main(String[] args) {
        System.out.println(gradeNumber("2021-09", 1));
    }
}
