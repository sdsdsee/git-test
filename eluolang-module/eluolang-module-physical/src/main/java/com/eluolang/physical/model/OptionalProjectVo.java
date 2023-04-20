package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionalProjectVo {
    /**
     * 选择几个
     */
    private int customizeTheNumber;
    /**
     * 项目id
     */
    private int proId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 项目名称
     */
    private String proName;
    /**
     * 使用性别
     */
    private String useSex;
}
