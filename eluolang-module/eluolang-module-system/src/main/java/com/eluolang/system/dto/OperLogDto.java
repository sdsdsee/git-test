package com.eluolang.system.dto;

public class OperLogDto {
    /** 操作用户 */
    private String operName;
    /** 操作类型 */
    private String operType;
    /** 开始时间 */
    private String startTime;
    /** 结束时间 */
    private String endTime;
    /** 页码 */
    private Integer pageNum;
    /** 每页显示条数 */
    private Integer pageSize;

    @Override
    public String toString() {
        return "OperLogDto{" +
                "operName='" + operName + '\'' +
                ", operType=" + operType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
