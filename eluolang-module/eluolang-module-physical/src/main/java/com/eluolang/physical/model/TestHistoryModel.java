package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestHistoryModel {
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
     * 评语(优秀,良,及格)
     */
    private String comment;
    /**
     * 删除标志
     */
    private Integer isDelete;
    /**
     * 监考员id
     */
    private String createByExaminer;
    /**
     * 设备编号
     */
    private String createByDevice;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 体测成绩
     */
    private String tesData;
    /**
     * 最终分数
     */
    private String score;
    /**
     * 性别:1男2女
     */
    private String userSex;
    /**
     * 年级
     */
    private String grade;
    /**
     * 部门id
     */
    private String orgId;
    /**
     * 测试项目
     */
    private int testProject;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 单位(cm,m,s)
     */
    private String proUnit;
    /**
     * 项目名称简写
     */
    private String proNameAbbreviation;
    /**
     * 最终成绩
     */
    private String data;
}
