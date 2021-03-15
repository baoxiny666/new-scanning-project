package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

@Data
public class ExecuteOperate {
    private String recordId;//当前选中的记录ID
    private String areaNo;//当前选中的区域编码
    private String departId;//当前选中的部门ID
    private String  recordStatus;//当前选中的记录状态
    private Boolean actionType;//当前操作类型
    private User userInfo;
    private String createTime;
    private String updateTime;
    private String actionTime;
}
