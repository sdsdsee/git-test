package com.eluolang.physical.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllAvoidExportDto {
    @ExcelIgnore
    private String id;
    @ExcelIgnore
    private String userId;
    @ExcelIgnore
    private String orgId;
    @ExcelProperty("姓名")
    private String userName;
    @ExcelProperty("班级名称")
    private String className;
    @ExcelProperty("学校名称")
    private String schoolName;
    @ExcelProperty("学籍号")
    private String studentCode;
    @ExcelProperty("学号")
    private String studentId;
    @ExcelProperty("理由")
    private String reason;
    @ExcelProperty("项目名称")
    private String proName;
    @ExcelProperty("申请时间")
    private String time;
    @ExcelProperty("状态")
    private String state;
}
