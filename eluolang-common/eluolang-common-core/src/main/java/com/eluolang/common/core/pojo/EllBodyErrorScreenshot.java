package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllBodyErrorScreenshot implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 项目id
     */
    private String proId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 错误原因
     */
    private String errorTraces;
    /**
     * 截图nginx地址
     */
    private String errorImageUri;
    /**
     * 是否是重测
     */
    private int isRetest;
}
