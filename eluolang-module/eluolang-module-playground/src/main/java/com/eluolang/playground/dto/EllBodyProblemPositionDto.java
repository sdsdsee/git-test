package com.eluolang.playground.dto;

import com.eluolang.common.core.pojo.EllBodyErrorScreenshot;
import com.eluolang.common.core.pojo.EllClassHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllBodyProblemPositionDto {
    //建议
    private String suggestion;
    //问题部位
    private List<Integer> bodyList;
    //成绩
    private List<EllScoreProDto> histories;
}
