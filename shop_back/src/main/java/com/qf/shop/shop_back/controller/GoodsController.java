package com.qf.shop.shop_back.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Reference
    private IGoodsService goodsService;

    @Value("${image.path}")
    private String path;

    @RequestMapping("/goodslist")
    public String goodslist(Model model){
        List<Goods> goodslist = goodsService.queryall();
        System.out.println(goodslist);
        model.addAttribute("goodslist",goodslist);
        model.addAttribute("path",path);
        return "goodslist";
    }

    @RequestMapping("/goodsdel/{id}")
    public String goodsdel(@PathVariable Integer id){
        goodsService.goodsdel(id);
        return "redirect:/goods/goodslist";
    }

    @RequestMapping("/goodsedit")
    public String goodsedit(@RequestParam("file")MultipartFile file,Goods goods) throws IOException {
        if(file.getOriginalFilename() != null && !"".equals(file.getOriginalFilename())){
            // 上传图片
            StorePath spath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), "JPG", null);
            String fullPath = spath.getFullPath();

            goods.setGimage(fullPath);
        }
        goodsService.goodsedit(goods);
        return "redirect:/goods/goodslist";
    }

    @RequestMapping("/goodsqueryone/{id}")
    public String goodsqueryone(@PathVariable Integer id,Model model){
        Goods goods = goodsService.goodsqueryone(id);
        model.addAttribute("goods",goods);
        return "goodsedit";
    }

    @RequestMapping("/goodsadd")
    public String goodsadd(@RequestParam("file")MultipartFile file, Goods goods) throws IOException {
        if(file.getOriginalFilename() != null && !"".equals(file.getOriginalFilename())){
            // 上传图片
            StorePath spath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), "JPG", null);
            String fullPath = spath.getFullPath();

            goods.setGimage(fullPath);
            // 主键回填返回记录的id
            goods.setTid(1);
        }

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

    @RequestMapping("querypath")
    @ResponseBody
    @CrossOrigin
    public String querypath(){
        return path;
    }
}
