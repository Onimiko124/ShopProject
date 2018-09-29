package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsService {

    List<Goods> queryall();

    Goods goodsadd(Goods goods);

    List<Goods> querygoodsnew();

    int goodsdel(Integer id);

    int goodsedit(Goods goods);

    Goods goodsqueryone(Integer id);
}
