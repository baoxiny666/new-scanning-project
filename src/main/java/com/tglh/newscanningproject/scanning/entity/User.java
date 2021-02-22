package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String userNo;
    private String userName;
    private String userPwd;
    private Integer departId;
    private String userPhone;
    private Integer postId;
    private String userPhoto;
    private Date createTime;
    private Date updateTime;
    private Date loginTime;
    private Integer userStatus;
}
