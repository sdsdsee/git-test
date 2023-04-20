package com.eluolang.common.core.hardware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年05月25日 10:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnOffDeviceDto {
  /**
   * {
   *  "cmd": 20,
   *  "ident": xxxxxx,//指令标识
   *  "deviceId": "xxxxxxxxxx,
   *  "startTime": xxxxxxxxxx,
   *  "endTime": xxxxxxxxx,
   *  "status"：x
   *  }
   */

    /** 发送指令 */
    private int cmd;
    /** 指令标识 */
    private int ident;
    /** 设备ID */
    private String deviceId;
    /** 开始时间(当值为-1,不设置开机或待机时间)*/
    private String startTime;
    /** 结束时间(当值为-1,不设置开机或待机时间) */
    private String endTime;
    /** 1.开机，2.待机 */
    private Integer status;
}
