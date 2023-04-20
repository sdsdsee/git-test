package com.eluolang.physical.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String id;
    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String userName;
    /**
     * 性别:1男2女
     */
    @ExcelProperty("性别")
    private Integer userSex;
    /**
     * 所属部门标识
     */
    @ExcelProperty("所属部门")
    private String deptName;
    /**
     * 出生日期
     */
    @ExcelProperty("出生日期")
    private String userBirth;
    /**
     * 学号
     */
    @ExcelProperty("学号")
    private String studentId;
    /**
     * 家庭地址
     */
    @ExcelProperty("家庭地址")
    private String homeAddress;
    /**
     * 入学时间
     */
    @ExcelProperty("入学时间")
    private String enTime;
    /**
     * 电话号
     */
    @ExcelIgnore
    private String phone;
    /**
     * 名族
     */
    @ExcelProperty("民族")
    private String name;
    /**
     * 微信的openId是否注册空为否
     */
    @ExcelIgnore
    private String isWxReg;
    /**
     * 部门ID
     */
    @ExcelIgnore
    private String orgId;
    /**
     * 是否已认证
     */
    @ExcelIgnore
    private String isAuthentication;
    /**
     * 图片nginx地址
     */
    @ExcelIgnore
    private String fileUrl;
    /**
     * 入学年级
     */
    @ExcelIgnore
    private String enGrade;
    /**
     * 学籍号
     */
    @ExcelIgnore
    private String studentCode;
    /**
     * 身份证
     */
    @ExcelProperty("身份证")
    private String idCard;
}
