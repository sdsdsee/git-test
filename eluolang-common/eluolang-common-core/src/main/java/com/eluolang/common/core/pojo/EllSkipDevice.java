package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipDevice implements Serializable, Cloneable {
    /**
     *
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 设备号
     */
    private String deviceCoding;
}