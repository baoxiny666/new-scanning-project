package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class DepartMent {
    private Integer id;
    private String departName;
    private Integer departPid;
    private Date createTime;
    private Date updateTime;

    private List<DepartMent> children;
}
