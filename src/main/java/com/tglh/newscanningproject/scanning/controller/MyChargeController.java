package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.MyChargeList;
import com.tglh.newscanningproject.scanning.entity.ScanMyChargeAction;
import com.tglh.newscanningproject.scanning.entity.ScanPermission;
import com.tglh.newscanningproject.scanning.service.MyChargeService;
import com.tglh.newscanningproject.utils.AesUtil;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("myCharge")
public class MyChargeController {
    @Autowired
    private MyChargeService myChargeService;
    //查看详情信息


    //我负责的点击详情进入页面
    @RequestMapping("/showRecord")
    @ResponseBody
    private String showRecord(String  id,String userNo) {
        /*
               -----展示详细内容
              获取 scan_records 记录中的ID之后去(数据库表scan_records中) 查询对应的记录，
               ---在scan_permission中有对应的记录
                 --如果在action_permission表中有depart_ID,证明它只能进行归档操作
                 --如果在action_permission表中有area_No,证明是有作业长的权限，会有审核 处理 驳回 等操作内容
        */
        try{
            //总的jsonObject
            JSONObject jsonObject = new JSONObject();



            //我负责的-包含查询权限表中是否有对应的记录，如果有看是否有 填入 所属部门的字段，
            //如果是填入区域的字段内容，那么就可能有 审核 处理的权限
            //首先去查询当前记录id对应的内容
            MyChargeList myChargeListDetail = myChargeService.selectCurrentIdDetail(id);
            //将数据库存储的itemids存到数据库的RecordItems字段内
            myChargeListDetail.setRecordItems(JSONArray.fromObject(myChargeListDetail.getItemIds()));

            //去scan_action表中去查询记录
            List stepInfoList = new ArrayList();
            List stepInfoEncodeList = new ArrayList();
            List<ScanMyChargeAction> scanMyChargeActionsList = myChargeService.selectScanActionRecords(id);
            for(int stepInfoIndex = 0;stepInfoIndex<scanMyChargeActionsList.size();stepInfoIndex++){
                HashMap<String,String> stepInfoHashMap = new HashMap<>();
                stepInfoHashMap.put("title",scanMyChargeActionsList.get(stepInfoIndex).getActionTypeName());
                stepInfoHashMap.put("desc",scanMyChargeActionsList.get(stepInfoIndex).getActionUserName()+" "+scanMyChargeActionsList.get(stepInfoIndex).getActionTime());
                stepInfoList.add(stepInfoHashMap);
                stepInfoEncodeList.add(scanMyChargeActionsList.get(stepInfoIndex).getActionType());
            }
            Integer actionType = scanMyChargeActionsList.get(scanMyChargeActionsList.size()-1).getActionType();

            //查询出当前用户有哪个权限
            ScanPermission scanPermission = myChargeService.selectDetailPermission(userNo);
            //安全区域讲解
            if("area_safe".equals(scanPermission.getRoleNo())){
                //判断动作类型 ["驳回","新增","审核","处理","归档"]
                //新增
                if(1 == actionType){
                    JSONObject obj = new JSONObject();
                    obj.put("isVerify",1);
                    obj.put("isHandle",0);
                    obj.put("isDone",0);
                    obj.put("recordInfo",myChargeListDetail);
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
                    obj.put("recordInfo",myChargeListDetail);
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
                    obj.put("recordInfo",myChargeListDetail);
                    obj.put("stepInfo",stepInfoList);
                    //已经到达为 3 状态的阶段
                    List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                    obj.put("recordInfo",myChargeListDetail);
                    obj.put("stepInfo",stepInfoList);
                    //已经到达为 3 状态的阶段
                    List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                        obj.put("recordInfo",myChargeListDetail);
                        obj.put("stepInfo",stepInfoList);
                        //已经到达为 3 状态的阶段
                        List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                        obj.put("recordInfo",myChargeListDetail);
                        obj.put("stepInfo",stepInfoList);
                        obj.put("actionInfo",new Object[0]);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }

                }
            }else if("depart_safe".equals(scanPermission.getRoleNo())){
                if(1 == actionType){
                    JSONObject obj = new JSONObject();
                    obj.put("isVerify",0);
                    obj.put("isHandle",0);
                    obj.put("isDone",0);
                    obj.put("recordInfo",myChargeListDetail);
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
                    obj.put("recordInfo",myChargeListDetail);
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
                    obj.put("recordInfo",myChargeListDetail);
                    obj.put("stepInfo",stepInfoList);
                    //已经到达为 3 状态的阶段
                    List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                    obj.put("recordInfo",myChargeListDetail);
                    obj.put("stepInfo",stepInfoList);
                    //已经到达为 3 状态的阶段
                    List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                        obj.put("recordInfo",myChargeListDetail);
                        obj.put("stepInfo",stepInfoList);
                        //已经到达为 3 状态的阶段
                        List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                        obj.put("recordInfo",myChargeListDetail);
                        obj.put("stepInfo",stepInfoList);
                        obj.put("actionInfo",new Object[0]);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }

                }
            }else if("all_safe".equals(scanPermission.getRoleNo())){
                if(1 == actionType){
                    JSONObject obj = new JSONObject();
                    obj.put("isVerify",0);
                    obj.put("isHandle",0);
                    obj.put("isDone",0);
                    obj.put("recordInfo",myChargeListDetail);
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
                    obj.put("recordInfo",myChargeListDetail);
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
                    obj.put("recordInfo",myChargeListDetail);
                    obj.put("stepInfo",stepInfoList);
                    //已经到达为 3 状态的阶段
                    List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                    obj.put("recordInfo",myChargeListDetail);
                    obj.put("stepInfo",stepInfoList);
                    //已经到达为 3 状态的阶段
                    List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                        obj.put("recordInfo",myChargeListDetail);
                        obj.put("stepInfo",stepInfoList);
                        //已经到达为 3 状态的阶段
                        List<ScanMyChargeAction> scanMyChargeActionList = myChargeService.selectScanActionHandle(id);
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
                        obj.put("recordInfo",myChargeListDetail);
                        obj.put("stepInfo",stepInfoList);
                        obj.put("actionInfo",new Object[0]);
                        jsonObject.put("code",200);
                        jsonObject.put("message","读取记录信息成功");
                        jsonObject.put("data",obj);
                        return jsonObject.toJSONString();
                    }
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
