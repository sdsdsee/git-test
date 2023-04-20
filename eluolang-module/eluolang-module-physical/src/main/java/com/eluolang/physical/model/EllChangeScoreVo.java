package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllChangeScoreVo {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 成绩id
     */
    private String historyId;
    /**
     * 成绩
     */
    private String data;
    /**
     * 修改人id
     */
    private int updateById;
    /**
     * 项目id
     */
    private int proId;
}
