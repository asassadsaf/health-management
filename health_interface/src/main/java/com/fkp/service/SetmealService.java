package com.fkp.service;

import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.pojo.Setmeal;

import java.util.List;

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult pageQuery(QueryPageBean queryPageBean);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);
}
