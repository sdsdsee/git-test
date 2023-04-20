package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllEyesInfoDto {
    //右眼视力
    private Double rightVision;
    //左眼视力
    private Double leftVision;
    //左眼屈光度
    private Double leftDiopter;
    //右眼屈光度
    private Double rightDiopter;
    //日期
    private String testTime;
}
