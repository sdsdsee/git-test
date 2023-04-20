package com.eluolang.common.core.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author ZengXiaoQian
 * @createDate 2020-12-8
 */
public class ExportUtil {

    /**
     * 导出数据
     *
     * @param maps     导入的数据
     * @param pathName 导出的地址+excel表名
     * @param objClass class对象
     */
    public static void exportExcel(List maps, String pathName, Class<?> objClass) {
        //指定文件输出位置
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(pathName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ExcelWriter excelWriter = EasyExcelFactory.getWriter(outputStream);

        //将要输出的内容填充到Sheet里
        Sheet sheet = new Sheet(1, 0, (Class<? extends BaseRowModel>) objClass);

        try {
            excelWriter.write(maps, sheet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    excelWriter.finish();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 生成excel表格
     *
     * @param columsList
     * @param sheetName  sheet页的名字
     * @param savePath   excel保存的路径 D:/text.xls
     */
    public static void createExcel(List<?> columsList, String sheetName, String savePath, String[] columnNames, String[] columns) {
        FileOutputStream out = null;
        try {
            //创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建sheet
            HSSFSheet sheet = createSheet(workbook, sheetName);
            //创建单元格样式
            HSSFCellStyle style = createCellStyle(workbook);
            //创建头部
            createHead(sheet, style, columnNames);
            //创建主体内容
            createMainContent(sheet, style, columsList, columns);
            out = new FileOutputStream(savePath);
            workbook.write(out);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("======e========" + e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建sheet
     *
     * @param workbook  工作簿
     * @param sheetName sheet名字
     * @return
     */
    public static HSSFSheet createSheet(HSSFWorkbook workbook, String sheetName) {
        HSSFSheet sheet = null;
        //2创建sheet表格
        sheet = workbook.createSheet(sheetName);
        //设置所有的单元格格式
        sheet.setDefaultColumnWidth(20);
        //单独设置时必须是256的倍数
        //sheet.setColumnWidth(0,5120);  
        //sheet.setColumnWidth(1,5120);  
        //sheet.setColumnWidth(2,25600);
        return sheet;
    }

    /**
     * 创建单元格样式
     *
     * @param workbook 工作簿
     * @return
     */
    public static HSSFCellStyle createCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = null;
        //1创建文字样式
        HSSFFont font = workbook.createFont();
        //设置文字字体
        font.setFontName("宋体");
        //设置文字大小
        font.setFontHeightInPoints((short) 10);
        //2.创建单元格样式
        style = workbook.createCellStyle();
        // 左右居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框大小
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        //3.将文字样式应用到单元格中
        style.setFont(font);
        return style;
    }

    /**
     * 设置excel头部
     *
     * @param sheet
     * @param style
     */
    public static void createHead(HSSFSheet sheet, HSSFCellStyle style, String[] columnNames) {
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(style);
        }
    }

    /**
     * 创建主题内容
     *
     * @param sheet      sheet表格
     * @param style      单元格样式
     * @param columsList
     */
    public static void createMainContent(HSSFSheet sheet, HSSFCellStyle style, List<?> columsList, String[] colums) {
        /*
         * 遍历数据创建相应的行以及单元格
         * 第一行已经创建，从第二行开始创建，下标为1
         */
        for (int i = 0; i < columsList.size(); i++) {
            //创建行
            HSSFRow row = sheet.createRow(i + 1);
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(columsList.get(i)));
            for (int j = 0; j < colums.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(jsonObject.get(colums[j]).toString());
                cell.setCellStyle(style);
            }
        }
    }


}
