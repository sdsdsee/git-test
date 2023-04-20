package com.eluolang.physical.dto;

public class ClassScoreDto {
    /** 管理员id */
    private Integer optId;
    /** 部门id */
    private Integer deptId;
    /** 计划id */
    private String planId;

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
