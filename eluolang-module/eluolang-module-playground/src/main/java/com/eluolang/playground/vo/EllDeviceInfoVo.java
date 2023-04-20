package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//设备信息
public class EllDeviceInfoVo {
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 信息id
     */
    private String infoId;
    /**
     * 信息名称
     */
    private String infoTitle;
    /**
     * 信息类型
     */
    private int infoType;
    /**
     * 文本内容
     */
    private String text;
    /**
     * 文件地址
     */
    private String fileUrl;
}
