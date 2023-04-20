package com.eluolang.device.vo;

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
public class DeviceEnvironmentParamDto implements Serializable {
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
     * 发送指令
     */
    private int cmd;
    /**
     * 指令标识
     */
    private int ident;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     *温度（℃）
     */
    private String temperature;
    /**
     *湿度（RH%）
     */
    private String humidity;
    /**
     *PM2.5
     */
    private String particles;
    /**
     *光照度（lux）
     */
    private String illuminance;
    /**
     *噪声（dB）
     */
    private String noise;
}
