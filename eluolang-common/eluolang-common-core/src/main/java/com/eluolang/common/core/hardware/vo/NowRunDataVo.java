package com.eluolang.common.core.hardware.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年11月30日 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NowRunDataVo {
    private String dailyId;
    private String userId;
    private String startTime;
    private String endTime;
}
