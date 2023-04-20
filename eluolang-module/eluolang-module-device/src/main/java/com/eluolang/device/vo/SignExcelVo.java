package com.eluolang.device.vo;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年07月29日 10:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignExcelVo {
    /** 学生姓名 */
    @ExcelProperty("姓名")
    private String username;
    /** 学校id */
    @ExcelIgnore
    private Integer schoolId;
    /** 学校名称 */
    @ExcelProperty("学校")
    private String schoolName;
    /** 入学时间 */
    @ExcelIgnore
    private String enTime;
    /** 入学年级 */
    @ExcelIgnore
    private Integer enGrade;
    /** 年级 */
    @ExcelProperty("年级编码")
    private Integer grade;
    /** 班级id */
    @ExcelIgnore
    private Integer classId;
    /** 班级名称 */
    @ExcelProperty("班级")
    private String className;
    /** 班级路径 */
    @ExcelIgnore
    private String classPath;
    /** 学生id */
    @ExcelProperty("教育ID")
    private String studentId;
    /** 计划名称 */
    @ExcelProperty("计划名称")
    private String planName;
    /** 签到时间 */
    @ExcelProperty("检录时间")
    private String signTime;
}
