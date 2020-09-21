/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.dto;

import lombok.Data;

/**
 * 用户基本信息类
 *
 * @author xujiali
 */
@Data
public class UserDto {

    /**
     * 用户主键id
     */
    private Long   userId;
    /**
     * 登录账号
     */
    private String account;
    /**
     * 姓名
     */
    private String name;
}
