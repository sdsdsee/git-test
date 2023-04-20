package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllClassRankDto {
    //排名
    private int rank;
    //排名变动
    private int change;
    //班级名称
    private String className;
    //达标率
    private int rate;

}
