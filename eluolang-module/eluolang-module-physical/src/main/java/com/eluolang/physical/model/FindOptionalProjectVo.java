package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindOptionalProjectVo {
    //计划id
    private String planId;
    //项目id
    private String proId;
    //使用性别
    private String useSex;
    //项目名称
    private String proName;
    //自选数量
    private int customizeTheNumber;
    //项目选择的人数
    private int num;
}
