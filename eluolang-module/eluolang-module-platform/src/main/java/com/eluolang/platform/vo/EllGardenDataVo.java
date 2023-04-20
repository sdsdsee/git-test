package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllGardenDataVo {
    /**
     * 设备总数
     */
    private int deviceAll;
    /**
     * 正常设备
     */
    private int deviceNormal;
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
