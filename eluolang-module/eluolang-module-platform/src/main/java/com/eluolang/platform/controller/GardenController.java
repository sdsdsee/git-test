package com.eluolang.platform.controller;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllLargeScreen;
import com.eluolang.common.core.web.Result;
import com.eluolang.platform.redis.DeviceRService;
import com.eluolang.platform.service.GardenService;
import com.eluolang.platform.util.CreateTime;
import com.eluolang.platform.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "园区管理")
public class GardenController {
    @Autowired
    private GardenService gardenService;
    @Autowired
    private DeviceRService deviceRService;

    @GetMapping("/selGarAlarm")
    @ApiOperation("/查询警报")
    public Result selGarAlarm(int createBy) {
        PageHelper.startPage(1, 2);
        return new Result(HttpStatus.SUCCESS, "成功", gardenService.selAlarm(createBy, 0));
    }

    @Async
    @GetMapping("/selData")
    public Result selLeftData(int createBy, int deviceType) {
        EllLargeScreen ellDevice = new EllLargeScreen();
        ellDevice.setCreateBy(createBy);
        ellDevice.setDeviceType(deviceType);
        List<EllDeviceVo> ellDeviceVoList = gardenService.queryAllByLimit(ellDevice);
        PageInfo info = new PageInfo<>(ellDeviceVoList);
        //正常设备
        int deviceNormal = 0;
        for (int i = 0; i < ellDeviceVoList.size(); i++) {
           Boolean success= deviceRService.selOnDeviceCount(ellDeviceVoList.get(i).getDeviceId());
            //查询正常个数
            if (success) {
                deviceNormal++;
            }
        }
        //获取日期
        List<String> dayString = CreateTime.dateCalendar(-8);
        List<EllDayElectricityVo> ellDayElectricityVoList = new ArrayList<>();
        //最近7天
        //dayString.size()为本天dayString.size()-1昨天
        for (int i = 0; i < dayString.size() - 1; i++) {
            EllDayElectricityVo ellDayElectricityVo = new EllDayElectricityVo();
            ellDayElectricityVo.setDay(CreateTime.takeOutTime(dayString.get(i)));
            //往前数几天
            ellDayElectricityVo.setKwh(gardenService.selDayElectricity(createBy, (dayString.size() - 1) - i, deviceType));
            ellDayElectricityVoList.add(ellDayElectricityVo);
        }
        EllGardenDataVo ellGardenDataVo = new EllGardenDataVo();
        ellGardenDataVo.setDeviceAll((int) info.getTotal());
        ellGardenDataVo.setDeviceNormal(deviceNormal);
        ellGardenDataVo.setEllDayElectricityVoList(ellDayElectricityVoList);
        //本月用电量
        ellGardenDataVo.setThisMonth(gardenService.selElectricity(createBy, 0, deviceType));
        //上月用电量
        ellGardenDataVo.setLastMonth(gardenService.selElectricity(createBy, 1, deviceType));
        //前月用电量
        ellGardenDataVo.setPrecedingMonth(gardenService.selElectricity(createBy, 2, deviceType));
        return new Result(HttpStatus.SUCCESS, "成功", ellGardenDataVo);
    }

    @GetMapping("/selGarAlarmNum")
    @ApiOperation("/查询警报数量")
    public Result selGarAlarm(int createBy, int deviceType) {
        return new Result(HttpStatus.SUCCESS, "成功", gardenService.selAlarm(createBy, deviceType).size());
    }

    @GetMapping("/selEnvironment")
    @ApiOperation("/查询环境参数")
    public Result selEnvironment(int createBy, int deviceType) {
        List<EllDeNameAndId> ellDeviceVoList = gardenService.selDeviceNmeAndId(createBy, deviceType);
        List<DeviceEnvironmentParamDtoVo> deviceEnvironmentParamDtoList = deviceRService.selEnvironment(ellDeviceVoList, deviceType);
        return new Result(HttpStatus.SUCCESS, "成功", deviceEnvironmentParamDtoList);
    }

    @GetMapping("/selCar")
    @ApiOperation("查询车位")
    public Result selCar() {
        return new Result(HttpStatus.SUCCESS, "成功", deviceRService.selTruckSpaceNum("240305004110343"));
    }
}
