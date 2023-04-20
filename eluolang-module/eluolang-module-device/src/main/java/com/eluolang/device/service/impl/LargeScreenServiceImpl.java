package com.eluolang.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.eluolang.common.core.constant.CacheConstant;

import com.eluolang.common.core.constant.HardwareStatus;
import com.eluolang.common.core.constant.HttpStatus;

import com.eluolang.common.core.pojo.EllElectricQuantity;
import com.eluolang.common.core.pojo.ToiletEvaluate;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.DeviceResponseStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.device.dto.HeartbeatDeviceStatusDto;
import com.eluolang.device.mapper.DeviceMapper;
import com.eluolang.device.mapper.LargeScreenMapper;
import com.eluolang.device.service.DeviceService;
import com.eluolang.device.service.LargeScreenService;
import com.eluolang.device.service.feign.PlatformRemoteService;

import com.eluolang.device.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 设备业务逻辑层
 *
 * @author dengrunsen
 */
@Transactional
@Service
public class LargeScreenServiceImpl implements LargeScreenService {
    private static Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class.getName());

    @Autowired
    private LargeScreenMapper largeScreenMapper;

    @Autowired
    private PlatformRemoteService platformRemoteService;


    @Autowired
    RedisService redisService;

    private Integer stateTime = 180;


    @Override
    public String saveHeartBeat(String data) {
        logger.info("method===saveHeartBeat" + data + "--->" + DateUtils.getMillisecondTime());
        HeartbeatDeviceStatusVo heartbeatDeviceStatusVo = new HeartbeatDeviceStatusVo();
        try {
            HeartbeatDeviceStatusDto heartbeatDeviceStatusDto = JSON.parseObject(data, HeartbeatDeviceStatusDto.class);
            //保存设备的心跳信息
            redisService.setCacheObject(CacheConstant.DEVICE_STATE + heartbeatDeviceStatusDto.getDeviceId(), heartbeatDeviceStatusDto.getStatus(), stateTime, TimeUnit.SECONDS);
            //封装需要返回响应给硬件的信息
            heartbeatDeviceStatusVo.setCmd(HardwareStatus.HEARTBEAT_DEVICE_STATUS);
            heartbeatDeviceStatusVo.setIdent(heartbeatDeviceStatusDto.getIdent());
            heartbeatDeviceStatusVo.setDeviceId(heartbeatDeviceStatusDto.getDeviceId());
            if (redisService.getCacheObject(CacheConstant.PARKING_SPACE) != null) {
                heartbeatDeviceStatusVo.setParkingNum(redisService.getCacheObject(CacheConstant.PARKING_SPACE));
            }
            heartbeatDeviceStatusVo.setCode(DeviceResponseStatus.RESPONSE_SUCCESS.getCode());
        } catch (Exception e) {
            //出现异常将code更改为1
            heartbeatDeviceStatusVo.setCode(DeviceResponseStatus.RESPONSE_ERROR.getCode());
            e.printStackTrace();
        } finally {
            logger.info("method===saveHeartBeat--->ending--->" + DateUtils.getMillisecondTime());
            return JSON.toJSONString(heartbeatDeviceStatusVo);
        }
    }

    @Override
    public String saveUseElectricityAmount(String data) {
        logger.info("method===saveUseElectricityAmount" + data + "--->" + DateUtils.getMillisecondTime());
        UseElectricityAmountVo useElectricityAmountVo = new UseElectricityAmountVo();
        try {
            UseElectricityAmountDto useElectricityAmountDto = JSON.parseObject(data, UseElectricityAmountDto.class);
            EllElectricQuantity ellElectricQuantity = new EllElectricQuantity();
            ellElectricQuantity.setUseElectricity(useElectricityAmountDto.getKwh());
            ellElectricQuantity.setDeviceId(useElectricityAmountDto.getDeviceId());
            ellElectricQuantity.setCreateTime(DateUtils.getNowDateBeforeOne());
            Result result = platformRemoteService.addElectricity(ellElectricQuantity);
            if (result.getCode() == HttpStatus.ERROR) {
                throw new Exception("添加设备前一天用电量到数据库失败");
            }
            //封装需要返回响应给硬件的信息
            useElectricityAmountVo.setCmd(HardwareStatus.HEARTBEAT_DEVICE_STATUS);
            useElectricityAmountVo.setIdent(useElectricityAmountDto.getIdent());
            useElectricityAmountVo.setDeviceId(useElectricityAmountDto.getDeviceId());
            useElectricityAmountVo.setCode(DeviceResponseStatus.RESPONSE_SUCCESS.getCode());
        } catch (Exception e) {
            //出现异常将code更改为1
            useElectricityAmountVo.setCode(DeviceResponseStatus.RESPONSE_ERROR.getCode());
            e.printStackTrace();
        } finally {
            logger.info("method===saveUseElectricityAmount--->ending--->" + DateUtils.getMillisecondTime());
            return JSON.toJSONString(useElectricityAmountVo);
        }
    }

    @Override
    public String saveDeviceEnvironmentParam(String data) {
        logger.info("method===saveDeviceEnvironmentParam" + data + "--->" + DateUtils.getMillisecondTime());
        DeviceEnvironmentParamVo deviceEnvironmentParamVo = new DeviceEnvironmentParamVo();
        try {
            DeviceEnvironmentParamDto deviceEnvironmentParamDto = JSON.parseObject(data, DeviceEnvironmentParamDto.class);
            //保存设备的心跳信息
            redisService.setCacheObject(CacheConstant.DEVICE_ENVIRONMENT_INFO + deviceEnvironmentParamDto.getDeviceId(), deviceEnvironmentParamDto, 3600, TimeUnit.SECONDS);
            //封装需要返回响应给硬件的信息
            deviceEnvironmentParamVo.setCmd(HardwareStatus.HEARTBEAT_DEVICE_STATUS);
            deviceEnvironmentParamVo.setIdent(deviceEnvironmentParamDto.getIdent());
            deviceEnvironmentParamVo.setDeviceId(deviceEnvironmentParamDto.getDeviceId());
            deviceEnvironmentParamVo.setCode(DeviceResponseStatus.RESPONSE_SUCCESS.getCode());
        } catch (Exception e) {
            //出现异常将code更改为1
            deviceEnvironmentParamVo.setCode(DeviceResponseStatus.RESPONSE_ERROR.getCode());
            e.printStackTrace();
        } finally {
            logger.info("method===saveDeviceEnvironmentParam--->ending--->" + DateUtils.getMillisecondTime());
            return JSON.toJSONString(deviceEnvironmentParamVo);
        }
    }

    @Override
    public List<Integer> selOperIdByDeviceId(String deviceId) {
        return largeScreenMapper.selOperIdByDeviceId(deviceId);
    }

    @Override
    public int addToiletEvaluate(ToiletEvaluate toiletEvaluate) {
        return largeScreenMapper.addToiletEvaluate(toiletEvaluate);
    }

    @Override
    public List<ToiletEvaluate> selToiletEvaluate(String deviceId) {
        return largeScreenMapper.selToiletEvaluate(deviceId);
    }

    @Override
    public int selToiletEvaluateByLevel(String deviceId, Integer level) {
        return largeScreenMapper.selToiletEvaluateByLevel(deviceId, level);
    }
}

