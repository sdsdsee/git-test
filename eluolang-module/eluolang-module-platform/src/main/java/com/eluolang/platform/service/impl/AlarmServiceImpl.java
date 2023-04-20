package com.eluolang.platform.service.impl;

import com.eluolang.platform.mapper.AlarmMapper;
import com.eluolang.platform.service.AlarmService;
import com.eluolang.platform.util.CreateTime;
import com.eluolang.platform.vo.EllAlarmVo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AlarmServiceImpl implements AlarmService {
    @Autowired
    private AlarmMapper alarmMapper;

    @Override
    public List<EllAlarmVo> selAlarmVo(EllAlarmVo ellAlarmVo) {
        return alarmMapper.selAlarm(ellAlarmVo);
    }

    @Override
    public List<Integer> selAlarmCount(int createBy, int deviceType) {
        List<String> days = CreateTime.dateCalendar(-15);
        List<Integer> alarmCount = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            alarmCount.add(alarmMapper.selAlarmCount(days.get(i), createBy, deviceType));
        }
        return alarmCount;
    }

    @Override
    public int deleteAlarm(String alarmId) {
        return alarmMapper.deleteAlarm(alarmId);
    }
}
