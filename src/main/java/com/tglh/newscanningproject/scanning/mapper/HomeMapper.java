package com.tglh.newscanningproject.scanning.mapper;

import com.tglh.newscanningproject.scanning.entity.Banner;
import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.Notice;
import com.tglh.newscanningproject.scanning.entity.SafeModules;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMapper {
    //获取主页菜单选项
    public List<SafeModules> modules();


    //获取主页菜单选项
    public List<Banner> banner();

    //获取主页菜单选项
    public Notice notice();

}
