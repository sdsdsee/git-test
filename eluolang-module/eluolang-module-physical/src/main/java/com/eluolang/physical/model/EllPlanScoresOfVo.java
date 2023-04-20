package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllPlanScoresOfVo implements Serializable {

    /**
     * 占比计划id
     */
    private String proportionPlanId;
    /**
     * 占比
     */
    private String proportion;
    /**
     * 是否本次计划1是2平常测试计划3平常考试计划
     */
    private int isThis;

}
