package com.eluolang.playground.mapper;

import com.eluolang.common.core.pojo.EllClassHistory;
import com.eluolang.common.core.pojo.EllTestRulesScore;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.playground.dto.*;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface WisdomIndividualFileMapper {
    //查询规则
    List<EllTestRulesScore> selTestRulesScore(@Param("grade") Integer grade, @Param("schoolId") Integer schoolId, @Param("proId") Integer proId);

    //通过学生Id查询学生信息
    EllUser selUserById(String id);

    //查询跑步总里程
    EllStudentGradeDto selRunMileage(String userId);

    //查询近期的最好成绩
    List<EllProScoreDto> selStudentData(@Param("userId") String userId, @Param("dayNum") Integer dayNum);

    //查询该项目好于该成绩的人数
    Integer selBetterThanGradeNum(@Param("grade") Integer grade, @Param("proId") int proId, @Param("type") Integer type);

    //查询本周任务完成情况
    Integer selWeekRun(@Param("userId") String userId, @Param("time") long time);

    //查询学生信息
    EllIndividualUserDto selUser(String userId);

    //查询用户所有计划分数
    List<EllEndScoreDto> selPlanScore(String userId);

    //查询用户不同项目的成绩
    List<EllClassGradeDto> selClassGrade(@Param("userId") String userId, @Param("gradeDay") List<EllClassGradeDayDto> gradeDay);

    //查询测试过的项目
    List<EllClassGradeDayDto> selTestPro(String userId);

    //查询视力最近的15条
    List<EllEyesInfoDto> selEyesInfo(String userId);

    //查询不同项目最后一条的成绩
    List<EllClassHistory> selLastOneProHistory(String userId);

    //查询建议规则
    List<EllSuggestionDto> selSuggestion(Integer proId);
}
