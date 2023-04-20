package com.eluolang.app.manage.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.directory.SearchResult;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllTestHistoryVo implements Serializable {
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
     * 体测成绩json
     */
    //返回前端不返回这个属性
    @JsonIgnore
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
    private String proName;
    /**
     * 最终成绩
     */
    private String data;
    /**
     * 是否重测成绩
     */
    private int isRetest;
    /**
     * 项目简介
     */
    private String synopsis;
    /**
     * 每次成绩
     */
    List<EllAppHistoryVo> ellAppHistoryVoList;

}
