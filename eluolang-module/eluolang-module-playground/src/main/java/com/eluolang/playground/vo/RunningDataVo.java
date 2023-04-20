package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2023年03月21日 11:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunningDataVo {
    /** 每日跑步人数 */
    private Integer times;
    /** 每日跑步里程 */
    private String mileage;
    /** 日期 */
    private String date;
}
