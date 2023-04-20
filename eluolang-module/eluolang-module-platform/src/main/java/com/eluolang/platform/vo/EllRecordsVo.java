package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllRecordsVo {
    /**
     * 本月
     */
    private int thisMonth;
    /**
     * 上月
     */
    private int lastMonth;
    /**
     * 前月
     */
    private int precedingMonth;
    /**
     * 、用电量
     */
    private List<EllDayElectricityVo> ellDayElectricityVoList;
}
