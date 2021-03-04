package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.Banner;
import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.Notice;
import com.tglh.newscanningproject.scanning.entity.SafeModules;
import com.tglh.newscanningproject.scanning.mapper.HomeMapper;
import com.tglh.newscanningproject.scanning.mapper.RulesBackMapper;
import com.tglh.newscanningproject.scanning.service.HomeService;
import com.tglh.newscanningproject.scanning.service.RulesBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private HomeMapper homeMapper;


    @Override
    public List<SafeModules> modules() {
        return homeMapper.modules();
    }

    @Override
    public List<Banner> banner() {
        return  homeMapper.banner();
    }

    @Override
    public Notice notice() {
        return  homeMapper.notice();
    }
}
