package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllToiletEnvironmentVo {

    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 温度（℃）
     */
    private String temperature;
    /**
     * 湿度（RH%）
     */
    private String humidity;
    /**
     * PM2.5
     */
    private String particles;
    /**
     * 氨氮（mg/L）
     */
    private String mgL;
    /**
     * 硫化氢（ppm）
     */
    private String ppm;
}
