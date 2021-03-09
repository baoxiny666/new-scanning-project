package com.tglh.newscanningproject.scanning.service;

import com.tglh.newscanningproject.scanning.entity.*;
import java.util.List;

public interface MyChargeService {
      Integer selectListPageSize();
      ScanPermission selectCurrentPermission(MyChargeList myChargeList);

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
