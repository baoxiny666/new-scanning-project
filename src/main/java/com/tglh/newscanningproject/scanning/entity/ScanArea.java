package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

import java.util.List;

@Data
public class ScanArea {
    private String area_name;
    private String area_no;
    private Integer depart_id;
    private String depart_name;
    private String manage_phone;
    private String manage_user_name;
    private String manage_user_no;


    private String label;
    private Integer value;
    private Integer pid;
    private List<ScanArea> children;
}
