package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllBmiParticulars {
    /**
     * id
     */
    private String id;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 身高
     */
    private String height;
    /**
     * 体重
     */
    private String weight;
    /**
     * BMI值
     */
    private String bmi;
    /**
     * 身高体重情况
     */
    private String bmiComment;
    /**
     * 是否删除(0:未删除，1:已删除)
     */
    private Integer isDelete;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 本次计划第几次
     */
    private Integer countNum;
    /**
     * 是否为最后一级(0:否，1:是)
     */
    private Integer isLast;
}
