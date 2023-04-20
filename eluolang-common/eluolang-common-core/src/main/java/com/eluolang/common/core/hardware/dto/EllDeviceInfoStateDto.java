package com.eluolang.common.core.hardware.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDeviceInfoStateDto {
    @ApiModelProperty(name = "设备ID", value = "设备id", required = true)
    //设备ID
    private String deviceId;
    //设备名称
    @ApiModelProperty(name = "设备名称", value = "设备名称", required = true)
    private String deviceName;
    //设备类型1一体机2大屏
    @ApiModelProperty(name = "设备类型", value = "1一体机2大屏", required = true)
    private int type;
    //项目Id
    @ApiModelProperty(name = "项目Id", value = "项目Id", required = true)
    private int proId;
    //项目名称
    @ApiModelProperty(name = "项目名称", value = "项目名称", required = true)
    private String proName;
    //项目简写
    @ApiModelProperty(name = "项目简写", value = "项目简写", required = true)
    private String proAbbr;
    //教师id
    @ApiModelProperty(name = "教师id", value = "", required = false)
    private String heldBy;
    //教师姓名
    @ApiModelProperty(name = "教师姓名", value = "", required = false)
    private String heldByTeacherName;
    //设备模式
    @ApiModelProperty(name = "模式", value = "1日常锻炼2课堂模式3体测体考模式", required = true)
    private Integer mode;
}
