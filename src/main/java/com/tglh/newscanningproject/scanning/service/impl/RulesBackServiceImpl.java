package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.mapper.RulesBackMapper;
import com.tglh.newscanningproject.scanning.service.RulesBackService;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RulesBackServiceImpl implements RulesBackService {
    @Autowired
    private RulesBackMapper rulesBackMapper;
    @Override
    public List<DepartMent> selectTree() {
        List<DepartMent> list = rulesBackMapper.selectTree();
        return list;
    }
}
