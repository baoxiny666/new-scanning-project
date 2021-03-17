package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

import java.util.List;
@Data
public class ScanRecordAdvise {
    private Integer id;
    private ScanArea areaData;
    private String selectItems;
    private String descData;
    private String imgArray;
    private String recordUserName;
    private String recordUserPhone;
    private Location location;
    private User userInfo;
    private String appClientInfo;
    private String uuid;
    private String maxId;
    private String createTime;
    private String updateTime;
    private String actionTime;

}
