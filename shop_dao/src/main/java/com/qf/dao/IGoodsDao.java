package com.qf.dao;

import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsDao {

    List<Goods> queryall();

    int goodsadd(Goods goods);

    List<Goods> querygoodsnew();
}
