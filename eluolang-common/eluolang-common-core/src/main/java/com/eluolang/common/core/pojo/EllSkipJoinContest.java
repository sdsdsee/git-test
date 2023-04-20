package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipJoinContest implements Serializable, Cloneable {
    /**
     *
     */
    private String id;
    /**
     * 用户id
     */
    private String skipUserId;
    /**
     * 比赛Id
     */
    private String conTestId;
    /**
     * 状态1.已预约2.已完成
     */
    private int state;
    /**
     * 参数用户姓名
     */
    private String userName;
    /**
     * 年龄
     */
    private int age;
    /**
     * 参赛编号
     */
    private String conNumber;
    /**
     * 跳绳成绩id
     */
    private String skipNumId;
    /**
     * 跳绳视频id
     */
    private String skipVideoId;
    /**
     * 成绩排名
     */
    private int skipRanking;
    /**
     * 修改人id
     */
    private int updateId;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 创建时间
     */
    private String createTime;
}
