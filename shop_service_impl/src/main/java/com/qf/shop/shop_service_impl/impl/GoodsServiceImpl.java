package com.qf.shop.shop_service_impl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private IGoodsDao goodsDao;

    @Override
    public List<Goods> queryall() {
        return goodsDao.queryall();
    }

    @Override
    public int goodsadd(Goods goods) {
        return goodsDao.goodsadd(goods);
    }

    @Override
    public List<Goods> querygoodsnew() {
        return goodsDao.querygoodsnew();
    }

    @Override
    public int goodsdel(Integer id) {
        return goodsDao.goodsdel(id);
    }

    @Override
    public int goodsedit(Goods goods) {
        System.out.println(goods);
        return goodsDao.goodsedit(goods);
    }

    @Override
    public Goods goodsqueryone(Integer id) {
        return goodsDao.goodsqueryone(id);
    }
}
