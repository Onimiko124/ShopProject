package com.qf.dao;

import com.qf.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICartDao {

    int addCart(Cart cart);

    int delCart(Integer id);

    List<Cart> queryCartByuid(Integer uid);

    List<Cart> queryCartByUidAndGids(@Param("uid") Integer uid, @Param("gid") Integer[] gid);

    List<Cart> queryCartByCids(@Param("cid") Integer[] cid);
}
