package com.eluolang.common.core.util;


import com.eluolang.common.core.constant.SystemStatus;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间转换类
 *
 * @author suziwei
 * @date 2020/8/21
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || "null".equals(seconds)) {
            return SystemStatus.ERROR;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp3Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || "null".equals(seconds)) {
            return SystemStatus.ERROR;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 获取当前时间前一天日期
     * <p>
     * 2021年5月20日 13:11:05
     */
    public static String getNowDateBeforeOne() {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        //ca.add(Calendar.YEAR, -1); //年份减1
        ca.add(Calendar.DATE, -1); //前一天
        Date lastMonth = ca.getTime(); //结果
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        String res = simpleDateFormat.format(lastMonth);
        return res;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date3TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    public static String getYear() {
        return dateTimeNow(YYYY);
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd0
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String nowDate() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个时间差(小时)
     */
    public static BigDecimal getDatePoorHours(Date endDate, Date nowDate) {
        long nh = 1000 * 60 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少小时
        return new BigDecimal(diff).divide(new BigDecimal(nh), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算两个时间差(分钟)
     */
    public static BigDecimal getDatePoorMin(Date endDate, Date nowDate) {
        long nh = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少小时
        return new BigDecimal(diff).divide(new BigDecimal(nh), 2, BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 秒转分(xx'xx")
     */
    public static String getSecToMin(Integer time) {
        String mSec;
        String mMin;
        // 秒数
        int sec = time % 60;
        // 分钟数
        int min = (time - sec) / 60;
        // 如果秒数没超过10就在他前面加个0
        if (sec < 10) {
            mSec = "0" + sec;
        } else {
            mSec = String.valueOf(sec);
        }
        // 如果分钟数没超过10就在他前面加个0
        if (min < 10) {
            mMin = "0" + min;
        } else {
            mMin = String.valueOf(min);
        }
       return mMin + "'" + mSec + "\"";
    }
    /**
     * 获取当前时间  毫秒级别
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间  毫秒级别
     * @return
     */
    public static String getMillisecondTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        return format.format(new Date());
    }

    /**
     * 确定当前时间来获取该时间的上个月的月份
     */
    public static String getLastMonth() {
        String lastMonth = StringUtils.EMPTY;
        try {
            String s = DateUtils.getMillisecondTime();
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM");
            Date date2 = simple.parse(s);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date2);
            calendar.add(calendar.DATE, -1);
            lastMonth = simple.format(calendar.getTime());//上个月
        } catch (Exception e) {
            return lastMonth;
        }
        return lastMonth;
    }

    /**
     * 获取上个月的第一天
     */
    public static String getLastMonthFirstday() {
        String lastMonthFirstday = StringUtils.EMPTY;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            lastMonthFirstday = DateUtils.dateTime(calendar.getTime());
        } catch (Exception e) {
            return lastMonthFirstday;
        }
        return lastMonthFirstday;
    }
    /**
     * 获取传入月的第一天
     */
    public static String getMonthFirstDay(String date) {
        String lastMonthFirstday = StringUtils.EMPTY;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(date,DateUtils.YYYY_MM));
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            lastMonthFirstday = DateUtils.dateTime(calendar.getTime());
        } catch (Exception e) {
            return lastMonthFirstday;
        }
        return lastMonthFirstday;
    }
    /**
     * 获取传入月的最后一天
     */
    public static String getMonthLastDay(String date) {
        String lastMonthFirstday = StringUtils.EMPTY;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(date,DateUtils.YYYY_MM));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            lastMonthFirstday = DateUtils.dateTime(calendar.getTime());
        } catch (Exception e) {
            return lastMonthFirstday;
        }
        return lastMonthFirstday;
    }

    /**
     * 获取上个月的最一天
     */
    public static String getLastMonthLastoneday() {
        String lastMonthLastoneday = StringUtils.EMPTY;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            lastMonthLastoneday = format.format(calendar.getTime());
        } catch (Exception e) {
            return lastMonthLastoneday;
        }
        return lastMonthLastoneday;
    }

    /**
     * 计算两个的天数
     */
    public static Integer getDays(Date endDate, Date nowDate) {
        int diff = (int) ((endDate.getTime() - nowDate.getTime()) / (1000 * 3600 * 24) + 1);
        return diff;
    }

    /**
     * 获取指定月份的天数
     * <p>
     * 2021年5月20日 13:11:05
     * @param date 指定日期
     * @return Integer
     */
    public static Integer getMonthDays(String date) {
        Calendar a = Calendar.getInstance();
        a.setTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD, date));
        //把日期设置为当月第一天
        a.set(Calendar.DATE, 1);
        //日期回滚一天，也就是最后一天
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);

        return maxDate;
    }

    /**
     * 获取指定日期
     * <p>
     * 2021年5月20日 13:11:05
     * @param date 指定日期
     * @return Integer
     */
    public static Date getLastDays(int date) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, date);
        Date start = c.getTime();
        return start;
    }

    /**
     * 获取指定日期
     * <p>
     * 2021年5月20日 13:11:05
     * @param date 指定日期
     * @return Integer
     */
    public static Long getToDayTime(String date) throws ParseException {
        String[] s = date.split(":");
        Integer hour = Integer.valueOf(s[0]);
        Integer fen = Integer.valueOf(s[1]);
        Integer miao = Integer.valueOf(s[2]);
        Integer time = hour*3600 + fen*60 + miao;
        return Long.valueOf(time);
    }


    /**
     * 获取指定日期
     * <p>
     * 2021年5月20日 13:11:05
     * @param date 指定日期
     * @return Integer
     */
    public static String getToDayTime1(String date) {
        String[] s = date.split(":");
        return s[0]+":"+s[1];
    }

    /**
     * 获取指定日期
     * <p>
     * 2021年5月20日 13:11:05
     * @param date 指定日期
     * @return Integer
     */
    public static Long getHourDays(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date dates = simpleDateFormat.parse(date);
        long ts = dates.getTime()/1000;
        return ts;
    }


    /**
     * 比较两个时间 时分秒 大小
     * @param s1
     * @param s2
     * @return
     */
    public static boolean compTime(String s1,String s2){
        try {
            if (s1.indexOf(":")<0||s2.indexOf(":")<0) {
                System.out.println("格式不正确");
            }else{
                String[]array1 = s1.split(":");
                int total1 = Integer.valueOf(array1[0])*3600+Integer.valueOf(array1[1])*60;
                String[]array2 = s2.split(":");
                int total2 = Integer.valueOf(array2[0])*3600+Integer.valueOf(array2[1])*60;
                return total1-total2>0?true:false;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            return true;
        }
        return false;

    }


    /**
     * 获取指定日期
     * <p>
     * 2021年5月20日 13:11:05
     * @param date 指定日期
     * @return Integer
     */
    public static String getHourDaysToStr(Long date) {
        Date date1 = new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String res = simpleDateFormat.format(date1);
        return res;
    }

    /**
     * 当天的近七天日期
     * @return
     */
    public static List<String> getNearSevenDay() {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7 ; i++) {
            Date date = DateUtils.addDays(new Date(), -(i+1));
            String formatDate = sdf.format(date);
            list.add(formatDate);
        }
        return list;
    }
    /**
     * 当天的近十天日期
     * @return
     */
    public static List<String> getNearTenDay() {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10 ; i++) {
            Date date = DateUtils.addDays(new Date(), -(i+1));
            String formatDate = sdf.format(date);
            list.add(formatDate);
        }
        return list;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getToDayTime("23:59:59"));

        System.out.println(getHourDaysToStr(0L));
    }
}
