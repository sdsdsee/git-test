package com.eluolang.common.core.pojo;

import java.io.Serializable;

/**
 * 操作日志表
 *
 * @author suziwei
 * @date 2020/8/20
 */

public class OperLog implements Serializable,Cloneable
{
    /** 日志id */
    private String id ;
    /** 操作用户名 */
    private String operName ;
    /** 操作用户姓名 */
    private String operRealName;
    /** 操作时间 */
    private Long operTime ;
    /** 主机地址 */
    private String operIp ;
    /** 操作模块 */
    private String operModule ;
    /** 操作对象;0:用户 1:设备 */
    private Integer operObj ;
    /** 操作类型;0:其他 1:登录 2:退出 3:新增 4:修改 5:删除 6:授权 7:导出 8:导入 9:强退 10:生成代码 11:清空数据*/
    private Integer operType ;
    /** 操作结果 */
    private String operResult ;
    /** 操作内容 */
    private String operContent ;
    /** 操作状态;0:异常  1:正常 */
    private Integer operStatus ;
    /** 方法名称 */
    private String method ;
    /** 请求方式;PUT POST DELETE GET */
    private String requestMethod ;
    /** 返回参数 */
    private String jsonResult ;
    /** 错误消息 */
    private String errorMsg ;
    /** 请求参数 */
    private String operParam ;

    public OperLog() {
    }

    public OperLog(String id, String operName, String operRealName, Long operTime, String operIp, String operModule, Integer operObj, Integer operType, String operResult, String operContent, Integer operStatus, String method, String requestMethod, String jsonResult, String errorMsg, String operParam) {
        this.id = id;
        this.operName = operName;
        this.operRealName = operRealName;
        this.operTime = operTime;
        this.operIp = operIp;
        this.operModule = operModule;
        this.operObj = operObj;
        this.operType = operType;
        this.operResult = operResult;
        this.operContent = operContent;
        this.operStatus = operStatus;
        this.method = method;
        this.requestMethod = requestMethod;
        this.jsonResult = jsonResult;
        this.errorMsg = errorMsg;
        this.operParam = operParam;
    }

    @Override
    public String toString() {
        return "OperLog{" +
                "id=" + id +
                ", operName='" + operName + '\'' +
                ", operRealName='" + operRealName + '\'' +
                ", operTime=" + operTime +
                ", operIp='" + operIp + '\'' +
                ", operModule='" + operModule + '\'' +
                ", operObj=" + operObj +
                ", operType=" + operType +
                ", operResult='" + operResult + '\'' +
                ", operContent='" + operContent + '\'' +
                ", operStatus=" + operStatus +
                ", method='" + method + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", jsonResult='" + jsonResult + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", operParam='" + operParam + '\'' +
                '}';
    }

    public String getOperRealName() {
        return operRealName;
    }

    public void setOperRealName(String operRealName) {
        this.operRealName = operRealName;
    }

    /** 日志id */
    public String getId(){
        return this.id;
    }
    /** 日志id */
    public void setId(String id){
        this.id = id;
    }
    /** 操作用户 */
    public String getOperName(){
        return this.operName;
    }
    /** 操作用户 */
    public void setOperName(String operName){
        this.operName = operName;
    }
    /** 操作时间 */
    public Long getOperTime(){
        return this.operTime;
    }
    /** 操作时间 */
    public void setOperTime(Long operTime){
        this.operTime = operTime;
    }
    /** 主机地址 */
    public String getOperIp(){
        return this.operIp;
    }
    /** 主机地址 */
    public void setOperIp(String operIp){
        this.operIp = operIp;
    }
    /** 操作模块 */
    public String getOperModule(){
        return this.operModule;
    }
    /** 操作模块 */
    public void setOperModule(String operModule){
        this.operModule = operModule;
    }
    /** 操作对象;0:用户 1:设备 */
    public Integer getOperObj(){
        return this.operObj;
    }
    /** 操作对象;0:用户 1:设备 */
    public void setOperObj(Integer operObj){
        this.operObj = operObj;
    }
    /** 操作类型;0:登录 2:退出 3:新增 4:修改5: */
    public Integer getOperType(){
        return this.operType;
    }
    /** 操作类型;0:登录 2:退出 3:新增 4:修改5: */
    public void setOperType(Integer operType){
        this.operType = operType;
    }
    /** 操作结果 */
    public String getOperResult(){
        return this.operResult;
    }
    /** 操作结果 */
    public void setOperResult(String operResult){
        this.operResult = operResult;
    }
    /** 操作内容 */
    public String getOperContent(){
        return this.operContent;
    }
    /** 操作内容 */
    public void setOperContent(String operContent){
        this.operContent = operContent;
    }
    /** 操作状态;0:异常  1:正常 */
    public Integer getOperStatus(){
        return this.operStatus;
    }
    /** 操作状态;0:异常  1:正常 */
    public void setOperStatus(Integer operStatus){
        this.operStatus = operStatus;
    }
    /** 方法名称 */
    public String getMethod(){
        return this.method;
    }
    /** 方法名称 */
    public void setMethod(String method){
        this.method = method;
    }
    /** 请求方式;PUT POST DELETE GET */
    public String getRequestMethod(){
        return this.requestMethod;
    }
    /** 请求方式;PUT POST DELETE GET */
    public void setRequestMethod(String requestMethod){
        this.requestMethod = requestMethod;
    }
    /** 返回参数 */
    public String getJsonResult(){
        return this.jsonResult;
    }
    /** 返回参数 */
    public void setJsonResult(String jsonResult){
        this.jsonResult = jsonResult;
    }
    /** 错误消息 */
    public String getErrorMsg(){
        return this.errorMsg;
    }
    /** 错误消息 */
    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }
    /** 请求参数 */
    public String getOperParam(){
        return this.operParam;
    }
    /** 请求参数 */
    public void setOperParam(String operParam){
        this.operParam = operParam;
    }
}