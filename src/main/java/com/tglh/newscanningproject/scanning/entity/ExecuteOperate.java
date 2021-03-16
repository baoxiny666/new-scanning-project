package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

@Data
public class ExecuteOperate {
    private String recordId;//当前选中的记录ID
    private String areaNo;//当前选中的区域编码
    private String departId;//当前选中的部门ID
    private String  recordStatus;//当前选中的记录状态
    private Boolean actionType;//当前操作类型
    private User userInfo;//登录人信息
    private String actionDesc;//处理结果描述
    private String actionImages;//经过处理后上传的照片
    private String createTime;//创建时间
    private String updateTime;//更新时间
    private String actionTime;//动作时间
}
