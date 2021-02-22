package com.tglh.newscanningproject;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.tglh.newscanningproject.**")
@MapperScan("com.tglh.newscanningproject.scanning.mapper")
public class NewScanningProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewScanningProjectApplication.class, args);
    }

}
