package com.easyway.business.framework.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class Constant {

    /**
     * 返回JSON时指定日期属性格式化@JsonFormat(pattern="yyyy-MM-dd")
     */
    public static final String                  FORMAT_YMDHMS        = "yyyy-MM-dd HH:mm:ss";

    public static final String                  FORMAT_YMD           = "yyyy-MM-dd";

    public static final ThreadLocal<DateFormat> NORM_DATETIME_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_YMDHMS));

    public static final ThreadLocal<DateFormat> NORM_DATE_FORMAT     =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_YMD));

    public static final String                  EQUAL                = "=";
    public static final String                  NOT_EQUAL            = "!=";
    public static final String                  IN                   = "in";
    public static final String                  NOT_IN               = "not in";
    public static final String                  NOT_MORE_THAN        = ">=";
    public static final String                  NOT_LESS_THAN        = "<=";
    public static final String                  GREAT_THAN           = ">";
    public static final String                  LESS_THAN            = "<";
}
