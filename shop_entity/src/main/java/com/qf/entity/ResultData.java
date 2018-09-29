package com.qf.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ResultData<T> implements Serializable {
    private Integer code;   // 状态码: 0 登录suss,1 帐号错误, 2 密码错误, 3 其他错误
    private String msg; // 具体信息
    private T data; // 登录成功后的返回对象
}
