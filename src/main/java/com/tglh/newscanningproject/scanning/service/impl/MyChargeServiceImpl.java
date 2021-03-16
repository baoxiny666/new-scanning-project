package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.*;
import com.tglh.newscanningproject.scanning.mapper.HomeMapper;
import com.tglh.newscanningproject.scanning.mapper.MyChargeMapper;
import com.tglh.newscanningproject.scanning.service.HomeService;
import com.tglh.newscanningproject.scanning.service.MyChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MyChargeServiceImpl implements MyChargeService {
    @Autowired
    private MyChargeMapper myChargeMapper;


    @Override
    public List<MyChargeList> myChargeAreaList(MyChargeList myChargeList) {
        List<MyChargeList> myChargeAreaList   = myChargeMapper.myChargeAreaList(myChargeList);
        return myChargeAreaList;
    }


    @Override
    public Long selectChargeAreaTotal(MyChargeList myChargeList) {
        HashMap selectTotalMap = myChargeMapper.selectChargeAreaTotal(myChargeList);
        long total = Long.valueOf(selectTotalMap.get("count").toString());
        return total;
    }

    @Override
    public List<MyChargeList> myChargeDepartList(MyChargeList myChargeList) {
        List<MyChargeList> myChargeDepartList   = myChargeMapper.myChargeDepartList(myChargeList);
        return myChargeDepartList;
    }

    @Override
    public Long selectChargeDepartTotal(MyChargeList myChargeList) {
        HashMap selectTotalMap = myChargeMapper.selectChargeDepartTotal(myChargeList);
        long total = Long.valueOf(selectTotalMap.get("count").toString());
        return total;
    }

    @Override
    public List<MyChargeList> myChargeAllSafeList(MyChargeList myChargeList) {
        List<MyChargeList> myChargeAllSafeList   = myChargeMapper.myChargeAllSafeList(myChargeList);
        return myChargeAllSafeList;
    }

    @Override
    public Long selectChargeAllSafeTotal(MyChargeList myChargeList) {
        HashMap selectTotalMap = myChargeMapper.selectChargeAllSafeTotal(myChargeList);
        long total = Long.valueOf(selectTotalMap.get("count").toString());
        return total;
    }

    @Override
    public ScanPermission selectCurrentPermission(MyChargeList myChargeList) {
        ScanPermission scanPermission = myChargeMapper.selectCurrentPermission(myChargeList);
        return scanPermission;
    }

    @Override
    public Integer insertVerifyAction(ExecuteOperate executeOperate) {
        //日期格式化打印
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        executeOperate.setActionTime(dateNowStr);
        executeOperate.setUpdateTime(dateNowStr);
        executeOperate.setCreateTime(dateNowStr);
        //        myChargeMapper
        if(true == executeOperate.getActionType()){
            executeOperate.setRecordStatus("2");
        }else{
            executeOperate.setRecordStatus("0");
        }
        Integer insertVerifyActionInteger =  myChargeMapper.insertVerifyAction(executeOperate);
        //更新scan_records主表的状态
        myChargeMapper.updateScanRecords(executeOperate);
        return insertVerifyActionInteger;
    }

    @Override
    public Integer insertDoneAction(ExecuteOperate executeOperate) {
        //日期格式化打印
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        executeOperate.setActionTime(dateNowStr);
        executeOperate.setUpdateTime(dateNowStr);
        executeOperate.setCreateTime(dateNowStr);
        if(true == executeOperate.getActionType()){
            executeOperate.setRecordStatus("4");
        }else{
            executeOperate.setRecordStatus("0");
        }
        Integer insertDoneActionInteger =  myChargeMapper.insertDoneAction(executeOperate);
        //更新scan_records主表的状态
        myChargeMapper.updateScanRecords(executeOperate);
        return insertDoneActionInteger;
    }

    @Override
    public Integer insertHandleAction(ExecuteOperate executeOperate) {
        //日期格式化打印
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        executeOperate.setActionTime(dateNowStr);
        executeOperate.setUpdateTime(dateNowStr);
        executeOperate.setCreateTime(dateNowStr);

        executeOperate.setRecordStatus("3");


        Integer insertHandleActionInteger =  myChargeMapper.insertHandleAction(executeOperate);
        //更新scan_records主表的状态
        myChargeMapper.updateScanRecords(executeOperate);
        return insertHandleActionInteger;
    }

    @Override
    public MyChargeList selectCurrentIdDetail(String id) {
       MyChargeList myChargeListDetail =  myChargeMapper.selectCurrentIdDetail(id);
       return myChargeListDetail;
    }

    @Override
    public List<ScanMyChargeAction> selectScanActionRecords(String id) {
        List<ScanMyChargeAction> list  = myChargeMapper.selectScanActionRecords(id);
        return list;
    }

    @Override
    public List<ScanMyChargeAction> selectScanActionHandle(String id) {
        List<ScanMyChargeAction> scanMyChargeActionList = myChargeMapper.selectScanActionHandle(id);
        return scanMyChargeActionList;
    }

    @Override
    public ScanPermission selectDetailPermission(String userNo) {
        ScanPermission scanPermission =  myChargeMapper.selectDetailPermission(userNo);
        return scanPermission;
    }

    @Override
    public Integer selectListPageSize() {
        String listPageSize = myChargeMapper.selectListPageSize();
        return Integer.valueOf(listPageSize);
    }
}
