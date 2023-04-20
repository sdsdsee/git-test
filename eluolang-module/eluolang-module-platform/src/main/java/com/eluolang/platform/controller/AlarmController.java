package com.eluolang.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.platform.service.AlarmService;
import com.eluolang.platform.vo.EllAlarmVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@Api(tags = "一键报警管理")
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    @ApiOperation("查询警报")
    @PostMapping("/selAlarm")
    public Result selAlarm(@RequestBody EllAlarmVo ellAlarmVo) {
        PageHelper.startPage(ellAlarmVo.getPage(), 5);
        List<EllAlarmVo> ellAlarmVoList = alarmService.selAlarmVo(ellAlarmVo);
        PageInfo info = new PageInfo<>(ellAlarmVoList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("alarmVoList", ellAlarmVoList);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }


    @ApiOperation("查询最近15天警报次数")
    @GetMapping("/selDaysAlarm")
    public Result selDaysAlarm(int createBy, int deviceType) {
        return new Result(HttpStatus.SUCCESS, "成功", alarmService.selAlarmCount(createBy, deviceType));
    }
    @Log(title = "删除警报",operType = OperType.DELETE,content = "删除警报")
    @ApiOperation("删除警报")
    @DeleteMapping("/delAlarm")
    public Result delAlarm(String alarmId) {
        return new Result(HttpStatus.SUCCESS, "成功", alarmService.deleteAlarm(alarmId));
    }
}
