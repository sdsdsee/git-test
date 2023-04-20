package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDayElectricityVo {
    /**
     * 日期
     */
    private String day;
    /**
     * 用电量
     */
    private int kwh;
}
