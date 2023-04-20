package com.eluolang.device.vo;

import java.io.Serializable;

/**
 * @author dengrunsen
 * @date 2022年05月26日 9:41
 */
public class UseElectricityAmountDto implements Serializable {

    /**
     * cmd: 98
     * ident: xxxxxx
     * deviceId: "xxxxxxxxxxx"
     * time: "xxxxxxxx"0
     * kwh: x
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
     * 用电量(单位:千瓦时)
     */
    private Integer kwh;

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

    public Integer getKwh() {
        return kwh;
    }

    public void setKwh(Integer kwh) {
        this.kwh = kwh;
    }
}
