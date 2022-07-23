package com.fkp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fkp.constant.MessageConstant;
import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.entity.Result;
import com.fkp.pojo.CheckItem;
import com.fkp.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项管理
 */
@RestController     //相当于@Controller+@ResponseBody
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference  //远程注入
    private CheckItemService checkItemService;

    //新增检查项
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){    //@RequestBody将获得的json格式的请求数据封装到实体类中
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){   //服务调用失败
            e.printStackTrace();    //打印异常信息
            //数据响应，返回一个结果对象Result
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        //数据响应，返回一个结果对象Result
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //检查项分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        //spring会将PageResult对象转为json响应到页面
        return pageResult;
    }

    //删除检查项
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id){
        try {
            checkItemService.deleteById(id);
        }catch (RuntimeException e){    //捕获RuntimeException,代表要删除的检查项已经被关联到检查组，不允许删除
            e.printStackTrace();
            //将异常信息封装到Result对象中返回
            return new Result(false,e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //根据id查询检查项
    @RequestMapping("/findById")
    public Result findById(Integer id){
        CheckItem checkItem;
        try {
            checkItem = checkItemService.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    //修改表单项数据
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //查询全部表单项数据
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
