package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipNumber implements Serializable, Cloneable {
    /**
     *
     */
    private String id;
    /**
     * 跳绳用户id
     */
    private String skipUserId;
    /**
     * 跳绳个数
     */
    private int skipNum;
    /**
     * 平均数
     */
    private Double skipAvg;
    /**
     * 跳绳模式
     */
    private int skipPattern;
    /**
     * 跳绳时间
     */
    private int skipTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 是否为家庭作业0否1是
     */
    private Integer isHomework;
    /**
     * 家庭作业id
     */
    private String homeworkId;
}