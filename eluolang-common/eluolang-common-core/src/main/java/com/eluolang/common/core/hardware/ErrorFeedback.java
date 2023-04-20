package com.eluolang.common.core.hardware;

/**
 * 错误反馈
 *
 * @author suziwei
 * @date 2020/9/16
 */
public class ErrorFeedback {

    /**
     * cmd : 100
     * err_cmd : 1
     * err_ident : 2
     * error : 3
     * msg :
     */

    /** 返回指令，和发送的指令一致 */
    private int cmd;
    /** 返回错误指令的类型 */
    private int err_cmd;
    /** 错误指令的标识 */
    private int err_ident;
    /** 错误码 */
    private int error;
    /** 错误信息 */
    private String msg;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getErr_cmd() {
        return err_cmd;
    }

    public void setErr_cmd(int err_cmd) {
        this.err_cmd = err_cmd;
    }

    public int getErr_ident() {
        return err_ident;
    }

    public void setErr_ident(int err_ident) {
        this.err_ident = err_ident;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
