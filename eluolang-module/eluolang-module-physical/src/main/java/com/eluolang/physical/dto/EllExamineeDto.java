package com.eluolang.physical.dto;

public class EllExamineeDto {
    /** 分组id */
    private String groupId;

    /** 计划id */
    private String planId;

    /** 当前页码 */
    private Integer pageNum;

    /** 页面显示条数 */
    private Integer pageSize;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
