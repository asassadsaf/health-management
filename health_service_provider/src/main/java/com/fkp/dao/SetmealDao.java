package com.fkp.dao;

import com.fkp.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void addSetmealCheckgroupRel(@Param("setmealId") Integer setmealId,@Param("checkgroupId") Integer checkgroupId);

    Page<Setmeal> selectByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findSetmealById(Integer id);

    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    Setmeal findById(Integer id);
}
