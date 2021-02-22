package com.tglh.newscanningproject.scanning.mapper;

import com.tglh.newscanningproject.scanning.entity.User;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface UserMapper {
    //验证登录的正确性
    public Map loginCheck(User user);
}
