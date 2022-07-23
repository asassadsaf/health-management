package com.fkp.service;

import com.fkp.entity.Result;

import java.util.Map;

public interface OrderService {
    Result order(Map params);

    Map findById(Integer id) throws Exception;

    Integer findCountBySetmealId(Integer id);

    Map<String, Integer> findOrderReport(String day) throws Exception;
}
