package com.eluolang.common.core.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUser implements Serializable {
    private String id;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 性别:1男2女
     */
    private Integer userSex;
    /**
     * 密码
     */
    private String userPwd;
    /**
     * 微信的openId
     */
    private String wxOpenId;
    /**
     * 所属部门标识
     */
    private String orgId;
    /**
     * 删除标识0未删除1已删除
     */
    private String isDelete;
    /**
     * 出生日期
     */
    private String userBirth;
    /**
     * 学号
     */
    private String studentId;
    /**
     * 学籍号
     */
    private String studentCode;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 家庭地址
     */
    private String homeAddress;
    /**
     * 创建人ID
     */
    private String createBy;
    /**
     * 创建时间
     */
    private String createTime;
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
     * 是否已认证
     */
    private String isAuthentication;
    /**
     * 入学年级
     */
    private String enGrade;
    /***
     * 生源地代码
     */
    private String sourceCode;
    /**
     * 小程序身高
     */
    private String appHeight;
    /**
     * 小程序体重
     */
    private String appWeight;
}
