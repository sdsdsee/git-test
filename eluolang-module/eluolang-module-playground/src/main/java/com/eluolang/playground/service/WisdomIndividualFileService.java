package com.eluolang.playground.service;

import com.eluolang.common.core.pojo.EllClassHistory;
import com.eluolang.playground.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WisdomIndividualFileService {
    //计算身体各个部位的差别问题
    EllBodyProblemPositionDto calculationBodyError(String userId);

    //查询近期的最好成绩
    List<EllProScoreDto> selStudentData(String userId, Integer dayNum);

    //查询该项目好于该成绩的人数
    Integer selBetterThanGradeNum(Integer grade, int proId, Integer type);

    //查询跑步总里程
    EllStudentGradeDto selRunMileage(String userId);

    //查询本周任务完成情况
    Integer selWeekRun(String userId, long time);

    //查询学生信息
    EllIndividualUserDto selUser(String userId);

    //查询用户所有计划分数
    List<EllEndScoreDto> selPlanScore(String userId);

    //查询用户测试过的项目
    List<EllClassGradeDto> selClassGrade(String userId, List<EllClassGradeDayDto> proIds);

    //查询测试过的项目
    List<EllClassGradeDayDto> selTestPro(String userId);

    //查询不同项目最后一条的成绩
    List<EllClassHistory> selLastOneProHistory(String userId);

    //查询视力最近的15条
    List<EllEyesInfoDto> selEyesInfo(String userId);

}
