package com.eluolang.module.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllHeightAndWeightVo {
    /**
     * 身高
     */
    private Double height;
    /**
     * 体重
     */
    private Double weight;
    private String year;
}
