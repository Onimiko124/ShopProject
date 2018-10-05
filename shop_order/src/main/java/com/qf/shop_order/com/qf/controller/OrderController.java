package com.qf.shop_order.com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.Cart;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.util.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    private ICartService cartService;

    @Reference
    private IAddressService addressService;

    /**
     * 确认并填写订单，跳转到订单页面
     */
    @IsLogin(tologin =  true)
    @RequestMapping("/orderedit")
    public String orderedit(Integer[] gid, User user, Model model) {
        // 必须要登录用户，否则直接跳转到登录页面(必须要获取到用户的地址信息)
        System.out.println("订单信息-->" + Arrays.toString(gid));
        System.out.println("用户信息-->" + user);

        // 通过已经选中的商品id和用户的id，查询用户需要购买的购物车id
        List<Cart> carts = cartService.queryCartByUidAndGids(user.getId(), gid);
        // 查询用户的所有收货地址信息
        List<Address> addresslist = addressService.queryAddress(user.getId());
        System.out.println("order工程查询到的购物车列表-->" + carts);
        System.out.println("order工程查询到的地址列表-->" + addresslist);

        // 计算出购物车的总价
        float allprice = 0;
        for (Cart cart : carts) {
            allprice += cart.getGnumber() * cart.getGoods().getPrice();
        }
        // 转发到订单页面
        model.addAttribute("allprice",allprice);
        model.addAttribute("carts",carts);
        model.addAttribute("addresss",addresslist);
        return "orderedit";
    }

    @IsLogin(tologin = true)
    @ResponseBody
    @RequestMapping("/addaddress")
    public Address addaddress(Address address,User user) {
        address.setUid(user.getId());
        Address address1 = addressService.addAddress(address);
        System.out.println("添加后的address为--" + address1);
        return address1;
    }

    @RequestMapping("/addorder")
    @ResponseBody
    public String addorder(Integer aid,Integer[] cid) {
        System.out.println("收货地址的id-->" + aid);
        System.out.println("购物车的cid列表-->" + Arrays.toString(cid));
        return null;
    }
}
