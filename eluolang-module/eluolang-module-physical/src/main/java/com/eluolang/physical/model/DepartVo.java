package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartVo {
    /**
     * 部门id
     */
    private String id;
    /**
     * 部门名
     */
    private String deptName;
    /**
     * 父类id
     */
    private String parentId;
    //测试时候使用
    /*    private String userName;*/
}
