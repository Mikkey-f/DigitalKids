package com.digital.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/25 21:31
 */
@Slf4j
public class OssPutUtil {

    private final static String END_POINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private final static String ACCESS_KEY_ID = "LTAI5tRM7w24Y8D2xFDMxC9T";
    private final static String ACCESS_KEY_SECRET = "ZnfzCVFnSDJbY527Y7VPvx9xxMlWmj";
    private final static String MY_END_POINT = "http://web-zff.oss-cn-hangzhou.aliyuncs.com";
    private final static String BUCKET_NAME = "web-zff";

    public static String fileUpload(String fileName, String tempFilePath) throws FileNotFoundException {
        // 用于在OSS上命名，建议格式 ：年月日/文件名.后缀名，此时可以 以时间建立一个文件夹保存上传的图片
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String transformDate = simpleDateFormat.format(new Date());
        String objectName = transformDate + "/" + System.currentTimeMillis() + "_" + fileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        // 设置设置 HTTP 头 里边的 Content-Type
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(OssPutUtil.getContentType(fileName.substring(fileName.lastIndexOf("."))));
        objectMetadata.setContentDisposition("inline");
        // 上传文件流。
        InputStream inputStream = new FileInputStream(tempFilePath);

        ossClient.putObject(BUCKET_NAME, objectName, inputStream);

        return MY_END_POINT + "/" + objectName;
    }

    public static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }

        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }
}
