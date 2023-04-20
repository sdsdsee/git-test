package com.eluolang.device.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllElectricQuantity;
import com.eluolang.common.core.web.Result;
import com.eluolang.device.service.feign.PlatformRemoteService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * platform服务降级处理
 * @author renzhixing
 */
@Component
public class RemotePlatformFallBackFactory implements FallbackFactory<PlatformRemoteService> {

    private static final Logger log = LoggerFactory.getLogger(RemotePlatformFallBackFactory.class);

    @Override
    public PlatformRemoteService create(Throwable throwable) {
        return new PlatformRemoteService() {
            @Override
            public Result addElectricity(EllElectricQuantity ellElectricQuantity){
                return new Result(HttpStatus.ERROR,null,null);
            }
        };
    }
}
