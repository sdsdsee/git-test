package com.eluolang.physical.model;

public class ClassScoreVo {
    /** 部门id */
    private int id;
    /** 部门名称 */
    private String deptName;
    /** 不及格人数 */
    private String fail;
    /** 及格人数 */
    private String pass;
    /** 良好人数 */
    private String good;
    /** 优秀人数 */
    private String excellent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getExcellent() {
        return excellent;
    }

    public void setExcellent(String excellent) {
        this.excellent = excellent;
    }
}
