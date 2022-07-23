package com.fkp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fkp.constant.MessageConstant;
import com.fkp.entity.Result;
import com.fkp.pojo.Setmeal;
import com.fkp.service.MemberService;
import com.fkp.service.OrderService;
import com.fkp.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private OrderService orderService;

    @Reference
    private SetmealService setmealService;


    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Map<String,Object> map = new HashMap<>();
        //计算过去一年的月份
        Calendar calendar = Calendar.getInstance();
        //获取当前时间往前推12个月
        calendar.add(Calendar.MONTH,-12);
        List<String> months = new ArrayList<>();
        for(int i=0;i<12;i++){
            //从calendar锁定的时间往后推一个月
            calendar.add(Calendar.MONTH,1);
            months.add(new SimpleDateFormat("YYYY.MM").format(calendar.getTime()));
        }
        map.put("months",months);
        try {
            List<Integer> memberCount = memberService.findMemberCountByMonths(months);
            map.put("memberCount",memberCount);
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        Map result = new HashMap();
        try {
            //查询所有的套餐名称
            List<Setmeal> setmealList = setmealService.findAll();
            List<String> setmealNames = new ArrayList<>();
            List<Map<String, Object>> setmealCount = new ArrayList<>();
            if(setmealList != null && setmealList.size() > 0){
                for (Setmeal setmeal : setmealList) {
                    setmealNames.add(setmeal.getName());
                    Integer value = orderService.findCountBySetmealId(setmeal.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("name",setmeal.getName());
                    map.put("value",value);
                    setmealCount.add(map);
                }
            }
            //通过套餐名称查询预约表查出当前套餐的预约数
            result.put("setmealNames",setmealNames);
            result.put("setmealCount",setmealCount);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,result);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        /**
         * reportData:{
         *                     reportDate:null,
         *
         *                     todayNewMember :0,
         *                     totalMember :0,
         *                     thisWeekNewMember :0,
         *                     thisMonthNewMember :0,
         *
         *                     todayOrderNumber :0,
         *                     todayVisitsNumber :0,
         *                     thisWeekOrderNumber :0,
         *                     thisWeekVisitsNumber :0,
         *                     thisMonthOrderNumber :0,
         *                     thisMonthVisitsNumber :0,
         *
         *                     hotSetmeal :[
         *                         {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
         *                         {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
         *                     ]
         *                 }
         */
        Map result = new HashMap();
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String month = day.substring(0,7);
        result.put("reportDate",month);
        try {
            Map<String, Integer> memberReport = memberService.findMemberReport(day);
            result.putAll(memberReport);
            Map<String, Integer> orderReport = orderService.findOrderReport(day);
            result.putAll(orderReport);
            Result setmealResult = this.getSetmealReport();
            Map setmealMap = (Map) setmealResult.getData();
            List<Map<String, Object>> setmealList = (List<Map<String, Object>>) setmealMap.get("setmealCount");
            Collections.sort(setmealList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Integer o1_value = (Integer) o1.get("value");
                    Integer o2_value = (Integer) o2.get("value");
                    return -o1_value.compareTo(o2_value);
                }
            });
            List<Map<String,Object>> hotSetmeal = new ArrayList<>();
            Double total = 0.0;
            for (Map<String, Object> map : setmealList) {
                Integer setmeal_count = (Integer) map.get("value");
                total += setmeal_count;
            }
            for(int i=0;i<4;i++){
                Map<String, Object> map = setmealList.get(i);
                map.put("setmeal_count",map.get("value"));
                map.put("proportion",(Integer) map.get("setmeal_count")/total);
                map.remove("value");
                hotSetmeal.add(map);
            }
            result.put("hotSetmeal",hotSetmeal);
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request,HttpServletResponse response){
        try {
            Result resultDate = this.getBusinessReportData();
            Map result = (Map) resultDate.getData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //File.separator等于一个分隔符，windows下为\\,linux下为/
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            XSSFSheet sheet = excel.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            XSSFCell cell = row.getCell(5);
            cell.setCellValue(reportDate);

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Integer setmeal_count = (Integer) map.get("setmeal_count");
                Double proportion = (Double) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //使用输出流进行表格下载
            ServletOutputStream out = response.getOutputStream();
            //设置响应文件类型是excel类型
            response.setContentType("application/vnd.ms-excel");
            //设置响应头信息，以附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            excel.write(out);
            out.flush();
            out.close();
            excel.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request,HttpServletResponse response){
        try {
            Result resultDate = this.getBusinessReportData();
            Map result = (Map) resultDate.getData();
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            result.remove("hotSetmeal");
            //File.separator等于一个分隔符，windows下为\\,linux下为/
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";
            //模板编译，编译为.jasper的二进制文件
            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);
            //通过map中的值和查询数据库填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, result, new JRBeanCollectionDataSource(hotSetmeal));
            //使用输出流进行表格下载
            ServletOutputStream out = response.getOutputStream();
            //设置响应文件类型是excel类型
            response.setContentType("application/pdf");
            //设置响应头信息，以附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
