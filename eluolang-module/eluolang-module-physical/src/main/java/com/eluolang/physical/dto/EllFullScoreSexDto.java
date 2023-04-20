package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFullScoreSexDto {
    //人数
    private int numBoy;
    //达标率
    private int numGirl;
    //项目名称
    private String proName;
}
