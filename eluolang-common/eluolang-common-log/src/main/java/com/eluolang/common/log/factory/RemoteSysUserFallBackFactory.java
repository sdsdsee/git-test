package com.eluolang.common.log.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.service.RemoteSysUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ZengXiaoQian
 * @createDate 2020-11-14
 */
@Slf4j
@Component
public class RemoteSysUserFallBackFactory implements FallbackFactory<RemoteSysUserService> {

    @Override
    public RemoteSysUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteSysUserService() {
            @Override
            public Result getRealNameByName(String name) {
                return new Result(HttpStatus.ERROR);
            }
        };
    }
}
