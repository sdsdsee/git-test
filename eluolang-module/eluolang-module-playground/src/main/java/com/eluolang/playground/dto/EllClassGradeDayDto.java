package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllClassGradeDayDto {
    //项目id
    private Integer proId;
    //项目名称
    private String proName;
    //成绩
    private List<EllClassGradeDto> timeGrade;
}

