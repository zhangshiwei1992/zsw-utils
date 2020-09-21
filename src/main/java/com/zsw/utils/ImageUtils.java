/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;

/**
 * 图片工具类
 *
 * @author system
 */
public class ImageUtils {
    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr 图片数据
     * @param imgFilePath 图片地址
     * @return 执行结果
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) {
            // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    // 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param imageFilePath 图片地址
     * @return 删除结果
     */
    public static boolean deleteImage(String imageFilePath) {
        boolean result = false;
        File file = new File(imageFilePath);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }
}
