package com.eluolang.playground.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.playground.dto.MonthRunDataDto;
import com.eluolang.playground.dto.PlanTotalNumDto;
import com.eluolang.playground.dto.RunningDataDto;
import com.eluolang.playground.dto.RunningRankDto;
import com.eluolang.playground.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlayGroundMapper {
    /**
     * 查询相同时间点位触发的记录
     * @param mac
     * @param locationTime
     * @return
     */
    int selLocationDataIsExist(@Param("mac") String mac,@Param("locationTime") String locationTime);

    /**
     * 批量添加日常训练数据
     * @param ellDailyHistoryList
     * @return
     */
    int addDailyHistoryList(@Param("ellDailyHistoryList") List<EllDailyHistory> ellDailyHistoryList);

    /**
     * 根据学生id和检录时间查询跑步信息
     * @param userId
     * @param locationTime
     * @return
     */
    List<EllDailyHistoryVo> selDailyHistoryByLocationTime(@Param("dailyId") String dailyId, @Param("userId") String userId, @Param("locationTime") String locationTime);

    /**
     * 查询某个学生跑步里程总数
     * @param dailyId
     * @param userId
     * @return
     */
    CurrentRunningDetailsVo selMileageSumByUserId(@Param("dailyId") String dailyId, @Param("userId") String userId);

    /**
     * 查询日常锻炼计划总人数
     * @param dailyId
     * @return
     */
    Integer selPlanTotalNum(String dailyId);

    /**
     * 根据日常锻炼计划id查询计划详情
     * @param id
     * @return
     */
    EllDailyExercisePlan selDailyPlanById(String id);

    /**
     * 查询某计划有效时间段
     * @param dailyId
     * @return
     */
    List<EllDailyTimeQuantum> selDailyTimeQuantum(String dailyId);

    /**
     * 跑步每周人数统计
     * @param planTotalNumDto
     * @return
     */
    StandardNumVo selStandardNum(PlanTotalNumDto planTotalNumDto);

    /**
     * 根据设备id查询设备发布的视频信息
     */
    List<EllDeviceInfoVo> findDeviceInfo(String deviceId);

    /**
     * 根据设备id查询日常锻炼计划详情
     * @param deviceId
     * @return
     */
    EllDailyExercisePlan selExercisePlan(String deviceId);

    /**
     * 近30天每日跑步人数和里程
     * @param deptIdList
     * @return
     */
    List<RunningDataVo> selRunningData(@Param("deptIdList") List<Integer> deptIdList);

    /**
     * 查询某天运动人数
     * @param date
     * @return
     */
    TotalSportNumberVo selTotalSportNumber(@Param("date") String date,@Param("dailyId") String dailyId);

    /**
     * 查询某人运动排行
     * @param monthRunDataDto
     * @return
     */
    MonthRunDataVo selRankByUserId(MonthRunDataDto monthRunDataDto);

    /**
     *查询某人某月完成的跑步里程数
     * @return
     */
    Integer selMileageStandard(MonthRunDataDto monthRunDataDto);

    /**
     * 查询近十天跑步心率区间
     * @param dailyId
     * @param userId
     * @param day
     * @return
     */
    List<EllDailyHistoryVo> selHeartRateInterval(@Param("dailyId") String dailyId,@Param("userId") String userId,@Param("day") String day);

    /**
     * 当前跑步人员信息
     * @param runningDataDto
     * @return
     */
    CurrentUserRunDataVo selCurrentUserRunData(RunningDataDto runningDataDto);

    /**
     * 到达指定点位信息
     * @param runningDataDto
     * @return
     */
    GetLocationTimeVo selLocationTime(RunningDataDto runningDataDto);

    /**
     * 查询当次跑步的所有信息
     * @param runningDataDto
     * @return
     */
    List<EllDailyHistoryVo> selCurrentRunData(RunningDataDto runningDataDto);

    /**
     * 查询某人班级跑步排名
     * @param runningRankDto
     * @return
     */
    RunningRankVo selUserRunRank(RunningRankDto runningRankDto);

    /**
     * 查询某人学校跑步排名
     * @param runningRankDto
     * @return
     */
    RunningRankVo selUserRunRankBySchool(RunningRankDto runningRankDto);

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    EllUser selUserById(String userId);

    /**
     * 查询体测项目详情
     * @param name
     * @return
     */
    EllTestProject selTestProject(String name);

    /**
     * 跑步情况分析
     * @param date
     * @return
     */
    List<EllDailyHistoryVo> runSituationAnalyse(@Param("dailyId") String dailyId,@Param("userId") String userId,@Param("date") String date);

    /**
     * 根据人员id查询人员信息
     * @param userId
     * @return
     */
    UserCallVo selUserMessageById(String userId);

    /**
     * 根据部门id查询计划id
     * @param orgId
     * @return
     */
    String selDailyIdByOrgId(Integer orgId);

    /**
     * 根据旷视人脸底库照片id查询学生信息
     *
     * @param imgId
     * @return
     */
    EllUser selSignUserByCode(String imgId);

    /**
     * 根据部门id查询所属学校信息
     *
     * @param deptId
     * @return
     */
    EllDailyExercisePlan selPlanDataById(Integer deptId);
}
