package com.eluolang.module.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateTime {
    //带秒的时间
    public static String getTime() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        //Date da = sdf.parse(dateNowStr);
        return dateNowStr;
    }

    //不带分秒的时间
    public static Date time(Date date) throws ParseException {
        if (date == null) {
            return date;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(date);
        Date da = sdf.parse(dateNowStr);
        return da;
    }

    //不带分秒的时间
    public static String timeDay() throws ParseException {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        //Date da = sdf.parse(dateNowStr);
        return dateNowStr;
    }

    //不带天
    public static String timeYear() throws ParseException {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateNowStr = sdf.format(d);
        //Date da = sdf.parse(dateNowStr);
        return dateNowStr;
    }

    //获取时间蹉
    public static Long getTimestamp(Date date) {
        if (null == date) {
            return (long) 0;
        }
        String timestamp = String.valueOf(date.getTime());
        return Long.valueOf(timestamp);
    }

    //string时间去除分秒
    public static String excludeMinuteAndSecond(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }

    //获取星期几
    public static int dateToWeek(String dateString) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        //因为数组下标从0开始，而返回的是数组的内容，是数组{1,2,3,4,5,6,7}中用1~7来表示，所以要减1
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            week = 7;
        }
        return week;
    }

    //获取这一周的日期
    public static List<String> dateWeek(String dateString) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        //因为数组下标从0开始，而返回的是数组的内容，是数组{1,2,3,4,5,6,7}中用1~7来表示，所以要减1
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            week = 7;
        }
        //回到星期一的当天时间所以还有减1
        cal.add(Calendar.DAY_OF_WEEK, -(week - 1));
        List<String> listWeekDate = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                listWeekDate.add(simpleDateFormat.format(cal.getTime()));
            } else {
                cal.add(Calendar.DAY_OF_WEEK, 1);
                listWeekDate.add(simpleDateFormat.format(cal.getTime()));
            }
        }
        return listWeekDate;
    }

    //生成最近几年的年（包括本年）
    public static List<String> yearCalendar(int yearNum) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        cal.setTime(new Date());
        List<String> listYearDate = new ArrayList<>();
        for (int i = 0; i < yearNum; i++) {
            if (i == 0) {
                cal.add(Calendar.YEAR, 0);
                listYearDate.add(simpleDateFormat.format(cal.getTime()));
            } else {
                cal.add(Calendar.YEAR, -1);
                listYearDate.add(simpleDateFormat.format(cal.getTime()));
            }
        }
        //排序
        Collections.sort(listYearDate);
        return listYearDate;
    }

    //获取这一段时间的日期/比如前15天后15天的日期（包括本天）
    public static List<String> dateCalendar(int dayNum) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(new Date());
        List<String> listWeekDate = new ArrayList<>();
        for (int i = 0; i < Math.abs(dayNum); i++) {
            if (dayNum >= 0) {
                //本天
                if (i == 0) {
                    cal.add(Calendar.DAY_OF_MONTH, 0);
                    listWeekDate.add(simpleDateFormat.format(cal.getTime()));
                } else {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    listWeekDate.add(simpleDateFormat.format(cal.getTime()));
                }
            } else {
                //本天
                if (i == 0) {
                    cal.add(Calendar.DAY_OF_MONTH, 0);
                    listWeekDate.add(simpleDateFormat.format(cal.getTime()));
                } else {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    listWeekDate.add(simpleDateFormat.format(cal.getTime()));
                }
            }
        }
        //排序
        Collections.sort(listWeekDate);
        return listWeekDate;
    }

    //计算年龄
    public static int getAge(Date birthDay) throws Exception {

        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算

            throw new IllegalArgumentException(

                    "The birthDay is before Now.It's unbelievable!");

        }

        int yearNow = cal.get(Calendar.YEAR); //当前年份

        int monthNow = cal.get(Calendar.MONTH); //当前月份

        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期

        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);

        int monthBirth = cal.get(Calendar.MONTH);

        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth; //计算整岁数

        if (monthNow <= monthBirth) {

            if (monthNow == monthBirth) {

                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一

            } else {

                age--;//当前月份在生日之前，年龄减一

            }
        }
        return age;
    }

    public static void main(String[] args) {
        System.out.println(yearCalendar(8));
    }
}
