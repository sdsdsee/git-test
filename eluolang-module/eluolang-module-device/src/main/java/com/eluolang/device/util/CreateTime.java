package com.eluolang.device.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTime {
    public static String getTime() {
        Date d = new Date();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        //Date da = sdf.parse(dateNowStr);
        return dateNowStr;
    }

    public static String timeYear() throws ParseException {
        Date d = new Date();
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
    //时间蹉转时间
    public static String TimestampTransitionDate(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(timestamp);
        return sdf.format(new Date(lt));
    }

    public static void main(String[] args) {
        System.out.println(TimestampTransitionDate("1647944638559"));
    }
}
