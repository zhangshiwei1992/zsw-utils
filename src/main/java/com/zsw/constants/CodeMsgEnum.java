/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.constants;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 类SharkErrorMsg的实现描述：shark项目code代码，用于接口之间交互
 *
 * @author Jerry.Chen
 * @since 2018年3月11日 上午8:59:12
 */
@Getter
@AllArgsConstructor
public enum CodeMsgEnum {

    /**
     * 成功
     */
    OK("OK", "成功"),
    /**
     * 任务未完成
     */
    TASK_NOT_COMPLETE("task is not complete", "任务未完成"),
    /**
     * 记录找不到
     */
    RECORD_NOT_EXISTS("record is not exists", "记录找不到"),
    /**
     * 没有匹配的记录
     */
    NOT_MATCH_RECORDS("not match records", "没有匹配的记录"),
    /**
     * 字段没有值
     */
    VALUE_IS_BLANK("field_value_is_blank", "字段没有值"),
    /**
     * 字段的值无效，不符合预期的格式
     */
    VALUE_IS_INVALID("field value is invalid", "字段的值无效，不符合预期的格式"),
    /**
     * 匹配到了多条的记录
     */
    MORE_RECORDS("more than one record", "匹配到了多条的记录"),
    /**
     * 非法请求
     */
    ILLEGAL_REQUEST("illegal request", "非法请求"),
    /**
     * 重复提交
     */
    REPEATED_SUBMIT("repeated submit", "重复提交"),
    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION("system exception", "系统异常，请联系管理员"),
    /**
     * 黑名单用户
     */
    USER_IS_BLACKLIST("user is blacklist", "黑名单用户"),
    /**
     * 短信验证码无效
     */
    ILLEGAL_MSG_CODE("illegal msg validate code", "短信验证码无效"),

    /**
     * 手机号码无效
     */
    ILLEGAL_PHONE_NUMBER("illegal phone number", "手机号码无效"),

    /**
     * 图片验证码无效
     */
    ILLEGAL_CAPTCHA_CODE("illegal captcha validate code", "图片验证码无效");

    /**
     * 错误编号
     */
    private String code;
    /**
     * 提示信息
     */
    private String msg;

    /**
     * 条件获取枚举类信息
     *
     * @param code 流程状态编号
     * @return 枚举类信息
     */
    public static CodeMsgEnum getEnumByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CodeMsgEnum item : CodeMsgEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}
