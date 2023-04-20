package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveOtherRulesVo {
    /**
     * 性别
     */
    private String sex;
    /**
     * 年级
     */
    private String grade;
    /**
     * 项目id 不能修改
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
     * 项目名称 不能修改
     */
    private String projectName;

    /**
     * 创建的部门id(为空及为通用) 不能修改
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
     * 测试机会
     */
    private String chance;
    /**
     * 父类id
     */
    private String parentId;
}
