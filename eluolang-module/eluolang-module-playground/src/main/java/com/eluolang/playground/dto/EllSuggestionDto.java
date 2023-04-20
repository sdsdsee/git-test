package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSuggestionDto {
    //项目id
    private Integer proId;
    //男女
    private Integer sex;
    //评价
    private String comment;
    //建议内容
    private String guide;
}
