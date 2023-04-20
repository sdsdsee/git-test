package com.eluolang.common.core.hardware;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * ACK反馈
 *
 * @author suziwei
 * @date 2020/8/31
 */
public class Ack {

    /**
     * 命令码
     */
    private int cmd;

    /**
     * 需要响应的指令类型
     */
    @JSONField(name = "ack_cmd")
    private int ackCmd;

    /**
     * 需要响应的指令标识
     */
    @JSONField(name = "ack_ident")
    private int ackIdent;

    public Ack() {
    }

    public Ack(int cmd, int ackCmd, int ackIdent) {
        this.cmd = cmd;
        this.ackCmd = ackCmd;
        this.ackIdent = ackIdent;
    }

    @Override
    public String toString() {
        return "{" +
                "cmd=" + cmd +
                ",ack_cmd=" + ackCmd +
                ",ack_ident=" + ackIdent +
                '}';
    }

    public String toStringConstructor(int cmd,int ackCmd,int ackIdent) {
        return "{" +
                "cmd=" + cmd +
                ",ack_cmd=" + ackCmd +
                ",ack_ident=" + ackIdent +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getAckCmd() {
        return ackCmd;
    }

    public void setAckCmd(int ackCmd) {
        this.ackCmd = ackCmd;
    }

    public int getAckIdent() {
        return ackIdent;
    }

    public void setAckIdent(int ackIdent) {
        this.ackIdent = ackIdent;
    }




}
