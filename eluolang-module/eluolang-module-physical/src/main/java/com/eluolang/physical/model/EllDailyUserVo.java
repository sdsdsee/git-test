package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyUserVo {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 日常配置id
     */
    private String dailyId;
}
