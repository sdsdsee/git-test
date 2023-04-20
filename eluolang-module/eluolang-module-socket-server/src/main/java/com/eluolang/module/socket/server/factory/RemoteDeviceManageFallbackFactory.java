package com.eluolang.module.socket.server.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.service.feign.DeviceManageRemoteService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 设备管理服务降级处理
 * 
 * @author renzhixing
 */
@Component
public class RemoteDeviceManageFallbackFactory implements FallbackFactory<DeviceManageRemoteService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteDeviceManageFallbackFactory.class);

    @Override
    public DeviceManageRemoteService create(Throwable throwable)
    {
        log.error("设备服务调用失败:{}", throwable.getMessage());
        return new DeviceManageRemoteService()
        {
            @Override
            public Result saveHeartBeat(String dataBytes) {
                return new Result(HttpStatus.ERROR,null,null);
            }

            @Override
            public Result saveUseElectricityAmount(String data) {
                return new Result(HttpStatus.ERROR,null,null);
            }

            @Override
            public Result saveDeviceEnvironmentParam(String data) {
                return new Result(HttpStatus.ERROR,null,null);
            }

            @Override
            public Result selOperIdByDeviceId(String deviceId) {
                return new Result(HttpStatus.ERROR,null,null);
            }
        };
    }
}
