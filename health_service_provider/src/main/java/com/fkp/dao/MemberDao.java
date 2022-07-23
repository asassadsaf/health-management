package com.fkp.dao;

import com.fkp.pojo.Member;

import java.util.Date;
import java.util.Map;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    Member findById(Integer memberId);

    Integer findMemberCountBeforeDate(String month);

    /**
     *
     * @param queryParams
     *      Map -> key = "startDate"  && key = "endDate"
     * @return
     */
    Integer findMemberCountByDateLimit(Map<String, Date> queryParams);

    Integer findMemberCountAll();
}
