package com.eluolang.physical.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserVo {
    /**
     * id
     */
    private String id;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 所属部门标识
     */
    private String orgId;
    /**
     * 家庭地址
     */
    private String homeAddress;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 出生日期
     */
    private String userBirth;
    /**
     * 民族
     */
    private String ethnic;
    /**
     * 是否已认证
     */
    @ExcelIgnore
    private String isAuthentication;
    /**
     * 入学年级
     */
    private String enGrade;
    /**
     * 入学时间
     */
    private String enTime;
    /**
     * 学生id
     */
    private String studentId;
    /**
     * 学籍号
     */
    private String studentCode;
    /**
     * 生源地代码
     */
    private String sourceCode;
    /**
     * 身高
     */
    private String appHeight;
    /**
     *体重
     */
    private String appWeight;
}
