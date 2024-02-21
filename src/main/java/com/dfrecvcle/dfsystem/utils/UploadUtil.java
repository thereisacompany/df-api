package com.dfrecvcle.dfsystem.utils;

import com.dfrecvcle.dfsystem.exception.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@Component
public class UploadUtil {
    //源文件名
    private String originalFilename;

    //源文件后缀名
    private String suffix;

    //存入数据库里的tomcat虚拟路径
    private String dbPath;


//    @Autowired
//    private FileProperties fileProperties;

    //文件大小
    private long size;

    //实际存储路径
    private String realPath;

    /**
     * 文件上传工具类
     * @param attach
     * @param request
     * @param uploader 文件上传者
     * @return
     */

    public boolean doUpload(MultipartFile attach, HttpServletRequest request, String uploader){
//        System.out.println(fileProperties.getUrlLocation());
//        System.out.println(fileProperties.getSavePath());
        if(!attach.isEmpty()){
            originalFilename = attach.getOriginalFilename();
            System.out.println("==>上传的文件名："+originalFilename);

            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            System.out.println("==>上传的文件后缀名："+suffix);

            size = attach.getSize();
            System.out.println("==>上传文件的大小："+size);

            String currentFilename = System.currentTimeMillis()+ UUID.randomUUID().toString() + suffix;
            System.out.println("==>存储的上传文件名："+currentFilename);

            realPath = "/var/www/html/images/"+uploader ;
//            realPath = fileProperties.getSavePath()+uploader ;
            System.out.println("==>上传文件保存的真实路径："+realPath);

            File targetFile = new File(realPath, currentFilename);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }

            try{
                attach.transferTo(targetFile);
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            realPath = realPath + "/" + currentFilename;
//            dbPath =  request.getContextPath() + "/" + uploader + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/" + currentFilename;
            dbPath = "http://34.81.192.108/images/" + uploader + "/" + currentFilename;
//            dbPath = fileProperties.getUrlLocation()+"/Server/images/"+ uploader + "/" + currentFilename;
            System.out.println("URL："+dbPath);
            return true;
        }else{
            return false;
        }
    }
    public String getUploadFile(){
        return dbPath;
    }
}