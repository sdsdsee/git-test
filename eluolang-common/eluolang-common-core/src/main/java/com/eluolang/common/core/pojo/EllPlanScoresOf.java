package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllPlanScoresOf implements Serializable {
    /**
     * id
     */
    private int id;
    /**
     * 本次测试计划id
     */
    private String planId;
    /**
     * 占比计划id
     */
    private String proportionPlanId;
    /**
     * 占比
     */
    private String proportion;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 是否本次计划1是2平常测试计划3平常考试计划
     */
    private String isThis;
}
