package com.eluolang.module.report.controller;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.report.servier.ArchivesService;
import com.eluolang.module.report.vo.EllDailyExerciseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "体质健康档案")
@RequestMapping("/archives")
public class ArchivesController {
    @Autowired
    private ArchivesService archivesService;

    @ApiOperation("查询最近几年的测试人数")
    @GetMapping("/selYearNum")
    public Result selYearNum(int accountId, int yearNum, int isExam, int orgId) {
        return new Result(HttpStatus.SUCCESS, "成功", archivesService.selYearTestPersonNum(orgId, accountId, yearNum, isExam));
    }

    @ApiOperation("查询某一年的相应等级人数")
    @GetMapping("/selYearScoreGrade")
    public Result selYearScoreGrade(int accountId, String year, int isExam, int orgId) {
        return new Result(HttpStatus.SUCCESS, "成功", archivesService.selYearNum(orgId, accountId, year, isExam));
    }

    @ApiOperation("查询近几年相应等级人数")
    @GetMapping("/selAFewYearNum")
    public Result selAFewYearNum(int accountId, int isExam, int orgId) {
        return new Result(HttpStatus.SUCCESS, "成功", archivesService.selAFewYearNum(orgId, accountId, isExam));
    }

    @ApiOperation("日常锻炼情况")
    @GetMapping("/selExercise")
    public Result selExercise(int accountId, String year) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accomplish", 0);
        jsonObject.put("unfinished", 0);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("历年体育成绩")
    @GetMapping("/selYearAvgScore")
    public Result selYearAvgScore(int accountId, int yearNum, int isExam, int orgId) {
        return new Result(HttpStatus.SUCCESS, "成功", archivesService.selYearAvgScore(orgId, accountId, yearNum, isExam));
    }

    @ApiOperation("查询学生信息")
    @GetMapping("/selUser")
    public Result selUser(int orgId) {
        return new Result(HttpStatus.SUCCESS, "成功", archivesService.findUser(orgId));
    }

    @ApiOperation("体质测试变化")
    @GetMapping("/testChange")
    public Result testChange(String userId, int isExam) {
        return new Result(HttpStatus.SUCCESS, "成功", archivesService.selUserYearData(userId, isExam));
    }

    @ApiOperation("学期测评")
    @GetMapping("/selUserYearAvg")
    public Result selUserYearAvg(String userId) {
        return new Result(HttpStatus.SUCCESS, "成功", archivesService.selUserYearAvg(userId));
    }

    @ApiOperation("日常锻炼")
    @GetMapping("/selDailyExercise")
    public Result selDailyExercise(String userId) {
        List<EllDailyExerciseVo> ellDailyExerciseVoList = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            EllDailyExerciseVo ellDailyExerciseVo = new EllDailyExerciseVo();
            ellDailyExerciseVo.setYear("2022");
            ellDailyExerciseVo.setKm(10);
            ellDailyExerciseVoList.add(ellDailyExerciseVo);
        }

        return new Result(HttpStatus.SUCCESS, "成功", ellDailyExerciseVoList);
    }

    @ApiOperation("日常监测")
    @GetMapping("/sekDailyMonitoring")
    public Result sekDailyMonitoring(String userId) {
        return new Result(HttpStatus.SUCCESS, "成功", archivesService.selUserHeightAndWeight(userId));
    }
}
