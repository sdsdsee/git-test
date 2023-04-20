package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUpdateSkipContestVo implements Serializable, Cloneable {
    /**
     *
     */
    private String id;
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
     * 修改时间
     */
    private String updateTime;
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
     * 内容单位
     */
    private String contentUnit;
    /**
     * 奖品
     */
    private String prize;

}