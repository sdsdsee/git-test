package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年10月13日 9:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentRunningDetailsVo {
    /** 用户编号 */
    private String userId;
    /** 用户姓名 */
    private String userName;
    /** 耗时 */
    private String time;
    /** 里程 */
    private String mileageSum;
}
