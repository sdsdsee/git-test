package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipRankingVo implements Serializable, Cloneable {

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
     * 跳绳成绩
     */
    private String skipGrade;
    /**
     * 成绩排名
     */
    private int skipRanking;
    /**
     * 头像
     */
    private String imgUrl;
}
