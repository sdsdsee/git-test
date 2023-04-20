package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllLedInformationPublishVo {
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
}
