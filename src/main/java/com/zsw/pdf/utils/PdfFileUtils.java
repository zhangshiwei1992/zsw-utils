/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.pdf.utils;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.zsw.exception.BizRuntimeException;
import com.zsw.pdf.view.AsianFontProvider;

/**
 * 生成PDF文件 Created by Administrator on 2016/8/19 0019.
 *
 * @author xujiali
 */
public class PdfFileUtils {

    /**
     * 保存pdf文件
     *
     * @param out 输出字节流
     * @param html html字符串
     */
    public static void savePdf(OutputStream out, String html) {
        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        //Document document = new Document(PageSize.A4, 110, 110, 120, 140);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
        } catch (Exception e) {
            throw new BizRuntimeException(e);
        } finally {
            document.close();
        }
    }

    /**
     * 生成中文pdf
     *
     * @param out 输出字节流
     * @param html html字符串
     */
    public static void saveChinesePdf(OutputStream out, String html) {
        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        //Document document = new Document(PageSize.A4, 110, 110, 120, 140);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            XMLWorkerHelper.getInstance()
                    .parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()), Charset.forName("UTF-8"),
                            new AsianFontProvider());
        } catch (Exception e) {
            throw new BizRuntimeException(e);
        } finally {
            document.close();
        }
    }
}
