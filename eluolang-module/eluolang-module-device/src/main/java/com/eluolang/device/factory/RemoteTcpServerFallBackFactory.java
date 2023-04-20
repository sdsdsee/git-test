package com.eluolang.device.factory;


import com.eluolang.device.service.feign.TcpServerRemoteService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * tcp服务降级处理
 * @author renzhixing
 */
@Component
public class RemoteTcpServerFallBackFactory implements FallbackFactory<TcpServerRemoteService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteTcpServerFallBackFactory.class);


    @Override
    public TcpServerRemoteService create(Throwable throwable) {
        return null;
    }
}
