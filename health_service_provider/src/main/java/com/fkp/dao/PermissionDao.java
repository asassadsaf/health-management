package com.fkp.dao;

import com.fkp.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
}
