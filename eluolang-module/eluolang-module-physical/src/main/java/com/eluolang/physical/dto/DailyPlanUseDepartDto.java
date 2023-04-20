package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门表
 *
 * @author dengrunsen
 * @date 2021/3/4
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyPlanUseDepartDto {
    /**
     * id
     */
    private String id;
    /**
     * 日常id
     */
    private String dailyPlanId;
    /**
     * 部门Id
     */
    private String orgId;
}
