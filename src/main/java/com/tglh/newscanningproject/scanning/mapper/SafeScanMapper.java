package com.tglh.newscanningproject.scanning.mapper;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafeScanMapper {
    //获取部门菜单
    public List<DepartMent> selectTree();
}
