package com.eluolang.device.service;

import com.eluolang.common.core.pojo.ToiletEvaluate;

import java.util.List;

/**
 * 定义业务层的设备接口方法
 * @author dengrunsen
 */
public interface LargeScreenService {

    /**
     * 保存心跳信息
     * @param data
     * @return
     */
    String saveHeartBeat(String data);

    /**
     * 保存设备用电量
     * @param data
     * @return
     */
    String saveUseElectricityAmount(String data);

    /**
     * 保存设备环境参数
     * @param data
     * @return
     */
    String saveDeviceEnvironmentParam(String data);

    /**
     * 根据设备id查询有权限的管理员信息
     * @param deviceId
     * @return
     */
    List<Integer> selOperIdByDeviceId(String deviceId);

    /**
     * 添加厕所评价记录
     * @param toiletEvaluate
     * @return
     */
    int addToiletEvaluate(ToiletEvaluate toiletEvaluate);

    /**
     * 根据设备id查询最新的20条评价
     * @param deviceId
     * @return
     */
    List<ToiletEvaluate> selToiletEvaluate(String deviceId);

    /**
     * 查询设备各星级评价总数
     * @param deviceId
     * @param level
     * @return
     */
    int selToiletEvaluateByLevel(String deviceId,Integer level);
}
