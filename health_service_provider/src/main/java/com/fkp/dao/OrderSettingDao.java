package com.fkp.dao;

import com.fkp.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    void add(OrderSetting orderSetting);

    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    List<Map> getOrderSettingByMonth(Map<String, String> queryParam);

    void editNumberByDay(OrderSetting orderSetting);

    OrderSetting findByOrderDate (Date orderDate);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
