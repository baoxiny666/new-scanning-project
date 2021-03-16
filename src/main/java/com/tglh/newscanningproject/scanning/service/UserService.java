package com.tglh.newscanningproject.scanning.service;

import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
   User loginCheck(User user);
   User loginCheckUpdatePwd(User user);
   User updateUserPwd(User user);
   List<DepartMent> selectTree();

   void regist(User user);

   Integer postInformation(User user);
}
