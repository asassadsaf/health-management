package com.fkp.dao;

import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface CheckItemDao {

    void add(CheckItem checkItem);

    //使用分页助手时返回值为Page类型，mybatis框架会把查询结果封装到Page对象中
    Page<CheckItem> selectByCondition(String queryString);

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
