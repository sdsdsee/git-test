package com.eluolang.platform.redis;

import com.eluolang.platform.vo.DeviceEnvironmentParamDtoVo;
import com.eluolang.platform.vo.EllDeNameAndId;
import com.eluolang.platform.vo.EllDeviceVo;
import com.eluolang.platform.vo.EllToiletEnvironmentVo;

import java.util.List;

public interface DeviceRService {
    /**
     * 查询设备是否正常
     */
    boolean selOnDeviceCount(String deviceId);

    /**
     * 查询是否开机
     */
    boolean selIsOpen(String deviceId);

    /**
     * 园区查询环境参数
     */
    List<DeviceEnvironmentParamDtoVo> selEnvironment(List<EllDeNameAndId> ellDeNameAndIdList, int deviceType);

    /**
     * 设备单独环境参数
     */
    List<EllDeviceVo> selDeviceEnvironment(List<EllDeviceVo> ellDeviceVoList);

    /**
     * 添加环境参数
     */
    void addEnvironment(String deviceId, EllToiletEnvironmentVo ellToiletEnvironmentVo);

    /**
     * 保存车位
     */
    void addTruckSpaceNum(String LEDId, int trucSpace);

    /**
     * 查询车位
     */
    int selTruckSpaceNum(String LEDId);
}
