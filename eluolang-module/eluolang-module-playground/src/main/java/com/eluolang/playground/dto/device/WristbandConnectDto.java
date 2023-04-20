package com.eluolang.playground.dto.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年10月31日 11:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WristbandConnectDto {
    /** 指令 */
    private String cmd;
    /** 网关路数 */
    private String bleNum;
    /** 手环数量 */
    private String macNum;
    /** 手环mac地址集合 */
    private List<String> mac;
}
