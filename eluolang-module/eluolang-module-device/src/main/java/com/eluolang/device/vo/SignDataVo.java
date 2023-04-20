package com.eluolang.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年07月28日 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignDataVo {
    /** 部门id */
    private Integer deptId;
    /** 学校名称 */
    private String deptName;
    /** 应检人数 */
    private Integer shouldCheckedNum;
    /** 已检人数 */
    private Integer checkedNum;
}
