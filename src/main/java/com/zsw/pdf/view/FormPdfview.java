/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.pdf.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.zsw.pdf.utils.FreemarkerUtils;

/**
 * Created by Administrator on 2016/8/19 0019.
 *
 * @author xujiali
 */
public class FormPdfview extends AbstractITextPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        URL fileResource = FormPdfview.class.getResource("/pdf/templates");
        String fileName = "simpleForm.ftl";
        if (null != model.get("pdfFtlModel")) {
            fileName = String.valueOf(model.get("pdfFtlModel"));
        }
        String html = FreemarkerUtils.loadFtlHtml(new File(fileResource.getFile()), fileName, model);

        //        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
        XMLWorkerHelper.getInstance()
                .parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()), Charset.forName("UTF-8"),
                        new AsianFontProvider());
    }

    /*
     * private static final Font getChineseFont(float size) { Font FontChinese =
     * null; try { BaseFont bfChinese = BaseFont.createFont("STSong-Light",
     * "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED); FontChinese = new Font(bfChinese,
     * size, Font.NORMAL); } catch (DocumentException de) {
     * System.err.println(de.getMessage()); } catch (IOException ioe) {
     * System.err.println(ioe.getMessage()); } return FontChinese; }
     */
}
