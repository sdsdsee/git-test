package com.eluolang.module.sys.user.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年07月22日 14:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDepartVo {
    /***
     * 学校
     */
    @ExcelProperty("学校")
    private String schoolId;
    /**
     * 年级
     */
    @ExcelProperty("年级")
    private String gradeName;
    /**
     * 班级
     */
    @ExcelProperty("班级")
    private String className;
}
