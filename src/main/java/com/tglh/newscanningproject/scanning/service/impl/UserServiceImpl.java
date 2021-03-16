package com.tglh.newscanningproject.scanning.service.impl;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.User;
import com.tglh.newscanningproject.scanning.mapper.UserMapper;
import com.tglh.newscanningproject.scanning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    public User loginCheckUpdatePwd(User user) {
        User loginCheckUpdatePwd = userMapper.loginCheckUpdatePwd(user);
        return loginCheckUpdatePwd;
    }

    @Override
    public User updateUserPwd(User user) {
        Integer userUpdate = userMapper.updateUserPwd(user);
        return user;
    }

    @Override
    public List<DepartMent> selectTree() {
        List<DepartMent> list = userMapper.selectTree();
        return list;
    }

    @Override
    public void regist(User user) {
        //日期格式化打印
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str1 = sdf.format(d);
        user.setUpdateTime(str1);
        user.setCreateTime(str1);
        userMapper.regist(user);
    }

    @Override
    public Integer postInformation(User user) {
        //日期格式化打印
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str1 = sdf.format(d);
        user.setUpdateTime(str1);
        user.setCreateTime(str1);
        Integer postId = userMapper.insertPost(user);
        return  user.getId();
    }
}
