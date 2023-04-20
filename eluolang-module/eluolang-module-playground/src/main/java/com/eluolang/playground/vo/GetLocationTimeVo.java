package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月20日 11:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLocationTimeVo {
    /**
     * 跑步人员id
     */
    private String userId;

    /**
     * 触发点id
     */
    private String location;

    /**
     * 距离开始点位
     */
    private String locationTime;

    /**
     * 里程
     */
    private String rank;
}
