package com.eluolang.module.socket.server.config;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 自定义协议
 * |包头|控制命令|json长度|json数据|校验和|
 * @author renzhixing
 */
public class NewDeviceProtocol {

    /** 包头 2byte 0xAA 0xBB*/
    private byte[] head;
    /** 控制命令 2byte*/
    private short cmd;
    /** 长度 2byte*/
    private short length;
    /** json数据 */
    private byte[] data;
    /** 校验和 2byte */
//    private short sumCheck;

    public NewDeviceProtocol() {
    }

    public NewDeviceProtocol(byte[] head, short cmd, short length, String data) {
        try{
            this.head = head;
            this.cmd = cmd;
            this.length = length;
            this.data = data.getBytes("UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public NewDeviceProtocol(byte[] head, short cmd, short length, byte[] data) {
        this.head = head;
        this.cmd = cmd;
        this.length = length;
        this.data = data;
    }

    public byte[] getHead() {
        return head;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "DeviceProtocol{" +
                "head=" + Arrays.toString(head) +
                ", cmd=" + cmd +
                ", length=" + length +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
