package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSelDailyDetailsVo {
    /**
     * 页码
     */
    private int page;
    /**
     * 日常计划id
     */
    private String dailyId;
    /**
     * 账号
     */
    private String account;
    /**
     * 时间
     */
    private String day;
    /**
     * 部门Id
     */
    private String orgId;
    /**
     * 模式
     */
    private int type;
}
