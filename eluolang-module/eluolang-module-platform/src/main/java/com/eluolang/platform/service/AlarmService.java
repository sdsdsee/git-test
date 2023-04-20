package com.eluolang.platform.service;

import com.eluolang.platform.vo.EllAlarmVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlarmService {
    List<EllAlarmVo> selAlarmVo(EllAlarmVo ellAlarmVo);

    /**
     * 查询最近几天的报警次数
     */
    List<Integer> selAlarmCount(int createBy, int deviceType);
    /**
     * 删除警报
     */
    int deleteAlarm(String alarmId);
}
