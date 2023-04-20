package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindScoreConditionVo {
    private String id;
    /**
     * 人员id
     */
    private String userId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 监考员id
     */
    private String createByExaminer;
    /**
     * 设备编号
     */
    private String createByDevice;

    /**
     * 性别:1男2女
     */
    private String userSex;
    /**
     * 年级
     */
    private String userGrade;
    /**
     * 部门id
     */
    private String userOrgId;
    /**
     * 测试项目
     */
    private String testProject;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 最终成绩
     */
    private String data;
    /**
     * 最终分数
     */
    private String score;
    /**
     * 页数
     */
    private int page;
    /**
     * 计划类型
     */
    private int isExam;
    /**
     * 学生是否完成
     */
    private int finish;
    /**
     * 部门id
     */
    private String orgId;
    /**
     * 学生姓名
     */
    private String userName;
    /**
     * 账号的id
     */
    private int accountOrgId;
    /**
     * 成绩的测试时间
     */
    private String day;
}
