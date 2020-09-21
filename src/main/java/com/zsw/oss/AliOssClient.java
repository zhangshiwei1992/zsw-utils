/*
 * Copyright 2018 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.oss;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.yifenqi.boot.core.constants.CodeMsgEnum;
import com.zsw.exception.BizRuntimeException;

import app.myoss.cloud.core.lang.base.DateTimeFormatUtils;
import app.myoss.cloud.core.lang.dto.Result;
import app.myoss.cloud.core.lang.dto.TransformValue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云客户端
 *
 * @author xujiali
 */
@Slf4j
@Data
public class AliOssClient {

    private OSSClient ossClient;
    //空间
    private String    bucketName;
    //文件存储目录
    private String    filedir;

    /**
     * 构造器
     *
     * @param ossClient oss客户端
     * @param bucketName 仓库名
     * @param filedir 文件存储路径
     */
    public AliOssClient(OSSClient ossClient, String bucketName, String filedir) {
        this.ossClient = ossClient;
        this.bucketName = bucketName;
        this.filedir = filedir;
    }

    /**
     * 上传文件到OSS
     */
    public void uploadFileToOss() {
        final String keySuffixWithSlash = "parent_directory/";
        ossClient.putObject("<bucketName>", keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
        // 关闭client
        ossClient.shutdown();
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 上传文件夹中文件到oss
     *
     * @param filePath 文件完整路径
     * @param targetPath 上传到oss上的路径
     */
    public void uploadFile2OssWithPath(String filePath, String targetPath) {
        File fileOnServer = new File(filePath);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            this.uploadFile2OSS(fin, targetPath);
        } catch (FileNotFoundException e) {
            log.error("uploadImg2Oss fail reason is:{}", e);
            throw new BizRuntimeException("图片上传失败");
        }
    }

    /**
     * 登录ftp服务器，并将服务器中的文件上传到oss中
     *
     * @param host sftp连接的服务器的Ip
     * @param port sftp连接端口
     * @param userName 登录用户民
     * @param password 登录密码
     * @param timeout 超时时长
     * @param directory 目录
     * @param pos 目录精简层级
     * @param urlPrefix url前缀
     * @param tempFileDirectory 临时路径
     * @return 处理结果
     */
    public Result uploadFileFromSftpPath(String host, int port, String userName, String password, int timeout,
                                         String directory, int pos, String urlPrefix, String tempFileDirectory) {
        Result result = new Result();
        Session session = null;
        ChannelSftp channelSftp = null;
        List<String> fileNameList = new ArrayList<>();
        try {
            session = FtpUtils.genSession(host, port, userName, password, timeout);
            //通道
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            //连接通道
            channelSftp.connect();
            // 获取目录下所有的文件
            FtpUtils.genAllFiles(channelSftp, directory, fileNameList);
            // 将文件上传
            if (CollectionUtils.isNotEmpty(fileNameList)) {
                for (int i = 0; i < fileNameList.size(); i++) {
                    String filePath = fileNameList.get(i);
                    String[] split = filePath.split("/");
                    String fileName = split[split.length - 1];
                    // 图片路径
                    String urlPath = filePath;
                    for (int j = 0; j < pos + 1; j++) {
                        urlPath = urlPath.substring(urlPath.indexOf("/") + 1);
                    }
                    // oss文件路径
                    String targetPath = filePath;
                    for (int j = 0; j < pos; j++) {
                        targetPath = targetPath.substring(targetPath.indexOf("/") + 1);
                    }

                    // 远程读取文件上传到OSS
                    // 文件远程url地址
                    String url = urlPrefix + urlPath;
                    String tempFileParentDirectory = tempFileDirectory + "/" + targetPath.replace(fileName, "");
                    String tempFilePath = tempFileDirectory + "/" + targetPath;
                    log.info("url:{},tempFileParentDirectory:{},tempFilePath:{},targetPath:{}", url,
                            tempFileParentDirectory, tempFilePath, targetPath);
                    FtpUtils.saveFile(FtpUtils.getInputStreamByGet(url), tempFileParentDirectory, fileName);
                    uploadFile2OssWithPath(tempFilePath, targetPath);

                    //删除临时文件
                    File file = new File(tempFilePath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                result.setValue(fileNameList.size());
            }
            session.disconnect();
            channelSftp.quit();
        } catch (Exception e) {
            log.error("exception:{}", e);
            TransformValue.setErrorInfo(result, CodeMsgEnum.SYSTEM_EXCEPTION.getCode(),
                    CodeMsgEnum.SYSTEM_EXCEPTION.getMsg());
        } finally {
            log.info("finally deal");
            if (null != session) {
                session.disconnect();
            }
            if (null != channelSftp) {
                channelSftp.quit();
            }
        }
        return result;
    }

    /**
     * 将URL直接上传
     *
     * @param url URL地址
     */
    public void uploadImg2Oss(String url) {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2OSS(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
            log.error("uploadImg2Oss fail reason is:{}", e.getMessage());
            throw new BizRuntimeException("图片上传失败");
        }
    }

    /**
     * 上传图片到OSS
     *
     * @param file MultipartFile类型文件
     * @return 上传后文件名
     */
    public String uploadImg2Oss(MultipartFile file) {
        if (file.getSize() > 1024 * 1024) {
            log.error("uploadImg2Oss captcha reason is:{}", "上传图片大小不能超过1M");
            throw new BizRuntimeException("上传图片大小不能超过1M");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            throw new BizRuntimeException("图片上传失败");
        }
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl 文件地址
     * @return 图片路径
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器 如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf(".") + 1)));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
            log.info("uploadFile2OSS result is :{}", JSON.toJSON(putResult));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                log.error("Exception:{}", e);
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return OSS上文件的contentType
     */
    public String getContentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        if (filenameExtension.equalsIgnoreCase("pdf")) {
            return "application/pdf";
        }
        if (filenameExtension.equalsIgnoreCase("xls") || filenameExtension.equalsIgnoreCase("xlsx")) {
            return "application/vnd.ms-excel";
        }
        if (filenameExtension.equalsIgnoreCase(".mp4") || filenameExtension.equalsIgnoreCase("mp4")) {
            return "video/mp4";
        }
        if (filenameExtension.equalsIgnoreCase(".avi") || filenameExtension.equalsIgnoreCase("avi")) {
            return "video/avi";
        }
        if (filenameExtension.equalsIgnoreCase(".rm") || filenameExtension.equalsIgnoreCase("rm")) {
            return "application/vnd.rn-realmedia";
        }
        if (filenameExtension.equalsIgnoreCase(".rmvb") || filenameExtension.equalsIgnoreCase("rmvb")) {
            return "application/vnd.rn-realmedia-vbr";
        }
        if (filenameExtension.equalsIgnoreCase(".mov") || filenameExtension.equalsIgnoreCase("mov")
                || filenameExtension.equalsIgnoreCase(".MOV") || filenameExtension.equalsIgnoreCase("MOV")) {
            return "video/quicktime";
        }
        if (filenameExtension.equalsIgnoreCase(".3gp") || filenameExtension.equalsIgnoreCase("3gp")
                || filenameExtension.equalsIgnoreCase(".3GP") || filenameExtension.equalsIgnoreCase("3GP")) {
            return "video/3gpp";
        }
        if (filenameExtension.equalsIgnoreCase("zip")) {
            return "application/zip";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key 文件名
     * @return 有效URL
     */
    public String getUrl(String key) {
        // 设置URL过期时间为100年  3600l* 1000*24*365*10
        Date expiration = DateTimeFormatUtils.toDate(LocalDateTime.now().plusYears(100));
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 下载
     *
     * @param fileName 文件名
     * @return 文件流
     */
    public InputStream getOSS2InputStream(String fileName) {
        log.info("bucketName is {}", bucketName);
        log.info("fileName is {}", fileName);
        OSSObject ossObj = ossClient.getObject(bucketName, filedir + fileName);
        return ossObj.getObjectContent();
    }
}
