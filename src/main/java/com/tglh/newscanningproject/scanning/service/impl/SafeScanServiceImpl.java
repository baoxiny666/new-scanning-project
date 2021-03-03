package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.ScanArea;
import com.tglh.newscanningproject.scanning.entity.ScanAreaItems;
import com.tglh.newscanningproject.scanning.entity.ScanRecordAdvise;
import com.tglh.newscanningproject.scanning.mapper.SafeScanMapper;
import com.tglh.newscanningproject.scanning.service.SafeScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SafeScanServiceImpl implements SafeScanService {

    @Autowired
    private SafeScanMapper safeScanMapper;

    @Override
    public ScanArea areaInfo(String code) {
        ScanArea scanArea = safeScanMapper.areaInfo(code);
        return scanArea;
    }

    @Override
    public void addRecord(ScanRecordAdvise scanRecordAdvise) {
        safeScanMapper.addRecord(scanRecordAdvise);

    }

    @Override
    public void addRecordAction(ScanRecordAdvise scanRecordAdvise) {
        safeScanMapper.addRecordAction(scanRecordAdvise);
    }

    @Override
    public Map getMaxId() {
        Map maxId = safeScanMapper.getMaxId();
        return maxId;
    }

    @Override
    public List<ScanAreaItems> areaInfoItems(String code) {
        List<ScanAreaItems> list =  safeScanMapper.areaInfoItems(code);
        return list;
    }
}
