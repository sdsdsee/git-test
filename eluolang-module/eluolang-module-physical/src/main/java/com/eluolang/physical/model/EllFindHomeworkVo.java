package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 *查询成绩完成
 */
public class EllFindHomeworkVo {
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户id
     */
    private String userId;
    /**
     * homeworkId
     */
    private String homeworkId;
    /**
     * 跳绳时间
     */
    private int time;
    /**
     * 跳绳个数
     */
    private int num;
    /**
     * 、
     * 班级名称
     */
    private String className;
    /**
     * 班级id
     */
    private Integer orgId;
    /**
     * 完成度
     */
    private int state;
    /**
     * 作业设置参数
     */
    private int workSetting;
    /**
     * 作业模式
     */
    private int workPattern;
    /**
     * page
     */
    private int page;
}

