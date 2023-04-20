package com.eluolang.platform.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDeviceLocationVo {
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 位置
     */
    private String location;
    /**
     * 经纬度
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)//属性为 空（“”） 或者为 NULL 都不序列化
    private String locationXY;
    /**
     * 经纬度
     */
    private String[] site;
}
