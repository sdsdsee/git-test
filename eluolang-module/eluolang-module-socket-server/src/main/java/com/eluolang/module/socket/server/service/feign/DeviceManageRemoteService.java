package com.eluolang.module.socket.server.service.feign;

import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.factory.RemoteDeviceManageFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用设备管理微服务的service层
 *
 * @author renzhixing
 */
@FeignClient(value = "ELL-device", fallbackFactory = RemoteDeviceManageFallbackFactory.class)
public interface DeviceManageRemoteService {

    /**
     * 保存心跳信息
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/device/saveHeartBeat", method = RequestMethod.POST)
    Result saveHeartBeat(@RequestParam("data") String data);


    /**
     * 保存设备用电量
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/device/saveUseElectricityAmount", method = RequestMethod.POST)
    Result saveUseElectricityAmount(@RequestParam("data") String data);


    /**
     * 保存设备环境参数
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/device/saveDeviceEnvironmentParam", method = RequestMethod.POST)
    Result saveDeviceEnvironmentParam(@RequestParam("data") String data);

    /**
     * 根据设备id查询有权限的管理员信息
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/device/selOperIdByDeviceId", method = RequestMethod.POST)
    Result selOperIdByDeviceId(@RequestParam("deviceId") String deviceId);
}
