package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllPlanProjectChance {
    private String id;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 创建人
     */
    private Integer createById;
    /**
     * 机会
     */
    private Integer chance;
    /**
     * 项目id
     */
    private Integer proId;
    /**
     * 项目名称
     */
    private String proName;
    /**
     * 项目使用性别(1男2女)两者都(1,2)
     */
    private String useSex;
    /**
     * 测试时间
     */
    private String time;
    /**
     * 是否为必须测试
     */
    private String essential;
    /**
     * 项目简写
     */
    private String proNameAbbreviation;
    /**
     * 父项目id
     */
    private String parentId;
    /**
     * 项目占比
     */
    private String proGdp;
}
