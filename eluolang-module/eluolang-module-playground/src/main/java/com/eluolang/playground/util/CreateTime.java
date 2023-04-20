package com.eluolang.playground.util;

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
    public static String timeDayMoth() throws ParseException {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
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
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date date = calender.getTime();
        return date;
    }

    //下月1号
    public static Date nextMonth() {
        Calendar calender = Calendar.getInstance();
        calender.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH));
        calender.add(Calendar.DAY_OF_MONTH, 1);
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

    //获取前面第几周或后面第几周的周一日期
    public static String weekMonday(int week) {
        Calendar calender = Calendar.getInstance();
        calender.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calender.add(Calendar.DATE, week * 7);
        Date date = calender.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
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

    //时间转时间蹉到分钟
    public static Long dateTransitionTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.parse(timestamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //小时转时间蹉稻苗
    public static Long hourTransitionTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            return sdf.parse(timestamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //小时转时间蹉到分钟
    public static Long hourTransitionTimestampMinutes(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            return sdf.parse(timestamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取小时时间蹉
    public static Long getHourTransitionTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            System.out.println(sdf.format(new Date()));
            return sdf.parse(sdf.format(new Date())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取天时间蹉
    public static Long getDayTransitionTimestamp(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(day).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //将时间格式化为天
    public static String timeToDay(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(sdf.parse(time));
    }

    /**
     * 传入两个时间范围，返回这两个时间范围内的所有日期，并保存在一个集合中
     *
     * @param beginTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public static List<String> findEveryDay(String beginTime, String endTime)
            throws Exception {
        //创建一个放所有日期的集合
        List<String> dates = new ArrayList();
        //创建时间解析对象规定解析格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //将传入的时间解析成Date类型,相当于格式化
        Date dBegin = sdf.parse(beginTime);
        Date dEnd = sdf.parse(endTime);
        //将格式化后的第一天添加进集合
        dates.add(sdf.format(dBegin));
        //使用本地的时区和区域获取日历
        Calendar calBegin = Calendar.getInstance();
        //传入起始时间将此日历设置为起始日历
        calBegin.setTime(dBegin);
        //判断结束日期前一天是否在起始日历的日期之后
        while (dEnd.after(calBegin.getTime())) {
            //根据日历的规则:月份中的每一天，为起始日历加一天
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            //得到的每一天就添加进集合
            dates.add(sdf.format(calBegin.getTime()));
            //如果当前的起始日历超过结束日期后,就结束循环
        }
        return dates;
    }

    /**
     * 获取两个日期之间的所有月份 (年月)
     *
     * @param startTime
     * @param endTime
     * @return：list
     */
    public static List<String> getMonthBetweenDate(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {

                // 把日期添加到集合
                list.add(sdf.format(startDate));

                // 设置日期
                calendar.setTime(startDate);

                //把月数增加 1
                calendar.add(Calendar.MONTH, 1);

                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取两个日期之间的所有年
     *
     * @param startTime
     * @param endTime
     * @return：list
     */
    public static List<String> getYearBetweenDate(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {

                // 把日期添加到集合
                list.add(sdf.format(startDate));

                // 设置日期
                calendar.setTime(startDate);

                //把年数增加 1
                calendar.add(Calendar.YEAR, 1);

                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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
        List<String> list = new ArrayList<String>();
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM").parse(time);
            Date endDate = new Date();
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

    //获取指定日期是星期几
    public static Map<Integer, List<String>> getDayOfTheWeek(List<String> dateTimes) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<Integer, List<String>> dayMap = new HashMap<>();
        //星期天
        dayMap.put(0, new ArrayList<String>());
        //星期1
        dayMap.put(1, new ArrayList<String>());
        //星期2
        dayMap.put(2, new ArrayList<String>());
        //星期3
        dayMap.put(3, new ArrayList<String>());
        //星期4
        dayMap.put(4, new ArrayList<String>());
        //星期5
        dayMap.put(5, new ArrayList<String>());
        //星期6
        dayMap.put(6, new ArrayList<String>());
        for (int i = 0; i < dateTimes.size(); i++) {
            //把指定时间放入日历组件
            Date startDate = simpleDateFormat.parse(dateTimes.get(i));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            dayMap.get(index).add(dateTimes.get(i));
//            String[] weeks = new String[]{"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
//            System.out.println("今天是" + weeks[index]);
        }
        return dayMap;
    }

    //获取指定日期是星期几
    public static int getDayOfTheWeek(String dateTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //把指定时间放入日历组件
        Date startDate = simpleDateFormat.parse(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return index;
    }

    //获取指定时间那周的的起始时间戳（按周一为第一天）
    public static Long[] getCurrentWeekTimeFrame(String dateString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置一周的开始时间是星期几
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //start of the week开始时间戳
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        calendar.add(Calendar.DAY_OF_WEEK, -(calendar.get(Calendar.DAY_OF_WEEK) - 2));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        //end of the week结束时间戳
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTime = calendar.getTimeInMillis();
        return new Long[]{startTime, endTime};
    }

    //获取指定时间那周的的起始时间戳（按周日为第一天）
    public static Long[] getCurrentWeekTimeFrameSunday(String dateString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //start of the week开始时间戳
//        calendar.add(Calendar.DAY_OF_WEEK, -(calendar.get(Calendar.DAY_OF_WEEK) - 1));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startTime = calendar.getTimeInMillis();
        //end of the week结束时间戳
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTime = calendar.getTimeInMillis();
        return new Long[]{startTime, endTime};
    }

    public static void main(String[] args) throws Exception {
        Long[] weekTimeStamp = CreateTime.getCurrentWeekTimeFrame(CreateTime.weekMonday(0));
        List<String> day = CreateTime.findEveryDay(weekTimeStamp[0] / 1000 + "", weekTimeStamp[1] / 1000 + "");
    }
}
