package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsService {

    List<Goods> queryall();

    int goodsadd(Goods goods);

    List<Goods> querygoodsnew();
}
