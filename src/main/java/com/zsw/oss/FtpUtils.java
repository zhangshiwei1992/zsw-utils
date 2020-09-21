/*
 * Copyright 2019 yifen7.com All right reserved. This software is the
 * confidential and proprietary information of yifen7.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with yifen7.com.
 */

package com.zsw.oss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.yifenqi.boot.core.constants.CodeMsgEnum;

import app.myoss.cloud.core.lang.dto.Result;
import app.myoss.cloud.core.lang.dto.TransformValue;
import lombok.extern.slf4j.Slf4j;

/**
 * FTP连接获取文件
 *
 * @author xujiali
 * @since 2019年5月7日 下午1:10:36
 */
@Slf4j
public class FtpUtils {

    /**
     * 子路径格式
     */
    private static final String               DIRECTORY_KEY            = "%s/%s";
    /**
     * 文件全路径格式
     */
    private static final String               FILE_PATH_KEY            = "%s/%s";
    /**
     * 排除的文件夹.
     */
    private static final String               EXCLUDE_POINT_KEY        = ".";
    /**
     * 排除的文件夹..
     */
    private static final String               EXCLUDE_DOUBLE_POINT_KEY = "..";

    /**
     * sftp连接池.
     */
    private static final Map<String, Channel> SFTP_CHANNEL_POOL        = new HashMap<>();

    /**
     * 登录FTP服务器
     *
     * @param host FTP服务器的地址
     * @param port FTP服务器连接的端口
     * @param userName 连接FTP服务器的用户名
     * @param password 连接FTP服务器的密码
     * @return ftpClient
     */
    public static FTPClient login(String host, int port, String userName, String password) {
        try {
            FTPClient ftp = new FTPClient();
            ftp.connect(host, port);
            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                if (ftp.login(userName, password)) {
                    ftp.setControlEncoding("GBK");
                }
            }
            return ftp;
        } catch (Exception e) {
            log.error("login exception:{}", e);
            return null;
        }
    }

    /**
     * FTP服务器断开连接
     *
     * @param ftp FTP服务器
     * @return 返回结果
     */
    public static Result disConnection(FTPClient ftp) {
        Result result = new Result();
        try {
            if (ftp.isConnected()) {
                ftp.disconnect();
            }
            return result;
        } catch (Exception e) {
            log.error("disConnection exception:{}", e);
            return TransformValue.setErrorInfo(result, CodeMsgEnum.SYSTEM_EXCEPTION.getCode(),
                    CodeMsgEnum.SYSTEM_EXCEPTION.getMsg());
        }
    }

    /**
     * 切换到对应的路径下
     *
     * @param ftp FTP服务器
     * @param path 路径
     * @return 结果
     */
    public static Result changeWorkingDirectory(FTPClient ftp, String path) {
        Result result = new Result();
        try {
            ftp.changeWorkingDirectory(path);
            ftp.enterLocalActiveMode();
            return result;
        } catch (Exception e) {
            log.error("changeWorkingDirectory exception:{}", e);
            return TransformValue.setErrorInfo(result, CodeMsgEnum.SYSTEM_EXCEPTION.getCode(),
                    CodeMsgEnum.SYSTEM_EXCEPTION.getMsg());
        }
    }

    /**
     * 读取FTP服务器上指定路径的文件列表
     *
     * @param ftp ftp连接
     * @param path 路径
     */
    public static void readFiles(FTPClient ftp, String path) {
        try {
            changeWorkingDirectory(ftp, path);

            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                log.info(file.getName());
            }
        } catch (Exception e) {
            log.error("readFiles exception:{}", e);
        }
    }

    /**
     * 建立SFtp连接
     *
     * @param host sftp连接的服务器的Ip
     * @param port sftp连接端口
     * @param userName 登录用户民
     * @param password 登录密码
     * @param timeout 超时时长
     * @return session实例
     */
    public static Session genSession(String host, int port, String userName, String password, int timeout) {
        Session session = null;
        try {
            //创建JSch对象
            JSch jSch = new JSch();
            //根据用户名，主机ip和端口获取一个Session对象
            session = jSch.getSession(userName, host, port);
            //设置密码
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            //为Session对象设置properties
            session.setConfig(config);

            //设置超时
            session.setTimeout(timeout);
            //通过Session建立连接
            session.connect();
        } catch (Exception e) {
            log.error("genSession exception:{}", e);
        }
        return session;
    }

    /**
     * 建立SFtp连接
     *
     * @param host sftp连接的服务器的Ip
     * @param port sftp连接端口
     * @param userName 登录用户民
     * @param password 登录密码
     * @param timeout 超时时长
     * @return 通道实例
     */
    public static ChannelSftp genChannel(String host, int port, String userName, String password, int timeout) {
        Session session = null;
        ChannelSftp channelSftp = null;
        String key = host + "," + port + "," + userName + "," + password;

        try {
            if (null == SFTP_CHANNEL_POOL.get(key)) {
                log.info("SSS");
                session = genSession(host, port, userName, password, timeout);
                if (session.isConnected()) {
                    //通道
                    channelSftp = (ChannelSftp) session.openChannel("sftp");
                    //连接通道
                    channelSftp.connect();
                }
                SFTP_CHANNEL_POOL.put(key, channelSftp);
            } else {
                log.info("EEE");
                channelSftp = (ChannelSftp) SFTP_CHANNEL_POOL.get(key);
                session = channelSftp.getSession();
                if (!session.isConnected()) {
                    session.connect();
                }
                if (!channelSftp.isConnected()) {
                    channelSftp.connect();
                }
            }
            log.info("SFTP_CHANNEL_POOL.get(key):{}", JSON.toJSON(SFTP_CHANNEL_POOL.get(key)));
        } catch (Exception e) {
            log.error("genChannel exception:{}", e);
        } finally {
            if (null != session) {
                closeSession(session);
                log.info("session closed");
            }
            if (null != channelSftp) {
                closeChannel(channelSftp);
                log.info("channelSftp closed");
            }
        }
        return channelSftp;
    }

    /**
     * 建立SFtp链接通道
     *
     * @param session 链接
     * @return 通道
     */
    public static ChannelSftp genChannel(Session session) {
        ChannelSftp channelSftp = null;
        try {
            //通道
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            //连接通道
            channelSftp.connect();
        } catch (Exception e) {
            log.error("genChannel exception:{}", e);
        } finally {
            if (null != session) {
                closeSession(session);
            }
            if (null != channelSftp) {
                closeChannel(channelSftp);
            }
        }
        return channelSftp;
    }

    /**
     * 遍历文件名
     *
     * @param host sftp连接的服务器的Ip
     * @param port sftp连接端口
     * @param userName 登录用户民
     * @param password 登录密码
     * @param timeout 超时时长
     * @param directory 目录
     * @return 文件名清单
     */
    public static List<String> listFiles(String host, int port, String userName, String password, int timeout,
                                         String directory) {
        Session session = null;
        ChannelSftp channelSftp = null;
        List<String> fileNameList = new ArrayList<>();
        try {
            session = genSession(host, port, userName, password, timeout);
            //通道
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            //连接通道
            channelSftp.connect();
            genAllFiles(channelSftp, directory, fileNameList);
        } catch (Exception e) {
            log.info("listFiles exception:{}", e);
            return null;
        } finally {
            if (null != session) {
                closeSession(session);
            }
            if (null != channelSftp) {
                closeChannel(channelSftp);
            }
        }
        return fileNameList;
    }

    /**
     * 关闭连接
     *
     * @param session 服务
     */
    public static void closeSession(Session session) {
        session.disconnect();
    }

    /**
     * 关闭通道
     *
     * @param channelSftp 通道
     */
    public static void closeChannel(ChannelSftp channelSftp) {
        channelSftp.quit();
    }

    /**
     * 获取目录中所有子目录中文件名
     *
     * @param channelSftp sftp通道
     * @param directory 目录
     * @param fileNameList 文件名列表
     */
    public static void genAllFiles(ChannelSftp channelSftp, String directory, List<String> fileNameList) {
        try {
            Vector<LsEntry> fileList = channelSftp.ls(directory);
            for (LsEntry file : fileList) {
                // 判断是否是目录，如果是则继续 排除目录/.和/..
                if (!EXCLUDE_POINT_KEY.equals(file.getFilename())
                        && !EXCLUDE_DOUBLE_POINT_KEY.equals(file.getFilename())) {
                    if (file.getAttrs().isDir()) {
                        String path = String.format(DIRECTORY_KEY, directory, file.getFilename());
                        genAllFiles(channelSftp, path, fileNameList);
                    } else {
                        fileNameList.add(String.format(FILE_PATH_KEY, directory, file.getFilename()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("sftp genAllFiles exception:{}", e);
        }
    }

    /**
     * 远程下载文件到本地
     *
     * @param channelSftp 通道
     * @param filePath 远程文件全名
     * @param localPath 本地路径
     * @throws Exception 异常
     */
    public static void downloadFile(ChannelSftp channelSftp, String filePath, String localPath) throws Exception {
        try {
            String[] split = filePath.split("/");
            String remoteFileName = split[split.length - 1];
            String tempFileName = String.format(FILE_PATH_KEY, localPath, remoteFileName);
            String remotePath = filePath.replace("/" + remoteFileName, "");
            log.info("remoteFileName:{},remotePath:{},tempFileName:{}", remoteFileName, remotePath, tempFileName);

            //            channelSftp.cd(remotePath);
            channelSftp.get(filePath, tempFileName);

        } catch (Exception e) {
            log.error("downloadFile exception:{}", e);
            throw new Exception(e);
        }
    }

    /**
     * 下载文件
     *
     * @param host sftp连接的服务器的Ip
     * @param port sftp连接端口
     * @param userName 登录用户民
     * @param password 登录密码
     * @param timeout 超时时长
     * @param filePath 远程文件路径
     * @param localPath 本地路径
     */
    public static void downloadFile(String host, int port, String userName, String password, int timeout,
                                    String filePath, String localPath) {
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = genSession(host, port, userName, password, timeout);
            channelSftp = genChannel(session);
            downloadFile(channelSftp, filePath, localPath);

        } catch (Exception e) {
            log.error("exception:{}", e);
        } finally {
            if (null != session) {
                closeSession(session);
            }
            if (null != channelSftp) {
                closeChannel(channelSftp);
            }
        }
    }

    /**
     * 读取文件
     *
     * @param url 远程url
     * @return 结果
     */
    public static InputStream getInputStreamByGet(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            log.error("exception:{}", e);
        }
        return null;
    }

    /**
     * 写文件
     *
     * @param is 文件流
     * @param directory 文件路径
     * @param fileName 文件名
     */
    public static void saveFile(InputStream is, String directory, String fileName) {
        File fileDirectory = new File(directory);
        if (!fileDirectory.exists() && !fileDirectory.isDirectory()) {
            fileDirectory.mkdirs();
        }
        File file = new File(fileDirectory, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                log.error("exception:{}", e);
            }
        }
        try (BufferedInputStream bis = new BufferedInputStream(is);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
        } catch (IOException e) {
            log.error("exception:{}", e);
        }
    }

}
