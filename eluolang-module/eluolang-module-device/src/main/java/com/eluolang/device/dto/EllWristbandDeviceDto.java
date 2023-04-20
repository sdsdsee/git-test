package com.eluolang.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年11月09日 10:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllWristbandDeviceDto {
    /** 姓名 */
    private String name;
    /** mac地址 */
    private String mac;
    /** 手环NFCid */
    private String nfcId;
    /** 管理员帐号id */
    private Integer optId;
    /** 当前页码 */
    private Integer pageNum;
    /** 页面显示条数 */
    private Integer pageSize;
}
