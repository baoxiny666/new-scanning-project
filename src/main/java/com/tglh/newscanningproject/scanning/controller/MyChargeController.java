package com.tglh.newscanningproject.scanning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("myCharge")
public class MyChargeController {
    @RequestMapping("/index")
    @ResponseBody
    public  String index(){
        return "eeeee";
    }
}
