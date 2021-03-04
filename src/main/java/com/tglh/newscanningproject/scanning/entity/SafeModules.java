package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

@Data
public class SafeModules {
    private Integer id;
    private String moduleNo;
    private String moduleName;
    private String moduleDesc;
    private String moduleIcon;
    private String moduleBg;
    private String moduleRouter;
    private Integer isPermit;
    private  Integer moduleStatus;
    private Integer showOrder;
    private String createTime;
    private String updateTime;
}
