package com.eluolang.common.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllInfoFile implements Serializable, Cloneable {
    /**
     * id
     */
    @ApiModelProperty(name = "id", notes = "")
    private String id;
    /**
     * 文件id
     */
    @ApiModelProperty(name = "文件id", notes = "")
    private String fileId;
    /**
     * 信息id
     */
    @ApiModelProperty(name = "信息id", notes = "")
    private String infoId;
    /**
     * 间隔时间
     */
    @ApiModelProperty(name = "间隔时间", notes = "")
    private Integer intervalTime;
}