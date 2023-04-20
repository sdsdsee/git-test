package com.eluolang.device.dto;

import com.eluolang.device.util.RtspToMP4Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartTreadDto {
    ///开始的线程
    private Thread startTread;
    //创建线程的类
    private boolean isStart;
}
