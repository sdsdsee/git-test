package com.eluolang.common.core.web;

/**
 * Controller返回类
 *
 * @author suziwei
 * @date 2020/8/21
 */
public class ResultApi<T>
{
    /** 状态码 */
    public int code;
    /** 说明 */
    public String msg;
    /** 参数 */
    public T data;
    public ResultApi(){};
    public ResultApi(int code){
        this.code=code;
    }
    public ResultApi(int code, String msg){
        this.code=code;
        this.msg=msg;
    }
    public ResultApi(int code, String msg, T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public static ResultApi SUCCESS(){
        return new ResultApi(200);
    }

    public static ResultApi ERROR(){
        return new ResultApi(500);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
