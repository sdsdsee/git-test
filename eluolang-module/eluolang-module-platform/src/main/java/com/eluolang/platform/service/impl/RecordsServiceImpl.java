package com.eluolang.platform.service.impl;

import com.eluolang.common.core.pojo.EllDevice;
import com.eluolang.common.core.pojo.EllDeviceAlarm;
import com.eluolang.platform.fegin.TcpServerRemoteService;
import com.eluolang.platform.mapper.RecordsMapper;
import com.eluolang.platform.redis.DeviceRService;
import com.eluolang.platform.service.RecordsService;
import com.eluolang.platform.vo.EllDataVo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecordsServiceImpl implements RecordsService {
    @Autowired
    private RecordsMapper recordsMapper;
    @Autowired
    private DeviceRService deviceRService;
    @Autowired
    private TcpServerRemoteService tcpServerRemoteService;

    @Override
    public EllDataVo selData(int createBy) {
        //此账号下的设备
        List<EllDevice> ellDeviceList = recordsMapper.findDevice(createBy);
        //报警/求助数量
        int alarmCount = recordsMapper.selAlarmCount(createBy);
        //查询设备是否正常数量
        int onCount = 0;
        //查询设备是否待机
        int isOpen = 0;
        for (int i = 0; i < ellDeviceList.size(); i++) {
            Boolean b = deviceRService.selOnDeviceCount(ellDeviceList.get(i).getDeviceId());
            if (b == false) {
                onCount++;
            }
            //查询是否开机
            Boolean open = deviceRService.selIsOpen(ellDeviceList.get(i).getDeviceId());
            if (open == true) {
                isOpen++;
            }

        }
        EllDataVo ellDataVo = new EllDataVo();
        ellDataVo.setIsOpen(isOpen);
        ellDataVo.setAlarmCount(alarmCount);
        ellDataVo.setDeviceCount(ellDeviceList.size());
        ellDataVo.setErrorDevice(onCount);
        return ellDataVo;
    }

    @Override
    public List<EllDeviceAlarm> selAlarm(int createBy) {
        //只查询前三条
        PageHelper.startPage(1, 3);
        return recordsMapper.selAlarm(createBy);
    }

    @Override
    public int selDayElectricity(int createBy, int dayTime, int deviceType) {
        return recordsMapper.selDayElectricity(createBy, dayTime, deviceType);
    }

    @Override
    public int selElectricity(int createBy, int lastMonth, int deviceType) {
        return recordsMapper.selElectricity(createBy, lastMonth, deviceType);
    }


}
