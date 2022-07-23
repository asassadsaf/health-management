package com.fkp.service;

import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.entity.Result;
import com.fkp.pojo.CheckItem;

import java.util.List;

/**
 * 服务接口
 */
public interface CheckItemService {

    void add(CheckItem checkItem);

    PageResult pageQuery(QueryPageBean queryPageBean);

    void deleteById(Integer id) throws RuntimeException;

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
