package com.eluolang.device.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 心跳设备状态
 *
 * @author dengrunsen
 * @date 2020/9/1
 */
public class HeartbeatDeviceStatusDto implements Serializable {

    /**
     * cmd: 98
     * ident: xxxxxx
     * deviceId: "xxxxxxxxxxx"
     * status: x
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
     * 设备状态(1.在线,2.离线)
     */
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
