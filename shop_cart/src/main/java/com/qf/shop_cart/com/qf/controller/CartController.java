package com.qf.shop_cart.com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qf.entity.Cart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IGoodsService;
import com.qf.util.Constact;
import com.qf.util.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @Reference
    private IGoodsService goodsService;

    /**
     * 通过注解的方式进行登录认证，如果已经登录则将user对象回填到形参user中
     *
     * @param cart
     * @return
     */
    @IsLogin
    @RequestMapping("/addCart")
    public String addCart(Cart cart,
                          User user,
                          HttpServletResponse response,
                          @CookieValue(value = "cart_token", required = false) String cart_token) {
        System.out.println("调用了添加购物车的方法");
        // 添加到购物车时，先判断是否已经登录，如果已经登录则直接存放到user对应的数据库中。
        // 如果还未登录，先存放到cookie当中
        // 判断user是否为空
        System.out.println(user);
        if (user != null) {
            if (user.getId() != null && !"".equals(user.getId())) {
                // 如果已经登录，则直接添加到购物车的数据库中
                cart.setUid(user.getId());
                cartService.addCart(cart);
            }
        } else {
            // 如果没有登录，查看cookie中是否已经有购物车商品了
            List<Cart> cartlist = null;
            if (cart_token != null) {
                // 如果cookie中已经有购物车对象
                TypeToken<List<Cart>> typeToken = new TypeToken<List<Cart>>() {
                };
                // 将json数据转换为对象
                cartlist = new Gson().fromJson(cart_token, typeToken.getType());
                // 添加当前正在添加的商品对象
                cartlist.add(cart);
            } else {
                // 如果cookie中没有购物车对象
                cartlist = Collections.singletonList(cart);
            }
            System.out.println("cartlist-->" + cartlist);

            // 将添加后的购物车对象转为json数据
            String json = new Gson().toJson(cartlist);
            try {
                json = URLEncoder.encode(json, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Cookie cookie = new Cookie(Constact.CART_TOKEN, json);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60 * 1000);
            response.addCookie(cookie);
        }
        System.out.println("添加购物车方法执行结束");
        // 添加购物车完毕后，返回到添加购物车成功的页面
        return "addsucc";
    }

    @RequestMapping("getCart")
    @ResponseBody
    @IsLogin
    public String getCart(@CookieValue(value = "cart_token" ,required = false) String carts, User user) {
        // 进入页面时，查询购物车的数据信息
        // 先查询cookie中的购物车，如果已经登录，还要查询user用户下的购物车
        List<Cart> cartlist = cartService.getCartlist(carts, user);
        return "getCart(" + new Gson().toJson(cartlist) + ")";
    }

    @RequestMapping("/cartlist")
    @IsLogin
    public String cartlist(@CookieValue(value = "cart_token", required = false)String carts, User user, Model model) {
        // 点击后，执行获取购物车的操作
        List<Cart> cartlist = cartService.getCartlist(carts, user);
        model.addAttribute("carts",cartlist);
        // 计算出所有商品的总价
        //float allprice = 0;
//        for (Cart cart : cartlist) {
//            // 计算单个购物车商品的价格
//            Goods goods = goodsService.goodsqueryone(cart.getGoods().getId());
//            allprice += goods.getPrice() * cart.getGnumber();
//        }
//        System.out.println("allprice-->" + allprice);
//        model.addAttribute("allprice",allprice);
        return "cartlist";
    }

    /**
     * 合并购物车的方法
     */
    @RequestMapping("/merge")
    @ResponseBody
    public String merge(Integer uid,@CookieValue(value = "cart_token" ,required = false)String cart_token) {
        // 将cookie中的json数据转为List<>
        System.out.println("开始购物城工程合并购物车的方法");
        TypeToken<List<Cart>> typeToken = new TypeToken<List<Cart>>() {};
        List<Cart> cartlist = new Gson().fromJson(cart_token, typeToken.getType());
        System.out.println("cookie转为List的数据:" + cartlist);
        for (Cart cart : cartlist) {
            // 将购物车添加到该用户的数据库记录下
            cart.setUid(uid);
            System.out.println("循环中的购物车信息:" + cart);
            cartService.addCart(cart);
        }
        return "succ";
    }

    @IsLogin(tologin = true)
    @RequestMapping("/delCart")
    public String delCart(User user, @RequestParam("cid") Integer cid,@RequestParam("url") String url) {
        System.out.println("cid为-->" + cid);
        System.out.println("url为-->" + url);
        // 通过登录的用户信息，判断是否有该购物车
        List<Cart> carts = cartService.queryCartByuid(user.getId());
        for (Cart cart : carts) {
            if(cart.getId() == cid) {
                // 如果该用户有此购物车，删除该购物车
                cartService.delCart(cid);
            }
        }
        return "redirect:" + url;
    }
}
