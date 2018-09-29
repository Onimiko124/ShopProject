package com.qf.shop.shop_service_impl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IUserDao;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private IUserDao iuserDao;

    @Override
    public User login(String username) {
        return iuserDao.login(username);
    }

}
