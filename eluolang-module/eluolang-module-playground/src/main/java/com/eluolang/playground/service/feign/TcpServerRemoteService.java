package com.eluolang.playground.service.feign;

import com.eluolang.common.core.web.Result;
import com.eluolang.playground.factory.RemoteTcpServerFallBackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * 远程调用tcp服务的service层
 *
 * @author renzhixing
 */
@FeignClient(value = "ELL-socket-server", fallbackFactory = RemoteTcpServerFallBackFactory.class)
public interface TcpServerRemoteService {

    @ApiOperation(value = "点对点发送消息", notes = "点对点发送消息")
    @PostMapping("/webSocket/push/{toUserId}")
    public Result sendTo(@RequestParam("message") String message, @PathVariable(value = "toUserId") String toUserId) throws Exception;

    @PostMapping("/webSocket/sendToAll")
    public Result sendToAll(@RequestParam("message")String message) throws IOException;
}
