package com.tglh.newscanningproject.scanning.entity;
import lombok.Data;

//我负责的动作
@Data
public class ScanMyListAction {
    private Integer id;
    private Integer recordId;
    private Integer actionType;
    private String actionTypeName;
    private String actionItemIds;
    private String actionDesc;
    private String actionImages;
    private String actionUserNo;
    private String actionUserName;
    private String actionUserPhone;
    private String actionLocation;
    private String actionLocationName;
    private String appClientId;
    private String actionTime;
    private String createTime;
    private String updateTime;
}
