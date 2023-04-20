package com.eluolang.platform.mapper;

import com.eluolang.platform.vo.EllAlarmVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmMapper {
    /**
     * 查询报警
     */
    List<EllAlarmVo> selAlarm(@Param("ellAlarmVo") EllAlarmVo ellAlarmVo);

    /**
     * 查询最近几天的报警次数
     */
    int selAlarmCount(@Param("day") String day, @Param("createBy") int createBy, @Param("deviceType") int deviceType);

    /**
     * 删除警报
     */
    int deleteAlarm(String alarmId);
}
