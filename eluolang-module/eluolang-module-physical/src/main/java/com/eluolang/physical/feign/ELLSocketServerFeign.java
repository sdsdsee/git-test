package com.eluolang.physical.feign;

import com.eluolang.common.core.web.Result;
import com.eluolang.physical.feign.FeignFallBack.ELLSocketServerFeignFallBack;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(value = "ELL-socket-server", fallback = ELLSocketServerFeignFallBack.class)
public interface ELLSocketServerFeign {
    @ApiOperation(value = "webSocket广播消息", notes = "webSocket广播消息")
    @PostMapping("webSocket/sendToAll")
    public Result sendToAll(@RequestParam("message") String message) throws IOException;
}
