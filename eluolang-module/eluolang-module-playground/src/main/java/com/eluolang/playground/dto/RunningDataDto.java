package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月19日 15:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunningDataDto {
    /** 日常锻炼计划id */
    private String dailyId;
    /** 跑步人员id */
    private String userId;
    /** 开始时间 */
    private String startTime;
    /** 结束时间 */
    private String endTime;
    /** 里程 */
    private String rank;
}
