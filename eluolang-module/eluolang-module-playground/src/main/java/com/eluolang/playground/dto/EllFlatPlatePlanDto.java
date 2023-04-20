package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EllFlatPlatePlanDto {
    private String id;
    private String planTitle;
    private String planAddr;
    private String dateBegin;
    private String dateEnd;
    private String planRemark;
    private String isExam;
}
