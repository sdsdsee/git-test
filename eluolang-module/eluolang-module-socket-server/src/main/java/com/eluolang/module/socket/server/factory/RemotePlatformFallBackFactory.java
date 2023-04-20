package com.eluolang.module.socket.server.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllDeviceAlarm;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.service.feign.PlatformRemoteService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * platform服务降级处理
 * @author dengrunsen
 */
@Component
public class RemotePlatformFallBackFactory implements FallbackFactory<PlatformRemoteService> {

    private static final Logger log = LoggerFactory.getLogger(RemotePlatformFallBackFactory.class);

    @Override
    public PlatformRemoteService create(Throwable throwable) {
        return new PlatformRemoteService() {

            @Override
            public Result addAlarm(EllDeviceAlarm ellDeviceAlarm) {
                return new Result(HttpStatus.ERROR,null,null);
            }
        };
    }
}
