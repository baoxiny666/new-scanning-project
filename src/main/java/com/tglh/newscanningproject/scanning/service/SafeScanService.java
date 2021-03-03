package com.tglh.newscanningproject.scanning.service;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.ScanArea;
import com.tglh.newscanningproject.scanning.entity.ScanAreaItems;
import com.tglh.newscanningproject.scanning.entity.ScanRecordAdvise;

import java.util.List;
import java.util.Map;

public interface SafeScanService {
     ScanArea areaInfo(String code);
     void addRecord(ScanRecordAdvise scanRecordAdvise);
     void addRecordAction(ScanRecordAdvise scanRecordAdvise);
     Map getMaxId();
     List<ScanAreaItems> areaInfoItems(String code);

}
