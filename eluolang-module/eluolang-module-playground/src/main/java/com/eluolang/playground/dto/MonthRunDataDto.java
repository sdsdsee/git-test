package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.DocFlavor;

/**
 * @author dengrunsen
 * @date 2022年10月18日 15:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthRunDataDto {
    /** 日常锻炼计划id */
    private String dailyId;
    /** 跑步人员id */
    private String userId;
    /** 当前月份 */
    private String month;
}
