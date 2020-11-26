package com.easyway.business.framework.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    // 默认日期格式
    public static final String           DATE_FORMAT            = "yyyy-MM-dd";
    // 默认时间格式
    public static final String           DATETIME_FORMAT        = "yyyy-MM-dd HH:mm:ss";
    public static final String           DATETIME_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String           TIME_FORMAT            = "HH:mm:ss";

    public static final SimpleDateFormat NORM_YMD_FORMAT        = new SimpleDateFormat(DATE_FORMAT);
    public static final SimpleDateFormat NORM_YMDHM_FORMAT      = new SimpleDateFormat(DATETIME_MINUTE_FORMAT);
    public static final SimpleDateFormat NORM_YMDHMS_FORMAT     = new SimpleDateFormat(DATETIME_FORMAT);
    public static final SimpleDateFormat NORM_TIME_FORMAT       = new SimpleDateFormat(TIME_FORMAT);

    public static final SimpleDateFormat FORMAT_SHORT_DATE      = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat FORMAT_LONG_DATETIME   = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final long             MS                     = 1L;
    public static final long             SECOND_MS              = 1000L; // 一秒钟的毫秒数
    public static final long             MINUTE_MS              = 60000L; // 一分钟的毫秒数
    public static final long             HOUR_MS                = 3600000L; // 一小时的毫秒数
    public static final long             DAY_MS                 = 86400000L; // 一天的毫秒数

    /**
     * 获取当前日期
     * 
     * @return Timestamp
     */
    public static Timestamp currentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前日期
     * 
     * @return Date
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * @return 得到当前时间 yyyy-MM-dd
     */
    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String createTime = formatter.format(new Date());
        return createTime;
    }

    /**
     * @return 得到当前时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT);
        String createTime = formatter.format(new Date());
        return createTime;
    }

    /**
     * 当前日期，格式 yyyy-MM-dd
     *
     * @return 当前日期的标准形式字符串
     */
    public static String today() {
        return formatDate(new Date());
    }

    /**
     * 当前时间，格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串
     */
    public static String now() {
        return formatDateTime(new Date());
    }
    
    /**
     * 短日期格式字串yyyyMMdd
     * 
     * @return String
     */
    public static String getShortDateStr() {
        return FORMAT_SHORT_DATE.format(new Date());
    }

    /**
     * 长日期格式字串yyyyMMddHHmmss
     * 
     * @return String
     */
    public static String getLongDateStr() {
        return FORMAT_LONG_DATETIME.format(new Date());
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param date 被格式化的日期
     * @param format {@link SimpleDateFormat}
     * @return 格式化后的字符串
     */
    public static String format(Date date, DateFormat format) {
        if (null == format || null == date) {
            return null;
        }
        return format.format(date);
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param date 被格式化后的字符串
     * @param format {@link SimpleDateFormat}
     * @return 格式化的日期
     */
    public static Date format(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期时间<br>
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期
     */
    public static String formatDateTime(Date date) {
        if (null == date) {
            return null;
        }
        return NORM_YMDHMS_FORMAT.format(date);
    }

    /**
     * 格式化日期部分（不包括时间）<br>
     * 格式 yyyy-MM-dd
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
        if (null == date) {
            return null;
        }
        return NORM_YMD_FORMAT.format(date);
    }

    /**
     * 格式化时间<br>
     * 格式 HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串
     */
    public static String formatTime(Date date) {
        if (null == date) {
            return null;
        }
        return NORM_TIME_FORMAT.format(date);
    }

    /**
     * 格式化日期时间<br>
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期
     */
    public static Date formatDateTime(String date) {
        if (null == date) {
            return null;
        }
        return format(date, DATETIME_FORMAT);
    }

    /**
     * 格式化日期部分（不包括时间）<br>
     * 格式 yyyy-MM-dd
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串
     */
    public static Date formatDate(String date) {
        if (null == date) {
            return null;
        }
        return format(date, DATE_FORMAT);
    }

    /**
     * 时间转换为字符串yyyy-MM-dd
     * 
     * @param date
     * @return String
     */
    public static String ymdFormat(Date date) {
        if (date == null) {
            return null;
        }
        return NORM_YMD_FORMAT.format(date);
    }

    /**
     * 时间转换为字符串yyyy-MM-dd HH:mm
     * 
     * @param date
     * @return String
     */
    public static String ymdhmFormat(Date date) {
        if (date == null) {
            return null;
        }
        return NORM_YMDHM_FORMAT.format(date);
    }

    /**
     * 时间转换为字符串yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * @return String
     */
    public static String ymdhmsFormat(Date date) {
        if (date == null) {
            return null;
        }
        return NORM_YMDHMS_FORMAT.format(date);
    }

    /**
     * 毫秒数转化为日期
     * 
     * @param millionSeconds
     * @return String
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
     * @param subtrahend
     * @param minuend
     * @param diffField
     * @return 分钟
     */
    public static long dateDiff(Date subtrahend, Date minuend, long diffField) {
        long diff = minuend.getTime() - subtrahend.getTime();
        return diff / diffField;
    }

    /**
     * 根据日期增加天数
     * 
     * @param date 给定的日期
     * @param days 天数
     * @return 返回Date对象
     */
    public static Date dayAdd(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return new Date(cal.getTime().getTime());
    }

    /**
     * 获取指定日期N天后的时间
     * 
     * @param somedate
     * @param day
     * @return Date
     */
    public static Date getNextDay(Date somedate, int day) {
        if (somedate == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(somedate);
        cal.add(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTime().getTime());
    }

    /**
     * 获取N天后的时间
     * 
     * @param day
     * @return Timestamp
     */
    public static Timestamp getNextDay(int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, day);
        return new Timestamp(cal.getTime().getTime());
    }

    /**
     * 获得星期几(周日为1，周六为7)
     * 
     * @param date 给定日期
     * @return int
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得星期几（中文）
     * 
     * @param date
     * @return String
     */
    public static String getDayCN(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }
}
