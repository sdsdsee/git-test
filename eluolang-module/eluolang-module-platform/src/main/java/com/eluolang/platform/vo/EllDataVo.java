package com.eluolang.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDataVo implements Serializable, Cloneable {
    /**
     * 设备总数
     */
    private int deviceCount;
    /**
     * 是否待机
     */
    private int isOpen;
    /**
     * 异常设备
     */
    private int errorDevice;
    /**
     * 报警总数
     */
    private int alarmCount;
}
