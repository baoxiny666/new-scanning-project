package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

import java.util.List;

@Data
public class ScanPermission {
    private Integer id;
    private String userNo;
    private String roleNo;
    private Integer departId;
    private String areaNo;
    private String createTime;
    private String updateTime;
}
