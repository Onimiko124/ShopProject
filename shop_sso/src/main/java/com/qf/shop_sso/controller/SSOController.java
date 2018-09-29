package com.qf.shop_sso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.IUserService;
import com.qf.service.util.Constact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SSOController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IUserService userService;

    @RequestMapping("/tologin")
    public String tologin(){
        return "login";
    }

    /**
     * 实现登录操作，如果登录成功了，添加到Redis并且添加Cookie中
     * @return
     */
    @RequestMapping("/login")
    public String login(String username,String password, HttpServletResponse response,Model model){
        User user = userService.login(username);
        ResultData<User> data = new ResultData<>();
        if(user != null) {
            System.out.println(user);
            // 判断password是否正确
            if(user.getPassword().equals(password)){
                // 如果登录成功，添加到Redis中，并且存入cookie
                // 创建Cookie，key为常量值，value值为UUID的值
                String uuid = UUID.randomUUID().toString();
                Cookie cookie = new Cookie(Constact.LOGIN_TOKEN, uuid);
                cookie.setMaxAge(7 * 24 * 60 * 60 * 1000);
                cookie.setPath("/");
                response.addCookie(cookie);
                // 存放到Redis当中
                redisTemplate.opsForValue().set(uuid, user);
                redisTemplate.expire(uuid, 7, TimeUnit.DAYS);
                System.out.println("登录成功!");
                // 跳转到前台首页
                return "redirect:http://localhost:8085";
            } else {
                // 密码不匹配时
                data.setCode(2);
                data.setMsg("密码错误");
            }
        } else {
                // 用户名不存在的错误
                data.setCode(1);
                data.setMsg("用户不存在");
        }
            System.out.println("code-->" + data.getCode());
            System.out.println("msg-->" + data.getMsg());
            model.addAttribute("data",data);
            return "login";
//        }
    }

    // 登录的认证，当我们从前台工程跳转到搜索工程，登录信息应该保持不变
    @RequestMapping("/islogin")
    @ResponseBody
    public String islogin(@CookieValue(value = "login_token",required = false)String login_token) {
        /**
         * 判断是否登录了用户
         * @CookieValue注解简化了查询Cookie的步骤，可将查询到的value值直接返回给添加注解的参数
         * required属性可以设置是否在找不到对应的key值时抛出异常
         */
        // 通过常量值获取Cookie
//        Cookie[] cookies = request.getCookies();
//        User user = null;
//        for(int i=0;i<cookies.length;i++){
//            if(cookies[i].getName().equals(Constact.LOGIN_TOKEN)){
//                // 获取到key为login_token的value值，到Redis通过value进行查询是否存在该用户信息
//                String value = cookies[i].getValue();
//                user = (User) redisTemplate.opsForValue().get(value);
//                System.out.println("redis中的user-->" + user);
//            }
//        }
        // 如果遍历完还是没有key为login_token的cookie，则直接返回null
        User user = null;
        // 通过返回的value到Redis缓存数据库中查询
        if(login_token!=null && !"".equals(login_token)){
             user = (User) redisTemplate.opsForValue().get(login_token);
        }
        return "callback(" + new Gson().toJson(user) + ")";
    }

    /**
     * 注销的操作
     */
    @RequestMapping("/logout")
    public String logout(@CookieValue(value = "login_token",required = false) String login_token,HttpServletResponse response){
        if(login_token!=null && !"".equals(login_token)){
            // 清除redis中的数据信息
            redisTemplate.delete(Constact.LOGIN_TOKEN);

            // 如果cookie不为空，则把该cookie删除(设置过期时间为0)
            Cookie cookie = new Cookie(Constact.LOGIN_TOKEN,"");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            // 注销成功后，返回到登录页面
        }
            return "login";
    }
}
