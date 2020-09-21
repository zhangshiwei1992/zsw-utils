/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.pdf.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by Administrator on 2016/8/19 0019.
 *
 * @author xujiali
 */
public class Pdfview extends AbstractITextPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Paragraph header = new Paragraph(new Chunk("PDF 输出测试", getChineseFont(24)));
        document.add(header);
        document.add(new Paragraph("测试", getChineseFont(12)));
    }

    /**
     * 中文字体
     *
     * @param size 字体大小
     * @return 中文字体
     */
    private static Font getChineseFont(float size) {
        Font fontChinese = null;
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese, size, Font.NORMAL);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return fontChinese;
    }
}
