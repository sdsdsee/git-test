package com.eluolang.common.core.hardware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月09日 16:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalTrainDto {
    /** 手环mac地址 */
    private String mac;
    /** 训练检录开始时间 */
    private String startTime;
    /** 训练计划id */
    private String dailyId;
    /** 学生id */
    private String userId;
    /** 项目简称 */
    private String proName;
    private String location;
}
