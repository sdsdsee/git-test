package com.eluolang.physical.model;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年08月03日 14:17
 */
public class SchoolNumDto {
    private String planId;
    private List<Integer> deptIdList;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public List<Integer> getDeptIdList() {
        return deptIdList;
    }

    public void setDeptIdList(List<Integer> deptIdList) {
        this.deptIdList = deptIdList;
    }
}
