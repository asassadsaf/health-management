package com.fkp.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fkp.pojo.Permission;
import com.fkp.pojo.Role;
import com.fkp.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    //使用Dubbo通过网络远程调用服务提供方获取数据库中的用户信息

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByUsername(s);
        if(user == null){
            //用户名不存在
            return null;
        }
        //动态给该用户授权
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        return new org.springframework.security.core.userdetails.User(s,user.getPassword(),list);
    }
}
