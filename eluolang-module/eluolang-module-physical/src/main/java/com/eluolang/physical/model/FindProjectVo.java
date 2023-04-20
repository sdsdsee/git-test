package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindProjectVo {
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
     * 图标
     */
    private String proIcon;
    /**
     * 缩写
     */
    private String proNameAbbreviation;
}
