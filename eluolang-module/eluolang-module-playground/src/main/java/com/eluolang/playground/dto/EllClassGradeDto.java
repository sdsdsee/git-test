package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public
class EllClassGradeDto {
    //测试日期
    private String testTime;
    //成绩
    private String dataGrade;
}