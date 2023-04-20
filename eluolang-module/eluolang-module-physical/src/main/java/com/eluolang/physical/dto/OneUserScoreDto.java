package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneUserScoreDto {
    private String userId;
    private String userName;
    //成绩
    private String data;
    private String score;
    private String proName;
}
