package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月19日 9:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MileageStandardVo {
    /** 月份 */
    private String month;
    /** 已完成里程 */
    private String finishMileage;
    /** 当月任务里程 */
    private String monthMileage;
}
