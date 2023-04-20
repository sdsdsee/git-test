package com.eluolang.common.core.hardware.dto;

import java.io.Serializable;

/**
 * @author dengrunsen
 * @date 2022年05月27日 15:33
 */
public class OneClickHelpDto implements Serializable {

    /**
     * cmd: 91
     * ident: xxxxxx
     * deviceId: "xxxxxxxxxxx"
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

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
