package com.eluolang.physical.service;

import com.eluolang.common.core.pojo.EllDailyExercisePlan;
import com.eluolang.common.core.pojo.EllDailyPlanUseDepart;
import com.eluolang.common.core.pojo.EllDailyTimeQuantum;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.physical.dto.DailyPlanUseDepartDto;
import com.eluolang.physical.dto.EllDailyRunRankDto;
import com.eluolang.physical.dto.EllDailyTimeNumDto;
import com.eluolang.physical.dto.UserMonthDailyDto;
import com.eluolang.physical.model.EllDailyExercisePlanVo;
import com.eluolang.physical.model.EllDailyUserVo;
import com.eluolang.physical.model.EllSelDailyDetailsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DailyExerciseService {
    /**
     * 添加日常锻炼计划
     */
    int addDailyPlan(EllDailyExercisePlanVo ellDailyExercisePlan);

    /**
     * 添加日常时间段
     */
    int addDailyTime(List<EllDailyTimeQuantum> ellDailyTimeQuantum, String dailyId);

    /**
     * 查询是否有时间冲突的日常计划
     */
    List<EllDailyExercisePlan> selDailPlan(EllDailyExercisePlanVo ellDailyExercisePlan);

    /**
     * 通过日常计划查询
     */
    List<EllDailyExercisePlanVo> selDailyExercisePlanByName(String orgId, String account);

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
     * 通过日常计划查询
     */
    List<EllDailyExercisePlanVo> selDailyExercisePlan(String orgId, String account);

    /**
     * 查询所有部门
     */
    List<DailyPlanUseDepartDto> selPlanAllDepart(String orgId, String account);

    /**
     * 查询冲突时间段使用的部门
     */
    List<EllDailyPlanUseDepart> selPlanUseDp(String dailyId, String orgId);

    /**
     * 添加日常计划使用部门
     */
    int addPlanUseDp(List<DailyPlanUseDepartDto> pfDepartList, String dailyId);

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
    int updataDailyPlan(EllDailyExercisePlanVo ellDailyExercisePlan);

    /**
     * 查询时间点
     */
    List<EllDailyTimeQuantum> selUseTime(String dailyId, int timeType);

    /**
     * 查询日常计划人数
     */
    int selDailyNum(EllSelDailyDetailsVo ellSelDailyDetailsVo, String sex);

    /**
     * 查询当前跑步排行
     */
    List<EllDailyRunRankDto> selRankHistory(EllSelDailyDetailsVo ellSelDailyDetailsVo);

    /**
     * 查询未达标排行榜
     */
    List<EllDailyRunRankDto> selNotQualifiedHistoryRank(EllSelDailyDetailsVo ellSelDailyDetailsVo, int type, long startTime, long endTime);

    /**
     * 通过时间查看每个时间点跑步人数
     */
    int selTimeNum(String startTime, String endTime, String dailyId, String account, String orgId);

    /**
     * 通过不同的参数查询达标人数
     */
    int selMonthNum(String account, String orgId, String dailyId, Long monthStart, Long monthEnd, int type, String sex);

    /**
     * 查询一段日期的用户里程
     */
    int selUserTime(String userId, String dailyId, Long timeStart, Long timeEnd, int type);

    /**
     * 通过日常id查询日常
     */
    List<EllDailyExercisePlanVo> selDailyById(String dailyId);

    /**
     * 跑步里程达标示意图
     *
     * @param ellDailyUserVo
     * @return
     */
    List<UserMonthDailyDto> getUserMonthDaily(EllDailyUserVo ellDailyUserVo);

    /**
     * 人脸对比
     */
    String finUserId(String image, String deviceId);

    /**
     * 定时删除人脸信息
     */
    void delFaces();
}
