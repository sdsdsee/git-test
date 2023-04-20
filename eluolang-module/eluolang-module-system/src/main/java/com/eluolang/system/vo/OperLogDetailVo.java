package com.eluolang.system.vo;

public class OperLogDetailVo {
    /** id */
    private Integer id ;
    /** 操作日志id */
    private Integer operLogId ;
    /** 硬件响应时间 */
    private Long responseTime ;
    /** 操作结果 */
    private Integer operResult ;
    /** 操作类型 */
    private Integer operType ;
    private String operTypeName;
    /** 住户id */
    private String householdId ;
    /** 文件id */
    private String fileId ;
    /** 文件名称 */
    private String fileName;
    /** 设备id */
    private String deviceId;
    /** 指令标识 */
    private int ident;
    /** 设备名称 */
    private String deviceName;
    /** 设备类型 */
    private Integer deviceType;
    /** 设备具体地址 */
    private String specificLocation;
    /** 住户名称 */
    private String name;

    @Override
    public String toString() {
        return "OperLogDetailVo{" +
                "id=" + id +
                ", operLogId=" + operLogId +
                ", responseTime=" + responseTime +
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

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
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
