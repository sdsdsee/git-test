package com.eluolang.common.core.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年11月09日 9:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllWristbandDevice {
    /** 主键id */
    @ExcelIgnore
    private String id;
    /** 姓名 */
    @ExcelProperty("手环名称")
    private String name;
    /** mac地址 */
    @ExcelProperty("手环mac")
    private String mac;
    /** 手环NFCid */
    @ExcelProperty("手环NFC")
    private String nfcId;
    /** 部门id */
    @ExcelProperty("部门")
    private Integer deptId;
}
