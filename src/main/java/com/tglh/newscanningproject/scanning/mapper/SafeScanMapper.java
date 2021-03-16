package com.tglh.newscanningproject.scanning.mapper;

import com.tglh.newscanningproject.scanning.entity.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface SafeScanMapper {
    //区域选择项
    public List<ScanAreaItems> areaInfoItems(String code);
    //区域详细
    public ScanArea areaInfo(String code);
    //增加记录
    public void addRecord(ScanRecordAdvise scanRecordAdvise);
    //增加记录动作信息
    public void addRecordAction(ScanRecordAdvise scanRecordAdvise);
    //获取最大ID
    public Map getMaxId();

    //获取区域信息
    public List<ScanArea> selectAreaFilter();
    //获取状态选项
    public String selectStatusFilter();
    //获取每页显示的条数
    public String selectListPageSize();
    //查询列表总条数
    public HashMap selectTotal(MyUploadList myUploadList);
    //查新列表内容
    public List  myList(MyUploadList myUploadList);

    //查询当前id对应的详细信息
    MyUploadList selectCurrentIdDetail(String id);
    //查询当前id 对应的操作信息
    List<ScanMyListAction> selectScanActionRecords(String id);
    //查询详细页面的用户有哪些权限
    ScanPermission  selectDetailPermission(String userNo);

    List<ScanMyListAction> selectScanActionHandle(String id);



}
