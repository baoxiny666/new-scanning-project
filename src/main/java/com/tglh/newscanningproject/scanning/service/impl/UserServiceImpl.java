package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.User;
import com.tglh.newscanningproject.scanning.mapper.UserMapper;
import com.tglh.newscanningproject.scanning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
