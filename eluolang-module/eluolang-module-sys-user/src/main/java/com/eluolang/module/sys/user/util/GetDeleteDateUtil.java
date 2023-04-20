package com.eluolang.module.sys.user.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ZengXiaoQian
 * @createDate 2020-9-4
 */
public class GetDeleteDateUtil {
    public static String getDeleteDate(Date now, int days){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE,days);
        Date delete=calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dateString=sdf.format(delete);
        return dateString;
    }
}
