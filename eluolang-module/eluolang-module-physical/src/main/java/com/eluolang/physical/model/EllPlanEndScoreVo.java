package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年09月16日 17:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllPlanEndScoreVo {
    /** 项目名称 */
    private String projectName;
    /** 优秀人数 */
    private String excellentNum;
    /** 良好人数 */
    private String goodNum;
    /** 及格人数 */
    private String passNum;
    /** 不及格人数 */
    private String failNum;
}
