package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSmartVideoDto {
    //项目名称
    private String proName;
    //视频地址
    private String filePath;
}
