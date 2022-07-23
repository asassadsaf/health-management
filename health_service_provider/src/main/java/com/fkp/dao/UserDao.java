package com.fkp.dao;

import com.fkp.pojo.User;

public interface UserDao {
    User findByUsername(String username);
}
