package com.eluolang.module.socket.server.service.impl;

import com.alibaba.fastjson.JSON;

import com.eluolang.common.core.hardware.dto.CancelInfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.DeleteDeviceDto;
import com.eluolang.common.core.hardware.dto.InfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.OnOffDeviceDto;
import com.eluolang.common.redis.service.RedisService;

import com.eluolang.module.socket.server.config.NettyServerHandler;
import com.eluolang.module.socket.server.config.NewDeviceProtocol;
import com.eluolang.module.socket.server.service.PlatformCallHardwareService;

import com.eluolang.module.socket.server.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dengrunsen
 * @date 2022年05月20日 9:51
 */
@Service
public class PlatformCallHardwareServiceImpl implements PlatformCallHardwareService {

    private static Logger logger = LoggerFactory.getLogger(PlatformCallHardwareServiceImpl.class.getName());

    @Autowired
    RedisService redisService;

    @Override
    public Integer infoDelivery(InfoDeliveryDto infoDeliveryDto) {
        //将本次操作的命令标识存入redis当中
        redisService.setCacheObject(Constant.IDENT + infoDeliveryDto.getIdent(), JSON.toJSONString(infoDeliveryDto));
        //获取到deviceId,通过deviceId获取到map管理的连接通道
        String deviceId = infoDeliveryDto.getDeviceId();
        //将需要下发的指令信息转换成json字符串
        String data = JSON.toJSONString(infoDeliveryDto);
        //将读出的数据合并和字节数组,获取校验和
//        byte[] b1 = StringUtils.byteMerger(Constant.HEAD, StringUtils.shortToByte((short) infoDeliveryDto.getCmd()));
//        byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte((short) data.getBytes().length), data.getBytes());
//        short sumCheck = StringUtils.sumCheck(StringUtils.byteMerger(b1, b2), StringUtils.byteMerger(b1, b2).length);
        //封装下发指令,协议规定的格式
        NewDeviceProtocol deviceProtocol = new NewDeviceProtocol(Constant.HEAD, (short) infoDeliveryDto.getCmd(),
                (short) data.getBytes().length, data);
        //deviceId是Map管理每个channel通道的key,根据key获取到该设备id对应的通道发送下发有权限的管理员账号指令
        return NettyServerHandler.sendClientData(deviceId, deviceProtocol);
    }

    @Override
    public Integer cancelInfoDelivery(CancelInfoDeliveryDto cancelInfoDeliveryDto) {
        //将本次操作的命令标识存入redis当中
        redisService.setCacheObject(Constant.IDENT + cancelInfoDeliveryDto.getIdent(), JSON.toJSONString(cancelInfoDeliveryDto));
        //获取到deviceId,通过deviceId获取到map管理的连接通道
        String deviceId = cancelInfoDeliveryDto.getDeviceId();
        //将需要下发的指令信息转换成json字符串
        String data = JSON.toJSONString(cancelInfoDeliveryDto);
        //将读出的数据合并和字节数组,获取校验和
//        byte[] b1 = StringUtils.byteMerger(Constant.HEAD, StringUtils.shortToByte((short) infoDeliveryDto.getCmd()));
//        byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte((short) data.getBytes().length), data.getBytes());
//        short sumCheck = StringUtils.sumCheck(StringUtils.byteMerger(b1, b2), StringUtils.byteMerger(b1, b2).length);
        //封装下发指令,协议规定的格式
        NewDeviceProtocol deviceProtocol = new NewDeviceProtocol(Constant.HEAD, (short) cancelInfoDeliveryDto.getCmd(),
                (short) data.getBytes().length, data);
        //deviceId是Map管理每个channel通道的key,根据key获取到该设备id对应的通道发送下发有权限的管理员账号指令
        return NettyServerHandler.sendClientData(deviceId, deviceProtocol);
    }

    @Override
    public Integer onOffDevice(OnOffDeviceDto onOffDeviceDto) {
        //将本次操作的命令标识存入redis当中
        redisService.setCacheObject(Constant.IDENT + onOffDeviceDto.getIdent(), JSON.toJSONString(onOffDeviceDto));
        //获取到deviceId,通过deviceId获取到map管理的连接通道
        String deviceId = onOffDeviceDto.getDeviceId();
        //将需要下发的指令信息转换成json字符串
        String data = JSON.toJSONString(onOffDeviceDto);
        //将读出的数据合并和字节数组,获取校验和
//        byte[] b1 = StringUtils.byteMerger(Constant.HEAD, StringUtils.shortToByte((short) onOffDeviceDto.getCmd()));
//        byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte((short) data.getBytes().length), data.getBytes());
//        short sumCheck = StringUtils.sumCheck(StringUtils.byteMerger(b1, b2), StringUtils.byteMerger(b1, b2).length);
        //封装下发指令,协议规定的格式
        NewDeviceProtocol deviceProtocol = new NewDeviceProtocol(Constant.HEAD, (short) onOffDeviceDto.getCmd(),
                (short) data.getBytes().length, data);
        //deviceId是Map管理每个channel通道的key,根据key获取到该设备id对应的通道发送下发有权限的管理员账号指令
        return NettyServerHandler.sendClientData(deviceId, deviceProtocol);
    }

    @Override
    public Integer deleteDevice(DeleteDeviceDto deleteDeviceDto) {
        //将本次操作的命令标识存入redis当中
        redisService.setCacheObject(Constant.IDENT + deleteDeviceDto.getIdent(), JSON.toJSONString(deleteDeviceDto));
        //获取到deviceId,通过deviceId获取到map管理的连接通道
        String deviceId = deleteDeviceDto.getDeviceId();
        //将需要下发的指令信息转换成json字符串
        String data = JSON.toJSONString(deleteDeviceDto);
        //将读出的数据合并和字节数组,获取校验和
//        byte[] b1 = StringUtils.byteMerger(Constant.HEAD, StringUtils.shortToByte((short) onOffDeviceDto.getCmd()));
//        byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte((short) data.getBytes().length), data.getBytes());
//        short sumCheck = StringUtils.sumCheck(StringUtils.byteMerger(b1, b2), StringUtils.byteMerger(b1, b2).length);
        //封装下发指令,协议规定的格式
        NewDeviceProtocol deviceProtocol = new NewDeviceProtocol(Constant.HEAD, (short) deleteDeviceDto.getCmd(),
                (short) data.getBytes().length, data);
        //deviceId是Map管理每个channel通道的key,根据key获取到该设备id对应的通道发送下发有权限的管理员账号指令
        return NettyServerHandler.sendClientData(deviceId, deviceProtocol);
    }
}
