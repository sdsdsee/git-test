package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllRuleFormVo {
    /**
     * 大于的参数
     */
    private Double min;
    /**
     * 小于的参数
     */
    private Double max;
    /**
     * 得分
     */
    private String ruleScore;
    /**
     * 评语
     */
    private String remark;
}