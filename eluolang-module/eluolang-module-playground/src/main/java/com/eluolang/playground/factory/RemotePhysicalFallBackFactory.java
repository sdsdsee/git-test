package com.eluolang.playground.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.playground.service.feign.PhysicalRemoteService;
import com.eluolang.playground.vo.EllHistoryVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * physical服务降级处理
 * @author dengrunsen
 */
@Component
public class RemotePhysicalFallBackFactory implements FallbackFactory<PhysicalRemoteService> {

    private static final Logger log = LoggerFactory.getLogger(RemotePhysicalFallBackFactory.class);

    @Override
    public PhysicalRemoteService create(Throwable throwable) {
        return new PhysicalRemoteService() {

            @Override
            public Result submitScores(EllHistoryVo ellHistoryVo) throws IOException {
                return new Result(HttpStatus.ERROR,null,null);
            }
        };
    }
}
