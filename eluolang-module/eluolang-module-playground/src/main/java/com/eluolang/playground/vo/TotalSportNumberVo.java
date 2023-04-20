package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月18日 11:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalSportNumberVo {
    /** 日期 */
    private String date;
    /** 运动人数 */
    private String number;
}
