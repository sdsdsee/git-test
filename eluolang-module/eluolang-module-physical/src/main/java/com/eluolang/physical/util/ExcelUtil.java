package com.eluolang.physical.util;


import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author zjl
 */

public class ExcelUtil {
    //考试excel表格生成
    //contentItem每个单元格内容用逗号隔开
    public static String createExcel(List<String> headTables, List<String> contentItem) throws FileNotFoundException {

        // 创建一个表格
        Table table = new Table(1);
        // 动态添加 表头 headList --> 所有表头行集合
        List<List<String>> headList = new ArrayList<List<String>>();
        // 内容集合
        List<List<Object>> list = new ArrayList<List<Object>>();
        //表头
        //每一行的每一个单元格为一个list
        for (int i = 0; i < headTables.size(); i++) {
            List<String> headTable = new ArrayList<>();
            headTable.add(headTables.get(i));
            headList.add(headTable);
            table.setHead(headList);
        }
        //每一行的每一个单元格为一个list
        if (contentItem != null && contentItem.size() > 0) {
            for (int i = 0; i < contentItem.size(); i++) {
                List<Object> row = new ArrayList<>();
                String[] item = contentItem.get(i).split(",");
                for (int j = 0; j < item.length; j++) {
                    if (item[j] == null) {
                        row.add("");
                    } else {
                        row.add(item[j]);
                    }
                }
                list.add(row);
            }
        }
        String fileName = new Date().getTime() + IdUtils.fastSimpleUUID() + ".xlsx";
        String path = "";
        String url = "";
        if (FileUploadUtil.isLinux() == false) {
            path = WindowsSite.Path;
            url = WindowsSite.FILE_URL_EXCEL + fileName;
        } else {
            path = LinuxSite.Path;
            url = LinuxSite.FILE_URL_EXCEL + fileName;
        }
        //生成文件路径
        PathGeneration.createPath(path + fileName);
        ExcelWriter excelWriter = EasyExcelFactory.getWriter(new FileOutputStream(path + fileName));

        // 表单
        Sheet sheet = new Sheet(1, 0);
        sheet.setSheetName("模板");
        excelWriter.write(list, sheet, table);
        // 记得 释放资源
        excelWriter.finish();
        System.out.println("ok");
        return url;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> headTables = new ArrayList<>();
        List<String> contentItem = new ArrayList<>();
        headTables.add("姓名");
        headTables.add("性别");
        headTables.add("班级");
        headTables.add("总分");
        for (int i = 0; i < 3; i++) {
            headTables.add("项目");
            headTables.add("成绩");
            headTables.add("分数");
            contentItem.add("赵京龙,男,一班,32.6,1000米跑,3'50,12.4,一分钟跳绳,155,6.72,排球,47,5");
            contentItem.add("赵京龙2,男,二班,32.6,800米跑,3'50,12.4,仰卧起坐,,,实心球,47,5");
        }
        new ExcelUtil().createExcel(headTables, contentItem);
    }
}
