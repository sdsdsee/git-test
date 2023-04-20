package com.eluolang.common.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllInfoPublishTarget implements Serializable, Cloneable {
    /**
     * id
     */
    @ApiModelProperty(name = "id", notes = "")
    private String id;
    /**
     * 设备id
     */
    @ApiModelProperty(name = "设备id", notes = "")
    private String deviceId;
    /**
     * 信息id
     */
    @ApiModelProperty(name = "信息id", notes = "")
    private String infoId;
    /**
     * 发布时间
     */
    @ApiModelProperty(name = "发布时间", notes = "")
    private String releaseTime;
    /**
     * 发布顺序
     */
    @ApiModelProperty(name = "发布顺序", notes = "")
    private Integer releaseOrder;
}