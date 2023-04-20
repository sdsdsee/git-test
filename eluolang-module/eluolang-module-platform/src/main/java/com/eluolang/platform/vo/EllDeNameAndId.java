package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDeNameAndId {
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备报警次数
     */
    private int count;
}
