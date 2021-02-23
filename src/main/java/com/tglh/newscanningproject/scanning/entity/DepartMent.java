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

    private List<DepartMent> __child;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public Integer getDepartPid() {
        return departPid;
    }

    public void setDepartPid(Integer departPid) {
        this.departPid = departPid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<DepartMent> get__child() {
        return __child;
    }

    public void set__child(List<DepartMent> __child) {
        this.__child = __child;
    }
}
