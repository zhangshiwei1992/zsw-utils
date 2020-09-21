/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理公用方法
 *
 * @author xujiali
 */
public class StringDealUtils {

    /**
     * 截取字符串前后缀中字符集合
     *
     * @param str 原字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 截取后字符串
     */
    public static List<String> subStrByAffix(String str, String prefix, String suffix) {
        List<String> result = new ArrayList<>();
        int indexStart = str.indexOf(prefix);
        int indexEnd = 0;
        while (indexStart >= 0) {
            indexEnd = str.indexOf(suffix);
            String substr = str.substring(indexStart + 1, indexEnd);
            result.add(substr);
            str = str.substring(indexEnd + 1);
            indexStart = str.indexOf(prefix);
        }
        return result;
    }

    /**
     * 截取字符串前后缀中字符集合 - 包含前后缀
     *
     * @param str 原字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 包含前后缀截取后结构
     */
    public static List<String> subStrWithAffix(String str, String prefix, String suffix) {
        List<String> result = new ArrayList<>();
        int indexStart = str.indexOf(prefix);
        int indexEnd = 0;
        while (indexStart >= 0) {
            indexEnd = str.indexOf(suffix);
            String substr = str.substring(indexStart, indexEnd + 1);
            result.add(substr);
            str = str.substring(indexEnd + 1);
            indexStart = str.indexOf(prefix);
        }
        return result;
    }

    /**
     * 判断字符为中文
     *
     * @param str 原字符串
     * @return 是否含有中文
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否全部为中文字符
     *
     * @param temp 待检查字符串
     * @return 校验结果
     */
    public static boolean isAllChineseChar(String temp) {
        return temp.matches("[\\u4E00-\\u9FA5·]+");
    }

    /**
     * 将字符串中的中文括号替换为英文括号
     *
     * @param temp 待替换字符串
     * @return 处理完的字符串
     */
    public static String replaceChineseBrackets(String temp) {
        temp = temp.replaceAll("& #40;", "(")
                .replaceAll("& #39;", "'")
                .replaceAll("& lt;", "<")
                .replaceAll("& #41;", ")")
                .replaceAll("& gt;", ">");
        if (temp.contains("（")) {
            temp = temp.replaceAll("（", "(");
        } else if (temp.contains("）")) {
            temp = temp.replaceAll("）", ")");
        }
        return temp;
    }

}
