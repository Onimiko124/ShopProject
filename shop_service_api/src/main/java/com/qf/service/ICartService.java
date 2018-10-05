package com.qf.service;

import com.qf.entity.Cart;
import com.qf.entity.User;

import java.util.List;

public interface ICartService {

    Cart addCart(Cart cart);

    int delCart(Integer id);

    List<Cart> queryCartByuid(Integer uid);

    int delCartByuid(Integer uid);

    List<Cart> getCartlist(String cart_token,User user);

    List<Cart> queryCartByUidAndGids(Integer uid,Integer[] gid);
}
