package com.eluolang.platform.service;


import com.eluolang.common.core.pojo.EllDeviceAlarm;
import com.eluolang.platform.vo.EllDataVo;

import java.util.List;

public interface RecordsService {
    EllDataVo selData(int createBy);

    /**
     * 查询报警消息
     *
     * @param createBy
     * @return
     */
    List<EllDeviceAlarm> selAlarm(int createBy);
    /**
     * 查询进几天的所有设备的用电量
     */
    int selDayElectricity(int createBy,int dayTime,int deviceType);
    /**
     * 查询月用电量/0本月1上月2前月
     */
    int selElectricity(int createBy, int lastMonth,int deviceType);

}
