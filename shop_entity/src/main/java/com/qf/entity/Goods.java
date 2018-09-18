package com.qf.entity;

// 使用lombok插件帮助我们自动生成setget,toString,equals方法...

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

// 添加注解使用lombok
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Goods implements Serializable {

    private Integer id;
    private String title;
    private String ginfo;
    private double gcount;
    private Integer tid;
    private double allprice;
    private double price;
    private String gimage;
}
