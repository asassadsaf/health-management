package com.fkp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.fkp.constant.MessageConstant;
import com.fkp.constant.RedisMessageConstant;
import com.fkp.entity.Result;
import com.fkp.pojo.Member;
import com.fkp.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 处理会员相关操作
 */

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     *
     * @param params
     * @return
     */
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map params){
        String telephone = (String) params.get("telephone");
        String validateCode = (String) params.get("validateCode");
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(validateCodeInRedis != null && validateCodeInRedis.equals(validateCode)){//验证码输入正确
            try {
                //判断该用户是否为会员
                Member member = memberService.findByTelephone(telephone);
                if(member == null){
                    //不是会员，需要自动完成注册
                    member = new Member();
                    member.setRegTime(new Date());
                    member.setPhoneNumber(telephone);
                    memberService.add(member);
                }
                //向客户端浏览器写入Cookie，内容为手机号
                Cookie cookie = new Cookie("login_member_telephone",telephone);
                cookie.setPath("/");
                cookie.setMaxAge(60*60*24*30);
                response.addCookie(cookie);
                //将会员信息保存到Redis，代替Session
                String json = JSON.toJSON(member).toString();
                jedisPool.getResource().setex(telephone,60*30,json);
                return new Result(true,MessageConstant.LOGIN_SUCCESS);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(false,"登陆失败");
            }
        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
