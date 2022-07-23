package com.fkp.service;

import com.fkp.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonths(List<String> months);

    Map<String, Integer> findMemberReport(String day) throws Exception;
}
