package com.eluolang.device.dto;

import com.eluolang.common.core.hardware.dto.EllDeviceInfoStateDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备表
 *
 * @author dengrunsen
 * @date 2022/1/10
 */
public class EllDeviceInfodDto {
    /**
     * id
     */
    private String id;
    /**
     * 部门id
     */
    private Integer deptId;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String devName;
    /**
     * 设备ip
     */
    @ApiModelProperty(name = "设备ip", notes = "")
    private String deviceIp;
    /**
     * 经度
     */
    @ApiModelProperty(name = "经度", notes = "")
    private String locationX;
    /**
     * 纬度
     */
    @ApiModelProperty(name = "纬度", notes = "")
    private String locationY;
    /**
     * 开机时间
     */
    @ApiModelProperty(name = "开机时间", notes = "")
    private String actionTime;
    /**
     * 关机时间
     */
    @ApiModelProperty(name = "关机时间", notes = "")
    private String closeTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间", notes = "")
    private String createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(name = "修改时间", notes = "")
    private String updateTime;
    /**
     *
     */
    @ApiModelProperty(name = "", notes = "")
    private Integer isDelete;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "创建人", notes = "")
    private int createBy;
    /**
     * 设备类型1.灯杆2.厕所
     */
    @ApiModelProperty(name = "设备类型", notes = "")
    private int deviceType;
    /**
     * 位置
     */
    @ApiModelProperty(name = "位置", notes = "")
    private String location;
    /**
     * 摄像头 rtsp
     */
    @ApiModelProperty(name = "rtsp", notes = "")
    private String rtsp;
    /**
     * 状态
     */
    @ApiModelProperty(name = "状态", notes = "")
    private Integer deviceStates;

    /**
     * 设备信息
     */
    @ApiModelProperty(name = "设备信息", notes = "")
    private EllDeviceInfoStateDto deviceInfo;


    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getRtsp() {
        return rtsp;
    }

    public void setRtsp(String rtsp) {
        this.rtsp = rtsp;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public EllDeviceInfoStateDto getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(EllDeviceInfoStateDto deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public Integer getDeviceStates() {
        return deviceStates;
    }

    public void setDeviceStates(Integer deviceStates) {
        this.deviceStates = deviceStates;
    }
}
