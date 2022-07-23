package com.fkp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fkp.constant.RedisConstant;
import com.fkp.dao.CheckGroupDao;
import com.fkp.dao.CheckItemDao;
import com.fkp.dao.SetmealDao;
import com.fkp.entity.PageResult;
import com.fkp.entity.QueryPageBean;
import com.fkp.pojo.CheckGroup;
import com.fkp.pojo.CheckItem;
import com.fkp.pojo.Setmeal;
import com.fkp.service.CheckGroupService;
import com.fkp.service.CheckItemService;
import com.fkp.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    //从属性文件中读取要生成的html文件对应的目录
    @Value("${out_put_path}")
    private String outPutPath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        if(checkgroupIds !=null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckgroupRel(setmealId,checkgroupId);
            }
        }
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

        //当添加套餐后需要重新生成静态页面（套餐列表页面，套餐详情页面）
        generateMobileStaticHtml();
    }
    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
        //在生成静态页面之前查询数据
        List<Setmeal> list = setmealDao.findAll();

        //生成套餐列表的静态页面
        generateMobileSetmealListHtml(list);

        //生成套餐详情的静态页面
        generateMobileSetmealDetailHtml(list);
    }

    //生成套餐列表的静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        Map map = new HashMap();
        map.put("setmealList",list);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐详情的静态页面
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            map.put("setmeal",setmealDao.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }

    //用于生成静态页面
    public void generateHtml(String templateName, String htmlPageName, Map map){
        //获得配置对象，已经配置好了模板的目录和字符集
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer writer = null;
        try {
            //获取模板对象，传入模板名
            Template template = configuration.getTemplate(templateName);
            //创建输出流对象，指定输出文件的路径
            writer = new FileWriter(new File(outPutPath+"/"+htmlPageName));
            //输出html文件
            template.process(map,writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
//        Setmeal setmeal = setmealDao.findSetmealById(id);
//        List<Integer> checkGroupIds = setmealDao.findCheckGroupIdsBySetmealId(id);
//        List<CheckGroup> checkGroupList = new ArrayList<>();
//        for (Integer checkGroupId : checkGroupIds) {
//            CheckGroup checkGroup = checkGroupDao.findById(checkGroupId);
//            List<Integer> checkItemIds = checkGroupDao.findCheckItemIdsByCheckGroupId(checkGroup.getId());
//            List<CheckItem> checkItemList = new ArrayList<>();
//            for (Integer checkItemId : checkItemIds) {
//                CheckItem checkItem = checkItemDao.findById(checkItemId);
//                checkItemList.add(checkItem);
//            }
//            checkGroup.setCheckItems(checkItemList);
//            checkGroupList.add(checkGroup);
//        }
//        setmeal.setCheckGroups(checkGroupList);


        Setmeal setmeal2 = setmealDao.findById(id);

        return setmeal2;
    }
}
