package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyPlanUseDepart implements Serializable, Cloneable {
    /**
     *
     */
    private String id;
    /**
     * 日常计划id
     */
    private String dailyPlanId;
    /**
     * 使用部门
     */
    private String orgId;
    /**
     * 前端选择的哪一级
     */
    private String isThis;
}