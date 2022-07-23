package com.fkp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fkp.dao.MemberDao;
import com.fkp.pojo.Member;
import com.fkp.service.MemberService;
import com.fkp.utils.DateUtils;
import com.fkp.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if(password != null){
            //使用MD5将明文进行加密
            member.setPassword(MD5Utils.md5(password));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        Calendar calendar = Calendar.getInstance();
        List<Integer> list = new ArrayList<>();
        for (String month : months) {
            String year = month.split("\\.")[0];
            String monthOfYear = month.split("\\.")[1];
            calendar.set(Calendar.YEAR,Integer.parseInt(year));
            calendar.set(Calendar.MONTH,Integer.parseInt(monthOfYear)-1);
            int actualMaximum = calendar.getActualMaximum(Calendar.DATE);
            month += "."+actualMaximum;
            Integer count = memberDao.findMemberCountBeforeDate(month);
            list.add(count);
        }
        return list;
    }

    @Override
    public Map<String, Integer> findMemberReport(String day) throws Exception {
        /**     {
         *         todayNewMember :0,
         *         totalMember :0,
         *         thisWeekNewMember :0,
         *         thisMonthNewMember :0
         *       }
         */
        Map<String, Integer> result = new HashMap<>();
        Map<String,Date> queryParams = new HashMap<>();
        queryParams.put("startDate",DateUtils.parseString2Date(day));
        queryParams.put("endDate",DateUtils.parseString2Date(day));
        Integer todayNewMember = memberDao.findMemberCountByDateLimit(queryParams);
        result.put("todayNewMember",todayNewMember);
        Integer totalMember = memberDao.findMemberCountAll();
        result.put("totalMember",totalMember);
        Date thisWeekStart = DateUtils.getThisWeekMonday(DateUtils.parseString2Date(day));
        Date thisWeekEnd = DateUtils.getSundayOfThisWeek(DateUtils.parseString2Date(day));
        queryParams.put("startDate",thisWeekStart);
        queryParams.put("endDate",thisWeekEnd);
        Integer thisWeekNewMember = memberDao.findMemberCountByDateLimit(queryParams);
        result.put("thisWeekNewMember",thisWeekNewMember);
        Date thisMonthStart = DateUtils.getFirstDay4ThisMonth(DateUtils.parseString2Date(day));
        Date thisMonthEnd = DateUtils.getLastDay4ThisMonth(DateUtils.parseString2Date(day));
        queryParams.put("startDate",thisMonthStart);
        queryParams.put("endDate",thisMonthEnd);
        Integer thisMonthNewMember = memberDao.findMemberCountByDateLimit(queryParams);
        result.put("thisMonthNewMember",thisMonthNewMember);
        return result;
    }
}
