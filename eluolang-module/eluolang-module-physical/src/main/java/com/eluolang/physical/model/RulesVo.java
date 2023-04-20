package com.eluolang.physical.model;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RulesVo {
    /**
     * id 不能修改
     */
    @ApiParam(hidden = true)
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
    private String ruleScore;

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
     * 父类id
     */
    private int parentId;
    /**
     * 修改人id
     */
    private String createById;
    /**
     * 修改组的id
     */
    private String updateByCroupId;
}
