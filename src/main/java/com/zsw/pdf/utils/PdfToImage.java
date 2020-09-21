/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.pdf.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.itextpdf.text.pdf.PdfReader;

import lombok.extern.slf4j.Slf4j;

/**
 * pdf转图片
 *
 * @author xujiali
 */
@Slf4j
public class PdfToImage {

    /**
     * main方法检查
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        pdf2Image("/Users/xujiali/logs/png/testpdf.pdf", "/Users/xujiali/logs/png",
                "/Users/xujiali/logs/png/testpdfToImage.png", 300);
    }

    /***
     * PDF文件转PNG图片，全部页数
     *
     * @param pdfFilePath pdf完整路径
     * @param dstImgFolder 图片存放的文件夹
     * @param dpi dpi越大转换后越清晰，相对转换速度越慢
     * @param outPath 转出路径
     */
    public static void pdf2Image(String pdfFilePath, String dstImgFolder, String outPath, int dpi) {
        File file = new File(pdfFilePath);
        PDDocument pdDocument;
        try {
            String imgPDFPath = file.getParent();
            int dot = file.getName().lastIndexOf('.');
            // 获取图片文件名
            String imagePDFName = file.getName().substring(0, dot);
            String imgFolderPath = null;
            if (dstImgFolder.equals("")) {
                // 获取图片存放的文件夹路径
                imgFolderPath = imgPDFPath + File.separator + imagePDFName;
            } else {
                imgFolderPath = dstImgFolder + File.separator + imagePDFName;
            }

            if (createDirectory(imgFolderPath)) {

                pdDocument = PDDocument.load(file);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                /* dpi越大转换后越清晰，相对转换速度越慢 */
                PdfReader reader = new PdfReader(pdfFilePath);
                int pages = reader.getNumberOfPages();
                StringBuffer imgFilePath = null;
                List<BufferedImage> piclist = new ArrayList<>();
                for (int i = 0; i < pages; i++) {
                    String imgFilePathPrefix = imgFolderPath + File.separator + imagePDFName;
                    imgFilePath = new StringBuffer();
                    imgFilePath.append(imgFilePathPrefix);
                    imgFilePath.append("_");
                    imgFilePath.append(String.valueOf(i + 1));
                    imgFilePath.append(".png");
                    File dstFile = new File(imgFilePath.toString());
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    piclist.add(image);
                    ImageIO.write(image, "png", dstFile);
                }
                log.info("PDF文档转PNG图片成功！");

                yPic(piclist, outPath);

                log.info("PDF文档转PNG合成图片成功！");

            } else {
                log.info("PDF文档转PNG图片失败：" + "创建" + imgFolderPath + "失败");
            }

        } catch (IOException e) {
            log.error("pdf2Image error:{}", e);
        }
    }

    /**
     * 创建目录
     *
     * @param folder 目录
     * @return 目录是否创建成功
     */
    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }

    /**
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同
     *
     * @param piclist 文件流数组
     * @param outPath 输出路径
     */
    public static void yPic(List<BufferedImage> piclist, String outPath) {
        // 纵向处理图片
        if (piclist == null || piclist.size() <= 0) {
            log.info("图片数组为空!");
            return;
        }
        try {
            // 总高度
            int height = 0;
            // 总宽度
            int width = 0;
            // 临时的高度 , 或保存偏移高度
            int tHeight = 0;
            // 临时的高度，主要保存每个高度
            int eHeight = 0;
            // 图片的数量
            int picNum = piclist.size();
            // 保存读取出的图片
            File fileImg = null;
            // 保存每个文件的高度
            int[] heightArray = new int[picNum];
            // 保存图片流
            BufferedImage buffer = null;
            // 保存所有的图片的RGB
            List<int[]> imgRGB = new ArrayList<>();
            // 保存一张图片中的RGB数据
            int[] eImgRGB;
            for (int i = 0; i < picNum; i++) {
                buffer = piclist.get(i);
                // 图片高度
                tHeight = buffer.getHeight();
                heightArray[i] = tHeight;
                if (i == 0) {
                    // 图片宽度
                    width = buffer.getWidth();
                }
                // 获取总高度
                height += tHeight;
                // 从图片中读取RGB
                eImgRGB = new int[width * tHeight];
                eImgRGB = buffer.getRGB(0, 0, width, tHeight, eImgRGB, 0, width);
                imgRGB.add(eImgRGB);
            }
            // 设置偏移高度为0
            tHeight = 0;
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < picNum; i++) {
                eHeight = heightArray[i];
                if (i != 0) {
                    // 计算偏移高度
                    tHeight += eHeight;
                }
                // 写入流中
                imageResult.setRGB(0, tHeight, width, eHeight, imgRGB.get(i), 0, width);
            }
            File outFile = new File(outPath);
            // 写图片
            ImageIO.write(imageResult, "jpg", outFile);
        } catch (Exception e) {
            log.error("yPic :{}", e);
        }
    }

}
