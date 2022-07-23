package com.fkp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fkp.constant.MessageConstant;
import com.fkp.dao.MemberDao;
import com.fkp.dao.OrderDao;
import com.fkp.dao.OrderSettingDao;
import com.fkp.dao.SetmealDao;
import com.fkp.entity.Result;
import com.fkp.pojo.Member;
import com.fkp.pojo.Order;
import com.fkp.pojo.OrderSetting;
import com.fkp.pojo.Setmeal;
import com.fkp.service.OrderService;
import com.fkp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SetmealDao setmealDao;

    @Override
    public Result order(Map params) {
        //检查当前日期是否进行了预约设置
        String orderDate = (String) params.get("orderDate");
        Date date = null;
        try {
            date = DateUtils.parseString2Date(orderDate);
            OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
            if(orderSetting == null){
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //检查当前日期是否预约已满
            if(orderSetting.getReservations() >= orderSetting.getNumber()){
                return new Result(false,MessageConstant.ORDER_FULL);
            }
            //检查该用户是否为会员，通过手机号验证
            String telephone = (String) params.get("telephone");
            Member member = memberDao.findByTelephone(telephone);
            if(member == null){//如果不是会员则自动完成注册
                member = new Member();
                member.setName((String) params.get("name"));
                member.setPhoneNumber(telephone);
                member.setIdCard((String) params.get("idCard"));
                member.setSex((String) params.get("sex"));
                member.setRegTime(new Date());
                memberDao.add(member);
            }else { //如果是会员则需要判断是否重复提交
                Integer memberId = member.getId();
                Integer setmealId = Integer.parseInt((String) params.get("setmealId"));
                Order order = new Order(memberId,date,null,null,setmealId);
                List<Order> list = orderDao.findByCondition(order);
                if(list !=null && list.size() > 0){
                    //该用户在当前日期下已经有这个套餐的预约信息，不能重复预约
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }
            //进行预约,当天已预约人数加1
            orderSetting.setReservations(orderSetting.getReservations()+1);
            orderSettingDao.editReservationsByOrderDate(orderSetting);
            //保存预约信息到预约表
            Order order = new Order(member.getId(),date,(String) params.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String) params.get("setmealId")));
            orderDao.add(order);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"预约失败");
        }
    }

    @Override
    public Map findById(Integer id) throws Exception {
        //查询t_order表，之后通过memeber_id查询t_memeber表查出预约人，通过setmeal_id查询t_setmeal表查出检查套餐名
        List<Order> orderList = orderDao.findByCondition(new Order(id));
        Map result = new HashMap();
        if(orderList == null || orderList.size() == 0){
            throw new Exception("查询预约信息失败！");
        }
        Order order = orderList.get(0);
        result.put("orderDate",order.getOrderDate());
        result.put("orderType",order.getOrderType());
        Integer memberId = order.getMemberId();
        Integer setmealId = order.getSetmealId();
        Member member = memberDao.findById(memberId);
        Setmeal setmeal = setmealDao.findById(setmealId);
        result.put("member",member.getName());
        result.put("setmeal",setmeal.getName());
        return result;
    }

    @Override
    public Integer findCountBySetmealId(Integer id) {
        List<Order> orderList = orderDao.findByCondition(new Order(null,null,null,null,id));
        return orderList.size();
    }

    @Override
    public Map<String, Integer> findOrderReport(String day) throws Exception {
        /*
         *                     todayOrderNumber :0,
         *                     todayVisitsNumber :0,
         *                     thisWeekOrderNumber :0,
         *                     thisWeekVisitsNumber :0,
         *                     thisMonthOrderNumber :0,
         *                     thisMonthVisitsNumber :0,
         */
        Map<String, Integer> result = new HashMap<>();
        Integer orderNumber = 0;
        Integer visitNumber = 0;
        Map<String,Date> queryParams = new HashMap<>();
        queryParams.put("startDate",DateUtils.parseString2Date(day));
        queryParams.put("endDate",DateUtils.parseString2Date(day));
        List<Order> orderList = orderDao.findByOrderDateLimit(queryParams);
        if(orderList != null && orderList.size() > 0){
            for (Order order : orderList) {
                if("已到诊".equals(order.getOrderStatus())){
                    visitNumber++;
                }
                orderNumber++;
            }
        }
        result.put("todayOrderNumber",orderNumber);
        result.put("todayVisitsNumber",visitNumber);
        Date thisWeekStart = DateUtils.getThisWeekMonday(DateUtils.parseString2Date(day));
        Date thisWeekEnd = DateUtils.getSundayOfThisWeek(DateUtils.parseString2Date(day));
        queryParams.put("startDate",thisWeekStart);
        queryParams.put("endDate",thisWeekEnd);
        orderList = orderDao.findByOrderDateLimit(queryParams);
        orderNumber = 0;
        visitNumber = 0;
        if(orderList != null && orderList.size() > 0){
            for (Order order : orderList) {
                if("已到诊".equals(order.getOrderStatus())){
                    visitNumber++;
                }
                orderNumber++;
            }
        }
        result.put("thisWeekOrderNumber",orderNumber);
        result.put("thisWeekVisitsNumber",visitNumber);
        Date thisMonthStart = DateUtils.getFirstDay4ThisMonth(DateUtils.parseString2Date(day));
        Date thisMonthEnd = DateUtils.getLastDay4ThisMonth(DateUtils.parseString2Date(day));
        queryParams.put("startDate",thisMonthStart);
        queryParams.put("endDate",thisMonthEnd);
        orderList = orderDao.findByOrderDateLimit(queryParams);
        orderNumber = 0;
        visitNumber = 0;
        if(orderList != null && orderList.size() > 0){
            for (Order order : orderList) {
                if("已到诊".equals(order.getOrderStatus())){
                    visitNumber++;
                }
                orderNumber++;
            }
        }
        result.put("thisMonthOrderNumber",orderNumber);
        result.put("thisMonthVisitsNumber",visitNumber);
        return result;
    }
}
