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

    //获取最大ID
    public List<ScanArea> selectAreaFilter();
    public List<ScanArea>  selectCategoryArea();


}
