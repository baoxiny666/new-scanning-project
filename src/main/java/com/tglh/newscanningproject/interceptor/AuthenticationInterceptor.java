package com.tglh.newscanningproject.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;


@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        System.out.println("AuthInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前)");
        /*数字签名*/
        String sign = request.getHeader("sign");
        String sign_config_key = "1608";
        /*获取时间戳*/
        String timeStamp = request.getHeader("timeStamp");
        /*获取token*/
        String token = request.getHeader("token");

        PrintWriter out = null;
        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(timeStamp)) {
                JSONObject res = new JSONObject();
                res.put("isSuccess", false);
                res.put("errorCode", 407);
                out = response.getWriter();
                out.append(res.toString());
                return false;
        }else{
            String md5Secret = encryption(encryption(sign_config_key+timeStamp).substring(0,10));
            if(sign.equals(md5Secret)){
                String userId = JwtUtil.getUserId(token)==null?"":JwtUtil.getUserId(token);
                String isRedisUserId = redisTemplate.opsForValue().get(userId) == null?null:redisTemplate.opsForValue().get(userId).toString();
                if(isRedisUserId != null){
                    // 验证 token
                    Boolean panduan = JwtUtil.checkSign(isRedisUserId,response);
                    if(panduan == false){
                        JSONObject res = new JSONObject();
                        res.put("isSuccess", false);
                        res.put("errorCode", 407);
                        out = response.getWriter();
                        out.append(res.toJSONString());
                        return false;
                    }else{
                        redisTemplate.expire(userId, 30000, TimeUnit.SECONDS);
                    }
                }else{
                    JSONObject res = new JSONObject();
                    res.put("isSuccess", false);
                    res.put("errorCode", 407);
                    out = response.getWriter();
                    out.append(res.toJSONString());
                    return false;
                }
            } else{
                JSONObject res = new JSONObject();
                res.put("isSuccess", false);
                res.put("errorCode", 407);
                out = response.getWriter();
                out.append(res.toString());
                response.reset();
                return false;
            }
        }
        return true;
    }

    /*
     * md5 32位加密
     */
    public String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return re_md5;
    }

}
