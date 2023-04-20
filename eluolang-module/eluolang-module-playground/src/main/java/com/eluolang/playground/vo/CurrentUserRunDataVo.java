package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author dengrunsen
 * @date 2022年10月19日 16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserRunDataVo {
    /** 跑步人员id */
    private String userId;
    /** 跑步人员姓名 */
    private String userName;
    /** 班级名称 */
    private String className;
    /** 最佳成绩 */
    private String bestResult;
    /** 跑步开始时间 */
    private String startTime;
    /** 跑步结束时间 */
    private String endTime;
    /** 本次跑步用时 */
    private String time;
    /** 平均配速 */
    private String averageSpeed;
    /** 本次跑步里程数 */
    private Float mileage;
}
