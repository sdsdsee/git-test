package com.eluolang.physical.model;

import com.eluolang.common.core.pojo.EllTestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllHistoryVo {
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
     * 体测成绩
     */
    private Map<String, List<EllTestData>> testData;
    /**
     * 体测成绩转为json串存入数据库
     */
    private String testDataJson;
    /**
     * 分数
     */
    private String score;
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
     * 检录时间戳
     */
    private Long checkInTimestamp;
    /**
     * 结束时间蹉
     */
    private Long endTimestamp;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 是否重测成绩
     */
    private int isRetest;
    /**
     * 拓展数据
     */
    private String extraData;
    /**
     * 用时
     */
    private Long timeSpent;
    //智慧课堂
    /**
     * 课堂id
     */
    private String courseId;
    /**
     * 模式1.锻炼模式2.课程模式3.体考体测
     */
    private int mode;
    /**
     * 老师id
     */
    private String teacherId;
    /**
     * 最好成绩key
     */
    private String bestGradeKey;
}
