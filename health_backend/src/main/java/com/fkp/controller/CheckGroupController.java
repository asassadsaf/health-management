package com.fkp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fkp.constant.MessageConstant;
import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.entity.Result;

import com.fkp.pojo.CheckGroup;
import com.fkp.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 检查组管理
 */

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    //新增检查组
    @RequestMapping("/add")
    //表单数据是以json对象传过来的，使用@RequestBody注解封装到实体类中，检查项的ids是以数组对象传过来的，使用数组对象接收
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //检查组分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.pageQuery(queryPageBean);
    }

    //根据id查询检查组信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //根据id查询检查组关联的检查项Ids
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try {
            List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            //将list列表转为Integer数组
            Integer[] ids = new Integer[list.size()];
            list.toArray(ids);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,ids);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //编辑检查组信息以及检查组关联的检查项
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping("/delete")
    public Result deleteCheckGroup(Integer id){
        try {
            checkGroupService.deleteCheckGroup(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询所有检查组
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> list = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
