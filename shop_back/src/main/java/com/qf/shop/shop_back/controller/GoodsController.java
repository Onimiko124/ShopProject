package com.qf.shop.shop_back.controller;

import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @RequestMapping("/goodslist")
    public String goodslist(Model model){
        List<Goods> goodslist = goodsService.queryall();
        model.addAttribute("goodslist",goodslist);
        return "goodslist";
    }
}
