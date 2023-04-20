package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllAlarmVo {
    /**
     * id
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 位置
     */
    private String location;
    /**
     * 报警时间
     */
    private String alarmTime;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 警报id
     */
    private String alarmId;
    /**
     * page
     */
    private int page;
}
