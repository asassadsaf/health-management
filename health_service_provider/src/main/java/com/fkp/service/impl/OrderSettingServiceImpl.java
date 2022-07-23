package com.fkp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fkp.dao.OrderSettingDao;
import com.fkp.pojo.OrderSetting;
import com.fkp.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting : list) {
                Date orderDate = orderSetting.getOrderDate();
                long count = orderSettingDao.findCountByOrderDate(orderDate);
                if(count == 0){
                    orderSettingDao.add(orderSetting);
                }else {
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }

            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {  //date格式：yyyy-mm
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        int actualMaximum = cal.getActualMaximum(Calendar.DATE);
        String begin = date + "-1";
        String end = date + "-" + actualMaximum;
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("begin",begin);
        queryParam.put("end",end);
        List<Map> list = orderSettingDao.getOrderSettingByMonth(queryParam);
        for (Map<String, Object> map : list) {
            Integer orderDate = ((Date) map.get("orderDate")).getDate();
            map.remove("orderDate");
            map.put("date",orderDate);
        }
        return list;
    }

    @Override
    public void editNumberByDay(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count > 0){
            orderSettingDao.editNumberByDay(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }

    }
}
