package com.eluolang.module.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllAFewYearGradeNumVo {
    private String year;
    //优秀
    private int excellent;
    //良好
    private int good;
    //及格
    private int pass;
    //不及格
    private int noPass;
}
