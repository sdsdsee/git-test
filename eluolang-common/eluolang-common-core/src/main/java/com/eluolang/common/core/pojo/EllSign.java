package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年07月28日 15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSign {
    /** id */
    private String id;
    /** 学籍号 */
    private String studentCode;
    /** 计划id */
    private String planId;
    /** 签到时间 */
    private String signTime;
    /** 学校id */
    private Integer schoolId;
}
