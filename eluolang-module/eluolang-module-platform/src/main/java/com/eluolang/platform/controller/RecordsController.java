package com.eluolang.platform.controller;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.platform.service.RecordsService;
import com.eluolang.platform.util.CreateTime;
import com.eluolang.platform.vo.EllDayElectricityVo;
import com.eluolang.platform.vo.EllRecordsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Transactional
@Api(tags = "数据中心管理")
public class RecordsController {
    @Autowired
    private RecordsService recordsService;

    @ApiOperation("数量")
    @GetMapping("/dataCount")
    public Result dataCount(int createBy) {
        return new Result(HttpStatus.SUCCESS, "成功", recordsService.selData(createBy));
    }

    @ApiOperation("查询报警")
    @GetMapping("/selAlarmDa")
    public Result selAlarm(int createBy) {
        return new Result(HttpStatus.SUCCESS, "成功", recordsService.selAlarm(createBy));
    }

    @ApiOperation("查询电量")
    @GetMapping("/selElectricity")
    public Result selElectricity(int createBy) {
        //获取日期
        List<String> dayString = CreateTime.dateCalendar(-8);
        List<EllDayElectricityVo> ellDayElectricityVoList = new ArrayList<>();
        //最近7天
        //dayString.size()为本天dayString.size()-1昨天
        for (int i = 0; i < dayString.size()-1; i++) {
            EllDayElectricityVo ellDayElectricityVo = new EllDayElectricityVo();
            ellDayElectricityVo.setDay(CreateTime.takeOutTime(dayString.get(i)));
            //往前数几天
            ellDayElectricityVo.setKwh(recordsService.selDayElectricity(createBy, (dayString.size()-1)-i, 0));
            ellDayElectricityVoList.add(ellDayElectricityVo);
        }
        EllRecordsVo ellRecordsVo = new EllRecordsVo();
        //近几天的用电量
        ellRecordsVo.setEllDayElectricityVoList(ellDayElectricityVoList);
        //本月用电量
        ellRecordsVo.setThisMonth(recordsService.selElectricity(createBy, 0, 0));
        //上月用电量
        ellRecordsVo.setLastMonth(recordsService.selElectricity(createBy, 1, 0));
        //前月用电量
        ellRecordsVo.setPrecedingMonth(recordsService.selElectricity(createBy, 2, 0));
        return new Result(HttpStatus.SUCCESS, "成功", ellRecordsVo);
    }
}
