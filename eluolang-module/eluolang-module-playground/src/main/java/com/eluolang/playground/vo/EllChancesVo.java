package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllChancesVo {
    private List<String> scores;
    private Long timestamp;
}
