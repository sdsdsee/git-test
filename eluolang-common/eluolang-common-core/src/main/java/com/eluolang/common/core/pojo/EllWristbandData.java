package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 手环数据表
 * @author dengrunsen
 * @date 2022年10月09日 11:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllWristbandData {
    /** 数据来源 0：广播获取  1：连接获取 */
    private String source;
    /** 手环mac地址 */
    private String mac;
    /** 位置ID */
    private String location;
    /** 切换到当前位置ID的时间点，unix时间戳单位为ms */
    private String locationTime;
    /** 心率 */
    private String heart;
    /** 温度 */
    private String temperature;
    /** 血氧 百分比 */
    private String oxygen;
    /** 心率 温度 血氧 的测量时间 */
    private String measureTime;
    /** 步数 */
    private String step;
    /** 浅睡 */
    private String lightSleep;
    /** 深睡 */
    private String deepSleep;
    /** 电量 */
    private String battery;
    /** sos状态  1：触发报警  0：无报警 */
    private String sos;
    /** 0 关闭测量 1 心率开  2血氧开 */
    private String wristbandStatus;
}
