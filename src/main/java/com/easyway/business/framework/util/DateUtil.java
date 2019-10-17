package com.easyway.business.framework.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    // 默认日期格式
    public static final String           YMD_FORMAT     = "yyyy-MM-dd";
    // 默认时间格式
    public static final String           YMDHMS_FORMAT  = "yyyy-MM-dd HH:mm:ss";
    public static final String           YMDHM_FORMAT   = "yyyy-MM-dd HH:mm";
    public static final String           HMS_FORMAT     = "HH:mm:ss";
    
    public static final SimpleDateFormat FORMAT_YMD           = new SimpleDateFormat(YMD_FORMAT);
    public static final SimpleDateFormat FORMAT_YMDHM         = new SimpleDateFormat(YMDHM_FORMAT);
    public static final SimpleDateFormat FORMAT_YMDHMS        = new SimpleDateFormat(YMDHMS_FORMAT);

    public static final SimpleDateFormat SHORT_DATE_FORMAT    = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat LONG_DATETIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    
    public static final long             MS             = 1L;
    public static final long             SECOND_MS      = 1000L;// 一秒钟的毫秒数
    public static final long             MINUTE_MS      = 60000L;// 一分钟的毫秒数
    public static final long             HOUR_MS        = 3600000L;// 一小时的毫秒数
    public static final long             DAY_MS         = 86400000L;// 一天的毫秒数
    
    /**
     * 
     * @return
     */
    public static Timestamp currentTime() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * 获取当前日期
     * 
     * @return
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * @return
     * @description 得到当前时间 yyyy-MM-dd
     */
    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(YMD_FORMAT);
        String createTime = formatter.format(new Date());
        return createTime;
    }

    /**
     * @return
     * @description 得到当前时间 yyyy-MM-dd HH:mm:ss
     */
    public static String now() {
        return getCurrentTime();
    }
    
    /**
     * @return
     * @description 得到当前时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(YMDHMS_FORMAT);
        String createTime = formatter.format(new Date());
        return createTime;
    }
    
    /**
     * 短日期格式字串yyyyMMdd
     * 
     * @param date
     * @return
     */
    public static String getShortDateStr() {
        return SHORT_DATE_FORMAT.format(new Date());
    }

    /**
     * 长日期格式字串yyyyMMddHHmmss
     * 
     * @return
     */
    public static String getLongDateStr() {
        return LONG_DATETIME_FORMAT.format(new Date());
    }
    
    /**
     * 日期格式化
     * 
     * @param date
     * @param format
     * @return
     */
    public static Date formatDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 时间转换为字符串yyyy-MM-dd
     * 
     * @param date
     * @return
     */
    public static String ymdFormat(Date date) {
        if (date == null) {
            return null;
        }
        return FORMAT_YMD.format(date);
    }
    
    /**
     * 时间转换为字符串yyyy-MM-dd HH:mm
     * 
     * @param date
     * @return
     */
    public static String ymdhmFormat(Date date) {
        if (date == null) {
            return null;
        }
        return FORMAT_YMDHM.format(date);
    }
    
    /**
     * 时间转换为字符串yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static String ymdhmsFormat(Date date) {
        if (date == null) {
            return null;
        }
        return FORMAT_YMDHMS.format(date);
    }

    /**
     * 毫秒数转化为日期
     * 
     * @param millionSeconds
     * @return
     */
    public static String ymdhmsFormat(long millionSeconds) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        Date date = c.getTime();
        return ymdhmsFormat(date);
    }
    
    /**
     * 将日期字符串转化为日期。失败返回null。
     * 
     * @param date 日期字符串
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date stringToDate(String date, String pattern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = new SimpleDateFormat(pattern).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }
    
    /**
     * 将日期转化为日期字符串。失败返回null。
     * 
     * @param date 日期
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String pattern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = new SimpleDateFormat(pattern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }
    
    /**
     * 比较两个时间数相差多少分钟
     * 
     * @param begin
     * @param end
     * @return
     */
    public static long dateDiff(Date subtrahend, Date minuend, long diffField) {
        long diff = minuend.getTime() - subtrahend.getTime();
        return diff / diffField;
    }
    
    /**
     * 根据日期增加天数
     * 
     * @param date: 给定的日期
     * @param days: 天数
     * @return 返回Date对象
     */
    public static Date getDayAdd(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return new Date(cal.getTime().getTime());
    }

    /**
     * 将日期往后加几天
     * 
     * @param somedate
     * @param day
     * @return
     */
    public static Date getDateAfterDay(Date somedate, int day) {
        if (somedate == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(somedate);
        cal.add(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTime().getTime());
    }

    public static Timestamp getDateAfterDay(int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, day);
        return new Timestamp(cal.getTime().getTime());
    }
    
}
