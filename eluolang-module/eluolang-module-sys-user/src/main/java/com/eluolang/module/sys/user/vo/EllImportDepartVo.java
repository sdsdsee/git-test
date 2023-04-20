package com.eluolang.module.sys.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllImportDepartVo {
    /**
     * 市
     */
    @ExcelProperty("城市id")
    private String city;
    /**
     * 区
     */
    @ExcelProperty("区")
    private String district;
    /**
     * 学校
     */
    @ExcelProperty("学校名称")
    private String schoolName;
    /**
     * 学校
     */
    @ExcelProperty("年级名称")
    private String gradeName;
    /**
     * 班级名称
     */
    @ExcelProperty("班级名称")
    private String className;
    /**
     *班级编码
     */
    @ExcelProperty("班级编码")
    private String sign;

}
