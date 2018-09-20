package com.qf.shop.shop_back.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private IGoodsService goodsService;

    @RequestMapping("/goodslist")
    public String goodslist(Model model){
        List<Goods> goodslist = goodsService.queryall();
        System.out.println(goodslist);
        model.addAttribute("goodslist",goodslist);
        return "goodslist";
    }

    @RequestMapping("/goodsadd")
    public String goodsadd(@RequestParam("file")MultipartFile file, Goods goods){
        // 主键回填返回记录的id
        goods.setTid(1);
        int goodsadd = goodsService.goodsadd(goods);
        goods.setId(goodsadd);
        return "redirect:/goods/goodslist";
    }

    @RequestMapping("/querygoodsnew")
    @ResponseBody
    @CrossOrigin
    public List<Goods> querygoodsnew(){
        /**
            浏览器的同源策略不允许不同端口和ip协议之间进行跨域
            实现跨域的方式
            1.通过springmvc的@CrossOrigin注解，设置浏览器允许跨域
            2.ajax的dataType使用jsonp，利用了css和js等文件不受同源策略的漏洞，返回一个类似函数的字符串，然后在前端页面
            创建一个函数指定局部变量参数，再将形参获取到就可以使用。
          */
        return goodsService.querygoodsnew();
    }
}
