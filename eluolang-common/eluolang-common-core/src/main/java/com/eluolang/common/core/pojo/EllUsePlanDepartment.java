package com.eluolang.common.core.pojo;

public class EllUsePlanDepartment {
    /** uuid */
    private String id;
    /** 计划id */
    private String planId;
    /** 部门id */
    private String orgId;
    /** 规则id */
    private String useRule;
    /** 部门父级id */
    private Integer parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUseRule() {
        return useRule;
    }

    public void setUseRule(String useRule) {
        this.useRule = useRule;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
