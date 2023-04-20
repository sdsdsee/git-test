package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllProRankDto {
    //项目名称
    private String proName;
    //单位
    private String unit;
    //学生
    private List<EllProRankUserDto> users;
}

