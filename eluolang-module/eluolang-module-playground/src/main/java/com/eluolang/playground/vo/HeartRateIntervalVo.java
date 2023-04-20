package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月19日 14:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartRateIntervalVo {
    /** 日期 */
    private String date;
    /** 耗时 */
    private String time;
    /** 里程总数 */
    private String sumMileage;
}
