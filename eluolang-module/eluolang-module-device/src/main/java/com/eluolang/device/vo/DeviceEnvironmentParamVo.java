package com.eluolang.device.vo;

/**
 * @author dengrunsen
 * @date 2022年05月27日 13:24
 */
public class DeviceEnvironmentParamVo {

    /**
     * cmd : 92
     * ident : xxxxxx
     * deviceId : xxxxxxxxxxxx
     * code : 0
     */

    /** 返回指令，和发送的指令一致 */
    private int cmd;
    /** 指令标识 */
    private int ident;
    /** 设备ID */
    private String deviceId;
    /** 返回码 */
    private int code;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
