package com.fkp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fkp.dao.CheckItemDao;
import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.entity.Result;
import com.fkp.pojo.CheckItem;
import com.fkp.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */

//如果加了事务注解，在Service注解中要指定该服务是实现的哪个服务接口
@Service(interfaceClass = CheckItemService.class)
@Transactional  //事务注解
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired  //注入Dao对象
    private CheckItemDao checkItemDao;

    //新增检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //检查项分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //获取请求到的参数
        Integer currentPage = queryPageBean.getCurrentPage();   //当前页码
        Integer pageSize = queryPageBean.getPageSize();     //每页的记录数
        String queryString = queryPageBean.getQueryString();    //查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件
        //select * from t_checkitem limit (currentPage-1)*pageSize,pageSize
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        //获取要响应的数据
        long total = page.getTotal();   //记录总数
        List<CheckItem> rows = page.getResult();    //查询到的数据
        //将要响应的数据封装到PageResult对象中
        return new PageResult(total,rows);
    }

    //根据Id删除检查项
    @Override
    public void deleteById(Integer id)throws RuntimeException{
        //判断要删除的检查项是否被关联到检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){  //当前检查项已经被关联到检查组，不允许删除
            //抛出异常信息RuntimeException
            throw new RuntimeException("当前检查项被关联，不能删除！");
        }else { //当前检查项没有被关联到检查组，允许删除
            checkItemDao.deleteById(id);
        }
    }

    //根据id查询检查项
    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    //修改表单项数据
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
