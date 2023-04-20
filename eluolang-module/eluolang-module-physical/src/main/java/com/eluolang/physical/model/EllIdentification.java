package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllIdentification {
    /**
     * userId
     */
    private String userId;
    /**
     * 标识
     */
    private String identification;
    /**
     * 部门编号
     */
    private String orgId;
}
