/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.pdf.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理字符串数字相关的工具类,随机生成各种字段的代码 Created by Administrator on 2016/5/19 0019.
 *
 * @author xujiali
 */
@Slf4j
public class FreemarkerUtils {

    /**
     * 使用传入的Map对象，渲染指定的freemarker模板
     *
     * @param baseDir 根目录
     * @param fileName 文件名
     * @param globalMap 数据绑定
     * @return html字符串
     */
    public static String loadFtlHtml(File baseDir, String fileName, Map globalMap) {
        log.info("baseDir={};globalMap={}", baseDir, globalMap);
        if (baseDir == null || !baseDir.isDirectory() || globalMap == null || fileName == null || "".equals(fileName)) {
            throw new IllegalArgumentException("Directory file");
        }

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        try {
            cfg.setDirectoryForTemplateLoading(baseDir);
            cfg.setDefaultEncoding("UTF-8");
            //.RETHROW
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setClassicCompatible(true);
            Template temp = cfg.getTemplate(fileName);

            StringWriter stringWriter = new StringWriter();
            temp.process(globalMap, stringWriter);

            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("load fail file");
        }
    }

    /**
     * 根据classLoader以及filePath获取文件
     *
     * @param classLoader 类加载
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param globalMap 全局map
     * @return html
     */
    public static String loadFtlHtml(ClassLoader classLoader, String filePath, String fileName, Map globalMap) {
        log.info("filePath={};globalMap={}", filePath, globalMap);
        if (StringUtils.isBlank(filePath) || StringUtils.isBlank(fileName) || globalMap == null) {
            throw new IllegalArgumentException("Directory file");
        }

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        try {
            cfg.setTemplateLoader(new ClassTemplateLoader(classLoader, filePath));
            cfg.setDefaultEncoding("UTF-8");
            //.RETHROW
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setClassicCompatible(true);
            Template temp = cfg.getTemplate(fileName);

            StringWriter stringWriter = new StringWriter();
            temp.process(globalMap, stringWriter);

            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("load fail file");
        }
    }
}
