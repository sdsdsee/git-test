package com.eluolang.platform.fegin;

import com.eluolang.common.core.hardware.dto.CancelInfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.InfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.OnOffDeviceDto;
import com.eluolang.common.core.web.Result;
import com.eluolang.platform.fegin.fallBack.RemoteTcpServerFallBackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 远程调用tcp服务的service层
 *
 * @author renzhixing
 */
@FeignClient(value = "ELL-socket-server", fallbackFactory = RemoteTcpServerFallBackFactory.class)
public interface TcpServerRemoteService {
    /**
     * 信息发布
     *
     * @param infoDeliveryDto
     * @return
     */
    @ApiOperation(value = "信息发布", notes = "信息发布")
    @RequestMapping(value = "/tcp/server/infoDelivery", method = RequestMethod.POST)
    public Result infoDelivery(@RequestBody InfoDeliveryDto infoDeliveryDto) throws Exception;

    /**
     * 开关机设备
     *
     * @param onOffDeviceDto
     * @return
     */
    @ApiOperation(value = "开关机设备", notes = "开关机设备")
    @RequestMapping(value = "/tcp/server/onOffDevice", method = RequestMethod.POST)
    public Result onOffDevice(@RequestBody OnOffDeviceDto onOffDeviceDto) throws Exception;

    /**
     * 撤销信息发布
     *
     * @param cancelInfoDeliveryDto
     * @return
     */
    @ApiOperation(value = "撤销信息发布", notes = "撤销信息发布")
    @RequestMapping(value = "/tcp/server/cancelInfoDelivery", method = RequestMethod.POST)
    public Result cancelInfoDelivery(@RequestBody CancelInfoDeliveryDto cancelInfoDeliveryDto) throws Exception;

    @ApiOperation(value = "点对点发送消息", notes = "点对点发送消息")
    @PostMapping("/webSocket/push/{toUserId}")
    public Result sendTo(@RequestParam("message") String message, @PathVariable(value = "toUserId") String toUserId) throws Exception;
}
