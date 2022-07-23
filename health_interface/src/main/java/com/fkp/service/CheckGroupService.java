package com.fkp.service;

import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupService {

    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult pageQuery(QueryPageBean queryPageBean);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    CheckGroup findById(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteCheckGroup(Integer id);

    List<CheckGroup> findAll();
}
