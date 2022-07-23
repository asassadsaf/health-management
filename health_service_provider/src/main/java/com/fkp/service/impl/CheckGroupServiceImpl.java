package com.fkp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fkp.dao.CheckGroupDao;
import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.pojo.CheckGroup;
import com.fkp.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务
 */

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组,同时将检查组关联检查项
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组，操作t_checkgroup表
        checkGroupDao.add(checkGroup);
        //获取检查组id,使用mybatis框架将自增的id封装到了CheckGroup对象的id属性中
        Integer checkgroupId = checkGroup.getId();
        //可以传入两个参数，也可以将两个参数用Map来封装
//        if(checkitemIds != null && checkitemIds.length>0){
//            for (Integer checkitemId : checkitemIds) {
//                //设置检查组和检查项的多对多关联关系，操作t_checkgroup_checkitem表
//                checkGroupDao.setCheckGroupIdAndCheckItem(checkgroupId,checkitemId);
//            }
//        }
        if(checkitemIds != null && checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroupId",checkgroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupIdAndCheckItem(map);
            }
        }
    }

    //检查组分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckGroup> rows = page.getResult();
        return new PageResult(total,rows);
    }

    //根据检查组id查询检查组信息
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }


    //根据检查组id查询其关联检查项的ids
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //修改检查组数据，包括修改检查组基本信息t_checkgroup表和检查组关联的检查项信息t_checkgroup_checkitem表
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.edit(checkGroup);
        //删除原来的检查组和检查项的关联关系
        Integer id = checkGroup.getId();
        checkGroupDao.deleteCheckItemCheckGroupRelByCheckGroupId(id);
        if(checkItemIds != null && checkItemIds.length >0){
            for (Integer checkItemId : checkItemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId",id);
                map.put("checkitemId",checkItemId);
                checkGroupDao.setCheckGroupIdAndCheckItem(map);
            }
        }

    }

    //删除检查组以及对应的检查项
    @Override
    public void deleteCheckGroup(Integer id) {
        //删除检查组对应的检查项信息
        checkGroupDao.deleteCheckItemCheckGroupRelByCheckGroupId(id);
        //删除检查组信息
        checkGroupDao.deleteCheckGroupById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> list = checkGroupDao.findAll();
        return list;
    }

}
