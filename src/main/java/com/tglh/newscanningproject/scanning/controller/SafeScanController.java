package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.*;
import com.tglh.newscanningproject.scanning.service.SafeScanService;
import com.tglh.newscanningproject.utils.AesUtil;
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
import java.util.*;

@Controller
@RequestMapping("/safeScan")
public class SafeScanController {
    //获取环境对象
    @Autowired
    private Environment env;

    @Autowired
    private SafeScanService safeScanService;


    @RequestMapping("/myList")
    @ResponseBody
    private String myList(String  encrypted) {
        String encryptedCode = AesUtil.decrypt(encrypted,AesUtil.KEY);
        JSONObject encryptedCodeObj=JSONObject.parseObject(encryptedCode);
        System.out.println(encryptedCodeObj);
        MyUploadList myUploadList = (MyUploadList) JSONObject.toJavaObject(encryptedCodeObj, MyUploadList.class);  //通过JSONObject.toBean()方法进行对象间的转换
        if(myUploadList.getAreaSelect().size() > 0){
            myUploadList.setBuMenId(Integer.valueOf(myUploadList.getAreaSelect().get(0).toString()));
            myUploadList.setQuYuId(Integer.valueOf(myUploadList.getAreaSelect().get(1).toString()));
        }
        Integer page = myUploadList.getPageNum();
        Integer rows = myUploadList.getPageSize();
        myUploadList.setStartPageNum((page-1)*rows);
        myUploadList.setEndPageNum((page*rows));
        Long total = safeScanService.selectTotal(myUploadList);

        List list = safeScanService.myList(myUploadList);
        JSONObject obj = new JSONObject();
        HashMap m = new HashMap();
        m.put("totalCount",total);
        m.put("pageSize",rows);
        m.put("list",list);
        obj.put("code",200);
        obj.put("message","成功");
        obj.put("data",m);
        return obj.toJSONString();
    }

    @RequestMapping("/statusFilter")
    @ResponseBody
    private String statusFilter() {
            List list = safeScanService.statusFilter();
            JSONObject obj = new JSONObject();
            obj.put("code",200);
            obj.put("message","成功");
            obj.put("data",list);
            return obj.toJSONString();
    }

    @RequestMapping("/areaFilter")
    @ResponseBody
    private String areaFilter() {
        List<ScanArea> list = safeScanService.areaFilter();
        JSONObject obj = new JSONObject();
        obj.put("code",200);
        obj.put("message","成功");
        obj.put("data",list);
        return obj.toJSONString();
    }

    @RequestMapping("/addRecord")
    @ResponseBody
    private String addRecord(String  encrypted) {
        //解密 --- 前端代码传过来的 用户编码 和 密码
        String encryptedCode = AesUtil.decrypt(encrypted,AesUtil.KEY);
        JSONObject encryptedCodeObj=JSONObject.parseObject(encryptedCode);
        ScanRecordAdvise scanRecordAdvise = (ScanRecordAdvise) JSONObject.toJavaObject(encryptedCodeObj, ScanRecordAdvise.class);  //通过JSONObject.toBean()方法进行对象间的转换
        Map maxIdMap = safeScanService.getMaxId();
        scanRecordAdvise.setMaxId(maxIdMap.get("id").toString());
        safeScanService.addRecord(scanRecordAdvise);
        safeScanService.addRecordAction(scanRecordAdvise);
        JSONObject obj = new JSONObject();
        Map map = new HashMap();

        obj.put("code",200);
        obj.put("message","成功");
        obj.put("data",map);
        return obj.toJSONString();
    }


    @RequestMapping("/areaInfo")
    @ResponseBody
    private String areaInfo(String code) {
        ScanArea scanArea = safeScanService.areaInfo(code);
        List<ScanAreaItems> scanAreaItemsList = safeScanService.areaInfoItems(code);
        JSONObject obj = new JSONObject();
        Map map = new HashMap();
        map.put("areaInfo",scanArea);
        map.put("areaItems",scanAreaItemsList);
        obj.put("code",200);
        obj.put("message","成功");
        obj.put("data",map);
        return obj.toJSONString();
    }

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
            JSONObject obj = new JSONObject();
            obj.put("code",407);
            return obj.toJSONString();
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
        return "";
    }



    @RequestMapping("/delImg")
    @ResponseBody
    private String delImg(String imgUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            String propertyfileUpload = env.getProperty("vocs.fileUpload.rootSavePath");
            //用作返回的目录结
            String filePath = propertyfileUpload + "/" + imgUrl + "/";
            File deleteDir = new File(filePath);
            // 判断文件是否存在
            boolean flag = false;
            flag = deleteDir.exists();
            if (flag == true) {
                boolean deleteFlag = deleteDir.delete();
                if (deleteFlag == true) {
                    jsonObject.put("code",200);
                    jsonObject.put("message","成功删除了");
                    return jsonObject.toJSONString();
                }
            }
        }catch (Exception e){
            jsonObject.put("code",407);
            jsonObject.put("message","删除未成功");
            return jsonObject.toJSONString();
        }

        return "";
    }
}