package com.tglh.newscanningproject.scanning.mapper;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    //验证登录的正确性
    public User loginCheck(User user);

    //获取部门菜单
    public List<DepartMent> selectTree();


    //注册人员
    void regist(User user);

    Integer insertPost(User user);
}
