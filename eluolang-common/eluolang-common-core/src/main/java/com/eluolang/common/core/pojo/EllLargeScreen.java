package com.eluolang.common.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllLargeScreen {
    /**
     *
     */
    @ApiModelProperty(name = "", notes = "")
    private String id;
    /**
     * 设备编号
     */
    @ApiModelProperty(name = "设备编号", notes = "")
    private String deviceId;
    /**
     * 设备名称
     */
    @ApiModelProperty(name = "设备名称", notes = "")
    private String deviceName;
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
    /*  *//**
     * 厕位个数
     *//*
    @ApiModelProperty(name = "厕位个数", notes = "")
    private Integer toiletCubicle;
    *//**
     * 1男2女
     *//*
    @ApiModelProperty(name = "1男2女", notes = "")
    private Integer sex;*/
}
