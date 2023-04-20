package com.eluolang.physical.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllGetUsesInfoVo {
    /**
     * 设备id
     */
    @ApiModelProperty(notes = "设备id", required = true)
    private String deviceId;

    /**
     * 图片base64
     */
    @ApiModelProperty(notes = "图片base64", required = true)
    private List<String> images;
}
