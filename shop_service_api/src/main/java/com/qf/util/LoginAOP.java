package com.qf.util;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

// 配置切面
@Aspect
public class LoginAOP {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate redisTemplate;
    // 创建一个AOP类，为IsLogin注解添加环绕增强
    // execution表达式: (*)任意类型 (*..)任何包下 (*Controller)任何Controller结尾的文件 .*(..) (Controller内所有方法的任意参数类型)
    // annotation:所有使用到了IsLogin注解
    @Around("execution(* *..*Controller.*(..)) && @annotation(com.qf.util.IsLogin)")
    public Object isLogin(ProceedingJoinPoint proceedingJoinPoint) {

        // 在获取到的目标方法上Islogin注解
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 获取方法
        Method method = signature.getMethod();
        // 通过方法获取到注解
        IsLogin isLogin = method.getAnnotation(IsLogin.class);

        // 创建一个token变量用来存放cookie的登录信息
        String token = null;

        // 判断是否已经登录(cookie中获取，需要用到servlet的request)
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(Constact.LOGIN_TOKEN)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        User user = null;
        // 如果token不为空，通过token的值查询redis的user对象
        System.out.println("token ---> " + token);
        if(token != null && !"".equals(token)) {
            user = (User) redisTemplate.opsForValue().get(token);
            System.out.println("user---> " + user);
        }

        // 全路径(http:...../Controller/xxx)
        // System.out.println("URL -->" + request.getRequestURL() + "?" + request.getQueryString());
        // 根路径(/Controller/xxx)
        // System.out.println("URI -->" + request.getRequestURI() + "?" + request.getQueryString());
        // 当某些情况下，可能需要强制进行登录
        if(user == null && isLogin.tologin()) {
            String returnUrl = request.getRequestURL() + "?" + request.getQueryString();
            // 对符号进行替换
            returnUrl = returnUrl.replace("&","*");
            return "redirect:http://localhost:8084/sso/tologin?returnUrl=" + returnUrl;

        }
        // 获取目标方法的参数列表
        Object[] args = proceedingJoinPoint.getArgs();
        // 如果已经登录，则将user对象赋值对应的user
        for(int i=0;i<args.length;i++) {
            if(args[i] != null && args[i].getClass().equals(User.class)) {
                args[i] = user;
            }
        }

        Object obj = null;
        try {
            // 放行目标方法
            obj = proceedingJoinPoint.proceed(args);
            //System.out.println(obj);  addsucc
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
}
