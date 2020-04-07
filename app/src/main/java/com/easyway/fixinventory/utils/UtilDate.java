package com.easyway.fixinventory.utils;

import com.hanks.frame.utils.Ulog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 根据提供当前时间及给定的格式，返回时间字符串
 *
 * @author hanks7
 */
public class UtilDate {
    /**
     * 2019-12-04
     *
     * @return
     */
    public static long dateToNum(String strdate) {
        Ulog.i("dateToNum-strdate", strdate);
        if (strdate == null) {
            return 0;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long num = format.parse(strdate).getTime();
            Ulog.i("num", num);
            return num;
        } catch (ParseException e) {
            e.printStackTrace();
            Ulog.i("dateToNum-Catch", 0);
            return 0;
        }
    }

    /**
     * 返回当前日期时间字符串<br>
     * 将2016-06-02转成2016/6/2
     *
     * @return String 返回当前字符串型日期时间
     */
    public static String getFormDate(String dateTime) {
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar2.setTime(new SimpleDateFormat("yyyy-mm-dd")
                    .parse(dateTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy/m/d");
        Date date = new Date(calendar2.getTimeInMillis());
        String returnStr = f.format(date);
        return returnStr;
    }

}
