package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

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
    private String createTime;
    private String updateTime;
    private String loginTime;
    private Integer userStatus;

    //部门区域id 数组选择
    private List departSelect;
    private Integer secondDepart;
    private String userPost;
}
