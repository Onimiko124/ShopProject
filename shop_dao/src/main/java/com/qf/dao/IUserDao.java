package com.qf.dao;

import com.qf.entity.User;

public interface IUserDao {
    User login(String username);
}
