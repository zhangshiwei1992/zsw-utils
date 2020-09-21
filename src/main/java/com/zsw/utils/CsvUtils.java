/*
 * Copyright 2020 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * CSV文件工具类
 *
 * @author zhangshiwei
 * @since 2020-06-11 16:11:12
 */
@Slf4j
public class CsvUtils {

    /**
     * CSV文件生成并下载
     *
     * @param dataList 数据列表
     * @param fileDir 文件目录
     * @param fileName 文件名
     * @param response 响应信息
     */
    public static void createAndDownloadCSVFile(List<LinkedHashMap<String, Object>> dataList, String fileDir,
                                                String fileName, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(dataList) || StringUtils.isAnyBlank(fileDir, fileName) || null == response) {
            return;
        }

        BufferedWriter csvWriter = null;
        try {
            response.setHeader("Charset", "UTF-8");
            response.setHeader("Content-Type", "application/force-download");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment; filename=" + URLEncoder.encode(fileName, "utf8") + ".csv");
            response.flushBuffer();
            OutputStream outputStream = response.getOutputStream();

            File csvFile = new File(fileDir + File.separator + fileName + ".csv");

            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                boolean mkdirsResult = parent.mkdirs();
                log.info("mkdirsResult:{}", mkdirsResult);
            }
            boolean createNewFileResult = csvFile.createNewFile();
            log.info("createNewFileResult:{}", createNewFileResult);

            // GB2312使正确读取分隔符","
            csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);

            // 写入文件内容
            for (LinkedHashMap<String, Object> linkedHashMap : dataList) {
                Iterator<Entry<String, Object>> iterator = linkedHashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = iterator.next();
                    String rowStr;
                    Object object = entry.getValue();

                    if (null != object) {
                        String objectString = String.valueOf(object);
                        if (isNumeric(objectString) && objectString.length() > 10) {
                            objectString = "'" + objectString;
                        }
                        if (StringUtils.isBlank(objectString)) {
                            objectString = "  ";
                        }

                        rowStr = String.format("%s%s%s", "\"", objectString, "\",");
                    } else {
                        rowStr = String.format("%s%s%s", "\"", "  ", "\",");
                    }
                    csvWriter.write(rowStr);
                }
                csvWriter.newLine();
            }

            csvWriter.flush();

            byte[] buffer = CsvUtils.getFileByteArray(csvFile);
            if (null != buffer) {
                FileCopyUtils.copy(buffer, outputStream);
            }

            boolean deleteResult = csvFile.delete();
            log.info("deleteResult is{}", deleteResult);
        } catch (Exception e) {
            log.error("createAndDownloadCSVFile Exception : {}", e);
        } finally {
            try {
                if (null != csvWriter) {
                    csvWriter.close();
                }
            } catch (IOException e) {
                log.error("createAndDownloadCSVFile IOException : {}", e);
            }
        }
    }

    /**
     * 检查字符串是否为数值对象
     *
     * @param str 字符串
     * @return 是否为数值对象
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取文件字节流
     *
     * @param file 文件信息
     * @return 字节流
     */
    public static byte[] getFileByteArray(File file) {
        if (null == file) {
            return null;
        }

        byte[] buffer = null;
        FileInputStream fis;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
            if (file.exists()) {
                boolean deleteResult = file.delete();
                log.info("getFileByteArray deleteResult:{}", deleteResult);
            }
        } catch (Exception e) {
            log.info("getFileByteArray Exception:{}", e);
        }
        return buffer;
    }

}
