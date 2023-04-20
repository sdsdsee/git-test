package com.eluolang.physical.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreVo {
    @ExcelProperty("学籍号")
    /** id */
    private String studentCode;
    /**
     * 父节点
     */
    @ExcelProperty("50米跑")
    private String data;
}
