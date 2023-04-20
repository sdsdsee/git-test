package com.eluolang.module.socket.server.service.feign;

import com.eluolang.common.core.pojo.EllDeviceAlarm;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.factory.RemotePlatformFallBackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用platform服务的service层
 *
 * @author dengrunsen
 */
@FeignClient(value = "ELL-platform", fallbackFactory = RemotePlatformFallBackFactory.class)
public interface PlatformRemoteService {

    /**
     * 添加警报
     * @param ellDeviceAlarm
     * @return
     */
    @ApiOperation("添加警报")
    @PostMapping("/addAlarm")
    Result addAlarm(@RequestBody EllDeviceAlarm ellDeviceAlarm);

}
