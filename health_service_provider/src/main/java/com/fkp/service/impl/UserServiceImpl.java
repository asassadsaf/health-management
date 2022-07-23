package com.fkp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fkp.dao.PermissionDao;
import com.fkp.dao.RoleDao;
import com.fkp.dao.UserDao;
import com.fkp.pojo.Permission;
import com.fkp.pojo.Role;
import com.fkp.pojo.User;
import com.fkp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    //通过用户名查询用户信息和其角色信息以及该角色对应的权限信息
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);   //查询用户基本信息，不包含用户对应的角色
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }
}
