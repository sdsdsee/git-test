package com.eluolang.common.log.factory;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.service.RemoteLogService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志服务降级处理
 * @author ZengXiaoQian
 * @createDate 2020-9-10
 */

@Slf4j
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {

    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService()
        {
            @Override
            public Result insertLog(OperLog operLog)
            {
                return new Result(HttpStatus.ERROR);
            }
        };
    }
}
