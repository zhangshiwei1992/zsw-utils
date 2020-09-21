/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.oss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSSClient;

/**
 * 阿里云OSS相关实体类配置
 *
 * @author xujiali
 */
@Configuration
@ConditionalOnProperty(prefix = "common-utils.oss", value = "enabled", matchIfMissing = false)
public class OssConfig {

    /**
     * 阿里云OSS客户端配置实体bean
     *
     * @param endpoint 地域节点
     * @param accessKeyId 密钥Key
     * @param accessKeySecret 密钥
     * @return OSSClient实体Bean
     */
    @Bean(name = "common-utils.oss.config")
    @Autowired
    public OSSClient OSSClient(@Value("${common-utils.oss.config.endpoint}") String endpoint,
                               @Value("${common-utils.oss.config.accessKeyId}") String accessKeyId,
                               @Value("${common-utils.oss.config.accessKeySecret}") String accessKeySecret) {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 自定义OSS实体类配置
     *
     * @param ossClient 阿里云OSS客户端实体类
     * @param bucketName 仓库名称
     * @param filedir 路径
     * @return 自定义OSS实体类
     */
    @Bean(name = "common-utils.oss.client")
    public AliOssClient AliOssClient(@Qualifier("common-utils.oss.config") OSSClient ossClient,
                                     @Value("${common-utils.oss.client.bucketName}") String bucketName,
                                     @Value("${common-utils.oss.client.filedir}") String filedir) {
        return new AliOssClient(ossClient, bucketName, filedir);
    }

    /**
     * 阿里云的加速CDN
     *
     * @param domain 域名
     * @param bindOSS 绑定的OSS
     * @return 加速CDN
     */
    @Bean(name = "common-utils.oss.cdn")
    public AliOssCdn AliOssCdn(@Value("${common-utils.oss.cdn.domain}") String domain,
                               @Value("${common-utils.oss.cdn.bindOSS}") String bindOSS) {
        return new AliOssCdn(domain, bindOSS);
    }

}
