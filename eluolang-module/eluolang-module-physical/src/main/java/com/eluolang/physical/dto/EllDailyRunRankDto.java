package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyRunRankDto implements Serializable, Cloneable {
    /**
     * 成绩
     */
    private int mileage;

    /**
     * 排名
     */
    private int rank;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户姓名
     */
    private String userName;
}