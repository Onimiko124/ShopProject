package com.qf.util;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IsLogin {

    // 返回boolean类型
    boolean tologin() default false;
}
