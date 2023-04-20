package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//历史成绩
public class EllHistoricalPerformanceVo {
    /**
     * 成绩
     */
    private String data;
    /**
     * 得分
     */
    private String score;
    /**
     * 第几次
     */
    private String numTimes;
    /**
     * 是否重测
     */
    private int isRetest;
}
