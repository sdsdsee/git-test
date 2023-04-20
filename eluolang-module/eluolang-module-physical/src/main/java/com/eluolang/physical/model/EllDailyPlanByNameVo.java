package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyPlanByNameVo {
    /**
     * 日常名称
     */
    private String trainName;
    /**
     * 页码
     */
    private int page;
    /**
     * 账号id
     */
    private String account;
    /**
     * 部门id
     */
    private String orgId;
}
