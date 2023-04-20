package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSubscribeNumVo {
    /**
     * 已预约人数
     */
    private int numSubscribe;
    /**
     * 预约总数
     */
    private int num;
}
