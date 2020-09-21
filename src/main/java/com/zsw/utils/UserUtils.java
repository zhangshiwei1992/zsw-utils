/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.zsw.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

/**
 * 获取用户登录信息
 *
 * @author wushuifeng
 */
@Slf4j
public class UserUtils {

    /**
     * 获取租赁系统当前登陆人的用户编号
     *
     * @param request 请求信息
     * @return 用户信息
     */
    public static UserDto getUserByCookie(HttpServletRequest request) {
        UserDto user = new UserDto();
        try {
            String account = "";
            Long userId = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("sso_user_account".equals(cookie.getName())) {
                        account = cookie.getValue();
                    }
                    if ("sso_user_id".equals(cookie.getName())) {
                        userId = Long.valueOf(cookie.getValue());
                    }
                }
            }
            log.info("UserUtils---getUserByCookie---获取用户登录信息---account:{},userId:{}", account, userId);
            user.setAccount(account);
            user.setUserId(userId);
        } catch (Exception e) {
            log.error("获取cookie失败:{}", e);
        }
        return user;
    }
}
