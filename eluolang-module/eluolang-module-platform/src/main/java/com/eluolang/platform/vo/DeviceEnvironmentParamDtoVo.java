package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dengrunsen
 * @date 2022年05月27日 10:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEnvironmentParamDtoVo implements Serializable {
    /**
     * cmd: 92
     * ident: xxxxxx
     * deviceId: "xxxxxxxxxxx"
     * temperature: x
     * humidity: x
     * particles: x
     * illuminance: x
     * noise: x
     */


    /**
     * 设备名称
     */
    private String deviceName;
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
     * 光照度（lux）/氨氮
     */
    private String illuminance;
    /**
     * 噪声（dB）/硫化氢
     */
    private String noise;
    /**
     * 报警次数
     */
    private int count;
}
