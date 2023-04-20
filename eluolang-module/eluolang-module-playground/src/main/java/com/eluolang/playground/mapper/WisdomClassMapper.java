package com.eluolang.playground.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.vo.EllPlanStatusNumVo;
import com.eluolang.playground.vo.EllSelClassHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WisdomClassMapper {
    //添加成绩
    int addHisClass(@Param("histories") List<EllClassHistory> histories);

    //查询成绩
    List<EllSelClassHistory> selHisClass(@Param("history") EllSelClassHistory history);

    //添加视频
    int addVideo(@Param("classVideos") List<EllClassVideo> classVideos);

    //查询视频
    List<EllClassVideo> selVideo(@Param("classVideo") EllClassVideo classVideo);

    //添加课程时间
    int addClassTime(@Param("classTimes") List<EllSmartClassTime> classTimes);

    //删除课程时间
    int delClasTime(String schoolId);

    //查询课程时间
    List<EllSmartClassTime> selClassTime(Integer schoolId);

    //查询课程时间id
    EllSmartClassTime selClassTimeById(String Id);

    //通过班级查询课程时间
    List<EllSmartClassTime> selCourseTimeByClassId(Integer orgId);

    //查询课程时间
    List<EllSmartClassTimeDto> selCourseTimeByCourseId(Integer orgId);

    //添加课程
    int addClassSchedule(@Param("classSchedules") List<EllSmartClassSchedule> classSchedules);

    //通过帐号id查询学校id
    List<PfDepart> selSchools(@Param("accountId") int accountId, @Param("type") Boolean type);

    //通过帐号id查询学校id
    List<PfDepart> selSchoolsByOrgId(@Param("orgId") int orgId, @Param("type") Boolean type);

    //查询老师
    List<PfOperator> selTeacher(@Param("schoolId") Integer schoolId, @Param("accountId") Integer accountId);

    //查询日常计划
    EllDailyExercisePlan selDailyPlan(Integer schoolId);

    //删除课程
    int delCourseById(@Param("courseId") List<String> courseId);

    //单节课进行修改
    int updateCourseById(@Param("classSchedule") EllSmartClassSchedule classSchedule);

    //删除相应时间的课程
    int delCourseByTime(@Param("startTimeStamp") Long startTimeStamp, @Param("endTimeStamp") Long endTimeStamp, @Param("accountId") int accountId);

    //通过班级id查询学校
    PfDepart selSchoolByClassId(int classId);

    //上传课件
    int addCourseware(@Param("courseware") EllCourseware courseware);

    //查询课件
    List<EllCourseware> selCourse(@Param("coursewareName") String coursewareName, @Param("accountId") int accountId);

    //删除课件
    int delCourseware(@Param("coursewareId") List<String> coursewareId);

    //通过时间查询那一周的课表
    List<EllSmartClassScheduleDto> selSmartClassWeek(@Param("startTimeStamp") Long startTimeStamp, @Param("endTimeStamp") Long endTimeStamp, @Param("accountId") int accountId, @Param("weekDay") Integer weekDay, @Param("courseName") String courseName);

    //正在进行的课程
    List<EllSmartClassScheduleDto> selSmartClassIng(@Param("nowTimeStamp") Long nowTimeStamp, @Param("accountId") int accountId, @Param("courseName") String courseName);

    //查询每个项目的平均成绩以及最高成绩
    List<EllSmartGradeDto> selGradeAvgAndMax(@Param("classId") Integer classId, @Param("courseId") String courseId);

    //查询个人成绩
    List<EllStudentGradeDto> selUserGrade(@Param("userId") String userId, @Param("courseId") String courseId);

    //查询学生信息以及上课时间
    EllUserCourseDto selUserAndCourse(@Param("courseId") String courseId, @Param("userId") String userId);

    //查询视频
    List<EllClassVideo> selVideoByUserId(@Param("userId") String userId, @Param("proId") Integer proId, @Param("courseId") String courseId);

    //通过课程id查询
    List<EllGetUserByCourseIdDto> selUserByCourseId(@Param("courseId") String courseId, @Param("userName") String userName);

    //查询计划
    List<EllFlatPlatePlanDto> selPlanFlatPlate(@Param("isExam") int isExam, @Param("accountOrgId") int accountOrgId, @Param("status") Integer status);

    List<EllPlanStatusNumVo> selPlanStatusNum(@Param("isExam") int isExam, @Param("accountOrgId") int accountOrgId, @Param("status") Integer status);

    //查询每个人每个项目的最大/总数/最小数
    //type：1为时间越小学好，2是最大成绩越大越好，3是成绩总个数越大越好
    List<EllProScoreDto> selProScore(@Param("orgId") Integer orgId, @Param("proId") String proId, @Param("type") Integer type);

    //查询设备信息
    EllDevice selDevice(String deviceId);

    //通过日期查询某一天测试人数
    Integer selTestPeoNum(@Param("day") String day, @Param("orgId") Integer orgId);

    //查询某一个月每个项目的测试人次
    List<EllProTestNumDto> selProTestNum(@Param("day") String day, @Param("orgId") Integer orgId);

    //通过日期查询某一天每个项目的测试人次
    List<EllProTestNumDto> selProTestNumDay(@Param("day") String day, @Param("orgId") Integer orgId);

    //通过日期查询某一天男女测试人数
    Integer selProSexNumDay(@Param("day") String day, @Param("orgId") Integer orgId, @Param("sex") Integer sex);

    //查询测试成绩项目的名称
    List<String> selTestProName(Integer orgId);

    //部门人数
    Integer selSchoolNum(@Param("orgId") Integer orgId, @Param("sex") Integer sex);

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
    List<EllDeviceInfoDto> selInfoDevice(@Param("deviceId") String deviceId, @Param("type") String type);

    //查询发布文件
    List<String> selInfoFile(String infoId);

    //查询用户近多少天的跑步里程
    List<EllProRankUserDto> selDailyRunRanking(@Param("dailyIds") List<String> dailyIds, @Param("dayNum") int dayNum);

    //查询过去30天时间的班级达标每月目标的人数率
    Integer selPassNumBy30DayClass(@Param("classId") int classId, @Param("passNum") Double passNum);

    //查询学校的每个年级的锻炼规则
    List<EllConstitutionRulesDto> selConstitutionRules(Integer schoolId);

    //添加每个学校的计算规则
    Integer addConstitutionRules(@Param("constitutionRules") List<EllConstitutionRules> constitutionRules);

    //删除学校的计算规则
    Integer delConstitutionRules(Integer schoolId);
}
