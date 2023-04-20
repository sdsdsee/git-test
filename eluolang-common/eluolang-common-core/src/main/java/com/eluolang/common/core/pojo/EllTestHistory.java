package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.directory.SearchResult;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllTestHistory implements Serializable {
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
    private int updateBy;
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
    private int userSex;
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
     * 拓展数据
     */
    private String extraData;
    /**
     * 用时
     */
    private Long timeSpent;
    /**
     * 检录时间戳
     */
    private Long checkInTimestamp;
    /**
     * 结束时间蹉
     */
    private Long endTimestamp;
}
