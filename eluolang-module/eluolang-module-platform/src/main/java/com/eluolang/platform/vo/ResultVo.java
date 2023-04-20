package com.eluolang.platform.vo;

/**
 * Controller返回类
 *
 * @author suziwei
 * @date 2020/8/21
 */
public class ResultVo
{
    /** 状态码 */
    public int code;
    /** 说明 */
    public String msg;
    /** 参数 */
    public Object data;
    public ResultVo(){};
    public ResultVo(int code){
        this.code=code;
    }
    public ResultVo(int code,String message){
        this.code=code;
        this.msg=message;
    }
    public ResultVo(int code,String message,Object data){
        this.code=code;
        this.msg=message;
        this.data=data;
    }

    public static ResultVo SUCCESS(){
        return new ResultVo(200);
    }

    public static ResultVo ERROR(){
        return new ResultVo(500);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
