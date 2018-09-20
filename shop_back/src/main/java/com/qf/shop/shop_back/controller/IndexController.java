package com.qf.shop.shop_back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        System.out.println("进入首页");
        return "index";
    }

    /**
     * 页面跳转的方法，restful风格
     * @param pagename
     * @return
     */
    @RequestMapping("/topage/{pagename}")
    public String topage(@PathVariable("pagename") String pagename){
        return pagename;
    }
}
