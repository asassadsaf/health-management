package com.fkp.jobs;

import com.fkp.constant.RedisConstant;
import com.fkp.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //根据Redis中两个集合的差值获得垃圾图片的集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            for (String s : set) {
                //删除七牛服务器上的图片
                QiniuUtils.deleteFileFromQiniu(s);
                //删除Redis中删除图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
                System.out.println("自定义任务执行，清理垃圾图片："+s);

            }
        }
    }
}
