/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 文件处理公用方法
 *
 * @author xujiali
 */
@Slf4j
public class FileUitl {
    /**
     * 将文件转成base64 字符串
     *
     * @param path 文件路径
     * @return 字符串
     * @throws Exception 异常
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code base64字符
     * @param targetPath 目标文件地址
     * @throws Exception 抛出异常
     */
    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    /**
     * 将base64字符保存文本文件
     *
     * @param base64Code ase64字符
     * @param targetPath 目标文件地址
     * @throws Exception 抛出异常
     */
    public static void toFile(String base64Code, String targetPath) throws Exception {
        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    /**
     * 主方法
     *
     * @param args args
     */
    public static void main(String[] args) {
        try {
            String base64Code = encodeBase64File("E:\\ABC\\FC15263649680000000001.zip");
            log.info(base64Code);
            decoderBase64File(base64Code, "E:\\ABC\\test.zip");
        } catch (Exception e) {
            log.error("Exception:{}", e);
        }
    }

    /**
     * 读取excel文件每一行的数据组成list
     *
     * @param multipartFile 文件流
     * @return 每行数据的list
     */
    public static List<Row> genRowList(MultipartFile multipartFile) {
        List<Row> rowList = new ArrayList<>();
        Workbook hssfWorkbook = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            if (fileName != null && fileName.endsWith("xlsx")) {
                // Excel 2007
                hssfWorkbook = new XSSFWorkbook(inputStream);
            } else if (fileName != null && fileName.endsWith("xls")) {
                // Excel 2003
                hssfWorkbook = new HSSFWorkbook(inputStream);
            }

            if (hssfWorkbook != null) {
                // 只读取第一个sheet页
                Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
                if (hssfSheet != null) {
                    for (int rowNum = 0, size = hssfSheet.getLastRowNum(); rowNum <= size; rowNum++) {
                        rowList.add(hssfSheet.getRow(rowNum));
                    }
                }
            }

            inputStream.close();
        } catch (IOException e) {
            log.error("Exception:{}", e);
            log.error("根据文件信息获取Workbook错误:{}", e);
        }
        return rowList;
    }
}
