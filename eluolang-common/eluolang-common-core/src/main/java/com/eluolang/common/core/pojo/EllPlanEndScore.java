package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllPlanEndScore implements Serializable, Cloneable {
    /**
     * 计划id
     */
    private String planId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 最终分数
     */
    private Double endScore;
    /**
     * 最终评价
     */
    private String endComment;
}