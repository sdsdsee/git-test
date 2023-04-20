package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllStudentGradeDto {
    //项目名称
    private String proName;
    //成绩
    private String dataGrade;
    //单位
    private String unit;
    //项目id
    private Integer proId;
    //排名
    private Integer rank;
    //拓展数据
    private String extraData;
}
