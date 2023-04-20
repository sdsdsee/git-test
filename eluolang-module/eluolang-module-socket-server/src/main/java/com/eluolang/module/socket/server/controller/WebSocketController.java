package com.eluolang.module.socket.server.controller;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * webSocket服务控制器层
 *
 * @author dengrunsen
 */
@Api(tags = "webSocket服务控制器")
@RestController
@RequestMapping("webSocket")
public class WebSocketController {

    @GetMapping("index")
    public String index(){
        return "请求成功";
    }

    @ApiOperation(value = "点对点发送消息", notes = "点对点发送消息")
    @PostMapping("/push/{toUserId}")
    public Result sendTo(String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message,toUserId);
        return new Result(HttpStatus.SUCCESS, "发送成功");
    }

    @ApiOperation(value = "webSocket广播消息", notes = "webSocket广播消息")
    @PostMapping("/sendToAll")
    public Result sendToAll(String message) throws IOException {
        WebSocketServer.sendToAll(message);
        return new Result(HttpStatus.SUCCESS, "发送成功");
    }

}
