package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSelSexDetailsVo {

    /**
     * 日常计划id
     */
    private String dailyId;
    /**
     * 账号
     */
    private String account;
    /**
     * 部门Id
     */
    private String orgId;
    /**
     * 达标
     */
    private int attain;

}