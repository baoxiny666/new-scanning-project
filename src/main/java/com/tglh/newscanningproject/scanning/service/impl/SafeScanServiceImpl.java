package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.*;
import com.tglh.newscanningproject.scanning.mapper.SafeScanMapper;
import com.tglh.newscanningproject.scanning.service.SafeScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Map maxIdMap = safeScanMapper.getMaxId();
        return maxIdMap;
    }

    @Override
    public List<ScanAreaItems> areaInfoItems(String code) {
        List<ScanAreaItems> list =  safeScanMapper.areaInfoItems(code);
        return list;
    }

    @Override
    public List<ScanArea> areaFilter() {
        List<ScanArea> areaAll =   safeScanMapper.selectAreaFilter();
        List<ScanArea> list = new ArrayList();
        list = createTree(0,areaAll);
        return list;

    }


    private List<ScanArea> createTree(int pid, List<ScanArea> areaAll) {
        List<ScanArea> area = new ArrayList<>();
        for (ScanArea menu : areaAll) {
            if (pid == menu.getPid()) {
                area.add(menu);
                menu.setChildren(createTree(menu.getValue(), areaAll));
            }
        }
        return area;
    }
}
