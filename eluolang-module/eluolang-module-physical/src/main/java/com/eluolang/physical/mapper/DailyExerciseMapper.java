package com.eluolang.physical.mapper;

import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.*;
import com.eluolang.physical.dto.DailyPlanUseDepartDto;
import com.eluolang.physical.dto.EllDailyRunRankDto;
import com.eluolang.physical.dto.EllDailyTimeNumDto;
import com.eluolang.physical.model.EllDailyExercisePlanVo;
import com.eluolang.physical.model.EllSelDailyDetailsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DailyExerciseMapper {
    /**
     * 添加日常锻炼计划
     */
    int addDailyPlan(@Param("ellDailyExercisePlan") EllDailyExercisePlanVo ellDailyExercisePlan);

    /**
     * 添加日常时间段
     */
    int addDailyTime(@Param("ellDailyTimeQuantumList") List<EllDailyTimeQuantum> ellDailyTimeQuantumList, @Param("dailyId") String dailyId);

    /**
     * 查询是否有时间冲突的日常计划
     */
    List<EllDailyExercisePlan> selDailPlan(@Param("ellDailyExercisePlan") EllDailyExercisePlanVo ellDailyExercisePlan);

    /**
     * 通过日常名称日常计划查询
     */
    List<EllDailyExercisePlanVo> selDailyExercisePlanByName(@Param("orgId") String orgId, @Param("account") String account);

    /**
     * 查询日常计划发布的文本内容
     *
     * @param dailyId
     * @return
     */
    String selDailyTextContent(String dailyId);

    /**
     * 通过设备查询日常计划发布的文本内容
     */
    String selDailyTextContentDeviceId(String deviceId);

    /**
     * 查询权限所有部门
     */
    List<DailyPlanUseDepartDto> selPlanAllDepart(@Param("orgId") String orgId, @Param("account") String account);

    /**
     * 查询冲突时间段是否使用这个部门
     */
    List<EllDailyPlanUseDepart> selPlanUseDp(@Param("dailyId") String dailyId, @Param("orgId") String orgId);

    /**
     * 添加日常计划使用部门
     */
    int addPlanUseDp(@Param("pfDepartList") List<DailyPlanUseDepartDto> pfDepartList, @Param("dailyId") String dailyId);

    /**
     * 删除日常计划时间点
     */
    int delPlanTime(String dailyId);

    /**
     * 删除日常计划使用部门
     */
    int delPlanUseDp(String dailyId);

    /**
     * 修改日常计划
     */
    int updataDailyPlan(@Param("ellDailyExercisePlan") EllDailyExercisePlanVo ellDailyExercisePlan);

    /**
     * 查询时间点
     */
    List<EllDailyTimeQuantum> selUseTime(@Param("dailyId") String dailyId, @Param("timeType") int timeType);

    /**
     * 查询日常计划人数
     */
    int selDailyNum(@Param("ellSelDailyDetailsVo") EllSelDailyDetailsVo ellSelDailyDetailsVo, @Param("sex") String sex);

    /**
     * 查询当前跑步排行
     */
    List<EllDailyRunRankDto> selRankHistory(@Param("ellSelDailyDetailsVo") EllSelDailyDetailsVo ellSelDailyDetailsVo);

    /**
     * 查询未达标排行榜
     */
    List<EllDailyRunRankDto> selNotQualifiedHistoryRank(@Param("ellSelDailyDetailsVo") EllSelDailyDetailsVo ellSelDailyDetailsVo, @Param("type") int type, @Param("startTime") long startTime, @Param("endTime") long endTime);

    /**
     * 通过时间查看每个时间点跑步人数
     */
    int selTimeNum(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("dailyId") String dailyId, @Param("account") String account, @Param("orgId") String orgId);

    /**
     * 查询账号下的学校
     */
    List<PfDepart> selDepart(String account);

    /**
     * 通过不同的参数查询达标人数
     */
    int selMonthNum(@Param("account") String account, @Param("orgId") String orgId, @Param("dailyId") String dailyId, @Param("monthStart") Long monthStart, @Param("monthEnd") Long monthEnd, @Param("type") int type, @Param("sex") String sex);

    /**
     * 查询一段日期的用户里程
     */
    int selUserTime(@Param("userId") String userId, @Param("dailyId") String dailyId, @Param("timeStart") Long timeStart, @Param("timeEnd") Long timeEnd, @Param("type") int type);

    /**
     * 通过日常id查询日常
     */
    List<EllDailyExercisePlanVo> selDailyById(String dailyId);

    /**
     * 通过设备id查询学生的人脸特征
     */
    List<ImgUserRelevance> selUserRelevance(String deviceId);

    /**
     * 通过学生id查询学生的人脸特征
     */
    ImgUserRelevance selUserRelevanceById(String userId);

    /**
     * 通过设备查询绑定的部门
     */
    String selOrgIdByDeviceId(String deviceId);
}
