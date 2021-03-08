package com.tglh.newscanningproject.scanning.service;

import com.tglh.newscanningproject.scanning.entity.*;

import java.util.List;
import java.util.Map;

public interface SafeScanService {
     ScanArea areaInfo(String code);

     void addRecord(ScanRecordAdvise scanRecordAdvise);

     void addRecordAction(ScanRecordAdvise scanRecordAdvise);

     Map getMaxId();

     List<ScanAreaItems> areaInfoItems(String code);

     List<ScanArea> areaFilter();

     Integer selectListPageSize();

     List statusFilter();



     List<MyChargeList> myChargeList(MyChargeList myChargeList);

     Long selectChargeTotal(MyChargeList myChargeList);

     List<MyUploadList> myList(MyUploadList myUploadList);

     Long selectTotal(MyUploadList myUploadList);

}
