package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年11月03日 14:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCallVo {
    /** 跑步人员id */
    private String userId;
    /** 跑步人员姓名 */
    private String userName;
    /** 班级名称 */
    private String className;
    /** 学号 */
    private String studentId;
    /** 手环mac */
    private String mac;
    /** 项目简称 */
    private String proName;
    /** 耗时 */
    private String time;
    /** 当前心率 */
    private String heart;
    /** 里程 */
    private String mileage;
}
