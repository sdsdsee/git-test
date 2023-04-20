package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFindSkipJoinContestUserVo implements Serializable, Cloneable {
    /**
     * 状态1.已预约2.已完成
     */
    private String state;
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
     * 跳绳时间
     */
    private String skipTime;
    /**
     * 跳绳模式
     */
    private int skipPattern;
    /**
     * 跳绳个数
     */
    private int skipNum;
    /**
     * 跳绳视频地址
     */
    private String fileUrl;
    /**
     * 成绩排名
     */
    private int skipRanking;
    /**
     * 电话
     */
    private String phone;
}
