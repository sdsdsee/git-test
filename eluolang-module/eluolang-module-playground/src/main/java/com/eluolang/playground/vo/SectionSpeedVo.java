package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月21日 10:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionSpeedVo {
    /** 时间 */
    private String time;
    /** 心率区间 */
    private String heart;
}
