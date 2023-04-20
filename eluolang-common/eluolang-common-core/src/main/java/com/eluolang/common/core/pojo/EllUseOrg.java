package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUseOrg {
    /**
     * 计划id
     */
    private String planId;
    /**
     * 使用部门id
     */
    private Integer orgId;
    /**
     * 使用的规则在计划使用的规则中选择
     */
    private String useRule;
    /**
     *
     */
    private int id;
    /**
     * 部门父级id
     */
    private String parentId;
    /**
     * 部门名称
     */
    private String deptName;
}
