package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateScoreVo {
    /**
     * 成绩
     */
    private String testDateJson;
    /**
     * 最终成绩
     */
    private String data;
    /**
     * 最终分数
     */
    private String score;
    /**
     * id
     */
    private String id;
    /**
     * 项目最终评语
     */
    private String comment;
    /**
     * 是否是重测
     */
    private int isRetest;
    /**
     * 修改人id
     */
    private int updateById;
    /**
     * 拓展数据
     */
    private String extraData;
    /**
     * 检录时间戳
     */
    private Long checkInTimestamp;
    /**
     * 结束时间蹉
     */
    private Long endTimestamp;
    /**
     * 用时
     */
    private Long timeSpent;
}
