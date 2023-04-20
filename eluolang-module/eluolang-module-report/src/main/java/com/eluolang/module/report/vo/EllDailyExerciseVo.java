package com.eluolang.module.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyExerciseVo {
    /**
     * 年
     */
    private String year;
    /**
     * 公里
     */
    private int km;
}
