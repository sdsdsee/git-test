package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSmartGradeDto {
    //项目名称
    private String proName;
    //平均成绩
    private Integer avgGrade;
    //最高成绩
    private float maxGrade;
}
