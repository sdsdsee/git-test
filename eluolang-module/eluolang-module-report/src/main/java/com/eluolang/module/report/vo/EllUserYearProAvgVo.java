package com.eluolang.module.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUserYearProAvgVo {
    /**
     * 项目名称
     */
    private String name;
    /**
     * 近几年数据
     */
    private List<EllUserYearProDateVo> dateList;
}
