package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.MyChargeList;
import com.tglh.newscanningproject.scanning.entity.ScanPermission;
import com.tglh.newscanningproject.scanning.service.MyChargeService;
import com.tglh.newscanningproject.utils.AesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("myCharge")
public class MyChargeController {
    @Autowired
    private MyChargeService myChargeService;

    //我负责的列表配置
    @RequestMapping("/list")
    @ResponseBody
    private String list(String  encrypted) {
        //我负责的-包含查询权限表中是否有对应的记录，如果有看是否有 填入 所属部门的字段，
        // 如果是填入区域的字段内容，那么就可能有 审核 处理的权限
        String encryptedCode = AesUtil.decrypt(encrypted,AesUtil.KEY);
        JSONObject encryptedCodeObj=JSONObject.parseObject(encryptedCode);
        System.out.println(encryptedCodeObj);
        MyChargeList myChargeList = (MyChargeList) JSONObject.toJavaObject(encryptedCodeObj, MyChargeList.class);
        if(myChargeList.getAreaSelect().size() > 0){
            myChargeList.setBuMenId(Integer.valueOf(myChargeList.getAreaSelect().get(0).toString()));
            myChargeList.setQuYuId(Integer.valueOf(myChargeList.getAreaSelect().get(1).toString()));
        }


        //获得每页显示的条数
        Integer rows = myChargeService.selectListPageSize();
        Integer page = myChargeList.getPageNum();
        //Integer rows = myUploadList.getPageSize();
        myChargeList.setStartPageNum((page-1)*rows);
        myChargeList.setEndPageNum((page*rows));

        //去查询该有的权限
        ScanPermission scanPermission = myChargeService.selectCurrentPermission(myChargeList);
        if(scanPermission == null){
            JSONObject obj = new JSONObject();
            obj.put("code",404);
            obj.put("message","没有记录");
            return obj.toJSONString();
        }else{
            //安全区域讲解
            if("area_safe".equals(scanPermission.getRoleNo())){
                myChargeList.setAreaNo(scanPermission.getAreaNo());
                Long total = myChargeService.selectChargeAreaTotal(myChargeList);
                List list = myChargeService.myChargeAreaList(myChargeList);
                JSONObject obj = new JSONObject();
                HashMap m = new HashMap();
                m.put("totalCount",total);
                m.put("pageSize",rows);
                m.put("list",list);
                obj.put("code",200);
                obj.put("message","成功");
                obj.put("data",m);
                return obj.toJSONString();
            }else if("depart_safe".equals(scanPermission.getRoleNo())){
                myChargeList.setDepartId(scanPermission.getDepartId());
                Long total = myChargeService.selectChargeDepartTotal(myChargeList);
                List list = myChargeService.myChargeDepartList(myChargeList);
                JSONObject obj = new JSONObject();
                HashMap m = new HashMap();
                m.put("totalCount",total);
                m.put("pageSize",rows);
                m.put("list",list);
                obj.put("code",200);
                obj.put("message","成功");
                obj.put("data",m);
                return obj.toJSONString();
            }else if("all_safe".equals(scanPermission.getRoleNo())){
                Long total = myChargeService.selectChargeAllSafeTotal(myChargeList);
                List list = myChargeService.myChargeAllSafeList(myChargeList);
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
        }
        return null;
    }

}
