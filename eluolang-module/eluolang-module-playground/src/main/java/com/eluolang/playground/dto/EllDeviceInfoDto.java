package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDeviceInfoDto {
    //id
    private String infoId;
    //滑动间隔时间
    private Integer intervalTime;
    //类型
    private Integer infoType;
    //文本
    private String text;
    //文件地址
    private List<String> fileList;
}
