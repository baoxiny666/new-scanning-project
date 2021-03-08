package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.User;
import com.tglh.newscanningproject.scanning.mapper.UserMapper;
import com.tglh.newscanningproject.scanning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User loginCheck(User user) {
        User loginCheckBack = userMapper.loginCheck(user);
        return loginCheckBack;
    }

    @Override
    public List<DepartMent> selectTree() {
        List<DepartMent> list = userMapper.selectTree();
        return list;
    }

    @Override
    public void regist(User user) {
        userMapper.regist(user);
    }
}
