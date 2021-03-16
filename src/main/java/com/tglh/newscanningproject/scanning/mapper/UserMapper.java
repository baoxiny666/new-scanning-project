package com.tglh.newscanningproject.scanning.mapper;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    //验证登录的正确性
    User loginCheck(User user);
    //验证重置密码过程中是否有此用户名
    User loginCheckUpdatePwd(User user);
    //更改代码
    Integer updateUserPwd(User user);

    //获取部门菜单
    List<DepartMent> selectTree();

    //注册人员
    void regist(User user);

    Integer insertPost(User user);
}
