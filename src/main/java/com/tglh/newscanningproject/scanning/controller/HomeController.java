package com.tglh.newscanningproject.scanning.controller;

import com.alibaba.fastjson.JSONObject;
import com.tglh.newscanningproject.scanning.entity.Banner;
import com.tglh.newscanningproject.scanning.entity.Notice;
import com.tglh.newscanningproject.scanning.entity.SafeModules;
import com.tglh.newscanningproject.scanning.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;



    @RequestMapping("/modules")
    @ResponseBody
    public String modules(){
        List<SafeModules> modulesList =  homeService.modules();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("data",modulesList);
        return jsonObject.toJSONString();
    }

    @RequestMapping("/banner")
    @ResponseBody
    public String banner(){
        List<Banner> bannerList =  homeService.banner();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("data",bannerList);
        return jsonObject.toJSONString();
    }

    @RequestMapping("/notice")
    @ResponseBody
    public String notice(){
        Notice notice =  homeService.notice();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("data",notice);
        return jsonObject.toJSONString();
    }

}
