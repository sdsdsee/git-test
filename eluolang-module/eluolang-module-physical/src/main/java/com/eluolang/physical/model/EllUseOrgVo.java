package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUseOrgVo {
    /**
     * 计划id
     */
    private String planId;
    /**
     * 使用部门id
     */
    private Integer orgId;
    /**
     * 使用部门名称
     */
    private String orgName;
    /**
     * 使用的规则在计划使用的规则中选择
     */
    private String useRule;
    /**
     *
     */
    private int id;
    /**
     * 创建人id
     */
    private String createId;
    /**
     * 部门父级id
     */
    private String parentId;
    /**
     * 是否选择的这一级
     */
    private int isThis;
}
