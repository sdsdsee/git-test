package com.eluolang.playground.controller;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.service.WisdomClassService;
import com.eluolang.playground.service.WisdomIndividualFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "个人档案")
@RequestMapping("/individual")
public class WisdomIndividualFileController {
    //#成绩越大越好的项目
    //maxScorePro: 36,37,38,39,42,44,46,48,49,51,59,60,61,65,80,81,90
    @Value("${maxScorePro}")
    private String maxScorePro;
    //#成绩越小越好的项目
    //minScorePro: 40,41,45,47,50,52,53,54,55,56,62,63,64,66,75,76,78,79,82,83,84,85,89
    @Value("${minScorePro}")
    private String minScorePro;
    //#总数最大越好的
    //sumScorePro:
    @Value("${sumScorePro}")
    private String sumScorePro;
    @Autowired
    private WisdomIndividualFileService wisdomIndividualFileService;
    @Autowired
    private WisdomClassService wisdomClassService;
/*
    @ApiOperation("个人信息")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(String userId) {
        EllIndividualUserDto ellIndividualUserDto = wisdomIndividualFileService.selUser(userId);
        ellIndividualUserDto.setGrade(GradeCalculate.gradeName(GradeCalculate.gradeNumber(ellIndividualUserDto.getEnTime(), ellIndividualUserDto.getEnGrade())));
        return new Result(HttpStatus.SUCCESS, "成功", ellIndividualUserDto);
    }*/

    @ApiOperation("近期锻炼情况")
    @GetMapping("/recentlyExercise")
    public Result recentlyExercise(String userId) {
        //近7天不同项目的最好最差成绩
        List<EllProScoreDto> scoreDtos = wisdomIndividualFileService.selStudentData(userId, 7);
        List<EllStudentGradeDto> studentGradeDtos = new ArrayList<>();
        //跑步里程
        EllStudentGradeDto gradeRunDto = wisdomIndividualFileService.selRunMileage(userId);
        gradeRunDto.setDataGrade(gradeRunDto.getDataGrade());
        studentGradeDtos.add(gradeRunDto);
        for (int i = 0; i < scoreDtos.size(); i++) {
            EllStudentGradeDto studentGradeDto = new EllStudentGradeDto();
            int num = 0;
            studentGradeDto.setProId(scoreDtos.get(i).getProId());
            studentGradeDto.setProName(scoreDtos.get(i).getProName());
            studentGradeDto.setUnit(scoreDtos.get(i).getUnit());
            //判断项目的排序方式
            if (maxScorePro.contains("," + scoreDtos.get(i).getProId() + ",")) {
                studentGradeDto.setDataGrade(String.valueOf(scoreDtos.get(i).getMaxScore()));
                //越大越好
                num = wisdomIndividualFileService.selBetterThanGradeNum((int) scoreDtos.get(i).getMaxScore(), scoreDtos.get(i).getProId(), 1);
            } else {
                studentGradeDto.setDataGrade(String.valueOf(scoreDtos.get(i).getMinScore()));
                num = wisdomIndividualFileService.selBetterThanGradeNum((int) scoreDtos.get(i).getMinScore(), scoreDtos.get(i).getProId(), 2);
            }
            studentGradeDto.setRank(num + 1);
            studentGradeDtos.add(studentGradeDto);
        }

        return new Result(HttpStatus.SUCCESS, "成功", studentGradeDtos);
    }

/*    @ApiOperation("查询本周跑步任务情况")
    @GetMapping("/weekRunSituation")
    public Result weekRunSituation(String userId) throws ParseException {
        //获取本周一0点时间时间戳秒级别
        Long MONDAY = CreateTime.nowMonday().getTime();
        //查询本周任务完成情况
        Integer mileage = wisdomIndividualFileService.selWeekRun(userId, MONDAY);
        //查询学生
        EllIndividualUserDto ellIndividualUserDto = wisdomIndividualFileService.selUser(userId);
        //查询部门
        PfDepart pfDepart = wisdomClassService.selSchoolByClassId(ellIndividualUserDto.getOrgId());
        EllDailyExercisePlan ellDailyExercisePlan = wisdomClassService.selDailyPlan(pfDepart.getId());
        int moth = Integer.parseInt(com.eluolang.playground.util.CreateTime.timeDayMoth());
        //本周计划
        Double weekRunNum = 0.0;
        if (moth >= 7) {
            //下半年
            weekRunNum = ellDailyExercisePlan.getSecondWeeks();
        } else {
            //上半年
            weekRunNum = ellDailyExercisePlan.getFirstWeeks();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("weekRunNum", weekRunNum);
        jsonObject.put("mileage", mileage == null ? 0 : mileage);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }*/

    @ApiOperation("查询体育考试")
    @GetMapping("/getExamination")
    public Result getExamination(String userId) {
        List<EllEndScoreDto> scoreDtos = wisdomIndividualFileService.selPlanScore(userId);
        return new Result(HttpStatus.SUCCESS, "成功", scoreDtos);
    }

    @ApiOperation("查询随堂测试")
    @GetMapping("/getClassGrade")
    public Result getClassGrade(String userId) {
        List<EllClassGradeDayDto> selTestPro = wisdomIndividualFileService.selTestPro(userId);
        if (selTestPro.size() <= 0) {
            return new Result(HttpStatus.ERROR, "失败");
        }
        for (int i = 0; i < selTestPro.size(); i++) {
            List<EllClassGradeDto> selClassGrade = wisdomIndividualFileService.selClassGrade(userId, selTestPro);
            selTestPro.get(i).setTimeGrade(selClassGrade);
        }
        return new Result(HttpStatus.SUCCESS, "成功", selTestPro);
    }

    @ApiOperation("个人分析")
    @GetMapping("/getAnalysis")
    public Result getAnalysis(String userId) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomIndividualFileService.calculationBodyError(userId));
    }

/*    @ApiOperation("视力")
    @GetMapping("/getEyes")
    public Result getEyes(String userId) throws ParseException {
        List<EllEyesInfoDto> ellEyesInfoDtos = wisdomIndividualFileService.selEyesInfo(userId);
        if (ellEyesInfoDtos.size() <= 0) {
            EllEyesInfoDto ellEyesInfoDto = new EllEyesInfoDto();
            ellEyesInfoDto.setLeftDiopter(0.0);
            ellEyesInfoDto.setRightDiopter(0.0);
            ellEyesInfoDto.setLeftVision(0.0);
            ellEyesInfoDto.setRightVision(0.0);
            ellEyesInfoDto.setTestTime(CreateTime.timeDay());
            ellEyesInfoDtos.add(ellEyesInfoDto);
        }
        return new Result(HttpStatus.SUCCESS, "成功", ellEyesInfoDtos);
    }*/
}
