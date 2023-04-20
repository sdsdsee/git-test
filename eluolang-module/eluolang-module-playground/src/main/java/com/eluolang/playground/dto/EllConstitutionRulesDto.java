package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllConstitutionRulesDto {
    //规则名称
    private String ruleName;
    //规则
    private Integer ruleId;
    //年级
    private int grade;
}
