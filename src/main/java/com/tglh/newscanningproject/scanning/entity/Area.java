package com.tglh.newscanningproject.scanning.entity;

import lombok.Data;

import java.util.List;
@Data
public class Area {
    private String label;
    private String value;
    private List children;
}
