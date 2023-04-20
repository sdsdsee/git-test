package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllTotalScoreDto {
    //学校名称
    private String schoolName;
    //参考人数
    private Integer referenceNum;
    //总分
    private Double totalScore;
    //总平均分
    private Double totalAvgScore;
  /*  //排名
    private int rank;*/
}
