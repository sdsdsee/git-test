package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUserPlanHistoryVo {
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 成绩
     */
    private String data;
    /**
     * 分数
     */
    private String score;
    /**
     * 项目图表
     */
    private String iconUrl;
    /**
     * 项目id
     */
    private Integer proId;
}
