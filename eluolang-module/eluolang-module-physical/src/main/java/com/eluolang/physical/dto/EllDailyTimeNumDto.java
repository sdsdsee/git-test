package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyTimeNumDto {
    /**
     * 时间段
     */
    private String time;

    /**
     * 人数
     */
    private int num;
    /**
     * 上半年还是下半年
     */
    private int timeType;
}