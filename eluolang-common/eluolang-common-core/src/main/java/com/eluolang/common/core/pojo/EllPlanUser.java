package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllPlanUser implements Serializable, Cloneable {
    /**
     *
     */
    private String id;
    /**
     * 体测计划id
     */
    private String planId;
    /**
     * 学生id
     */
    private String userId;
    /**
     * 预约日期
     */
    private Integer subDateId;
    /**
     * 删除标志
     */
    private int isDelete;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 预约状态
     */
    private int state;
    /**
     * 是否为最近预约的计划
     */
    private int isLast;
    /**
     * 那一天
     */
    private String dayTime;
}