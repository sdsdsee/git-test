package com.eluolang.physical.model;

public class SchoolScoreVo {
    /** 部门id */
    private Integer deptId;
    /** 部门名称 */
    private String deptName;
    /** 10-30分人数 */
    private String oneScore;
    /** 30-60分人数 */
    private String twoScore;
    /** 60-100分人数 */
    private String threeScore;

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

    public String getOneScore() {
        return oneScore;
    }

    public void setOneScore(String oneScore) {
        this.oneScore = oneScore;
    }

    public String getTwoScore() {
        return twoScore;
    }

    public void setTwoScore(String twoScore) {
        this.twoScore = twoScore;
    }

    public String getThreeScore() {
        return threeScore;
    }

    public void setThreeScore(String threeScore) {
        this.threeScore = threeScore;
    }
}
