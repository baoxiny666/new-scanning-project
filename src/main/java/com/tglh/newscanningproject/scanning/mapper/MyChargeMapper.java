package com.tglh.newscanningproject.scanning.mapper;

import com.tglh.newscanningproject.scanning.entity.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface MyChargeMapper {
    //获取每页显示的条数
    public String selectListPageSize();

    ScanPermission selectCurrentPermission(MyChargeList myChargeList);

    //查询当前id对应的详细信息
    MyChargeList selectCurrentIdDetail(String id);
    //查询当前id 对应的操作信息
    List<ScanMyChargeAction> selectScanActionRecords(String id);
    //查询详细页面的用户有哪些权限
    ScanPermission  selectDetailPermission(String userNo);

    List<ScanMyChargeAction> selectScanActionHandle(String id);


    //查看区域部分
    List<MyChargeList> myChargeAreaList(MyChargeList myChargeList);
    HashMap selectChargeAreaTotal(MyChargeList myChargeList);


    //查看部门部分
    List<MyChargeList> myChargeDepartList(MyChargeList myChargeList);
    HashMap selectChargeDepartTotal(MyChargeList myChargeList);


    //查看区域部分
    List<MyChargeList> myChargeAllSafeList(MyChargeList myChargeList);
    HashMap selectChargeAllSafeTotal(MyChargeList myChargeList);


}
