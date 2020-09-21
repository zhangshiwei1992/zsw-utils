/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

/**
 * 与网销交互接口处理数据加密工具类
 *
 * @author xujiali
 * @since 2018年12月18日 下午5:25:13
 */
public class PhpEncryptUtil {

    /**
     * 随机生成随机长度的大小写字母
     *
     * @param bound 随机范围 1 - bound
     * @return 生成的验证码
     */
    public static String generateCharCode(int bound) {
        String val = "";
        String charStr = "char";
        String numStr = "num";
        Random random = new Random();
        int length = random.nextInt(bound);
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = numStr;
            if (random.nextInt(2) % 2 == 0) {
                charOrNum = charStr;
            }
            //输出字母还是数字
            if (charStr.equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = 97;
                if (random.nextInt(2) % 2 == 0) {
                    temp = 65;
                }
                val += (char) (random.nextInt(26) + temp);
            }
        }
        return val;
    }

    /**
     * 数据加密 将字符串插入随机大小写字母中
     *
     * @param str 原始字符串
     * @param bound 随机长度
     * @return 加密字符串
     */
    public static String encrypt(String str, int bound) {
        String result = "";
        if (StringUtils.isNotBlank(str)) {
            for (int i = 0; i < str.length(); i++) {
                result = String.format("%s%s%s", result, generateCharCode(bound), str.charAt(i));
            }
        }
        return result;
    }
}
