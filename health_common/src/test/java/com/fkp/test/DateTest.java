package com.fkp.test;

import com.fkp.utils.DateUtils;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTest {
    public static void main(String[] args) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,2021);
        cal.set(Calendar.MONTH,9);
        //cal.set(Calendar.DAY_OF_MONTH,9);
        int actualMaximum = cal.getActualMaximum(Calendar.DATE);
        String date = new SimpleDateFormat("yyyy-MM").format(cal.getTime());
        System.out.println(actualMaximum);
        System.out.println(date);


    }
    @Test
    public void test(){
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String month = day.substring(0,7);
        System.out.println(day);
        System.out.println(month);
    }

    @Test
    public void test2() throws Exception {
        String date = "2021-10-17";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        calendar.set(Calendar.DAY_OF_WEEK,2);
        //获取当前日期所在周的第一天的日期
        Date weekStart = calendar.getTime();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(weekStart));
        calendar.set(Calendar.WEEK_OF_YEAR,calendar.get(Calendar.WEEK_OF_YEAR)+1);
        calendar.set(Calendar.DAY_OF_WEEK,1);
        //获取当前日期所在周的最后一天的日期
        Date weekEnd = calendar.getTime();
        //解决跨年周的问题
        if (weekEnd.before(weekStart)){
            calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)+1);
            calendar.set(Calendar.MONTH,0);
            calendar.set(Calendar.DAY_OF_WEEK,1);
            weekEnd = calendar.getTime();
        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(weekEnd));
    }

    @Test
    public void test3() throws Exception {
        String date = "2021-12-31";
        Date firstDayOfWeek = DateUtils.getThisWeekMonday(DateUtils.parseString2Date(date));
        Date lastDayOfWeek = DateUtils.getSundayOfThisWeek(DateUtils.parseString2Date(date));
        System.out.println(DateUtils.parseDate2String(firstDayOfWeek));
        System.out.println(DateUtils.parseDate2String(lastDayOfWeek));
    }

    @Test
    public void test4() throws Exception {
        String date = "2021-12-31";
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth(DateUtils.parseString2Date(date));
        Date lastDay4ThisMonth = DateUtils.getLastDay4ThisMonth(DateUtils.parseString2Date(date));
        System.out.println(DateUtils.parseDate2String(firstDay4ThisMonth));
        System.out.println(DateUtils.parseDate2String(lastDay4ThisMonth));
    }
}
