/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.exception;

/**
 * 业务运行时异常，主要是为了规避PMD扫描 {@link RuntimeException}
 */
public class BizRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 6024601782489758956L;

    /**
     * 无参构造器
     */
    public BizRuntimeException() {
        super();
    }

    /**
     * 带有message参数构造器
     *
     * @param message 错误信息
     */
    public BizRuntimeException(String message) {
        super(message);
    }

    /**
     * 带有message和cause参数构造器
     *
     * @param message 错误信息
     * @param cause 异常原因
     */
    public BizRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 带有cause参数构造器
     *
     * @param cause 异常原因
     */
    public BizRuntimeException(Throwable cause) {
        super(cause);
    }
}
