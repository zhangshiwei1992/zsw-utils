/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 日期相关工具类
 *
 * @author xujiali
 */
@Slf4j
public class DateUtils {

    /**
     * 日期格式
     */
    private static final String DATE_FMT = "yyyy-MM-dd";

    /**
     * Date转LocalDateTime类型
     *
     * @param date 类型Date
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    /**
     * Date 转 LocalDate 类型
     *
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    /**
     * Date 转 LocalTime 类型
     *
     * @param date Date
     * @return LocalTime
     */
    public static LocalTime dateToLocalTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalTime localTime = localDateTime.toLocalTime();
        return localTime;
    }

    /**
     * LocalDateTime 转 Date
     *
     * @param localDateTime LocalDateTime
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * LocalDate 转 Date
     *
     * @param localDate LocalDate
     * @return Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * LocalTime 转 Date
     *
     * @param localTime LocalTime
     * @return Date
     */
    public static Date localTimeToDate(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 根据起息日，期数，固定特殊日期计算账单日期
     *
     * @param interestDate 起息日期
     * @param period 期数
     * @param day 固定日
     * @return 账单日期
     */
    public static Date genSpecialDate(Date interestDate, int period, int day) {
        Date date = null;
        LocalDate localDate = dateToLocalDate(interestDate);
        localDate = localDate.plusMonths(period);
        LocalDate finalLocalDate = LocalDate.of(localDate.getYear(), localDate.getMonth(), day);
        date = localDateToDate(finalLocalDate);
        log.info("date is:{}", date2String(date));
        return date;
    }

    /**
     * 日期转为String类型
     *
     * @param date 日期
     * @return 日期格式转换的字符串
     */
    public static String date2String(Date date) {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT);
        result = sdf.format(date);
        return result;
    }

    /**
     * String类型转换为Date
     *
     * @param dateString 字符串
     * @return 字符串转成日期
     */
    public static Date String2Date(String dateString) {
        Date result = null;
        try {
            if (StringUtils.isNotBlank(dateString)) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT);
                result = sdf.parse(dateString);
            }
        } catch (ParseException e) {
            log.error("String2Date error:{}", e);
        }
        return result;
    }

    /**
     * 按照指定格式将日期进行转化
     *
     * @param dateStr 日期的字符串格式
     * @param pattern 日期类型
     * @return 日期
     */
    public static Date formatDateStr(String dateStr, String pattern) {
        Date result = null;
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df);
            result = localDateTimeToDate(localDateTime);
        } catch (Exception e) {
            log.error("String2Date error:{}", e);
        }
        return result;
    }

    /**
     * 按照指定格式将日期进行转化
     *
     * @param dateStr 日期的字符串格式
     * @param pattern 日期类型
     * @return 日期
     */
    public static LocalDateTime dateStrToLocalDateTime(String dateStr, String pattern) {
        LocalDateTime result = null;
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            result = LocalDateTime.parse(dateStr, df);
        } catch (Exception e) {
            log.error("String2Date error:{}", e);
        }
        return result;
    }

    /**
     * 计算两个日期之间相差多少天
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期间隔天数
     */
    public static int betweenDays(Date startDate, Date endDate) {
        int result = 0;
        try {
            LocalDate startLocalDate = dateToLocalDate(startDate);
            LocalDate endLocalDate = dateToLocalDate(endDate);
            result = Long.valueOf(endLocalDate.toEpochDay() - startLocalDate.toEpochDay()).intValue();
        } catch (Exception e) {
            log.error("betweenDays error:{}", e);
        }
        return result;
    }

    /**
     * 得到几天前的时间
     *
     * @param d 日期
     * @param day 天数
     * @return 传入日期前几天的日期
     */
    public static Date getDateBefore(Date d, int day) {
        LocalDate currDate = dateToLocalDate(d);
        LocalDate resultLocalDate = currDate.minusDays(day);
        return localDateToDate(resultLocalDate);
    }

    /**
     * 得到几天后的时间
     *
     * @param d 日期
     * @param day 天数
     * @return 传入日期后几天的日期
     */
    public static Date getDateAfter(Date d, int day) {
        LocalDate currDate = dateToLocalDate(d);
        LocalDate resultLocalDate = currDate.plusDays(day);
        return localDateToDate(resultLocalDate);
    }

    /**
     * 获取当天几个月之后的前一天
     *
     * @param d 日期
     * @param mouth 月份
     * @return 获取当天几个月之后的前一天
     */
    public static int getBeforeDay(Date d, int mouth) {
        LocalDate currDate = dateToLocalDate(d);
        LocalDate finalDate = currDate.plusMonths(mouth).minusDays(1);
        return finalDate.getDayOfMonth();
    }

    /**
     * 获取HH:mm:ss格式
     *
     * @param date 日期
     * @return 日期的HH:mm:ss格式
     */
    public static String getTime(Date date) {
        return formatDate(date, "HH:mm:ss");
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param pattern 样式
     * @return 日期按照样式进行格式化
     */
    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (StringUtils.isNotBlank(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, DATE_FMT);
        }
        return formatDate;
    }

    /**
     * 获取日期对应的24小时制 -小时
     *
     * @param date 日期
     * @return 日期的小时数
     */
    public static Integer getHour(Date date) {
        Integer result = null;
        if (null == date) {
            return result;
        }
        String dateStr = formatDate(date, "yyyy-MM-dd HH");
        String hourStr = dateStr.substring(dateStr.length() - 2, dateStr.length());
        if ("00".equals(hourStr)) {
            result = Integer.valueOf(24);
        } else {
            result = Integer.valueOf(hourStr);
        }
        return result;
    }

    /**
     * 日期的字符串转成时间戳 - 毫秒数
     *
     * @param dateStr 日期字符串格式
     * @return 时间戳
     */
    public static Long parseDateStrToTimestamp(String dateStr) {
        Long result = null;
        try {
            LocalDateTime localDateTime = dateStrToLocalDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
            result = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * 日期转化为时间戳
     *
     * @param date 日期
     * @return 时间戳
     */
    public static Long parseDateToTimestamp(Date date) {
        Long result = null;
        try {
            LocalDateTime localDateTime = dateToLocalDateTime(date);
            result = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * LocalDateTime日期转化为时间戳
     *
     * @param localDateTime 日期LocalDateTime
     * @return 时间戳
     */
    public static Long parseLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        Long result = null;
        try {
            result = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * 时间戳转换为日期
     *
     * @param dateTimestamp 时间戳
     * @return 日期
     */
    public static Date parseTimestampToDate(Long dateTimestamp) {
        if (null == dateTimestamp) {
            return null;
        }
        Date result = new Date(dateTimestamp);
        return result;
    }

    /**
     * 系统默认最小的日期
     *
     * @return 系统最小日期
     */
    public static Date genMinestDate() {
        return new Date(Long.valueOf("-28800000"));
    }

    /**
     * 获取两个日期相差的月数
     *
     * @param d1 较大的日期
     * @param d2 较小的日期
     * @return 如果d1大于d2返回 月数差 否则返回0
     */
    public static int getMonthDiff(Date d1, Date d2) {
        LocalDateTime c1 = dateToLocalDateTime(d1);
        LocalDateTime c2 = dateToLocalDateTime(d2);
        if (parseLocalDateTimeToTimestamp(c1) < parseLocalDateTimeToTimestamp(c2)) {
            return 0;
        }
        int year1 = c1.get(ChronoField.YEAR);
        int year2 = c2.get(ChronoField.YEAR);
        int month1 = c1.get(ChronoField.MONTH_OF_YEAR);
        int month2 = c2.get(ChronoField.MONTH_OF_YEAR);
        int day1 = c1.get(ChronoField.DAY_OF_MONTH);
        int day2 = c2.get(ChronoField.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

    /**
     * 根据日期天数类型获取属于天数
     *
     * @param date 日期
     * @param chronoField 日历格式类型
     * @return 满足类型的某一天
     */
    public static int getDay(Date date, ChronoField chronoField) {
        int result = 0;
        try {
            LocalDate localDate = dateToLocalDate(date);
            result = localDate.get(chronoField);
        } catch (Exception e) {
            log.error("getDay error :{}", e);
        }
        return result;
    }

}
