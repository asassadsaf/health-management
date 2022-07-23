package com.fkp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fkp.constant.MessageConstant;
import com.fkp.entity.Result;
import com.fkp.pojo.OrderSetting;
import com.fkp.service.OrderSettingService;
import com.fkp.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile")MultipartFile multipartFile){
        try {
            List<String[]> list = POIUtils.readExcel(multipartFile);
            List<OrderSetting> data = new ArrayList<>();
            if(list.size() > 0){
                for (String[] row : list) {
                    OrderSetting orderSetting = new OrderSetting(new Date(row[0]),Integer.parseInt(row[1]));
                    data.add(orderSetting);
                }
            }
            orderSettingService.add(data);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("editNumberByDay")
    public Result editNumberByDay(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDay(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
