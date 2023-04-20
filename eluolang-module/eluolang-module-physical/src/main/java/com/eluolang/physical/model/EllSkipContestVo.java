package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


public class EllSkipContestVo {
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
    /**
     * 创建人
     */
    private int createBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConTitle() {
        return conTitle;
    }

    public void setConTitle(String conTitle) {
        this.conTitle = conTitle;
    }

    public String getConMatter() {
        return conMatter;
    }

    public void setConMatter(String conMatter) {
        this.conMatter = conMatter;
    }

    public int getConPattern() {
        return conPattern;
    }

    public void setConPattern(int conPattern) {
        this.conPattern = conPattern;
    }

    public int getConContent() {
        return conContent;
    }

    public void setConContent(int conContent) {
        this.conContent = conContent;
    }

    public int getConState() {
        return conState;
    }

    public void setConState(int conState) {
        this.conState = conState;
    }

    public String getConBeginTime() {
        return conBeginTime;
    }

    public void setConBeginTime(String conBeginTime) {
        this.conBeginTime = conBeginTime;
    }

    public String getConEndTime() {
        return conEndTime;
    }

    public void setConEndTime(String conEndTime) {
        this.conEndTime = conEndTime;
    }

    public String getRegBeginTime() {
        return regBeginTime;
    }

    public void setRegBeginTime(String regBeginTime) {
        this.regBeginTime = regBeginTime;
    }

    public String getRegEndTime() {
        return regEndTime;
    }

    public void setRegEndTime(String regEndTime) {
        this.regEndTime = regEndTime;
    }

    public String getContentUnit() {
        return contentUnit;
    }

    public void setContentUnit(String contentUnit) {
        this.contentUnit = contentUnit;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }
}