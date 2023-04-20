package com.eluolang.device.dto;

public class EllLargeScreenDto {

    /** 管理员帐号id */
    private Integer optId;

    /** 设备名称 */
    private String devName;

    /** 当前页码 */
    private Integer pageNum;

    /** 页面显示条数 */
    private Integer pageSize;

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
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
