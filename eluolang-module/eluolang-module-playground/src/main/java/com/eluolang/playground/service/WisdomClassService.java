package com.eluolang.playground.service;


import com.eluolang.common.core.hardware.dto.EllDeviceInfoStateDto;
import com.eluolang.common.core.pojo.*;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface WisdomClassService {
    //添加成绩
    int addHisClass(List<EllClassHistoryVo> histories);

    //查询成绩
    List<EllSelClassHistory> selHisClass(EllSelClassHistory history);

    //项目id
    int addVideo(EllClassVideoVo classVideo, MultipartFile video) throws IOException;

    //查询视频
    List<EllClassVideo> selVideo(EllClassVideo classVideo);

    //添加课程时间
    int addClassTime(List<EllSmartClassTime> classTimes);

    //查询课程时间id
    EllSmartClassTime selClassTimeById(String Id);

    //添加课程
    int addClassSchedule(EllSmartClassScheduleVo classSchedules) throws Exception;

    //删除课程
    int delCourseById(List<String> courseId);

    //课进行修改
    int updateCourse(EllSmartClassScheduleVo classScheduleVo) throws Exception;

    //单节课进行修改
    int updateCourseById(EllSmartClassSchedule classSchedule);

    //通过时间查询那一周的课表
    //@param weekDay星期几，weekNum前后第几周
    List<EllSmartClassScheduleDto> selSmartClassWeek(Long startTimeStamp, Long endTimeStamp, int accountId, Integer weekDay, String courseName) throws ParseException;

    //查询课程时间
    List<EllSmartClassTime> selClassTime(Integer schoolId);

    //通过帐号id查询学校id
    List<PfDepart> selSchools(int accountId, Boolean type);

    //通过帐号id查询学校id
    List<PfDepart> selSchoolsByOrgId(int orgId, Boolean type);

    //查询日常计划
    EllDailyExercisePlan selDailyPlan(Integer schoolId);

    //查询老师
    List<PfOperator> selTeacher(Integer schoolId, Integer accountId);

    //上传课件
    int addCourseware(MultipartFile file, EllCourseware courseware) throws ParseException, IOException;

    //查询课件
    List<EllCourseware> selCourse(String coursewareName, int accountId);

    //删除课件
    int delCourseware(List<String> coursewareId);

    //正在进行的课程
    List<EllSmartClassScheduleDto> selSmartClassIng(Long nowTimeStamp, int accountId, String courseName);

    //通过班级查询课程时间
    List<EllSmartClassTime> selCourseTimeByClassId(Integer orgId);

    //查询课程时间
    List<EllSmartClassTimeDto> selCourseTimeByCourseId(Integer orgId);

    //查询每个项目的平均成绩以及最高成绩
    List<EllSmartGradeDto> selGradeAvgAndMax(Integer classId, String courseId);

    //查询学生信息以及上课时间
    EllUserCourseDto selUserAndCourse(String courseId, String userId);

    //查询个人成绩
    List<EllStudentGradeDto> selUserGrade(String userId, String courseId);

    //查看单人的视频
    List<EllClassVideo> selVideoByUserId(String userId, Integer proId, String courseId);

    //通过课程id查询
    List<EllGetUserByCourseIdDto> selUserByCourseId(String courseId, String userName);

    //查询计划
    List<EllFlatPlatePlanDto> selPlanFlatPlate(int isExam, int accountOrgId, Integer status);


    //查询计划各个状态的人数
    List<EllPlanStatusNumVo> selPlanStatusNum(Integer isExam, int accountOrgId, Integer status);

    //查询每个人每个项目的最大/总数/最小数
    //type：1为时间越小学好，2是最大成绩越大越好，3是成绩总个数越大越好
    List<EllProScoreDto> selProScore(Integer orgId, String proId, Integer type);

    //查询设备信息
    EllDevice selDevice(String deviceId);

    //通过日期查询某一天测试人数
    int selTestPeoNum(String day, Integer orgId);

    //查询某一个月每个项目的测试人次
    List<EllProTestNumDto> selProTestNum(String day, Integer orgId);

    //通过日期查询某一天每个项目的测试人次
    List<EllProTestNumDto> selProTestNumDay(String day, Integer orgId);

    //通过日期查询某一天男女测试人数
    Integer selProSexNumDay(String day, Integer orgId, Integer sex);

    //生成词云
    String createWordCloud(String deviceId);

    //部门人数
    Integer selSchoolNum(Integer orgId, Integer sex);

    //统计日常跑步的里程
    Integer selDailyRunMileage(String dailyPlanId);

    //统计课堂模式测人数
    Integer selSmartTestNum(int orgId);

    //统计课堂模式下的人次
    Integer selSmartTestTimes(int orgId);

    //查询班级
    List<String> selClassId(int orgId);

    //查询班级
    List<PfDepart> selClass(int orgId);

    //查询设备发布信息
    List<EllDeviceInfoDto> selInfoDevice(String deviceId, String type);

    //查询发布文件
    List<String> selInfoFile(String infoId);

    //查询用户近多少天的跑步里程
    List<EllProRankUserDto> selDailyRunRanking(List<String> dailyIds, int dayNum);

    //查询过去30天时间的班级达标每月目标的人数
    Integer selPassNumBy30DayClass(int classId, Double passNum);

    //保存设备状态
    void deviceInfoState(EllDeviceInfoStateDto ellDeviceInfoStateDto);

    //获取设备状态
    EllDeviceInfoStateDto getDeviceInfoState(String deviceId);

    //通过班级id查询学校
    PfDepart selSchoolByClassId(int classId);

    //查询学校的每个年级的锻炼规则
    List<EllConstitutionRulesDto> selConstitutionRules(Integer schoolId);

    //添加每个学校的计算规则
    Integer addConstitutionRules(List<EllConstitutionRules> constitutionRules);

    //删除学校的计算规则
    Integer delConstitutionRules(Integer schoolId);
}
