package com.fkp.controller;


import com.fkp.constant.MessageConstant;
import com.fkp.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当spring security完成认证后，会将当前用户的信息保存到框架提供的上下文对象中，基于session
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();   //获取spring security提供的User对象
        System.out.println(user);
        if(user != null){
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }
}
