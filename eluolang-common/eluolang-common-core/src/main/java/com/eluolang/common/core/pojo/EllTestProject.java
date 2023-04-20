package com.eluolang.common.core.pojo;

import lombok.*;

import java.io.Serializable;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EllTestProject implements Serializable {
    /**
     * id
     */
    private Integer id;
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
    /**
     * 创建人
     */
    private String createById;
    /**
     * 修改人
     */
    private String updateById;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 父项目id
     */
    private String parentId;
    /**
     * 项目简介
     */
    private String synopsis;
}
