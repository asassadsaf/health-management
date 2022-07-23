package com.fkp.dao;

import com.fkp.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

//    void setCheckGroupIdAndCheckItem(@Param("checkgroupId") Integer checkgroupId, @Param("checkitemId") Integer checkitemId);
    void setCheckGroupIdAndCheckItem(Map<String, Integer> map);

    Page<CheckGroup> selectByCondition(String queryString);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    CheckGroup findById(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteCheckItemCheckGroupRelByCheckGroupId(Integer id);

    void deleteCheckGroupById(Integer id);

    List<CheckGroup> findAll();
}
