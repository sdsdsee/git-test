package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllGradeVideoVo {
    /**
     * 地址
     */
    private String fileUrl;
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * fps
     */
    private int fps;
    /**
     * 是否为重测
     */
    private int isRetest;
}
