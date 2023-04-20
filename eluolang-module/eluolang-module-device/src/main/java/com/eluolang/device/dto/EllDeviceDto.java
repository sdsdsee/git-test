package com.eluolang.device.dto;

import io.swagger.annotations.ApiModelProperty;

public class EllDeviceDto {

    /**
     * 管理员帐号id
     */
    private Integer optId;

    /**
     * 设备名称
     */
    private String devName;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 类型
     */
    private Integer deviceType;


    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 页面显示条数
     */
    private Integer pageSize;

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
