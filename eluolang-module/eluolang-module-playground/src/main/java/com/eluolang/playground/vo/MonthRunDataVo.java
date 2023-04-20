package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月18日 15:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthRunDataVo {
    /** 跑步人员id */
    private String userId;
    /** 排名 */
    private String rank;
    /** 总人数 */
    private String total;
    /** 剩余里程 */
    private String surplusMileage;
    /** 已完成里程 */
    private String finishMileage;
    /** 当月任务里程 */
    private String monthMileage;
    /** 累计跑步里程 */
    private String totalMileage;
}
