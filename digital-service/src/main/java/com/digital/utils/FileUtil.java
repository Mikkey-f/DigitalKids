package com.digital.utils;

import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.result.Result;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/25 15:11
 */
public class FileUtil {

    public static String downloadFileAndGetFilePath(MultipartFile file, String resourcePath) {
        // 构建image文件夹路径
        String imageFolderPath = resourcePath;
        File imageFolder = new File(imageFolderPath);
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String filePath = null;
        if (StringUtils.hasText(originalFilename)) {
            // 生成唯一的文件名，避免文件名冲突
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;
            // 构建文件的完整路径
            filePath = imageFolderPath + uniqueFileName;
            File destFile = new File(filePath);
            try {
                // 将文件写入到指定路径
                file.transferTo(destFile);
            } catch (IOException e) {
                throw new BusinessException(ResultErrorEnum.FILE_UPLOAD_ERROR);
            }
        }
        return filePath;
    }

    public static FileItem createFileItem(String filePath, String fileName){
        String fieldName = "file";
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", false,fileName);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(newfile);
             OutputStream os = item.getOutputStream()) {
            while ((bytesRead = fis.read(buffer, 0, 8192))!= -1)
            {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }



}
