package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRules implements Serializable {
    /**
     * id
     */
    private int id;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年级
     */
    private String grade;
    /**
     * 项目id
     */
    private int projectId;
    /**
     * 规则
     */
    private String rule;
    /**
     * 分数
     */
    private int ruleScore;
    /**
     * 删除标识
     */
    private int isDelete;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 创建的部门id(为空及为通用)
     */
    private String orgId;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 评语(良，优秀，及格)
     */
    private String remark;
    /**
     * 属于那套规则
     */
    private String parentId;

}
