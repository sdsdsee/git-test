package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年11月04日 17:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModeSwitchDto {
    /** 计划id */
    private String planId;
    /** 部门id */
    private String orgId;
    /** 类型（1:自由跑,2:测试跑） */
    private Integer type;
}
