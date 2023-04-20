package com.eluolang.module.sys.user.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDeriveDepart {
    /**
     * 年级
     */
    @ExcelProperty("学校名称")
    private String schoolName;
    /**
     * 年级
     */
    @ExcelProperty("年级")
    private String grade;
    /**
     * id
     */
    @ExcelProperty("部门id")
    private int id;
    /**
     * 部门名称
     */
    @ExcelProperty("班级名称")
    private String deptName;
    @ExcelIgnore
    private int parentId;
}
