package com.eluolang.platform.service.impl;


import com.eluolang.common.core.pojo.EllLargeScreen;
import com.eluolang.platform.mapper.GardenMapper;
import com.eluolang.platform.service.GardenService;
import com.eluolang.platform.vo.EllAlarmVo;
import com.eluolang.platform.vo.EllDeNameAndId;
import com.eluolang.platform.vo.EllDeviceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class GardenServiceImpl implements GardenService {
    @Autowired
    private GardenMapper gardenMapper;

    @Override
    public List<EllAlarmVo> selAlarm(int createBy, int deviceType) {
        return gardenMapper.selAlarm(createBy, deviceType);
    }

    @Override
    public int selElectricity(int createBy, int lastMonth, int deviceType) {
        return gardenMapper.selElectricity(createBy, lastMonth, deviceType);
    }

    @Override
    public List<EllDeviceVo> queryAllByLimit(EllLargeScreen ellDevice) {
        List<EllDeviceVo> ellDeviceVoList = gardenMapper.queryAllByLimit(ellDevice);
        return ellDeviceVoList;
    }

    @Override
    public int selDayElectricity(int createBy, int dayTime, int deviceType) {
        return gardenMapper.selDayElectricity(createBy, dayTime, deviceType);
    }

    @Override
    public   List<EllDeNameAndId>  selDeviceNmeAndId(int createBy, int deviceType) {
        return gardenMapper.selDeviceNmeAndId(createBy, deviceType);
    }
}
