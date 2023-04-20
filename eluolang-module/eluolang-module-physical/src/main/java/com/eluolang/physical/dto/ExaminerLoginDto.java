package com.eluolang.physical.dto;

public class ExaminerLoginDto {
    /** 验证码 */
    private String code;
    /** 设备id */
    private String deviceId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
