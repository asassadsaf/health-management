package com.fkp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fkp.constant.MessageConstant;
import com.fkp.constant.RedisConstant;
import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.entity.Result;
import com.fkp.pojo.Setmeal;
import com.fkp.service.SetmealService;
import com.fkp.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile){
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String endStr = originalFilename.substring(index - 1);
            String uploadFileName = UUID.randomUUID().toString() + endStr;
            try {
                QiniuUtils.upload2Qiniu(multipartFile.getBytes(),uploadFileName);
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,uploadFileName);
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
            }
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,uploadFileName);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.pageQuery(queryPageBean);
    }
}
