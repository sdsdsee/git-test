package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMonthDailyDto {
    /**
     * 里程
     */
    private int mileage;
    /**
     * 达标
     */
    private int reach;
}
