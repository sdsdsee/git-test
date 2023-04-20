package com.eluolang.physical.dto;

import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.pojo.PfDepart;
import io.swagger.models.auth.In;

import java.util.List;

public class GroupingDto {
    /** 部门集合 */
    private List<PfDepart> pfDepartList;
    /** 每组人数 */
    private Integer count;
    /** 管理员id */
    private Integer optId;
    /** 考试计划名称 */
    private String planName;
    /** 考试计划id */
    private String planId;

    public List<PfDepart> getPfDepartList() {
        return pfDepartList;
    }

    public void setPfDepartList(List<PfDepart> pfDepartList) {
        this.pfDepartList = pfDepartList;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
