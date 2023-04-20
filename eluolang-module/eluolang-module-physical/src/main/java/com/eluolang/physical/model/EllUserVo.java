package com.eluolang.physical.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.Style;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUserVo {
    /**
     * 名族
     */
    @ExcelProperty("民族")
    private String ethnic;
    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String userName;
    /**
     * 学号
     */
    @ExcelProperty("学号")
    private String studentId;
    /**
     * 身份证号
     */
    @ExcelProperty("身份证号")
    private String idCard;
    /**
     * 家庭地址
     */
    @ExcelProperty("家庭地址")
    private String homeAddress;
    /**
     * 创建人ID
     */
    @ExcelIgnore
    private String createBy;
    /**
     * 入学时间
     */
    @DateTimeFormat("YYYY-MM")
    @ExcelProperty("入学时间(格式：2018-09)")
    private String enTime;
    /**
     * 入学年级
     */
    @ExcelProperty("入学年级(换算成数字)")
    private String enGrade;
    /**
     * 入学班级
     */
    @ExcelProperty("所属班级(输入班级ID)")
    private String orgId;
    /**
     * 电话号
     */
    @ExcelIgnore
    private String phone;
    /**
     * 学籍号
     */
    @ExcelProperty("学籍号")
    private String studentCode;
    /**
     * 生源地代码
     */
    @ExcelProperty("生源地代码")
    private String sourceCode;
    /**
     * 生源地代码
     */
    @ExcelProperty("性别")
    private Integer sex;
}
