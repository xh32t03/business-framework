package com.easyway.business.framework.util;

import org.slf4j.Logger;

/**
 * 规范化日志打印工具，注意日志的级别选择：<br> 
 *
 * <p>
 *   <ol>
 *     <li>DEBUG <b>开发环境</b>应用调试，输出详细的应用状态
 *     <li>INFO <b>生产环境</b>运行状态观察，输出应用生命周期中的<b>正常重要事件</b>
 *     <li>WARN <b>生产环境</b>故障诊断，输出应用中的<b>可预期的异常事件</b>
 *     <li>ERROR <b>生产环境</b>故障诊断，输出应用中的<b>未预期的异常事件</b>
 *   </ol>
 * </p>
 * 
 * @author xl.liu
 */
public final class LogUtil {

    /**
     * 禁用构造函数
     */
    private LogUtil() {
        //禁用构造函数
    }

    /**
     * 生成<font color="blue">调试</font>级别日志<br>
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger
     * @param obj
     */
    public static void debug(Logger logger, Object... obj) {

        if (logger != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(getLogString(obj));
            }
        }

    }

    /**
     * 生成<font color="blue">通知</font>级别日志<br>
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger
     * @param obj
     */
    public static void info(Logger logger, Object... obj) {

        if (logger != null) {
            if (logger.isInfoEnabled()) {
                logger.info(getLogString(obj));
            }
        }

    }

    /**
     * 生成<font color="brown">警告</font>级别日志<br>
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger
     * @param obj
     */
    public static void warn(Logger logger, Object... obj) {

        if (logger != null) {
            logger.warn(getLogString(obj));
        }

    }

    /**
     * 生成<font color="brown">警告</font>级别日志<br>
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger
     * @param throwable
     * @param obj
     */
    public static void warn(Logger logger, Throwable throwable, Object... obj) {

        if (logger != null) {
            logger.warn(getLogString(obj), throwable);
        }

    }

    /**
     * 生成<font color="red">错误</font>级别日志<br>
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger
     * @param obj
     */
    public static void error(Logger logger, Object... obj) {

        if (logger != null) {
            logger.error(getLogString(obj));
        }

    }

    /**
     * 生成<font color="red">错误</font>级别日志<br>
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger
     * @param throwable
     * @param obj
     */
    public static void error(Logger logger, Throwable throwable, Object... obj) {

        if (logger != null) {
            logger.error(getLogString(obj), throwable);
        }

    }

    /**
    * 生成输出到日志的字符串
    *
    * @param obj 任意个要输出到日志的参数
    * @return
    */
    private static String getLogString(Object... obj) {

        StringBuilder log = new StringBuilder();

        for (Object o : obj) {
            log.append(o);
        }

        return log.toString();
    }

}