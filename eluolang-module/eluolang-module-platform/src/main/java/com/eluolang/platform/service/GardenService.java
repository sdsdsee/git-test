package com.eluolang.platform.service;


import com.eluolang.common.core.pojo.EllLargeScreen;
import com.eluolang.platform.vo.EllAlarmVo;
import com.eluolang.platform.vo.EllDeNameAndId;
import com.eluolang.platform.vo.EllDeviceVo;


import java.util.List;

public interface GardenService {
    /**
     * 查询警报
     *
     * @param createBy
     * @return
     */
    List<EllAlarmVo> selAlarm(int createBy, int deviceType);

    /**
     * 查询月用电量/0本月1上月2前月
     */
    int selElectricity(int createBy, int lastMonth,int deviceType);
    /**
     * 查询设备数据
     *
     * @param ellDevice 查询条件
     * @return 对象列表
     */
    List<EllDeviceVo> queryAllByLimit(EllLargeScreen ellDevice);
    /**
     * 查询进几天的所有设备的用电量
     */
    int selDayElectricity(int createBy,int dayTime,int deviceType);
    /**
     * 查询设备名称/id/报警次数
     */
    List<EllDeNameAndId> selDeviceNmeAndId(int createBy, int deviceType);

}
