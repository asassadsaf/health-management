package com.fkp.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

public class POITest {

//    //使用POI读取Excel文件中的数据，增强for循环
//    @Test
//    public void test1() throws IOException {
//        //加载指定文件，创建一个Excel对象（工作簿）
//        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("src/main/file/poi.xlsx")));
//        //读取Excel文件中的第一个Sheet标签页
//        XSSFSheet sheet = excel.getSheetAt(0);
//        //遍历Sheet标签页，获得每一行数据
//        for (Row row : sheet) {
//            //遍历每一行，获得每个单元格数据
//            for (Cell cell : row) {
//                System.out.println(cell.getStringCellValue());
//            }
//        }
//        //关闭资源
//        excel.close();
//    }
//
//    //使用POI读取Excel文件中的数据，普通for循环
//    @Test
//    public void test2() throws IOException {
//        //加载指定文件，创建一个Excel对象（工作簿）
//        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("src/main/file/poi.xlsx")));
//        //读取Excel文件中的第一个Sheet标签页
//        XSSFSheet sheet = excel.getSheetAt(0);
//        //获得最后一行的行号，从0开始
//        int lastRowNum = sheet.getLastRowNum();
//        for(int i=0;i<=lastRowNum;i++){
//            //通过索引获得行
//            XSSFRow row = sheet.getRow(i);
//            //获取当前行最后一个单元格的索引，从1开始
//            short lastCellNum = row.getLastCellNum();
//            for(int j=0;j<lastCellNum;j++){
//                //通过索引获得单元格
//                XSSFCell cell = row.getCell(j);
//                System.out.println(cell.getStringCellValue());
//            }
//        }
//        //关闭资源
//        excel.close();
//    }
//
//    //使用POI创建Excel文件并写入数据
//    @Test
//    public void test3() throws IOException {
//        //在内存中创建一个Excel文件（工作簿）
//        XSSFWorkbook excel = new XSSFWorkbook();
//        //创建一个工作表对象
//        XSSFSheet sheet = excel.createSheet("sheet1");
//        //在工作表中创建行对象
//        XSSFRow title = sheet.createRow(0);
//        //在行中创建单元格对象并写入数据
//        title.createCell(0).setCellValue("姓名");
//        title.createCell(1).setCellValue("地址");
//        title.createCell(2).setCellValue("年龄");
//        XSSFRow row1 = sheet.createRow(1);
//        row1.createCell(0).setCellValue("zhangsan");
//        row1.createCell(1).setCellValue("北京");
//        row1.createCell(2).setCellValue("21");
//        XSSFRow row2 = sheet.createRow(2);
//        row2.createCell(0).setCellValue("lisi");
//        row2.createCell(1).setCellValue("南京");
//        row2.createCell(2).setCellValue("22");
//
//        //创建一个输出流，通过输出流将内存中的Excel文件写到磁盘
//        FileOutputStream out = new FileOutputStream(new File("src/main/file/hello.xlsx"));
//        //将内存中的Excel文件写到输出流
//        excel.write(out);
//        out.flush();
//        excel.close();
//        out.close();
//    }
}
