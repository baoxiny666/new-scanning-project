package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

import java.util.List;

@Data
public class MyUploadList {
    private Integer id;
    private String areaNo;
    private Integer areaId;
    private String areaName;
    private Integer departId;
    private String departName;
    private String manageUser;
    private String manageUserName;
    private String managePhone;
    private String desc;
    private String itemIds;
    private String images;
    private Integer recordStatus;
    private String recordUserNo;
    private String recordUserName;
    private String recordUserPhone;
    private String recordLocation;
    private String recordLocationName;
    private String appClientId;
    private String createTime;
    private String updateTime;
    private String recordStatusName;

    //起始页 最终页
    private Integer startPageNum;
    private Integer endPageNum;

    //从前台传到后台的参数
    private Integer pageNum;
    private Integer pageSize;
    private String searchKey;
    private List areaSelect;
    private Integer statusSelect;
    private String dateSelect;
    private User userInfo;
    private ClientInfo clientInfo;

    private String buMenId;
    private String quYuId;




}
