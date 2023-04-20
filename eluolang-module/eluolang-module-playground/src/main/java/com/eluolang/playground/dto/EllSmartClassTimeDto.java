package com.eluolang.playground.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSmartClassTimeDto {

    //第几节课
    @ApiModelProperty(name = "第几节课", value = "第几节课:1", required = true)
    private String index;
    //上课开始时间
    @ApiModelProperty(name = "上课开始时间", value = "上课开始时间", required = true)
    private String beginTime;
    //上课结束时间
    @ApiModelProperty(name = "上课结束时间", value = "上课结束时间", required = true)
    private String endTime;

}
