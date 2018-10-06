package com.qf.dao;

import com.qf.entity.OrderDetils;
import com.qf.entity.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IOrderDao {

    Integer addOrder(Orders orders);

    // 批量的添加订单详情表
    Integer addOrderDetils(@Param("orderdetils") List<OrderDetils> orderdetils);

    List<Orders> queryByUid(Integer uid);

    Orders queryByOrderid(String orderid);

    // 当结算后，将订单的状态改变为已经支付
    int updateStatusByOrderid(@Param("orderid") String orderid,@Param("status") Integer status);
}
