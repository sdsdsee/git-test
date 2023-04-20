package com.eluolang.physical.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;


import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserRegisterVo {
    /**
     * 设备id
     */
    @ApiModelProperty(notes = "设备id", required = true)
    private String deviceId;
    /**
     * 用户标识
     */
    @ApiModelProperty(notes = "用户标识", required = false)
    private String identification;
    /**
     * 计划id
     */
    @ApiModelProperty(notes = "计划id", required = true)
    private String planId;
    /**
     * 项目id
     */
    @ApiModelProperty(notes = "项目id", required = false)
    private int proId;
    /**
     * 部门id
     */
    @ApiModelProperty(notes = "部门id", required = false)
    private int orgId;
    /**
     * 图片base64
     */
    @ApiModelProperty(notes = "图片base64", required = false)
    private String image;
    /**
     * 跑步判断 0是正常测试，1.是自由跑
     */
    @ApiModelProperty(notes = "跑步判断 0是正常测试，1.是自由跑", required = true)
    private int type;
    /**
     * 判断是否为课堂模式1,2都是课堂模式(1是锻炼模式2是测试模式)。3.体考体测模式
     */
    private int mode;
}

