package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

@Data
public class ScanArea {
    private String area_name;
    private String area_no;
    private Integer depart_id;
    private String depart_name;
    private String manage_phone;
    private String manage_user_name;
    private String manage_user_no;
}
