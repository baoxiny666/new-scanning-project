package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.*;
import com.tglh.newscanningproject.scanning.mapper.HomeMapper;
import com.tglh.newscanningproject.scanning.mapper.MyChargeMapper;
import com.tglh.newscanningproject.scanning.service.HomeService;
import com.tglh.newscanningproject.scanning.service.MyChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
