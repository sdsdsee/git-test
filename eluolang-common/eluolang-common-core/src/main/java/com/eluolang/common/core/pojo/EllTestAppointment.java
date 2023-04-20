package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllTestAppointment implements Serializable, Cloneable {
    /**
     * id
     */
    private Integer id;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 人数
     */
    private Integer num;
}