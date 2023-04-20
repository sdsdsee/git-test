package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgmentResultVo {
    private String data;
    private Double score;
    private String comment;
    private String testDataMap;
}
