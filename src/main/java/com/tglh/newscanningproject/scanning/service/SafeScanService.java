package com.tglh.newscanningproject.scanning.service;

import com.tglh.newscanningproject.scanning.entity.*;

import java.util.List;
import java.util.Map;

public interface SafeScanService {
     ScanArea areaInfo(String code);

     void addRecord(ScanRecordAdvise scanRecordAdvise);




     List<ScanAreaItems> areaInfoItems(String code);

     List<ScanArea> areaFilter();

     Integer selectListPageSize();

     List statusFilter();



     List<MyChargeList> myChargeList(MyChargeList myChargeList);

     Long selectChargeTotal(MyChargeList myChargeList);

     List<MyUploadList> myList(MyUploadList myUploadList);

     Long selectTotal(MyUploadList myUploadList);



     //查询传入的ID 对应的详细信息
     MyUploadList selectCurrentIdDetail(String id);
     //查询传入id对应的scan_action表中对应的记录数
     List<ScanMyListAction> selectScanActionRecords(String id);
     //查询已经到了3阶段的记录
     List<ScanMyListAction> selectScanActionHandle(String id);

     //查询详细页面中 根据当前用户号 看一下有什么样的权限
     ScanPermission selectDetailPermission(String userNo);

}
