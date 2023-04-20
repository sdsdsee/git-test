package com.eluolang.physical.dto;

public class EllExaminerDto {

    /** 考官姓名 */
    private String name;

    /** 考官电话 */
    private String phone;

    /** 管理员帐号id */
    private Integer optId;

    /** 当前页码 */
    private Integer pageNum;

    /** 页面显示条数 */
    private Integer pageSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
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
