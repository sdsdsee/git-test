package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreGradeDto {
    private String excellentScore;
    private String excellentScoreName = "优秀";
    private String goodScore;
    private String goodScoreName = "良";
    private String passScore;
    private String passScoreName = "及格";
    private String failScore;
    private String failScoreName = "不及格";

    public EllScoreGradeDto(String excellentScore, String goodScore, String passScore, String failScore) {
        this.excellentScore = excellentScore;
        this.goodScore = goodScore;
        this.passScore = passScore;
        this.failScore = failScore;
    }
}
