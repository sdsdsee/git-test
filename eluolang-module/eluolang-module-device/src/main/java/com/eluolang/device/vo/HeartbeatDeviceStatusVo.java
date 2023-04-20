package com.eluolang.device.vo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Serializable;

/**
 * 心跳设备状态
 *
 * @author dengrunsen
 * @date 2020/9/1
 */
public class HeartbeatDeviceStatusVo implements Serializable
{

    /**
     * cmd : 98
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
    /** 停车位剩余数量 */
    private int parkingNum;
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

    public Integer getParkingNum() {
        return parkingNum;
    }

    public void setParkingNum(Integer parkingNum) {
        this.parkingNum = parkingNum;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 将对象转化为字节
     */
    public byte[] toByteArray(){
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeShort(getCmd());
        byteBuf.writeInt(getIdent());
        byteBuf.writeBytes(getDeviceId().getBytes());
        byteBuf.writeByte(getCode());
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        return data;
    }

    @Override
    public String toString() {
        return "HeartbeatDeviceStatusVo{" +
                "cmd=" + cmd +
                ", ident=" + ident +
                ", deviceId='" + deviceId + '\'' +
                ", parkingNum=" + parkingNum +
                ", code=" + code +
                '}';
    }
}
