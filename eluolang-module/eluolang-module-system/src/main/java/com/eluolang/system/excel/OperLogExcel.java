package com.eluolang.system.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author ZengXiaoQian
 * @createDate 2020-12-8
 */
public class OperLogExcel {
    /** 日志id */
    @ExcelIgnore
    private Integer id ;
    /** 操作用户名 */
    @ExcelProperty("操作用户名")
    private String operName ;
    /** 操作用户姓名 */
    @ExcelProperty("操作用户姓名")
    private String operRealName;
    /** 操作时间 */
    @ExcelProperty("操作时间")
    private String operTime ;
    /** 主机地址 */
    @ExcelProperty("操作IP")
    private String operIp ;
    /** 操作模块 */
    @ExcelIgnore
    private String operModule ;
    /** 操作对象;0:用户 1:设备 */
    @ExcelProperty("操作对象")
    private String operObj ;
    /** 操作类型;0:其他 1:登录 2:退出 3:新增 4:修改 5:删除 6:授权 7:导出 8:导入 9:强退 10:生成代码 11:清空数据*/
    @ExcelProperty("操作类型")
    private String operType ;
    /** 操作结果 */
    @ExcelProperty("操作结果")
    private String operResult ;
    /** 操作内容 */
    @ExcelProperty("操作内容")
    private String operContent ;
    /** 操作状态;0:异常  1:正常 */
    @ExcelIgnore
    private Integer operStatus ;
    /** 方法名称 */
    @ExcelIgnore
    private String method ;
    /** 请求方式;PUT POST DELETE GET */
    @ExcelIgnore
    private String requestMethod ;
    /** 返回参数 */
    @ExcelIgnore
    private String jsonResult ;
    /** 错误消息 */
    @ExcelIgnore
    private String errorMsg ;
    /** 请求参数 */
    @ExcelIgnore
    private String operParam ;

    @Override
    public String toString() {
        return "OperLogExcel{" +
                "id=" + id +
                ", operName='" + operName + '\'' +
                ", operRealName='" + operRealName + '\'' +
                ", operTime='" + operTime + '\'' +
                ", operIp='" + operIp + '\'' +
                ", operModule='" + operModule + '\'' +
                ", operObj='" + operObj + '\'' +
                ", operType='" + operType + '\'' +
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperRealName() {
        return operRealName;
    }

    public void setOperRealName(String operRealName) {
        this.operRealName = operRealName;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
    }

    public String getOperModule() {
        return operModule;
    }

    public void setOperModule(String operModule) {
        this.operModule = operModule;
    }

    public String getOperObj() {
        return operObj;
    }

    public void setOperObj(String operObj) {
        this.operObj = operObj;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperResult() {
        return operResult;
    }

    public void setOperResult(String operResult) {
        this.operResult = operResult;
    }

    public String getOperContent() {
        return operContent;
    }

    public void setOperContent(String operContent) {
        this.operContent = operContent;
    }

    public Integer getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(Integer operStatus) {
        this.operStatus = operStatus;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getOperParam() {
        return operParam;
    }

    public void setOperParam(String operParam) {
        this.operParam = operParam;
    }
}
