package com.eluolang.playground.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EllTestVideoCmdDto {
    //命令 0播放1结束2暂停
    @ApiModelProperty(name = "命令", value = "0播放1结束2暂停", required = true)
    private int cmd;
    //所控制的大屏设备Id
    @ApiModelProperty(name = "所控制的大屏设备Id", value = "", required = true)
    private String toDevice;
    //文件地址
    @ApiModelProperty(name = "文件地址", value = "", required = true)
    private String url;
}
