package com.eluolang.platform.vo;

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
     * 开机时间
     */
    private String actionTime;
    /**
     * 结束时间
     */
    private String closeTime;
    /**
     * 信息id
     */
    private String infoId;
    /**
     * 信息发布的顺序
     */
    private String releaseOrder;
    /**
     * 间隔时间
     */
    private int intervalTime;
    /**
     * 信息名称
     */
    private String infoTitle;
    /**
     * 发布时间
     */
    private String releaseTime;
    /**
     * 版本
     */
    private String version;
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
    List<String> fileList;
}
