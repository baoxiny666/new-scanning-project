package com.tglh.newscanningproject.scanning.mapper;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.ScanArea;
import com.tglh.newscanningproject.scanning.entity.ScanAreaItems;
import com.tglh.newscanningproject.scanning.entity.ScanRecordAdvise;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SafeScanMapper {

    public List<ScanAreaItems> areaInfoItems(String code);

    public ScanArea areaInfo(String code);

    public void addRecord(ScanRecordAdvise scanRecordAdvise);
    public void addRecordAction(ScanRecordAdvise scanRecordAdvise);

    public Map getMaxId();

}
