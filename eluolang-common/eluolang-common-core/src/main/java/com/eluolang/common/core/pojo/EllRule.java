package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllRule {
    private int id;
    private String ruleName;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 创建人ID
     */
    private String createById;
    /**
     * 路径
     */
    private String path;
    /**
     * 项目id
     */
    private String proId;
}
