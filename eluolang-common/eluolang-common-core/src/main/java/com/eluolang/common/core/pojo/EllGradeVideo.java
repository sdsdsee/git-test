package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllGradeVideo implements Serializable, Cloneable {
    /**
     * 租户号
     */
    private String id;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 学生id
     */
    private String userId;
    /**
     * 项目id
     */
    private int projectId;
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 是否为重测视频
     */
    private int isRetest;
}