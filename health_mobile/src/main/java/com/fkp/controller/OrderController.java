package com.fkp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fkp.constant.MessageConstant;
import com.fkp.constant.RedisMessageConstant;
import com.fkp.entity.Result;
import com.fkp.pojo.Order;
import com.fkp.service.OrderService;
import com.fkp.utils.SMSUtils;
import com.fkp.utils.TestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约处理
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map params){
        //判断提交的验证码是否正确
        String telephone = (String) params.get("telephone");
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) params.get("validateCode");
        if(validateCodeRedis != null && validateCodeRedis.equals(validateCode)){
            //设置预约类型（微信预约，电话预约）
            params.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result = null;
            try {
                result = orderService.order(params);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
            //预约成功，发送通知短信
//            if(result.isFlag()){
//                try {
//                    TestClient.sendShortMessage(TestClient.ORDER_NOTICE,telephone,(String) params.get("orderDate"));
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
            return result;
        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
