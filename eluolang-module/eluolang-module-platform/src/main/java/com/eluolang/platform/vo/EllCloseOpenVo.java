package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllCloseOpenVo {
    /**
     * 命令
     */
    private int cmd;
    /**
     * 标识
     */
    private int ident;
    /**
     * 1,开机2，关机
     */
    private int status;
    /**
     * deviceId
     */
    private String deviceId;
}
