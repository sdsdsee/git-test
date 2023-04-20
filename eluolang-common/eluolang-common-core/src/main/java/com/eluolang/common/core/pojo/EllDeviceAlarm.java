package com.eluolang.common.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDeviceAlarm implements Serializable, Cloneable {
    /**
     * @ApiModelProperty(name = "", notes = "")
     * private String id;
     */
    private String id;
    /**
     *
     * 设备id
     */
    @ApiModelProperty(name = "设备id", notes = "")
    private String deviceId;
    /**
     * 警告状态1.路灯设备求助2.厕所设备报警
     */
    @ApiModelProperty(name = "警告状态1.路灯设备求助2.厕所设备报警", notes = "")
    private Integer alarmType;
    /**
     * 警告时间
     */
    @ApiModelProperty(name = "警告时间", notes = "")
    private String alarmTime;
    /**
     * 警告内容
     */
    @ApiModelProperty(name = "警告内容", notes = "")
    private String content;
    /**
     * 设备名称
     */
    @ApiModelProperty(name = "设备名称", notes = "")
    private String deviceName;

}