package com.eluolang.platform.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateTime {
    public static String getTime() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        //Date da = sdf.parse(dateNowStr);
        return dateNowStr;
    }

    public static Date time(Date date) throws ParseException {
        if (date == null) {
            return date;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(date);
        Date da = sdf.parse(dateNowStr);
        return da;
    }

    public static String timeDay() throws ParseException {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        //Date da = sdf.parse(dateNowStr);
        return dateNowStr;
    }

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

    //去掉时分秒年
    public static String takeOutTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = year.parse(time);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //时转为时间戳
    public static Long enTimeDivision(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date date = simpleDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //时分时间戳转为字符串
    public static String deTimeDivision(Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date1 = new Date(time);
        return simpleDateFormat.format(date1);
    }

    //格式话时间
    public static String deTimeFormatting(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date(time);
        return simpleDateFormat.format(date1);
    }

    public static void main(String[] args) {
        System.out.println(takeOutTime("2022-06-09"));
    }
}
