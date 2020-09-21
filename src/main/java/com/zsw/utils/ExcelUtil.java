/*
 * Copyright 2020 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.zsw.utils.excel.ExcelUtils;

/**
 * Excel处理的通用方法
 *
 * @author xujiali
 * @since 2020年4月20日 下午6:41:34
 */
public class ExcelUtil {

    /**
     * 下载excel - web
     *
     * @param response 请求返回
     * @param fileName 文件名
     * @param labelNameList 标签名列表
     * @param sheetNameList sheet列表
     * @param dataList 数据
     * @param regionMap 单元格合并
     * @param mapColumnWidth 自定义列宽
     */
    public static void downloadExcel(HttpServletResponse response, String fileName, String[] labelNameList,
                                     String[] sheetNameList, List<List<String[]>> dataList, HashMap regionMap,
                                     HashMap mapColumnWidth) {
        ExcelUtils excelUtils = ExcelUtils.initialization();
        excelUtils.setLabelName(labelNameList);
        excelUtils.setDataLists(dataList);
        excelUtils.setFileName(fileName);
        excelUtils.setResponse(response);
        excelUtils.setSheetName(sheetNameList);
        excelUtils.setRegionMap(regionMap);
        excelUtils.setMapColumnWidth(mapColumnWidth);
        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 下载异常excel
     *
     * @param response 请求返回
     * @param exception 异常
     */
    public static void downloadExceptionExcel(HttpServletResponse response, Exception exception) {
        String name = "下载失败异常信息";

        // 展示内容
        List<List<String[]>> dataList = new ArrayList<>();
        List<String[]> sheetDataList = new ArrayList<>();
        String[] title = new String[] { name };
        sheetDataList.add(title);
        String[] dataArray = new String[] { JSON.toJSONString(exception) };
        sheetDataList.add(dataArray);
        dataList.add(sheetDataList);

        // 第一个sheet页单元格列宽
        HashMap<Integer, HashMap<Integer, Integer>> mapColumnWidth = new HashMap<>();
        HashMap<Integer, Integer> mapColumn = new HashMap<>();
        mapColumn.put(0, 80);
        mapColumnWidth.put(1, mapColumn);

        // 合并单元格 前10行第1列
        HashMap<Integer, List<Integer[]>> regionMap = new HashMap<>();
        List<Integer[]> regionList = new ArrayList<>();
        regionList.add(new Integer[] { 1, 20, 0, 0 });
        regionMap.put(1, regionList);

        // sheet页名称
        String[] sheetName = new String[] { name };

        String currentDateTime = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());

        // 文件名称
        String fileName = String.format("%s%s", name, currentDateTime);

        ExcelUtils excelUtils = ExcelUtils.initialization();
        excelUtils.setSheetName(sheetName);
        excelUtils.setDataLists(dataList);
        excelUtils.setFileName(fileName);
        excelUtils.setResponse(response);
        excelUtils.setRegionMap(regionMap);
        excelUtils.setMapColumnWidth(mapColumnWidth);
        excelUtils.exportForExcelsOptimize();
    }

}
