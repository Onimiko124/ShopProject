package com.qf.shop.shop_service_impl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IAddressDao;
import com.qf.dao.ICartDao;
import com.qf.dao.IOrderDao;
import com.qf.entity.*;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IAddressDao addressDao;

    @Autowired
    private ICartDao cartDao;
    @Override
    public String addOrderAndOrderDetils(Integer[] cid, Integer uid, Integer aid) {
        // 查询出地址id对应的收货人和收货地址
        Address address = addressDao.queryAddressByid(aid);

        // 查询出购物车的信息，顺便把商品信息一起查询出来
        List<Cart> cartlist = cartDao.queryCartByCids(cid);
        System.out.println("查询出来的购物车列表为-->" + cartlist);
//         计算出所有商品的总价
        double allprice = 0;
        for (Cart cart : cartlist) {
            // 计算单个购物车商品的价格
            Goods goods = cart.getGoods();
            allprice += goods.getPrice() * cart.getGnumber();
        }
        System.out.println("allprice-->" + allprice);

        // 添加订单时，分别添加订单和订单详情表
        Orders orders = new Orders();
        // 封装订单信息数据
        orders.setOrderid(UUID.randomUUID().toString());
        orders.setPerson(address.getPerson());
        orders.setCode(Integer.parseInt(address.getCode()));
        orders.setAddress(address.getAddress());
        orders.setOprice(allprice);
        orders.setPhone(address.getPhone());
        orders.setUid(uid);
        orders.setOrdertime(new Date());
        orders.setStatus(0);    // 0 未支付 - 1 已支付
        System.out.println("订单的信息为-->" + orders);

        Integer orderid = orderDao.addOrder(orders);
        System.out.println("回填的id为-->" + orderid);
        // 订单详情表根据购物车遍历进行生成对象后，添加到List集合中
        List<OrderDetils> orderDetils = new ArrayList<>();
        for (Cart cart : cartlist) {
            OrderDetils orderDetil = new OrderDetils();
            orderDetil.setGcount(cart.getGnumber());
            orderDetil.setGid(cart.getGoods().getId());
            orderDetil.setGimage(cart.getGoods().getGimage());
            orderDetil.setGinfo(cart.getGoods().getGinfo());
            orderDetil.setGname(cart.getGoods().getTitle());
            orderDetil.setOid(orderid);
            System.out.println("订单详情为-->" + orderDetil);
            orderDetils.add(orderDetil);
        }
        // 执行添加订单详情的方法，添加完毕后，删除用户下的购物车(执行购物车的id)
        orderDao.addOrderDetils(orderDetils);
        for (Cart cart : cartlist) {
            cartDao.delCart(cart.getId());
        }

        // 添加成功后，返回订单id
        return orders.getOrderid();
    }

    @Override
    public List<Orders> queryByUid(Integer uid) {
        return orderDao.queryByUid(uid);
    }

    @Override
    public Orders queryByOrderid(String orderid) {
        return orderDao.queryByOrderid(orderid);
    }

    @Override
    public int updateStatusByOrderid(String orderid, Integer status) {
        return orderDao.updateStatusByOrderid(orderid,status);
    }
}
