package com.eluolang.system.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author ZengXiaoQian
 * @createDate 2020-12-15
 */
public class DeliveryOperLogDetailExcel {
    /** id */
    @ExcelIgnore
    private Integer id ;
    /** 操作日志id */
    @ExcelIgnore
    private Integer operLogId ;
    /** 硬件响应时间 */
    @ExcelProperty("响应时间")
    private String responseTime ;
    /** 操作结果 */
    @ExcelProperty("操作结果")
    private Integer operResult ;
    /** 操作类型 */
    @ExcelIgnore
    private Integer operType ;
    @ExcelProperty("操作类型")
    private String operTypeName;
    /** 住户id */
    @ExcelIgnore
    private String householdId ;
    /** 文件id */
    @ExcelIgnore
    private String fileId ;
    /** 文件名称 */
    @ExcelProperty("文件名称")
    private String fileName;
    /** 设备id */
    @ExcelIgnore
    private String deviceId;
    /** 指令标识 */
    @ExcelIgnore
    private int ident;
    /** 设备名称 */
    @ExcelProperty("设备名称")
    private String deviceName;
    /** 设备类型 */
    @ExcelIgnore
    private Integer deviceType;
    /** 设备具体地址 */
    @ExcelIgnore
    private String specificLocation;
    /** 住户名称 */
    @ExcelIgnore
    private String name;

    @Override
    public String toString() {
        return "ConfigOperLogDetailExcel{" +
                "id=" + id +
                ", operLogId=" + operLogId +
                ", responseTime='" + responseTime + '\'' +
                ", operResult=" + operResult +
                ", operType=" + operType +
                ", operTypeName='" + operTypeName + '\'' +
                ", householdId='" + householdId + '\'' +
                ", fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", ident=" + ident +
                ", deviceName='" + deviceName + '\'' +
                ", deviceType=" + deviceType +
                ", specificLocation='" + specificLocation + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperLogId() {
        return operLogId;
    }

    public void setOperLogId(Integer operLogId) {
        this.operLogId = operLogId;
    }

    public Integer getOperResult() {
        return operResult;
    }

    public void setOperResult(Integer operResult) {
        this.operResult = operResult;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getOperTypeName() {
        return operTypeName;
    }

    public void setOperTypeName(String operTypeName) {
        this.operTypeName = operTypeName;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getSpecificLocation() {
        return specificLocation;
    }

    public void setSpecificLocation(String specificLocation) {
        this.specificLocation = specificLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
