package com.eluolang.physical.model;

import com.eluolang.common.core.pojo.EllAvoidProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvoidApplyVo {
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
     * 处理人id
     */
    private String conductorId;
    /**
     * 驳回免测原因
     */
    private String cause;
    /**
     * 部门id
     */
    private int orgId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 审核时间
     */
    private String auditTime;
    /**
     * 提交时间
     */
    private String submitTime;
    /**
     * 是否为重新提交0为否1为是
     */
    private int isResubmit;
    /**
     * 学生姓名
     */
    private String userName;
    /**
     * 免测项目
     */
    private List<EllAvoidProject> ellAvoidProjectList;
    /**
     * 计划标题
     */
    private String planTitle;
    /**
     * 计划地址
     */
    private String address;
    /**
     * 计划开始时间
     */
    private String dateBegin;
    /**
     * 计划结束时间
     */
    private String dateEnd;
    /**
     * imagePath
     */
    private String imagePath;

}
