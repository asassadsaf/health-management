package com.fkp.controller;

import com.aliyuncs.exceptions.ClientException;
import com.fkp.constant.MessageConstant;
import com.fkp.constant.RedisMessageConstant;
import com.fkp.entity.Result;
import com.fkp.utils.SMSUtils;
import com.fkp.utils.TestClient;
import com.fkp.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;


/**
 * 验证码操作
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    //体检预约验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //给用户发送验证码
        try {
            TestClient.sendShortMessage(TestClient.VALIDATE_CODE,telephone,validateCode.toString());
            //保存验证码到redis(5分钟)
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    //登录验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //给用户发送验证码
        try {
            TestClient.sendShortMessage(TestClient.VALIDATE_CODE,telephone,validateCode.toString());
            //保存验证码到redis(5分钟)
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
