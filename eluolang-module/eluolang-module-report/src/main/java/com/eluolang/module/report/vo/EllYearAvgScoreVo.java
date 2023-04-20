package com.eluolang.module.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllYearAvgScoreVo {
    private String year;
    private int avgScore;
}
