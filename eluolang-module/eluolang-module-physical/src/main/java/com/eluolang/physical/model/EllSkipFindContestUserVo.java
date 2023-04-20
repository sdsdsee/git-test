package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipFindContestUserVo {
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
    private String state;
    /**
     * 参赛编号
     */
    private String conNumber;
    /**
     * 跳绳成绩id
     */
    private String skipNumId;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 页码
     */
    private int page;
}
