package com.eluolang.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EllDeviceInfoDto {
    /**
     * id
     */
    private String id;

    /**
     * 设备名称
     */
    private String devName;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备摄像头rstp
     */
    private String rtsp;
    /**
     * 绑定部门名称
     */
    private String deptName;
}
