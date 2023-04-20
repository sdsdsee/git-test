package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayParamVo {
    /**
     * 宽度
     */
    private int screenWidth;
    /**
     * 高度
     */
    private int screenHeight;
    /**
     * 颜色
     */
    private int color;
    /**
     * 显示样式
     */
    private int displayType;
    /**
     * 显示速度
     */
    private int displaySpeed;
    /**
     * 间隔时间
     */
    private int stayTime;
    /**
     * led信息id
     */
    private String infoId;
    /**
     * 图片地址
     */
    private String fileUrl;
    /**
     * 设备id
     */
    private String deviceIds;
}