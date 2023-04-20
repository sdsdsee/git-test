package com.eluolang.common.core.hardware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年05月31日 10:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDeviceDto {
    /**
     * {
     *  "cmd": 25,
     *  "ident": xxxxxx,//指令标识
     *  "deviceId": "xxxxxxxxxx
     *  }
     */

    /** 发送指令 */
    private int cmd;
    /** 指令标识 */
    private int ident;
    /** 设备ID */
    private String deviceId;
}
