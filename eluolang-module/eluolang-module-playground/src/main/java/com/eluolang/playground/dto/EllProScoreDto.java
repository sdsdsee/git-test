package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllProScoreDto {
    private float maxScore;
    private float minScore;
    private float sumScore;
    private String proName;
    private String userName;
    private String unit;
    private Integer userSex;
    private String userId;
    private Integer proId;
}
