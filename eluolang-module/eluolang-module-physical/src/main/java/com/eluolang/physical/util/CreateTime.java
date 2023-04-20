package com.eluolang.physical.util;

import com.eluolang.common.core.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateTime {
    public static String getTime() {
        Date d = new Date();
        //System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        //Date da = sdf.parse(dateNowStr);
        return dateNowStr;
    }

    public static String getYear() {
        Date d = new Date();
        //System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String dateNowStr = sdf.format(d);
        //Date da = sdf.parse(dateNowStr);
        return dateNowStr;
    }

    public static String getMonth() {
        Date d = new Date();
        //System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
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
    //本月一号
    public static Date nowMonth() {
        Calendar calender = Calendar.getInstance();
        calender.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calender.set(Calendar.DAY_OF_MONTH,calender.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date date = calender.getTime();
        return date;
    }
    //下月1号
    public static Date nextMonth() {
        Calendar calender = Calendar.getInstance();
        calender.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calender.set(Calendar.DAY_OF_MONTH,calender.getActualMaximum(Calendar.DAY_OF_MONTH));
        calender.add(Calendar.DAY_OF_MONTH,1);
        Date date = calender.getTime();
        return date;
    }
    //本周一时间
    public static Date nowMonday() {
        Calendar calender = Calendar.getInstance();
        calender.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date date = calender.getTime();
        return date;
    }
    //下周一
    public static Date nextMonday() {
        Calendar calender = Calendar.getInstance();
        calender.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calender.add(Calendar.DATE, 7);
        Date date = calender.getTime();
        return date;
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
    } //时间转时间蹉

    public static Long dateTransitionTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(timestamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }//小时转时间蹉

    public static Long hourTransitionTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            return sdf.parse(timestamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //转时间蹉
    public static Long yearTransitionTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(timestamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //到秒
    public static Long monthTransitionTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            return sdf.parse(timestamp).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取时间字符串中的年月
    public static Map<String, Integer> getStrTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Integer> stringMap = new HashMap<>();
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // 获取年份和月份和
            int Year = calendar.get(Calendar.YEAR);
            int Month = calendar.get(Calendar.MONTH) + 1;
            stringMap.put("year", Year);
            stringMap.put("month", Month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringMap;
    }

    //时间段内的月份
    public static List<String> someTime(String time) {
   /*     String y1 = "2021-11";// 开始时间
        String y2 = "2022-05";// 结束时间*/
        //
        List<String> list = new ArrayList<String>();
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM").parse(time); //开始时间 y1 2021-11
            Date endDate = new Date();  //结束时间 当前时间  2022-05

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            // 获取开始年份和开始月份
            int startYear = calendar.get(Calendar.YEAR);
            int startMonth = calendar.get(Calendar.MONTH);
            // 获取结束年份和结束月份
            calendar.setTime(endDate);
            int endYear = calendar.get(Calendar.YEAR);
            int endMonth = calendar.get(Calendar.MONTH);
            for (int i = startYear; i <= endYear; i++) {
                String date = "";
                if (startYear == endYear) {
                    for (int j = startMonth; j <= endMonth; j++) {
                        if (j < 9) {
                            date = i + "-0" + (j + 1);
                        } else {
                            date = i + "-" + (j + 1);
                        }
                        list.add(date);
                    }

                } else {
                    if (i == startYear) {
                        for (int j = startMonth; j < 12; j++) {
                            if (j < 9) {
                                date = i + "-0" + (j + 1);
                            } else {
                                date = i + "-" + (j + 1);
                            }
                            list.add(date);
                        }
                    } else if (i == endYear) {
                        for (int j = 0; j <= endMonth; j++) {
                            if (j < 9) {
                                date = i + "-0" + (j + 1);
                            } else {
                                date = i + "-" + (j + 1);
                            }
                            list.add(date);
                        }
                    } else {
                        for (int j = 0; j < 12; j++) {
                            if (j < 9) {
                                date = i + "-0" + (j + 1);
                            } else {
                                date = i + "-" + (j + 1);
                            }
                            list.add(date);
                        }
                    }

                }

            }

            // 所有的月份已经准备好
            //System.out.println(list);
      /*      for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(DateUtils.timeStamp2Date("1665464628", DateUtils.YYYY_MM_DD_HH_MM_SS));
    }
}
