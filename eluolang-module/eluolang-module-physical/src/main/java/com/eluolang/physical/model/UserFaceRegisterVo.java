package com.eluolang.physical.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFaceRegisterVo {
    /**
     * 设备id
     */
    @ApiModelProperty(notes = "设备id", required = true)
    private String deviceId;
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
     * 图片base64
     */
    @ApiModelProperty(notes = "图片base64", required = true)
    private String image;
}
