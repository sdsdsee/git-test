package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年10月21日 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunningRankDto {
    /** 日常锻炼计划id */
    private String dailyId;
    /** 跑步人员id */
    private String userId;
    /** 人员所属部门id */
    private Integer deptId;
    /** 日期 */
    private String date;
}
