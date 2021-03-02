package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.PicDetail;
import com.tglh.newscanningproject.utils.FileTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/safeScan")
public class SafeScanController {
    //获取环境对象
    @Autowired
    private Environment env;

    @RequestMapping("/uploadImg")
    @ResponseBody
    private String uploadImg(HttpServletRequest request) {
        //定义输出流

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try{
            CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            commonsMultipartResolver.setDefaultEncoding("utf-8");
            if (commonsMultipartResolver.isMultipart(request)){
                MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;
                Map<String, MultipartFile> map = mulReq.getFileMap();

                // key为前端的name属性，value为上传的对象（MultipartFile）
                for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {
                    // 自己的保存文件逻辑
                    System.out.println("key:"+entry.getKey()+":"+"value:"+entry.getValue());

                    MultipartFile fileValue = entry.getValue();
                    String fileExtendsName = fileValue.getOriginalFilename()
                            .substring(fileValue.getOriginalFilename().lastIndexOf("."));
                    byte[] imgBytes = fileValue.getBytes();

                    //设置目录结构
                    //获取当前日期
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateCategory = formatter.format(date);
                    String uuidSign = UUID.randomUUID().toString();
                    String propertyfileUpload = env.getProperty("vocs.fileUpload.rootSavePath");

                    //用作返回的目录结构
                    String backUrl = dateCategory+"/"+uuidSign+ fileExtendsName;
                    String filePath = propertyfileUpload +"/"+ dateCategory +"/";

                    File pointDir = new File(filePath);
                    if(!pointDir.exists()&&!pointDir.isDirectory()){//判断文件目录是否存在
                        pointDir.mkdirs();
                    }
                    File file=new File(filePath +uuidSign+ fileExtendsName);
                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);
                    bos.write(imgBytes);

                    JSONObject obj = new JSONObject();
                    Map mapPic = new HashMap();
                    mapPic.put("url",backUrl);
                    obj.put("code",200);
                    obj.put("data",mapPic);
                    return obj.toJSONString();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }




     /*   //定义输出流
        FileOutputStream fileOutputStream = null;
        //项目上传图片路径
        String picDetailUrl = picDetail.getFilePath();
        File transferFile = new File(picDetailUrl);

        String fileExtendsName = FileTypeUtils.getFileTypeByFile(transferFile);
        //将blob流转换为 byte 进行存储在目录机构里面
        byte[] bytes = null;
        try {
            FileInputStream fis = new FileInputStream(picDetailUrl);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1){
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            bytes =  bos.toByteArray();
            //bytes转换为真正的文件夹目录
            //设置目录结构
            //获取当前日期
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateCategory = formatter.format(date);
            String uuidSign = System.currentTimeMillis()+ UUID.randomUUID().toString();
            String propertyfileUpload = env.getProperty("vocs.fileUpload.rootSavePath");

            String filePath = propertyfileUpload +"/"+ dateCategory +"/";


            File pointDir = new File(filePath);
            if(!pointDir.exists()&&pointDir.isDirectory()){//判断文件目录是否存在
                pointDir.mkdirs();
            }

            fileOutputStream = new FileOutputStream(filePath+uuidSign+fileExtendsName);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();

            JSONObject obj = new JSONObject();
            obj.put("code",200);
            obj.put("url","");

        } catch(Exception ex){
            ex.printStackTrace();
        }
*/
        return "";
    }
}
