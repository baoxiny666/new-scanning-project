package com.tglh.newscanningproject.scanning.service;

import com.tglh.newscanningproject.scanning.entity.*;
import java.util.List;

public interface MyChargeService {
      Integer selectListPageSize();
      ScanPermission selectCurrentPermission(MyChargeList myChargeList);

      //查询传入的ID 对应的详细信息
      MyChargeList selectCurrentIdDetail(String id);
      //查询传入id对应的scan_action表中对应的记录数
      List<ScanMyChargeAction> selectScanActionRecords(String id);
      //查询已经到了3阶段的记录
      List<ScanMyChargeAction> selectScanActionHandle(String id);

      //查询详细页面中 根据当前用户号 看一下有什么样的权限
      ScanPermission selectDetailPermission(String userNo);

      //区域级别的
      List<MyChargeList> myChargeAreaList(MyChargeList myChargeList);
      Long selectChargeAreaTotal(MyChargeList myChargeList);


      //部门级别的
      List<MyChargeList> myChargeDepartList(MyChargeList myChargeList);
      Long selectChargeDepartTotal(MyChargeList myChargeList);

      //安全管理员
      List<MyChargeList> myChargeAllSafeList(MyChargeList myChargeList);
      Long selectChargeAllSafeTotal(MyChargeList myChargeList);


}
