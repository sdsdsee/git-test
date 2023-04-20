package com.eluolang.module.socket.server.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.service.feign.SystemManageRemoteService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 报表管理服务降级处理
 * 
 * @author renzhixing
 */
@Component
public class RemoteSystemManageFallbackFactory implements FallbackFactory<SystemManageRemoteService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteSystemManageFallbackFactory.class);

    @Override
    public SystemManageRemoteService create(Throwable throwable) {
        return new SystemManageRemoteService() {
            @Override
            public Result getIdent() {
                return new Result(HttpStatus.ERROR,null,null);
            }
        };
    }
}
