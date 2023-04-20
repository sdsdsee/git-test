package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 日常锻炼成绩
 * @TableName ell_daily_history
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyHistoryVo implements Serializable {
    /**
     *
     */
    private String id;

    /**
     * 与上一点位距离的里程
     */
    private Integer mileage;

    /**
     * 日常锻炼计划
     */
    private String dailyId;

    /**
     * 项目类型1.跑步里程
     */
    private Integer type;

    /**
     * 手环mac地址
     */
    private String mac;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 触发点id
     */
    private String location;

    /**
     * 距离开始点位
     */
    private String locationTime;

    /**
     * 心率
     */
    private String heartRate;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 血氧百分比
     */
    private String oxygen;

    /**
     * 心率血氧测量时间
     */
    private String measureTime;

    /**
     * 步数
     */
    private String step;

    /**
     * 电量
     */
    private String battery;

    /**
     * sos状态  1：触发报警  0：无报警
     */
    private Integer sos;

    /**
     * 0 关闭测量 1 心率开  2血氧开
     */
    private Integer wristbandStatus;

    private static final long serialVersionUID = 1L;
}
