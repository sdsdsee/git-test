package com.eluolang.module.socket.server.controller;

import com.eluolang.common.core.constant.HardwareStatus;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.hardware.dto.CancelInfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.DeleteDeviceDto;
import com.eluolang.common.core.hardware.dto.InfoDeliveryDto;

import com.eluolang.common.core.hardware.dto.OnOffDeviceDto;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.service.PlatformCallHardwareService;
import com.eluolang.module.socket.server.service.feign.DeviceManageRemoteService;
import com.eluolang.module.socket.server.service.feign.SystemManageRemoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *tcp服务控制器层
 *
 * @author dengrunsen
 * @date 2022年05月20日 10:46
 */
@Api(tags = "tcp服务控制器")
@RestController
@RequestMapping("tcp/server")
public class SocketServerController {

    private static Logger logger = LoggerFactory.getLogger(SocketServerController.class.getName());

    @Resource
    PlatformCallHardwareService platformCallHardwareService;

    @Autowired
    SystemManageRemoteService systemManageRemoteService;
    @Autowired
    DeviceManageRemoteService deviceManageRemoteService;
    /**
     * 信息发布
     *
     * @param infoDeliveryDto
     * @return
     */
    @ApiOperation(value = "信息发布", notes = "信息发布")
    @RequestMapping(value = "/infoDelivery", method = RequestMethod.POST)
    public Result infoDelivery(@RequestBody InfoDeliveryDto infoDeliveryDto) throws Exception {
        //封装下发给硬件的参数对象
        infoDeliveryDto.setCmd(HardwareStatus.INFO_DELIVERY);
        infoDeliveryDto.setIdent((Integer)systemManageRemoteService.getIdent().getData());
        Integer count = platformCallHardwareService.infoDelivery(infoDeliveryDto);
        return new Result(HttpStatus.SUCCESS, "操作成功",count);
    }

    /**
     * 撤销信息发布
     *
     * @param cancelInfoDeliveryDto
     * @return
     */
    @ApiOperation(value = "撤销信息发布", notes = "撤销信息发布")
    @RequestMapping(value = "/cancelInfoDelivery", method = RequestMethod.POST)
    public Result cancelInfoDelivery(@RequestBody CancelInfoDeliveryDto cancelInfoDeliveryDto) throws Exception {
        //封装下发给硬件的参数对象
        cancelInfoDeliveryDto.setCmd(HardwareStatus.CANCEL_INFO_DELIVERY);
        cancelInfoDeliveryDto.setIdent((Integer)systemManageRemoteService.getIdent().getData());
        Integer count = platformCallHardwareService.cancelInfoDelivery(cancelInfoDeliveryDto);
        return new Result(HttpStatus.SUCCESS, "操作成功",count);
    }

    /**
     * 开关机设备
     *
     * @param onOffDeviceDto
     * @return
     */
    @ApiOperation(value = "开关机设备", notes = "开关机设备")
    @RequestMapping(value = "/onOffDevice", method = RequestMethod.POST)
    public Result onOffDevice(@RequestBody OnOffDeviceDto onOffDeviceDto) throws Exception {
        //封装下发给硬件的参数对象
        onOffDeviceDto.setCmd(HardwareStatus.ON_OFF_DEVICE);
        onOffDeviceDto.setIdent((Integer)systemManageRemoteService.getIdent().getData());
        Integer count = platformCallHardwareService.onOffDevice(onOffDeviceDto);
        return new Result(HttpStatus.SUCCESS, "操作成功",count);
    }

    /**
     * 删除设备
     *
     * @param deleteDeviceDto
     * @return
     */
    @ApiOperation(value = "删除设备", notes = "删除设备")
    @RequestMapping(value = "/deleteDevice", method = RequestMethod.POST)
    public Result deleteDevice(@RequestBody DeleteDeviceDto deleteDeviceDto) throws Exception {
        //封装下发给硬件的参数对象
        deleteDeviceDto.setCmd(HardwareStatus.DELETE_DEVICE);
        deleteDeviceDto.setIdent((Integer)systemManageRemoteService.getIdent().getData());
        Integer count = platformCallHardwareService.deleteDevice(deleteDeviceDto);
        return new Result(HttpStatus.SUCCESS, "操作成功",count);
    }
}
