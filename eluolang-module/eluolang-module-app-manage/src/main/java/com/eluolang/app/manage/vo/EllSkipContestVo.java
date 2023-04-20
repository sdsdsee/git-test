package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipContestVo implements Serializable, Cloneable {
    /**
     * 比赛id
     */
    private String contestId;
    /**
     * 比赛名称
     */
    private String conTitle;
    /**
     * 比赛注意事项
     */
    private String conMatter;
    /**
     * 比赛模式1定时2定数
     */
    private int conPattern;
    /**
     * 比赛内容(多少个/多少时间)
     */
    private int conContent;
    /**
     * 比赛状态1.未发部2.报名中3.结束 5(查询除未发部的所有)
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
     * 内容单位
     */
    private String contentUnit;
    /**
     * 参赛编号
     */
    private String conNumber;
    /**
     * 奖品
     */
    private String prize;
    /**
     * 本人比赛状态 0未报名1已报名2已完成3比赛已结束
     */
    private int state;
    /**
     * 本人id
     */
    private String skipUserId;
    /**
     * 是否只查询本人参加的比赛0否1是
     */
    private int isSelf;
}