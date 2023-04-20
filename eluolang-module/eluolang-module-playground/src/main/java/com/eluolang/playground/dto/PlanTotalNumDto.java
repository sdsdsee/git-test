package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月17日 11:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanTotalNumDto {
    /** 日常锻炼计划id */
    private String dailyId;
    /** 开始时间 */
    private String startTime;
    /** 结束时间 */
    private String endTime;
    /** 达标里程数 */
    private String mileage;
}
