package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllRuleFormattingVo {
    /**
     * 规则
     */
    private List<RulesVo> rulesVo;
    /**
     * 男女
     */
    private int sex;
    /**
     * 大于等于为1 小于等于为2
     */
    private int symbol;
    /**
     * 规则组id
     */
    private int rulesId;

    /**
     * 格式话后的规则
     */
    private List<EllRuleFormVo> ruleForm;


}
