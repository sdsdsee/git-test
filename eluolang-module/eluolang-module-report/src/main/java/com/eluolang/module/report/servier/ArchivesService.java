package com.eluolang.module.report.servier;

import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.module.report.vo.*;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArchivesService {
    /**
     * 查询账号最近几年的测试的总人数
     *
     * @param accountId
     * @return
     */
    List<EllSelYearPersonNumVo> selYearTestPersonNum(int orgId, int accountId, int yearNum, int isExam);

    /**
     * 查询某一年某一区间分数的人数
     * greater大于某个数
     * less小于某个数
     * accountId账号id
     * year 那一年
     */
    EllYearScoreGradeVo selYearNum(int orgId, int accountId, String year, int isExam);

    /**
     * 查询一段时间的某一区间各个等级分数的人数
     *
     * @param accountId
     * @param isExam
     * @return
     */
    List<EllAFewYearGradeNumVo> selAFewYearNum(int orgId, int accountId, int isExam);

    /**
     * 查询学生信息
     */
    List<EllUser> findUser(int orgId);

    /**
     * 查询某几年的平均分
     */
    List<EllYearAvgScoreVo> selYearAvgScore(int orgId, int accountId, int dayNum, int isExam);

    /**
     * 查询某用户的一项目近5年的平均分(身高体重，肺活量，50米跑，体前屈，跳绳，立定跳远)
     */
    List<EllUserYearProAvgVo> selUserYearData(String userId, int isExam);

    /**
     * 查询用户近5年的体测成绩
     */
    List<EllYearAvgScoreVo> selUserYearAvg(String userId);

    /**
     * 查询用户的最近5年身高体重
     */
    List<EllHeightAndWeightVo> selUserHeightAndWeight(String userId);

}
