package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllAvoidProject implements Serializable,Cloneable {
    /**
     *
     */
    private String id;
    /**
     * 项目id
     */
    private int proId;
    /**
     * 项目名称
     */
    private String proName;
    /**
     * 免测申请id
     */
    private String avoidTestId;
}