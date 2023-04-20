package com.eluolang.physical.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchOptionalVo {
    /**
     * userId
     */
    @ExcelIgnore
    private String userId;
    //身份证
    @ExcelProperty("学籍号")
    private String idCard;
    //项目id
    @ExcelProperty("项目标识")
    private String proId;
    /**
     * planId
     */
    @ExcelIgnore
    private String planId;
}
