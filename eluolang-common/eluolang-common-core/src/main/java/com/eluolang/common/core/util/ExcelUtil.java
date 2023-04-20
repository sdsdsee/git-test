package com.eluolang.common.core.util;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.eluolang.common.core.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Create By : SZY
 * Time : 2019/6/28 16:35 <br>
 * excel 模板导出工具类，结合 OSSObjectUtils 和 ExcelExportUtil 一起，获得模板到temp，然后写入内容到模板，并输出到excel
 */
public class ExcelUtil {


    /**
     * 根据不同的浏览器处理文件名，防止中文文件名乱码
     *
     * @param filePrefix  前缀
     * @param templateUrl 模板链接
     * @param fileSuffix  后缀
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String handlerFileName(String filePrefix, String templateUrl, String fileSuffix) throws UnsupportedEncodingException {
        String fileName = null;
        //前缀
        if (StringUtils.isNotBlank(filePrefix)) {
            fileName = new String(filePrefix.getBytes(), "UTF-8");
        } else {
            String templateName = templateUrl.substring(templateUrl.lastIndexOf("/") + 1, templateUrl.lastIndexOf(".xlsx"));
            fileName = new String(templateName.getBytes(), "UTF-8");
        }
        //后缀
        if (StringUtils.isNotBlank(fileSuffix)) {
            fileName = fileName + "-" + fileSuffix;
        }
        HttpServletRequest request = ServletUtils.getRequest();
        String userAgent = request.getHeader("USER-AGENT");
        if (userAgent.contains("Edge") || userAgent.contains("MSIE")) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF8"), "ISO8859-1");
        }
        fileName = fileName + ".xlsx";
        return fileName;
    }

    public static boolean isLinux(){
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    /**
     * 导入解析excel
     *
     * @param file
     * @param titleRows
     * @param headerRows
     * @param pojoClass
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerfiy(true);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new BaseException("excel文件不能为空");
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
        return list;
    }

    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerfiy(true);
        try {
            ExcelImportResult<T> result = ExcelImportUtil.importExcelMore(file.getInputStream(), pojoClass, params);
            return result;
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    /**
     * @param title    表格标题名
     * @param headers  表格属性列名数组 （第一行标题）
     * @param failList 需要显示的导入不成功数据集合
     */
    public static XSSFWorkbook exportExcel(String title, String[] headers, String[] remarks, List<String[]> failList, List<String[]> succList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式
        XSSFCellStyle failStyle = workbook.createCellStyle();
        // 设置这些样式
        failStyle.setFillForegroundColor(new XSSFColor(Color.RED));
        failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        failStyle.setBorderBottom(BorderStyle.THIN);
        failStyle.setBorderLeft(BorderStyle.THIN);
        failStyle.setBorderRight(BorderStyle.THIN);
        failStyle.setBorderTop(BorderStyle.THIN);

        XSSFCellStyle succStyle = workbook.createCellStyle();
        // 设置这些样式
        succStyle.setFillForegroundColor(new XSSFColor(Color.WHITE));
        succStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        succStyle.setBorderBottom(BorderStyle.THIN);
        succStyle.setBorderLeft(BorderStyle.THIN);
        succStyle.setBorderRight(BorderStyle.THIN);
        succStyle.setBorderTop(BorderStyle.THIN);
        // 生成一个字体

        // 生成并设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(new XSSFColor(Color.BLUE));
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);

        // 产生表格备注行excel第一行
        XSSFRow remarkRow = sheet.createRow(0);

        for (short i = 0; i < remarks.length; i++) {
            XSSFCell remarkCell = remarkRow.createCell(i);
            remarkCell.setCellStyle(style2);
            XSSFRichTextString text = new XSSFRichTextString(remarks[i]);
            remarkCell.setCellValue(text);
        }
        // 产生表格标题行
        XSSFRow row = sheet.createRow(1);

        for (short i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellStyle(style2);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        int failSize = 0;
        // 遍历集合数据，产生数据行
        if (failList != null && failList.size() > 0) {
            for (int fail = 0; fail < failList.size(); fail++) {
                XSSFRow temprow = sheet.createRow(fail + 1);
                String[] list1 = failList.get(fail);
                for (int k = 0; k < list1.length; k++) {
                    XSSFCell tempCell = temprow.createCell(k);
                    tempCell.setCellStyle(failStyle);
                    tempCell.setCellValue(list1[k]);
                }
            }
            failSize = failList.size();
        }


        if (succList != null && succList.size() > 0) {
            for (int succ = 0; succ < succList.size(); succ++) {

                XSSFRow temprow = sheet.createRow(succ + failSize + 1);
                String[] list1 = succList.get(succ);
                for (int j = 0; j < list1.length; j++) {
                    XSSFCell tempCell = temprow.createCell(j);
                    tempCell.setCellStyle(succStyle);
                    tempCell.setCellValue(list1[j]);
                }
            }
        }

        return workbook;
    }

}
