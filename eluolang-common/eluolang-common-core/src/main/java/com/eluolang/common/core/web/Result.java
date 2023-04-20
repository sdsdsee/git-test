package com.eluolang.common.core.web;

/**
 * Controller返回类
 *
 * @author suziwei
 * @date 2020/8/21
 */
public class Result
{
    /** 状态码 */
    public int code;
    /** 说明 */
    public String message;
    /** 参数 */
    public Object data;
    public Result(){};
    public Result(int code){
        this.code=code;
    }
    public Result(int code,String message){
        this.code=code;
        this.message=message;
    }
    public Result(int code,String message,Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public static Result SUCCESS(){
        return new Result(200);
    }

    public static Result ERROR(){
        return new Result(500);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
