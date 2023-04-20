package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllPlanEndScoreVo {

    /**
     * 计划最终分数
     */
    private String planEndScore;
    /**
     * 每项成绩的最终分数
     */
    List<EllUserPlanHistoryVo> ellScoreList;
    /**
     * 最终评价
     */
    private String endComment;
}
