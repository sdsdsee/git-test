package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllAvoidTest implements Serializable, Cloneable {
    /**
     *
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 免测理由
     */
    private String reason;
    /**
     * 状态(1待确认2确认3驳回
     */
    private int state;
    /**
     * 驳回原因
     */
    private String cause;
    /**
     * 处理人id
     */
    private String conductorId;
    /**
     * 部门id
     */
    private Integer orgId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 审核时间
     */
    private String auditTime;
    /**
     * 是否为重新提交0为否1为是
     */
    private int isResubmit;
    /**
     * 提交时间
     */
    private String submitTime;
}