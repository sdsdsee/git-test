package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFullScoreDto {
    //人数
    private int num;
    //达标率
    private String percent;
    //项目名称
    private String proName;
}
