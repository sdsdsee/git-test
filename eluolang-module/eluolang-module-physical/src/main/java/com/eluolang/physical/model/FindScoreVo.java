package com.eluolang.physical.model;

import com.eluolang.common.core.pojo.EllTestData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//Serializable序列化
public class FindScoreVo implements Serializable {
    /**
     * 班级编号
     */
    private String sig;
    /**
     * 学号
     */

    private String studentId;
    /**
     * 家庭地址
     */
    private String homeAddress;
    /**
     * 用户编号
     */
    private String studentCode;
    /**
     * 名族
     */
    private String ethnic;
    /**
     * 年级
     */
    private String userGrade;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 班级名称
     */
    private String deptName;
    /**
     * 班级备注
     */
    private String classRemark;
    /**
     * 入学时间
     */

    private String enTime;
    /**
     * 入学年级
     */
    private int enGrade;
    private String userName;
    //
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
     * 最终成绩
     */
    private String data;
    /**
     * 是否重测成绩
     */
    private int isRetest;
    /**
     * 是否免测
     */
    private int isFreeTest;
    /**
     * 考试成绩json
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)//为null或者‘’ 不返回给前端
    private String tesData;
    /**
     * 历史考试成绩
     */
    private List<EllHistoricalPerformanceVo> historyData;
    /**
     * 出生日期
     */
    private String userBirth;
    /**
     * 生源地代码
     */
    private String sourceCode;
    /**
     * t拓展数据
     */
    private String extraData;
}
