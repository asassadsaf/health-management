package com.fkp.dao;

import com.fkp.pojo.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByCondition(Order order);

    void add(Order order);

    List<Order> findByOrderDateLimit(Map<String, Date> queryParams);
}
