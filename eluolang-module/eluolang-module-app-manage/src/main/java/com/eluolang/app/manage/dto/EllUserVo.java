package com.eluolang.app.manage.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUserVo implements Serializable {
    /**
     * userId(用户Id)
     */
    private String id;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 微信的openId
     */
    private String wxOpenId;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 学号
     */
    private String studentId;
    /**
     * 学籍号
     */
    private String studentCode;
    /**
     * 家庭地址
     */
    private String homeAddress;
    /**
     * 入学时间
     */
    private String enTime;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 名族
     */
    private String ethnic;
    /**
     * 入学年级
     */
    private String enGrade;
    /**
     * 头像地址
     */
    private String imageUrl;
    /**
     * 非对称加密的身份证号
     */
    private String idCard;
    /**
     * 角色1.本人2.父亲3.母亲4游客
     */
    private int userRole;
    /**
     * 生源地代码
     */
    private String sourceCode;
    /**
     * 小程序身高
     */
    private String appHeight;
    /**
     *小程序体重
     */
    private String appWeight;
    /**
     * 部门id
     */
    private String orgId;
}