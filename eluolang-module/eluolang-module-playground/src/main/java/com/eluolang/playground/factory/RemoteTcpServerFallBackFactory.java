package com.eluolang.playground.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.playground.service.feign.TcpServerRemoteService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * tcp服务降级处理
 * @author renzhixing
 */
@Component
public class RemoteTcpServerFallBackFactory implements FallbackFactory<TcpServerRemoteService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteTcpServerFallBackFactory.class);

    @Override
    public TcpServerRemoteService create(Throwable throwable) {
        return new TcpServerRemoteService() {
            @Override
            public Result sendTo(String message, String toUserId) throws Exception {
                return new Result(HttpStatus.ERROR,null,null);
            }

            @Override
            public Result sendToAll(String message) throws IOException {
                return new Result(HttpStatus.ERROR,null,null);
            }
        };
    }
}
