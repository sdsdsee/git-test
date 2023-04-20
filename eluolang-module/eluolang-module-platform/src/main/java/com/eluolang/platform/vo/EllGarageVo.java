package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllGarageVo {
    //停车库唯一标识码
    private String parkSyscode;
    //停车库名称
    private String parkName;
    //父停车库唯一标识
    private String parentParkSyscode;
    //停车库车位总数
    private String totalPlace;
    //停车库固定车位总数
    private String totalPermPlace;
    //停车库固定车位总数
    private String totalReservePlace;
    //停车库车位剩余数
    private String leftPlace;
    //停车库固定车位剩余数
    private String leftPermPlace;
    //停车库预约车位剩余数
    private String leftReservePlace;
}
