package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChanceVo {
    private String planId;
    private String createById;
    private int chance;
    private int proId;
    private String proName;
    /**
     * 项目使用性别(1男2女)两者都(1,2)
     */
    private String useSex;
    private int time;
    private String essential;
    private String id;
    /**
     * 项目占比
     */
    private String proGdp;
}
