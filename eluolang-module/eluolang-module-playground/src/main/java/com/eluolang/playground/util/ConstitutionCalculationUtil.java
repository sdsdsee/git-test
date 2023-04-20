package com.eluolang.playground.util;

import com.eluolang.playground.dto.EllBodyProblemPositionDto;

import java.util.List;
import java.util.Map;

//计算体质好坏
public class ConstitutionCalculationUtil {
    public static List<EllBodyProblemPositionDto> bodyCalculation(Map<Integer, List<Integer>> scoreGradeMap) {

        for (Integer key : scoreGradeMap.keySet()) {
            //计算平均
            for (int i = 0; i < scoreGradeMap.get(key).size(); i++) {

            }
        }
        return null;
    }
}
