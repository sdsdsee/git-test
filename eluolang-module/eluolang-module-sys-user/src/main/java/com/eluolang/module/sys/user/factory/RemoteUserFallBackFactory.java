package com.eluolang.module.sys.user.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.sys.user.service.feign.UserRemoteService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证服务降级处理
 *
 * @author ZengXiaoQian
 * @createDate 2020-9-28
 */
@Component
@Slf4j
public class RemoteUserFallBackFactory implements FallbackFactory<UserRemoteService> {

    @Override
    public UserRemoteService create(Throwable throwable) {
        log.error("认证服务调用失败:{}", throwable.getMessage());
        return new UserRemoteService() {
            @Override
            public Result logout(HttpServletRequest request) {
                return new Result(HttpStatus.ERROR);
            }
        };
    }
}
