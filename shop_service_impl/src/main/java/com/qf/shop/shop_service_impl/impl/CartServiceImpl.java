package com.qf.shop.shop_service_impl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.qf.dao.ICartDao;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Cart;
import com.qf.entity.Goods;
import com.qf.entity.User;
import com.qf.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartDao cartDao;

    @Autowired
    private IGoodsDao goodsDao;

    @Override
    public Cart addCart(Cart cart) {
        System.out.println(cart.getId());
        int cartid = cartDao.addCart(cart);
        cart.setId(cartid);
        return cart;
    }

    @Override
    public int delCart(Integer id) {
        return cartDao.delCart(id);
    }

    @Override
    public List<Cart> queryCartByuid(Integer uid) {
        return cartDao.queryCartByuid(uid);
    }

    @Override
    public int delCartByuid(Integer uid) {
        return cartDao.delCart(uid);
    }

    @Override
    public List<Cart> getCartlist(String cart_token, User user) {
        List<Cart> cartlist = null;
        // 如果没有登录，就直接获取cookie的购物车
        if(user != null) {
            // 获取user的购物车
            cartlist = queryCartByuid(user.getId());
        } else {
            // 获取cookie内的购物车
            TypeToken<List<Cart>> typeToken = new TypeToken<List<Cart>>() {};
            // 转换为对象
            cartlist = new Gson().fromJson(cart_token, typeToken.getType());
        }

        // 当购物车列表不为空时，需要将每一个添加到购物车的商品id对应查询并添加到购物车列表中
        if(cartlist != null) {
            for(int i=0;i<cartlist.size();i++) {
                cartlist.get(i).setGoods(goodsDao.goodsqueryone(cartlist.get(i).getGid()));
                System.out.println("cartlist的第" + i + "-->" + cartlist.get(i).getGoods());
            }
        }

        return cartlist;
    }

    @Override
    public List<Cart> queryCartByUidAndGids(Integer uid, Integer[] gid) {
        System.out.println("serviceimpl -->" + Arrays.toString(gid));
        List<Cart> carts = cartDao.queryCartByUidAndGids(uid, gid);
        for(int i=0;i < carts.size();i++) {
            // 给每一个购物车列表中填入商品的信息
            Goods goods = goodsDao.goodsqueryone(carts.get(i).getGid());
            System.out.println("填入的商品信息" + i + goods);
            carts.get(i).setGoods(goods);
        }
        return carts;
    }

    @Override
    public List<Cart> queryCartByCids(Integer[] cid) {

        // 根据购物车的数组id，查询出所有匹配id的购物车
        return cartDao.queryCartByCids(cid);
    }
}
