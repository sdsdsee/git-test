package com.eluolang.module.report.mapper;

import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.module.report.vo.EllHeightAndWeightVo;
import com.eluolang.module.report.vo.EllSelYearPersonNumVo;
import com.eluolang.module.report.vo.EllUserYearProDateVo;
import com.eluolang.module.report.vo.EllYearAvgScoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArchivesMapper {
    /**
     * 查询账号最近几年的测试的总人数
     *
     * @param accountId
     * @param dayNum
     * @return
     */
    EllSelYearPersonNumVo selYearTestPersonNum(@Param("orgId") int orgId, @Param("accountId") int accountId, @Param("dayNum") int dayNum, @Param("isExam") int isExam);

    /**
     * 查询某一年某一区间分数的人数
     * greater大于某个数
     * less小于某个数
     * accountId账号id
     * year 那一年
     */
    int selYearNum(@Param("orgId") int orgId, @Param("accountId") int accountId, @Param("year") String year, @Param("greater") int greater, @Param("less") int less, @Param("isExam") int isExam);

    /**
     * 查询学生信息
     */
    List<EllUser> findUser(int orgId);

    /**
     * 查询某一年的平均分
     */
    EllYearAvgScoreVo selYearAvgScore(@Param("orgId") int orgId, @Param("accountId") int accountId, @Param("dayNum") int dayNum, @Param("isExam") int isExam);

    /**
     * 查询某用户的一项目前面某一年的平均分
     */
    EllUserYearProDateVo selUserYearData(@Param("userId") String userId, @Param("proId") int proId, @Param("isExam") int isExam, @Param("yearNum") int yearNum);

    /**
     * 查询用户某一年的体测成绩
     */
    EllYearAvgScoreVo selUserYearAvg(@Param("userId") String userId, @Param("yearNum") int yearNum);

    /**
     * 查询用户某一年的身高体重
     */
    EllHeightAndWeightVo selUserHeightAndWeight(@Param("userId") String userId, @Param("yearNum") int yearNum);
}
