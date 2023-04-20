package com.eluolang.playground.dto.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年10月31日 13:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WristbandTimingDto {
    /** 指令 */
    private String type;
    /** 当前时间时间戳 */
    private String time;
//    /** 消息内容 */
//    private String message;
//    /** 手环数量 */
//    private String macNum;
//    /** 手环mac地址集合 */
//    private List<String> mac;
}
