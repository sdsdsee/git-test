package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAvoidVo {
    /**
     * 申请id
     */
    private String id;
    /**
     * 部门id
     */
    private int orgId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 项目id
     */
    private int proId;
    private int page;
    /**
     * accountId账号id
     */
    private int accountId;
    /**
     * 审核时间
     */
    private String auditTime;
    /**
     * 提交时间
     */
    private String submitTime;
    /**
     * 姓名
     */
    private String userName;
}
