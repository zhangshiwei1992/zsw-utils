/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.zsw.constants.DateConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 车辆信息工具类
 *
 * @author zhangshiwei
 * @since 2018年9月30日下午3:36:33
 */
@Slf4j
public class VehicleUtils {

    /**
     * 将含有逗号的字符串转为list
     *
     * @param string 带有","的字符串
     * @return 分割后的list
     */
    public static List<String> stringToList(String string) {
        if (StringUtils.isBlank(string)) {
            return new ArrayList<>();
        }
        return Arrays.asList(string.split(","));
    }

    /**
     * 将字符串转为list
     *
     * @param string 字符串
     * @param splitChar 分隔字符
     * @return 分割后的list
     */
    public static List<String> stringToList(String string, String splitChar) {
        if (StringUtils.isBlank(string) || StringUtils.isNotBlank(splitChar)) {
            return null;
        }
        return Arrays.asList(string.split(splitChar));
    }

    /**
     * list转为string,内容用逗号隔开
     *
     * @param list 集合
     * @return 字符串
     */
    public static String listToString(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return StringUtils.join(list, ",");
    }

    /**
     * 身份证件号码脱敏,隐藏第 5到第14位:3424**********2356
     *
     * @param idNo 身份证号码
     * @return 脱敏后的身份证号码
     */
    public static String idNoDesensitize(String idNo) {
        String result = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(idNo)) {
            result = StringUtils.left(idNo, 4)
                    .concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idNo, 4), 15, "*"), "*"));
        }
        return result;
    }

    /**
     * 截掉字符串第一位的符号 '
     *
     * @param string 待处理字符串
     * @return 无 ' 的字符串
     */
    public static String deleteSymbol(String string) {
        if (StringUtils.isNotBlank(string) && string.contains("'")) {
            string = string.substring(string.indexOf("'"));
        }
        return string;
    }

    /**
     * 获取昨天的时间 今天20180914,昨天就是20180913
     *
     * @return 昨天的时间8位参数
     */
    public static String getYesterdayDate() {
        String getYesterdayDate = new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtils.getDateBefore(new Date(), 1))
                .substring(0, 8);
        log.info("getYesterdayDate :{} ", getYesterdayDate);
        return getYesterdayDate;
    }

    /**
     * 获取当前时间: 20190403121212
     *
     * @return 时间
     */
    public static String getCurrentTime() {
        String currentTime = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
        log.info("当前时间:{}", currentTime);
        return currentTime;
    }

    /**
     * 获取文件大小
     *
     * @param size 文件长度 byte
     * @return 方便识别的内存
     */
    public static String getFileSize(long size) {
        long initSize = size;
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
        }
        long bSize = size;

        if (size < 1024) {
            return size + "." + (initSize - bSize * 1024) / 100 + "KB";
        } else {
            size = size / 1024;
        }
        long kbSize = size;

        if (size < 1024) {
            return size + "." + (bSize - kbSize * 1024) / 100 + "MB";
        } else {
            long gbSize = size / 1024;
            return gbSize + "." + (kbSize - gbSize * 1024) / 100 + "GB";
        }
    }

    /**
     * 获取文件大小
     *
     * @param file 文件 或 文件夹
     * @return 方便识别的内存
     */
    public static String getFileSize(File file) {
        String result = StringUtils.EMPTY;
        if (file == null || !file.exists()) {
            return result;
        }

        // 传入的是: 文件
        if (file.isFile()) {
            result = getFileSize(file.length());

            // 传入的是: 文件夹
        } else if (file.isDirectory()) {
            long size = 0L;
            ArrayList<File> fileList = new ArrayList<>();
            getFiles(fileList, file);
            for (File f : fileList) {
                size += f.length();
            }
            result = getFileSize(size);
        }

        return result;
    }

    /**
     * 获取文件下所有的文件
     *
     * @param fileList 文件集合
     * @param file 文件或文件夹
     */
    public static void getFiles(ArrayList<File> fileList, File file) {
        File[] allFiles = file.listFiles();
        if (allFiles != null && allFiles.length > 0) {
            for (File f : allFiles) {
                if (f.isFile()) {
                    fileList.add(f);
                } else {
                    getFiles(fileList, f);
                }
            }
        }
    }

    /**
     * 判断是否是整数或者是携带一位或者两位的小数
     *
     * @param obj 待校验数据
     * @return 校验结果
     */
    public static boolean checkTwoDecimal(Object obj) {
        boolean checkResult = false;
        try {
            if (obj != null) {
                String source = obj.toString();
                log.info("判断是否是整数或者是携带一位或者两位的小数:{}", source);
                Pattern pattern = Pattern.compile("^[+]?([0-9]+(.[0-9]{1,2})?)$");
                if (pattern.matcher(source).matches()) {
                    checkResult = true;
                }
            }
        } catch (Exception e) {
            log.error("判断是否是整数或者是携带一位或者两位的小数 - 异常:{}", e);
        }
        return checkResult;
    }

    /**
     * 日期格式转换: 加上当前的时间
     *
     * @param date 年月日
     * @param isFirst 是否当天第一个
     * @return 年月日时分秒
     */
    public static Date dateToDateTime(Date date, Boolean isFirst) {
        if (null == date) {
            date = new Date();
        }
        String dateString = DateUtils.date2String(date);
        log.info("dateToDateTime-dateString:{}", dateString);

        Date calDate = new Date();
        if (isFirst) {
            calDate = DateUtils.formatDateStr("2020-01-01 00:00:01", "yyyy-MM-dd HH:mm:ss");
        }

        String currentDateString = DateUtils.formatDate(new Date(), DateConstants.DATE_TIME_NANO_FORMAT);
        String currentTime = currentDateString.substring(10);
        log.info("dateToDateTime-currentDateString:{} , currentTime:'{}'", currentDateString, currentTime);

        String newDateTimeString = dateString + currentTime;
        log.info("dateToDateTime-newDateTimeString:{}", newDateTimeString);
        try {
            if (StringUtils.isNotBlank(newDateTimeString)) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DATE_TIME_NANO_FORMAT);
                return sdf.parse(newDateTimeString);
            }
        } catch (Exception e) {
            log.error("String2Date error:{}", e);
        }
        return date;
    }

}
