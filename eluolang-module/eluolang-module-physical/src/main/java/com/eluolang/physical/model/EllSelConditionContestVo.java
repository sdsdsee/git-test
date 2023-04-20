package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSelConditionContestVo {
    /**
     *
     */
    private String id;
    /**
     * 比赛名称
     */
    private String conTitle;
    /**
     * 比赛模式1定时2定数
     */
    private int conPattern;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 比赛状态1.未发部2.报名中3.比赛开始4.结束
     */
    private int conState;
    /**
     * 比赛开始时间
     */
    private String conBeginTime;
    /**
     * 比赛结束时间
     */
    private String conEndTime;
    /**
     * 报名开始时间
     */
    private String regBeginTime;
    /**
     * 报名结束时间
     */
    private String regEndTime;
    /**
     * 页码
     */
    private int page;
    /**
     * 创建人
     */
    private int createBy;

}
