package com.fkp.dao;

import com.fkp.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(Integer userId);
}
