package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月21日 16:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunningRankVo {
    /** 日期 */
    private String date;
    /** 完成里程 */
    private String finishMileage;
    /** 班级排名 */
    private String rank;
    /** 全校排名 */
    private String schoolRank;
}
