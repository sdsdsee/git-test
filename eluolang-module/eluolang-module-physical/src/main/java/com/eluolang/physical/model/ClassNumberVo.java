package com.eluolang.physical.model;

public class ClassNumberVo {
    /** 部门id */
    private Integer id;
    /** 部门名称 */
    private String deptName;
    /** 总人数 */
    private String total;
    /** 男生人数 */
    private String man;
    /** 女生人数 */
    private String woman;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public String getWoman() {
        return woman;
    }

    public void setWoman(String woman) {
        this.woman = woman;
    }
}
