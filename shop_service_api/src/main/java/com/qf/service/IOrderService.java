package com.qf.service;

import com.qf.entity.Orders;

import java.util.List;

public interface IOrderService {

    String addOrderAndOrderDetils(Integer[] cid,Integer uid,Integer aid);

    List<Orders> queryByUid(Integer uid);

    Orders queryByOrderid(String orderid);

    // 当结算后，将订单的状态改变为已经支付
    int updateStatusByOrderid(String id,Integer status);
}
