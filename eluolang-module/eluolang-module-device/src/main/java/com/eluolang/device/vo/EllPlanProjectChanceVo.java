package com.eluolang.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllPlanProjectChanceVo {
//    private Integer id;
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
     * 已测人数
     */
    private Integer measured;

    /**
     * 项目测试时间
     */
    private Integer time;
}
