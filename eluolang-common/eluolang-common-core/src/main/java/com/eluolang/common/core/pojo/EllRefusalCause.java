package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllRefusalCause implements Serializable, Cloneable {
    /**
     *
     */
    private String id;
    /**
     * 免测申请id
     */
    private String avoidTestId;
    /**
     * 原因
     */
    private String cause;
}