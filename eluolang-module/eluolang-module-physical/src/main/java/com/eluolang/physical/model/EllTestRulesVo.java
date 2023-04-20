package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllTestRulesVo {
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
    /**
     * 项目名称
     */
    private String proName;
    /**
     * 单位(cm,m,s)
     */
    private String proUnit;
    /**
     * 项目名称简写
     */
    private String proNameAbbreviation;
    /**
     * 删除标识
     */
    private int isDelete;
    /**
     * 图标
     */
    private String proIcon;
}
