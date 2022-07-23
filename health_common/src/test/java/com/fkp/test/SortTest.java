package com.fkp.test;

import org.junit.Test;

import java.util.*;

public class SortTest {
    @Test
    public void test(){
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","aa");
        map1.put("value",20);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","bb");
        map2.put("value",30);
        Map<String,Object> map3 = new HashMap<>();
        map3.put("name","cc");
        map3.put("value",40);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer o1_value = (Integer) o1.get("value");
                Integer o2_value = (Integer) o2.get("value");
                return -o1_value.compareTo(o2_value);
            }
        });
        for (Map<String, Object> map : list) {
            System.out.println(map.get("value"));
        }
    }
}
