package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllLedPublishInfo implements Serializable, Cloneable {
    /**
     *
     */

    private String id;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 内容
     */
    private String content;
    /**
     * 类型
     */
    private Integer type;
    /**
     * led信息
     */
    private String ledInfoId;
    /**
     * 创建时间
     */
    private String createTime;
}