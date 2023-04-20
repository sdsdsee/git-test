package com.eluolang.common.core.pojo;

/**
 * 考生分组表
 * @author dengrunsen
 * @date 2022/1/11
 */
public class EllExamineeGroup {
    /** id */
    private String id;
    /** 分组名称 */
    private String groupName;
    /** 计划id */
    private String planId;
    /** 操作员id */
    private Integer optId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }
}
