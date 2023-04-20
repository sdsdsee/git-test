package com.eluolang.physical.model;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProjectVo {
    /**
     * id
     */
    @ApiParam(hidden = true)
    private int id;
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
     * 创建人/修改人
     */
    private String createBy;
    /**
     * 图标
     */
    private String proIcon;
    /**
     * 项目简介
     */
    private String synopsis;
}
