package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.User;
import com.tglh.newscanningproject.scanning.service.UserService;
import com.tglh.newscanningproject.utils.AesUtil;
import com.tglh.newscanningproject.utils.JwtUtil;
import com.tglh.newscanningproject.utils.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/login")
    @ResponseBody
    public String login(String encrypted){
        JSONObject jsonObject=new JSONObject();

        //解密 --- 前端代码传过来的 用户编码 和 密码
        String encryptedCode = AesUtil.decrypt(encrypted,AesUtil.KEY);
        JSONObject encryptedCodeObj=JSONObject.parseObject(encryptedCode);
        String userNo  = encryptedCodeObj.get("userNo").toString();
        String userPwd  = encryptedCodeObj.get("userPwd").toString();

        //解码后 --- 放到User对象中
        Md5 md5 = new Md5();
        User user = new User();
        user.setUserNo(userNo);
        String oneSecret = md5.encrypt32(userNo + "" + userPwd).substring(0, 12);
        String twoSecret = md5.encrypt32(oneSecret);
        user.setUserPwd(twoSecret);

        //去往数据库查询是否有此用户
        User checkUserBack = userService.loginCheck(user);

        //设置存放 redis 的前置标识
        String lhtg_redis_flag = "bxyxxc:qigcyj:";

        //检查用户名 密码
        if(checkUserBack != null){
            //获取查询回来的用户编码和密码
            String userPwdBack = checkUserBack.getUserPwd();
            String userNoBack = checkUserBack.getUserNo();
            //拼接jwt秘钥
            String totalSecret =  lhtg_redis_flag+""+userNo;
            //如果返回的密码和前端传过来的密码一致
            if(twoSecret.equals(userPwdBack)) {
                String token= JwtUtil.sign(totalSecret);
                redisTemplate.opsForValue().set(totalSecret,token);
                //设置token有效的时间
                redisTemplate.expire(totalSecret, 300000, TimeUnit.SECONDS);
                JSONObject obj = new JSONObject();
                JSONObject obj_inner = new JSONObject();
                obj_inner.put("token",token);
                obj_inner.put("user", AesUtil.encrypt(JSON.toJSONString(checkUserBack),"87a1ec63db4c1d34"));
                obj.put("code",200);
                obj.put("data",obj_inner);
                obj.put("isSuccess",true);
                return JSON.toJSONString(obj);
            }else {
                JSONObject obj = new JSONObject();
                obj.put("isSuccess", false);
                obj.put("code",407);
                return obj.toJSONString();
            }
        }else{
            JSONObject obj = new JSONObject();
            obj.put("isSuccess", false);
            obj.put("code",407);
            return obj.toJSONString();
        }

    }
}
