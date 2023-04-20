package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyTimeQuantum {
    /**
     * id
     */
    private String id;
    /**
     * 开始时间
     */
    private String startHour;
    /**
     * 结束时间
     */
    private String endHour;
    /**
     * 日常计划id
     */
    private String dailyId;
    /**
     * 上班年还是下半年
     */
    private int timeType;
}
