package com.eluolang.module.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUserYearProDateVo {
    /**
     * 年份
     */
    private String year;
    /**
     * 平均分
     */
    private int score;
}
