package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllElectricQuantity implements Serializable, Cloneable {
    /**
     *
     */

    private String id;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 用电量w/h
     */
    private Integer useElectricity;
    /** 设备id */
    private String deviceId ;
}