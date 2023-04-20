package com.eluolang.physical.dto;

import java.util.List;

public class SchoolScoreDto {
    private List<DataBean> dataBeanList;
    /** 计划id */
    private String planId;

    public List<DataBean> getDataBeanList() {
        return dataBeanList;
    }

    public void setDataBeanList(List<DataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public static class DataBean{
        /** 部门id */
        private Integer deptId;
        /** 部门名称 */
        private String deptName;

        public Integer getDeptId() {
            return deptId;
        }

        public void setDeptId(Integer deptId) {
            this.deptId = deptId;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }
    }
}
