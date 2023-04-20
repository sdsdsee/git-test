package com.eluolang.platform.mapper;

import com.eluolang.common.core.pojo.EllDevice;
import com.eluolang.common.core.pojo.EllDeviceAlarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecordsMapper {
    /**
     * 查询设备
     */
    List<EllDevice> findDevice(int createBy);

    /**
     * 报警总数
     */
    int selAlarmCount(int createBy);

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
    int selDayElectricity(@Param("createBy") int createBy, @Param("dayTime") int dayTime, @Param("deviceType") int deviceType);

    /**
     * 查询月用电量/0本月1上月2前月
     */
    int selElectricity(@Param("createBy") int createBy, @Param("lastMonth") int lastMonth, @Param("deviceType") int deviceType);
}
