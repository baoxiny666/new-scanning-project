package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.MyUploadList;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("/regist")
    @ResponseBody
    public String regist(String encrypted){
        JSONObject jsonObject=new JSONObject();

        //解密 --- 前端代码传过来的 用户编码 和 密码
        String encryptedCode = AesUtil.decrypt(encrypted,AesUtil.KEY);
        JSONObject encryptedCodeObj=JSONObject.parseObject(encryptedCode);
        User user = (User) JSONObject.toJavaObject(encryptedCodeObj, User.class);  //通过JSONObject.toBean()方法进行对象间的转换
        List departSelect = user.getDepartSelect();
        user.setDepartId(Integer.valueOf(departSelect.get(0).toString()));
        user.setPostId(Integer.valueOf(departSelect.get(1).toString()));
        //解码后 --- 放到User对象中
        Md5 md5 = new Md5();
        String oneSecret = md5.encrypt32(user.getUserNo() + "" + user.getUserPwd()).substring(0, 12);
        String twoSecret = md5.encrypt32(oneSecret);
        user.setUserPwd(twoSecret);
        try{
            userService.regist(user);
            JSONObject obj = new JSONObject();
            obj.put("isSuccess", true);
            obj.put("code",200);
            return obj.toJSONString();
        }catch(Exception e){
            JSONObject obj = new JSONObject();
            obj.put("isSuccess", false);
            obj.put("code",407);
            return obj.toJSONString();
        }

    }

    @RequestMapping("/depart")
    @ResponseBody
    public String depart(String encrypted){
        try{
            // 查询出所有的菜单数据集合
            List<DepartMent> departMenus = userService.selectTree();
            // 生成菜单树
            List<DepartMent> tree = createTree(0, departMenus);
            JSONObject obj = new JSONObject();
            obj.put("isSuccess", true);
            obj.put("code",200);
            obj.put("data",tree);
            return obj.toJSONString();
        }catch (Exception e){
            JSONObject obj = new JSONObject();
            obj.put("isSuccess", false);
            obj.put("code",407);
            return obj.toJSONString();
        }
    }


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




    /**
     * 递归生成菜单树,第一次递归
     */
    private List<DepartMent> createTree(int pid, List<DepartMent> depart) {

        //当我们遍历第一级节点的时候
        //第一级节点---紧接着进行下一级---下一级再进行
        if(pid == 0){
            List<DepartMent> treeMenu = new ArrayList<>();
            for (DepartMent departMenu : depart) {
                if (pid == departMenu.getDepartPid()) {
                    if(1 == departMenu.getId()){
                        treeMenu.add(departMenu);
                    }else{
                        treeMenu.add(departMenu);
                        departMenu.set__child(createTree(departMenu.getId(), depart));
                    }

                }
            }
            return treeMenu;
        }else{
            List<DepartMent> treeMenu = new ArrayList<>();
            Optional<DepartMent> departMentOptional = depart.stream().filter(item -> item.getId().equals(pid)).findFirst();
            if(departMentOptional.isPresent()){
                DepartMent xinfu = new DepartMent();
                xinfu.setId(departMentOptional.get().getId());
                xinfu.setDepartName(departMentOptional.get().getDepartName());
                xinfu.setDepartPid(departMentOptional.get().getDepartPid());
                treeMenu.add(xinfu);
                for (DepartMent departMenu : depart) {
                    if (pid == departMenu.getDepartPid()) {
                        treeMenu.add(departMenu);
                        departMenu.set__child(null);
                    }
                }
            }

            return treeMenu;
        }

    }

}
