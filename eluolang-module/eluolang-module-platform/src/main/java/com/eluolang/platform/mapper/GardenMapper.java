package com.eluolang.platform.mapper;

import com.eluolang.common.core.pojo.EllLargeScreen;
import com.eluolang.platform.vo.EllAlarmVo;
import com.eluolang.platform.vo.EllDeNameAndId;
import com.eluolang.platform.vo.EllDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GardenMapper {
    /**
     * 查询警报
     *
     * @param createBy
     * @return
     */
    List<EllAlarmVo> selAlarm(@Param("createBy") int createBy, @Param("deviceType") int deviceType);

    /**
     * 查询月用电量/0本月1上月2前月
     */
    int selElectricity(@Param("createBy") int createBy, @Param("lastMonth") int lastMonth, @Param("deviceType") int deviceType);

    /**
     * 查询设备数据
     *
     * @param ellDevice 查询条件
     * @return 对象列表
     */
    List<EllDeviceVo> queryAllByLimit(@Param("ellDevice") EllLargeScreen ellDevice);

    /**
     * 查询进几天的所有设备的用电量
     */
    int selDayElectricity(@Param("createBy") int createBy, @Param("dayTime") int dayTime, @Param("deviceType") int deviceType);

    /**
     * 查询设备名称/id/报警次数
     */
    List<EllDeNameAndId>  selDeviceNmeAndId(@Param("createBy") int createBy, @Param("deviceType") int deviceType);
}
