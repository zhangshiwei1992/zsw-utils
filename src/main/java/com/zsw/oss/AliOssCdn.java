/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.oss;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里OSS cdn配置类
 *
 * @author xujiali
 */
@Slf4j
@Data
public class AliOssCdn {

    //当前地址
    private String domain;
    //目标地址
    private String bindOSS;

    /**
     * AliOssCdn构造器
     *
     * @param domain 域名
     * @param bindOSS 绑定OSS
     */
    public AliOssCdn(String domain, String bindOSS) {
        this.domain = domain;
        this.bindOSS = bindOSS;
    }

    /**
     * 替换url
     *
     * @param url URL地址
     * @return 解析URL结果
     */
    public String replaceUrl(String url) {
        return url.replace(bindOSS, domain);
    }
}
