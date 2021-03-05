package com.tglh.newscanningproject.scanning.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.*;
import com.tglh.newscanningproject.scanning.mapper.SafeScanMapper;
import com.tglh.newscanningproject.scanning.service.SafeScanService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public List statusFilter() {
        String statusFilterString = safeScanMapper.selectStatusFilter();
        JSONArray jsonArray = JSONArray.fromObject(statusFilterString);
        JSONArray ja = new JSONArray();
        for(int z =0;z<jsonArray.size();z++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label",jsonArray.get(z));
            jsonObject.put("value",z);
            ja.add(jsonObject);
        }
        return ja;
    }

    @Override
    public List<MyUploadList> myList(MyUploadList myUploadList) {
        List<MyUploadList> statusFilterString = safeScanMapper.myList(myUploadList);
        return statusFilterString;
    }

    @Override
    public Long selectTotal(MyUploadList myUploadList) {
        HashMap selectTotalMap = safeScanMapper.selectTotal(myUploadList);
        long total = Long.valueOf(selectTotalMap.get("count").toString());
        return total;
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
