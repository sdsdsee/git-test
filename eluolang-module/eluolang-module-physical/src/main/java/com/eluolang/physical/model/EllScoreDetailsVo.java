package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreDetailsVo {
    //账号id
    private int accountOrgId;
    //部门id
    private int orgId;
    //计划id
    private String planId;
    //项目id
    private int proId;
}
