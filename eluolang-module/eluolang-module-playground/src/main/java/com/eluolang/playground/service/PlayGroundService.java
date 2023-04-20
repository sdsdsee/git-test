package com.eluolang.playground.service;

import com.eluolang.common.core.hardware.dto.PhysicalTrainDto;
import com.eluolang.common.core.pojo.*;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.vo.*;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;

/**
 * 智慧操场模块逻辑层
 *
 * @author dengrunsen
 * @date 2022年08月19日 13:56
 */
public interface PlayGroundService {

    /** 分析当次训练体能数据 */
    int physicalTrainAnalysis(PhysicalTrainDto physicalTrainDto);

    /**
     * 查询相同时间点位触发的记录
     * @param mac
     * @param locationTime
     * @return
     */
    int selLocationDataIsExist(String mac, String locationTime);

    /**
     * 批量添加日常训练数据
     * @param ellDailyHistoryList
     * @return
     */
    int addDailyHistoryList(List<EllDailyHistory> ellDailyHistoryList);

    /**
     * 当前跑步人员详情
     * @param physicalTrainDto
     * @return
     */
    CurrentRunningDetailsVo selCurrentRunningDetails(PhysicalTrainDto physicalTrainDto);

    /**
     * 根据学生id和检录时间查询跑步信息
     * @param userId
     * @param locationTime
     * @return
     */
    List<EllDailyHistoryVo> selDailyHistoryByLocationTime(String dailyId, String userId, String locationTime);

    /**
     * 查询某个学生跑步里程总数
     * @param dailyId
     * @param userId
     * @return
     */
    CurrentRunningDetailsVo selMileageSumByUserId(@Param("dailyId") String dailyId,@Param("userId") String userId);

    /**
     * 跑步每周人数统计
     * @return
     */
    StandardNumVo selRunningNumberCensus(PlanTotalNumDto planTotalNumDto);

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
    List<RunningDataVo> selRunningData(List<Integer> deptIdList);

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
     * 查询某天运动人数
     * @param date
     * @return
     */
    TotalSportNumberVo selTotalSportNumber(String date,String dailyId);

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
    List<EllDailyHistoryVo> selHeartRateInterval(String dailyId,String userId,String day);

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
     * 查询某人跑步排名
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

//    /**
//     * 长跑测试成绩添加
//     * @return
//     */
//    int longRacePhysicalAdd(PhysicalTrainDto physicalTrainDto) throws IOException;

    /**
     * 长跑结束测试成绩错误处理
     * @param physicalTrainDto
     * @return
     */
    int longRaceResult(PhysicalTrainDto physicalTrainDto) throws IOException;

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
    List<EllDailyHistoryVo> runSituationAnalyse(String dailyId,String userId,String date);

//    /**
//     * 连接手环
//     * @param list
//     */
//    void issueInstruction(List<String> list);
//
//    /**
//     * 解除连接
//     * @param list
//     */
//    void removeIssued(List<String> list);

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
