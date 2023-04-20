package com.eluolang.platform.redis.impl;


import com.eluolang.common.core.hardware.dto.DeviceEnvironmentParamDto;
import com.eluolang.platform.redis.DeviceRService;
import com.eluolang.platform.vo.DeviceEnvironmentParamDtoVo;
import com.eluolang.platform.vo.EllDeNameAndId;
import com.eluolang.platform.vo.EllDeviceVo;
import com.eluolang.platform.vo.EllToiletEnvironmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class DeviceRServiceImpl implements DeviceRService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean selOnDeviceCount(String deviceId) {
        boolean success = redisTemplate.hasKey("device:state:" + deviceId);
        return success;
    }

    @Override
    public boolean selIsOpen(String deviceId) {
        Object type = redisTemplate.opsForValue().get("device:state:" + deviceId);
        if (type != null && type != "" && type.equals(1)) {
            return true;
        }
        return false;
    }

    @Override
    public List<DeviceEnvironmentParamDtoVo> selEnvironment(List<EllDeNameAndId> ellDeNameAndIdList, int deviceType) {
        List<DeviceEnvironmentParamDtoVo> deviceEnvironmentParamDtoList = new ArrayList<>();
        for (int i = 0; i < ellDeNameAndIdList.size(); i++) {
            DeviceEnvironmentParamDtoVo deviceEnvironmentParamDtoVo = new DeviceEnvironmentParamDtoVo();
            if (deviceType == 1) {
                DeviceEnvironmentParamDto deviceEnvironmentParamDto = (DeviceEnvironmentParamDto) redisTemplate.opsForValue().get("device:info:" + ellDeNameAndIdList.get(i).getDeviceId());
                if (deviceEnvironmentParamDto != null) {
                    //参数封装
                    deviceEnvironmentParamDtoVo.setDeviceName(ellDeNameAndIdList.get(i).getDeviceName());
                    deviceEnvironmentParamDtoVo.setHumidity(deviceEnvironmentParamDto.getHumidity());
                    deviceEnvironmentParamDtoVo.setIlluminance(deviceEnvironmentParamDto.getIlluminance());
                    deviceEnvironmentParamDtoVo.setParticles(deviceEnvironmentParamDto.getParticles());
                    deviceEnvironmentParamDtoVo.setTemperature(deviceEnvironmentParamDto.getTemperature());
                    deviceEnvironmentParamDtoVo.setNoise(deviceEnvironmentParamDto.getNoise());
                    deviceEnvironmentParamDtoVo.setCount(ellDeNameAndIdList.get(i).getCount());
                }
            } else {
                EllToiletEnvironmentVo ellToiletEnvironmentVo = (EllToiletEnvironmentVo) redisTemplate.opsForValue().get("toilet:info:" + ellDeNameAndIdList.get(i).getDeviceId());
                if (ellToiletEnvironmentVo != null) {
                    //参数封装
                    deviceEnvironmentParamDtoVo.setDeviceName(ellDeNameAndIdList.get(i).getDeviceName());
                    deviceEnvironmentParamDtoVo.setHumidity(ellToiletEnvironmentVo.getHumidity());
                    deviceEnvironmentParamDtoVo.setIlluminance(ellToiletEnvironmentVo.getMgL());
                    deviceEnvironmentParamDtoVo.setParticles(ellToiletEnvironmentVo.getParticles());
                    deviceEnvironmentParamDtoVo.setTemperature(ellToiletEnvironmentVo.getTemperature());
                    deviceEnvironmentParamDtoVo.setNoise(ellToiletEnvironmentVo.getPpm());
                    deviceEnvironmentParamDtoVo.setCount(ellDeNameAndIdList.get(i).getCount());

                }
            }
            if (deviceEnvironmentParamDtoVo.getDeviceName() == null) {
                //参数封装
                deviceEnvironmentParamDtoVo.setDeviceName(ellDeNameAndIdList.get(i).getDeviceName());
                deviceEnvironmentParamDtoVo.setHumidity("暂无");
                deviceEnvironmentParamDtoVo.setIlluminance("暂无");
                deviceEnvironmentParamDtoVo.setParticles("暂无");
                deviceEnvironmentParamDtoVo.setTemperature("暂无");
                deviceEnvironmentParamDtoVo.setNoise("暂无");
            }
            deviceEnvironmentParamDtoList.add(deviceEnvironmentParamDtoVo);
        }
        return deviceEnvironmentParamDtoList;
    }

    @Override
    public List<EllDeviceVo> selDeviceEnvironment(List<EllDeviceVo> ellDeviceVoList) {
        for (int i = 0; i < ellDeviceVoList.size(); i++) {
            DeviceEnvironmentParamDtoVo deviceEnvironmentParamDtoVo = new DeviceEnvironmentParamDtoVo();
            if (ellDeviceVoList.get(i).getDeviceType() == 1) {
                DeviceEnvironmentParamDto deviceEnvironmentParamDto = (DeviceEnvironmentParamDto) redisTemplate.opsForValue().get("device:info:" + ellDeviceVoList.get(i).getDeviceId());
                if (deviceEnvironmentParamDto != null) {
                    //参数封装
                    deviceEnvironmentParamDtoVo.setDeviceName(ellDeviceVoList.get(i).getDeviceName());
                    deviceEnvironmentParamDtoVo.setHumidity(deviceEnvironmentParamDto.getHumidity());
                    deviceEnvironmentParamDtoVo.setIlluminance(deviceEnvironmentParamDto.getIlluminance());
                    deviceEnvironmentParamDtoVo.setParticles(deviceEnvironmentParamDto.getParticles());
                    deviceEnvironmentParamDtoVo.setTemperature(deviceEnvironmentParamDto.getTemperature());
                    deviceEnvironmentParamDtoVo.setNoise(deviceEnvironmentParamDto.getNoise());
                }
            } else {
                EllToiletEnvironmentVo ellToiletEnvironmentVo = (EllToiletEnvironmentVo) redisTemplate.opsForValue().get("toilet:info:" + ellDeviceVoList.get(i).getDeviceId());
                if (ellToiletEnvironmentVo != null) {
                    //参数封装
                    deviceEnvironmentParamDtoVo.setDeviceName(ellDeviceVoList.get(i).getDeviceName());
                    deviceEnvironmentParamDtoVo.setHumidity(ellToiletEnvironmentVo.getHumidity());
                    deviceEnvironmentParamDtoVo.setIlluminance(ellToiletEnvironmentVo.getMgL());
                    deviceEnvironmentParamDtoVo.setParticles(ellToiletEnvironmentVo.getParticles());
                    deviceEnvironmentParamDtoVo.setTemperature(ellToiletEnvironmentVo.getTemperature());
                    deviceEnvironmentParamDtoVo.setNoise(ellToiletEnvironmentVo.getPpm());
                }
            }
            if (deviceEnvironmentParamDtoVo.getDeviceName() == null) {
                //参数封装
                deviceEnvironmentParamDtoVo.setDeviceName(ellDeviceVoList.get(i).getDeviceName());
                deviceEnvironmentParamDtoVo.setHumidity("暂无");
                deviceEnvironmentParamDtoVo.setIlluminance("暂无");
                deviceEnvironmentParamDtoVo.setParticles("暂无");
                deviceEnvironmentParamDtoVo.setTemperature("暂无");
                deviceEnvironmentParamDtoVo.setNoise("暂无");
            }
            ellDeviceVoList.get(i).setDeviceEnvironmentParamDtoVo(deviceEnvironmentParamDtoVo);
        }
        return ellDeviceVoList;
    }

    @Override
    public void addEnvironment(String deviceId, EllToiletEnvironmentVo ellToiletEnvironmentVo) {
        redisTemplate.opsForValue().set("toilet:info:" + deviceId, ellToiletEnvironmentVo);
    }

    @Override
    public void addTruckSpaceNum(String LEDId, int trucSpace) {
        redisTemplate.opsForValue().set("LED:trucSpace:" + LEDId, trucSpace);
    }

    @Override
    public int selTruckSpaceNum(String LEDId) {
        if (!redisTemplate.hasKey("LED:trucSpace:" + LEDId)) {
            return 0;
        }
        return (int) redisTemplate.opsForValue().get("LED:trucSpace:" + LEDId);
    }
}
