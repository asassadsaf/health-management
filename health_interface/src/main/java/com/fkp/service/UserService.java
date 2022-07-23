package com.fkp.service;

import com.fkp.pojo.User;

public interface UserService {
    User findByUsername(String username);
}
