package com.eluolang.physical.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllReturnScoreVo {
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
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
     * 所属部门名称
     */
    private String orgName;
    /**
     * 出生日期
     */

    private String userBirth;
    /**
     * 学号
     */

    private String studentId;
    /**
     * 家庭地址
     */

    private String homeAddress;
    /**
     * 入学时间
     */

    private String enTime;
    /**
     * 入学年级
     */
    private int enGrade;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 名族
     */
    private String ethnic;
    /**
     * 微信的openId
     */
    private String wxOpenId;
    /**
     * 部门ID
     */
    private String orgId;
    /**
     * 是否已认证
     */
    private String isAuthentication;
    /**
     * 图片nginx地址
     */
    private String fileUrl;
    /**
     * 年级
     */
    private String userGrade;
    /**
     * 成绩详情
     */
    private List<FindScoreVo> score;
    /**
     * 是否测试完成
     */
    private int isAccomplish;
    /**
     * 计划最终成绩
     */
    private String endScore;
    /**
     * faceInfo
     */
    private String faceInfo;
    /**
     * 学生是否核实成绩
     */
    private int verify;
    /**
     * 用户编号
     */
    private String studentCode;
    /**
     * 班级编号
     */
    private String sig;
    /**
     * 班级名称
     */
    private String deptName;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 班级备注
     */
    private String classRemark;
    /**
     * 考核状态
     */
    private String AppraisalStatus;

    /**
     * 生源地代码
     */
    private String sourceCode;
}
