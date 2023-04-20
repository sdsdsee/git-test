package com.eluolang.module.report.dto;

import io.swagger.models.auth.In;

public class TestSituationQueryDto {
    /** 部门id */
    private Integer deptId;
    /** 管理员id */
    private Integer optId;
    /** 测试项目id */
    private Integer proId;
    /** 评语 */
    private String comment;
    /** 年份 */
    private Integer year;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
