package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextDisplayParamVo {
    private int screenWidth;
    private int screenHeight;
    private int fontSize;
    private int fontColor;
    private int displayType;
    private int displaySpeed;
    private int stayTime;
    /**
     * led信息id
     */
    private String infoId;
    private String content;
    private String deviceIds;
}