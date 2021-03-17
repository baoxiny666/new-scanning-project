package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.*;
import com.tglh.newscanningproject.scanning.service.SafeScanService;
import com.tglh.newscanningproject.utils.AesUtil;
import com.tglh.newscanningproject.utils.FileTypeUtils;
import net.sf.json.JSONArray;
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
import java.nio.channels.FileChannel;
import java.sql.Blob;
import java.text.DecimalFormat;
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
        //获得每页显示的条数
        Integer rows = safeScanService.selectListPageSize();
        Integer page = myUploadList.getPageNum();
        //Integer rows = myUploadList.getPageSize();
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
    //2021-0316 在我上报的内容中也加入了审核归档内容--暂定 我上报的点击详情进入页面
   /* @RequestMapping("/showMyListRecord")
    @ResponseBody
    private String showMyListRecord(String  id,String userNo) {

        try {
            //总的jsonObject
            JSONObject jsonObject = new JSONObject();
            //首先去查询当前记录id对应的内容
            MyUploadList myUploadListDetail = safeScanService.selectCurrentIdDetail(id);
            //将数据库存储的itemids存到数据库的RecordItems字段内
            myUploadListDetail.setRecordItems(JSONArray.fromObject(myUploadListDetail.getItemIds()));

            //去scan_action表中去查询记录
            List stepInfoList = new ArrayList();
            List stepInfoEncodeList = new ArrayList();
            List<ScanMyListAction> scanMyListActionsList = safeScanService.selectScanActionRecords(id);
            for (int stepInfoIndex = 0; stepInfoIndex < scanMyListActionsList.size(); stepInfoIndex++) {
                HashMap<String, String> stepInfoHashMap = new HashMap<>();
                stepInfoHashMap.put("title", scanMyListActionsList.get(stepInfoIndex).getActionTypeName());
                stepInfoHashMap.put("desc", scanMyListActionsList.get(stepInfoIndex).getActionUserName() + " " + scanMyListActionsList.get(stepInfoIndex).getActionTime());
                stepInfoList.add(stepInfoHashMap);
                stepInfoEncodeList.add(scanMyListActionsList.get(stepInfoIndex).getActionType());
            }
            Integer actionType = scanMyListActionsList.get(scanMyListActionsList.size() - 1).getActionType();



            //查询出当前用户有哪个权限
            ScanPermission scanPermission = safeScanService.selectDetailPermission(userNo);
            if(scanPermission == null){
                JSONObject obj = new JSONObject();
                obj.put("isVerify",0);
                obj.put("isHandle",0);
                obj.put("isDone",0);
                obj.put("recordInfo",myUploadListDetail);
                obj.put("stepInfo",stepInfoList);
                obj.put("actionInfo",new Object[0]);
                jsonObject.put("code",200);
                jsonObject.put("message","读取记录信息成功");
                jsonObject.put("data",obj);
                return jsonObject.toJSONString();
            }else{
                //安全区域讲解
                if("area_safe".equals(scanPermission.getRoleNo())){
                    String areaNo = scanPermission.getAreaNo();
                    String myUploadDetailAreaNo = myUploadListDetail.getAreaNo();
                    if(areaNo.equals(myUploadDetailAreaNo)){
                        if(1 == actionType){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",1);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            obj.put("actionInfo",new Object[0]);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else if(2 == actionType){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",1);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            obj.put("actionInfo",new Object[0]);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else if(3 == actionType){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            //已经到达为 3 状态的阶段
                            List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                            obj.put("actionInfo",scanMyChargeActionList);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else if(4 == actionType){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            //已经到达为 3 状态的阶段
                            List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                            obj.put("actionInfo",scanMyChargeActionList);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else if(0 == actionType){
                            boolean isContainsEncodeOne = stepInfoEncodeList.contains(1);
                            boolean isContainsEncodeTwo = stepInfoEncodeList.contains(2);
                            //如果两个都包含 1 ，2
                            if(isContainsEncodeOne && isContainsEncodeTwo){
                                JSONObject obj = new JSONObject();
                                obj.put("isVerify",0);
                                obj.put("isHandle",0);
                                obj.put("isDone",0);
                                obj.put("recordInfo",myUploadListDetail);
                                obj.put("stepInfo",stepInfoList);
                                //已经到达为 3 状态的阶段
                                List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                                obj.put("actionInfo",scanMyChargeActionList);
                                jsonObject.put("code",200);
                                jsonObject.put("message","读取记录信息成功");
                                jsonObject.put("data",obj);
                                return jsonObject.toJSONString();
                            }else{
                                JSONObject obj = new JSONObject();
                                obj.put("isVerify",0);
                                obj.put("isHandle",0);
                                obj.put("isDone",0);
                                obj.put("recordInfo",myUploadListDetail);
                                obj.put("stepInfo",stepInfoList);
                                obj.put("actionInfo",new Object[0]);
                                jsonObject.put("code",200);
                                jsonObject.put("message","读取记录信息成功");
                                jsonObject.put("data",obj);
                                return jsonObject.toJSONString();
                            }

                        }
                    }else{
                        JSONObject obj = new JSONObject();
                        obj.put("isVerify",0);
                        obj.put("isHandle",0);
                        obj.put("isDone",0);
                        obj.put("recordInfo",myUploadListDetail);
                        obj.put("stepInfo",stepInfoList);
                        obj.put("actionInfo",new Object[0]);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }
                }else if("depart_safe".equals(scanPermission.getRoleNo())){
                    Integer departId = scanPermission.getDepartId();
                    Integer myUploadDetailDepartId = myUploadListDetail.getDepartId();
                    if(departId == myUploadDetailDepartId){
                        if(1 == actionType){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            obj.put("actionInfo",new Object[0]);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else if(2 == actionType){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            obj.put("actionInfo",new Object[0]);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else if(3 == actionType){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            //已经到达为 3 状态的阶段
                            List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                            obj.put("actionInfo",scanMyChargeActionList);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else if(4 == actionType){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            //已经到达为 3 状态的阶段
                            List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                            obj.put("actionInfo",scanMyChargeActionList);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else if(0 == actionType){
                            boolean isContainsEncodeOne = stepInfoEncodeList.contains(1);
                            boolean isContainsEncodeTwo = stepInfoEncodeList.contains(2);
                            //如果两个都包含 1 ，2
                            if(isContainsEncodeOne && isContainsEncodeTwo){
                                JSONObject obj = new JSONObject();
                                obj.put("isVerify",0);
                                obj.put("isHandle",0);
                                obj.put("isDone",0);
                                obj.put("recordInfo",myUploadListDetail);
                                obj.put("stepInfo",stepInfoList);
                                //已经到达为 3 状态的阶段
                                List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                                obj.put("actionInfo",scanMyChargeActionList);
                                jsonObject.put("code",200);
                                jsonObject.put("message","读取记录信息成功");
                                jsonObject.put("data",obj);
                                return jsonObject.toJSONString();
                            }else{
                                JSONObject obj = new JSONObject();
                                obj.put("isVerify",0);
                                obj.put("isHandle",0);
                                obj.put("isDone",0);
                                obj.put("recordInfo",myUploadListDetail);
                                obj.put("stepInfo",stepInfoList);
                                obj.put("actionInfo",new Object[0]);
                                jsonObject.put("code",200);
                                jsonObject.put("message","读取记录信息成功");
                                jsonObject.put("data",obj);
                                return jsonObject.toJSONString();
                            }

                        }
                    }else{
                        JSONObject obj = new JSONObject();
                        obj.put("isVerify",0);
                        obj.put("isHandle",0);
                        obj.put("isDone",0);
                        obj.put("recordInfo",myUploadListDetail);
                        obj.put("stepInfo",stepInfoList);
                        obj.put("actionInfo",new Object[0]);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }
                }else if("all_safe".equals(scanPermission.getRoleNo())){
                    if(1 == actionType){
                        JSONObject obj = new JSONObject();
                        obj.put("isVerify",0);
                        obj.put("isHandle",0);
                        obj.put("isDone",0);
                        obj.put("recordInfo",myUploadListDetail);
                        obj.put("stepInfo",stepInfoList);
                        obj.put("actionInfo",new Object[0]);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }else if(2 == actionType){
                        JSONObject obj = new JSONObject();
                        obj.put("isVerify",0);
                        obj.put("isHandle",0);
                        obj.put("isDone",0);
                        obj.put("recordInfo",myUploadListDetail);
                        obj.put("stepInfo",stepInfoList);
                        obj.put("actionInfo",new Object[0]);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }else if(3 == actionType){
                        JSONObject obj = new JSONObject();
                        obj.put("isVerify",0);
                        obj.put("isHandle",0);
                        obj.put("isDone",0);
                        obj.put("recordInfo",myUploadListDetail);
                        obj.put("stepInfo",stepInfoList);
                        //已经到达为 3 状态的阶段
                        List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                        obj.put("actionInfo",scanMyChargeActionList);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }else if(4 == actionType){
                        JSONObject obj = new JSONObject();
                        obj.put("isVerify",0);
                        obj.put("isHandle",0);
                        obj.put("isDone",0);
                        obj.put("recordInfo",myUploadListDetail);
                        obj.put("stepInfo",stepInfoList);
                        //已经到达为 3 状态的阶段
                        List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                        obj.put("actionInfo",scanMyChargeActionList);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }else if(0 == actionType){
                        boolean isContainsEncodeOne = stepInfoEncodeList.contains(1);
                        boolean isContainsEncodeTwo = stepInfoEncodeList.contains(2);
                        //如果两个都包含 1 ，2
                        if(isContainsEncodeOne && isContainsEncodeTwo){
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            //已经到达为 3 状态的阶段
                            List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                            obj.put("actionInfo",scanMyChargeActionList);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }else{
                            JSONObject obj = new JSONObject();
                            obj.put("isVerify",0);
                            obj.put("isHandle",0);
                            obj.put("isDone",0);
                            obj.put("recordInfo",myUploadListDetail);
                            obj.put("stepInfo",stepInfoList);
                            obj.put("actionInfo",new Object[0]);
                            jsonObject.put("code",200);
                            jsonObject.put("message","读取记录信息成功");
                            jsonObject.put("data",obj);
                            return jsonObject.toJSONString();
                        }
                    }
                }

            }
            //安全区域讲解
        }catch (Exception e){
            JSONObject obj = new JSONObject();
            obj.put("code",407);
            obj.put("message","读取记录信息失败");
            obj.put("data",obj);
            return obj.toJSONString();
        }
        return null;
    }*/

    @RequestMapping("/showMyListRecord")
    @ResponseBody
    private String showMyListRecord(String  id,String userNo) {

        try {
            //总的jsonObject
            JSONObject jsonObject = new JSONObject();
            //首先去查询当前记录id对应的内容
            MyUploadList myUploadListDetail = safeScanService.selectCurrentIdDetail(id);
            //将数据库存储的itemids存到数据库的RecordItems字段内
            myUploadListDetail.setRecordItems(JSONArray.fromObject(myUploadListDetail.getItemIds()));

            //去scan_action表中去查询记录
            List stepInfoList = new ArrayList();
            List stepInfoEncodeList = new ArrayList();
            List<ScanMyListAction> scanMyListActionsList = safeScanService.selectScanActionRecords(id);
            for (int stepInfoIndex = 0; stepInfoIndex < scanMyListActionsList.size(); stepInfoIndex++) {
                HashMap<String, String> stepInfoHashMap = new HashMap<>();
                stepInfoHashMap.put("title", scanMyListActionsList.get(stepInfoIndex).getActionTypeName());
                stepInfoHashMap.put("desc", scanMyListActionsList.get(stepInfoIndex).getActionUserName() + " " + scanMyListActionsList.get(stepInfoIndex).getActionTime());
                stepInfoList.add(stepInfoHashMap);
                stepInfoEncodeList.add(scanMyListActionsList.get(stepInfoIndex).getActionType());
            }
            Integer actionType = scanMyListActionsList.get(scanMyListActionsList.size() - 1).getActionType();
            if(1 == actionType){
                JSONObject obj = new JSONObject();
                obj.put("isVerify",0);
                obj.put("isHandle",0);
                obj.put("isDone",0);
                obj.put("recordInfo",myUploadListDetail);
                obj.put("stepInfo",stepInfoList);
                obj.put("actionInfo",new Object[0]);
                jsonObject.put("code",200);
                jsonObject.put("message","读取记录信息成功");
                jsonObject.put("data",obj);
                return jsonObject.toJSONString();
            }else if(2 == actionType){
                JSONObject obj = new JSONObject();
                obj.put("isVerify",0);
                obj.put("isHandle",0);
                obj.put("isDone",0);
                obj.put("recordInfo",myUploadListDetail);
                obj.put("stepInfo",stepInfoList);
                obj.put("actionInfo",new Object[0]);
                jsonObject.put("code",200);
                jsonObject.put("message","读取记录信息成功");
                jsonObject.put("data",obj);
                return jsonObject.toJSONString();
            }else if(3 == actionType){
                JSONObject obj = new JSONObject();
                obj.put("isVerify",0);
                obj.put("isHandle",0);
                obj.put("isDone",0);
                obj.put("recordInfo",myUploadListDetail);
                obj.put("stepInfo",stepInfoList);
                //已经到达为 3 状态的阶段
                List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                obj.put("actionInfo",scanMyChargeActionList);
                jsonObject.put("code",200);
                jsonObject.put("message","读取记录信息成功");
                jsonObject.put("data",obj);
                return jsonObject.toJSONString();
            }else if(4 == actionType){
                JSONObject obj = new JSONObject();
                obj.put("isVerify",0);
                obj.put("isHandle",0);
                obj.put("isDone",0);
                obj.put("recordInfo",myUploadListDetail);
                obj.put("stepInfo",stepInfoList);
                //已经到达为 3 状态的阶段
                List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                obj.put("actionInfo",scanMyChargeActionList);
                jsonObject.put("code",200);
                jsonObject.put("message","读取记录信息成功");
                jsonObject.put("data",obj);
                return jsonObject.toJSONString();
            }else if(0 == actionType){
                boolean isContainsEncodeOne = stepInfoEncodeList.contains(1);
                boolean isContainsEncodeTwo = stepInfoEncodeList.contains(2);
                //如果两个都包含 1 ，2
                if(isContainsEncodeOne && isContainsEncodeTwo){
                    JSONObject obj = new JSONObject();
                    obj.put("isVerify",0);
                    obj.put("isHandle",0);
                    obj.put("isDone",0);
                    obj.put("recordInfo",myUploadListDetail);
                    obj.put("stepInfo",stepInfoList);
                    //已经到达为 3 状态的阶段
                    List<ScanMyListAction> scanMyChargeActionList = safeScanService.selectScanActionHandle(id);
                    obj.put("actionInfo",scanMyChargeActionList);
                    jsonObject.put("code",200);
                    jsonObject.put("message","读取记录信息成功");
                    jsonObject.put("data",obj);
                    return jsonObject.toJSONString();
                }else{
                    JSONObject obj = new JSONObject();
                    obj.put("isVerify",0);
                    obj.put("isHandle",0);
                    obj.put("isDone",0);
                    obj.put("recordInfo",myUploadListDetail);
                    obj.put("stepInfo",stepInfoList);
                    obj.put("actionInfo",new Object[0]);
                    jsonObject.put("code",200);
                    jsonObject.put("message","读取记录信息成功");
                    jsonObject.put("data",obj);
                    return jsonObject.toJSONString();
                }

            }
        }catch (Exception e){
            JSONObject obj = new JSONObject();
            obj.put("code",407);
            obj.put("message","读取记录信息失败");
            obj.put("data",obj);
            return obj.toJSONString();
        }
        return null;
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

    @RequestMapping("/fileList")
    @ResponseBody
    public String fileList()  {
        JSONObject totalObject = new JSONObject();
        Integer index = 1;
        try {
            List listname = new ArrayList();
            String propertyfileUpload = env.getProperty("vocs.fileUpload.mustReadSavePath");
            String mustStaticReadHttpPath = env.getProperty("vocs.fileUpload.mustReadRealHttpPath");
            totalObject.put("baseURL", mustStaticReadHttpPath);
            readAllFile(propertyfileUpload, listname, mustStaticReadHttpPath,index);
            totalObject.put("code",200);
            totalObject.put("message","成功");
            totalObject.put("data",listname);
            return totalObject.toJSONString();

        }catch (Exception e){
            totalObject.put("code",407);
            totalObject.put("message","失败");
            return totalObject.toJSONString();
        }
    }

    public static void readAllFile(String filepath,List listname,String mustStaticReadHttpPath,Integer index) throws IOException {
        File file= new File(filepath);
        Calendar cal = Calendar.getInstance();
        JSONObject obj = new JSONObject();
        if(!file.isDirectory()){
            JSONObject fileObj = new JSONObject();
            FileInputStream fis = null;
            FileChannel fileChannel = null;
            //获取文件名称
            String fileName = file.getName();
            //获取文件后缀名
            String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            //获取文件最后的修改时间
            long changeTime = file.lastModified();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cal.setTimeInMillis(changeTime);
            String fileTime = formatter.format(cal.getTime());
            //获取文件大小
            String fileSize;
            fis = new FileInputStream(file);
            fileChannel = fis.getChannel();
            DecimalFormat df = new DecimalFormat("0");
            if((double)((double) fis.available() / 1024) > 1000) {
                fileSize= df.format((double)((double) fileChannel.size() / 1024 / 1024)) + "M";
            } else {
                fileSize= df.format((double)((double) fis.available() / 1024)) + "K";
            }
            fileObj.put("fileSuffix",fileSuffix);
            fileObj.put("fileName",file.getName());
            fileObj.put("filePath",mustStaticReadHttpPath+file.getName());
            fileObj.put("fileTime",fileTime);
            fileObj.put("fileSize",fileSize);
            fileObj.put("id",index);
            index+=1;
            listname.add(fileObj);
        }else if(file.isDirectory()){
            String[] filelist=file.list();
            for(int i = 0;i<filelist.length;i++){
                File readfile = new File(filepath);
                if (!readfile.isDirectory()) {
                    JSONObject fileObj = new JSONObject();
                    FileInputStream fis = null;
                    FileChannel fileChannel = null;
                    //获取文件名称
                    String fileName = file.getName();
                    //获取文件后缀名
                    String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    //获取文件最后的修改时间
                    long changeTime = file.lastModified();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cal.setTimeInMillis(changeTime);
                    String fileTime = formatter.format(cal.getTime());
                    //获取文件大小
                    String fileSize;
                    fis = new FileInputStream(file);
                    fileChannel = fis.getChannel();
                    DecimalFormat df = new DecimalFormat("0");
                    if((double)((double) fis.available() / 1024) > 1000) {
                        fileSize= df.format((double)((double) fileChannel.size() / 1024 / 1024)) + "M";
                    } else {
                        fileSize= df.format((double)((double) fis.available() / 1024)) + "K";
                    }
                    fileObj.put("fileSuffix",fileSuffix);
                    fileObj.put("fileName",file.getName());
                    fileObj.put("filePath",mustStaticReadHttpPath+file.getName());
                    fileObj.put("fileTime",fileTime);
                    fileObj.put("fileSize",fileSize);
                    fileObj.put("id",index);
                    index+=1;
                    listname.add(fileObj);
                } else if (readfile.isDirectory()) {
                    readAllFile(filepath + "\\" + filelist[i],listname,mustStaticReadHttpPath,index);//递归
                }
            }
        }

    }
}
