package com.eluolang.module.report.mapper;

import com.eluolang.common.core.pojo.EllBmiParticulars;
import com.eluolang.common.core.pojo.EllEyesightParticulars;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.module.report.dto.NearSevenDaysTestDto;
import com.eluolang.module.report.dto.OverYearsTestScoreDto;
import com.eluolang.module.report.dto.TestSituationQueryDto;
import com.eluolang.module.report.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表管理接口
 * @author dengrunsen
 * @createDate 2022-3-3
 */
@Mapper
public interface ReportMapper {
    /**
     * 根据部门id查询其下总人数
     * @param deptId
     * @param optId
     * @return
     */
    SchoolNumberVo selUserCount(@Param("deptId") Integer deptId, @Param("optId") Integer optId);

    /**
     * 身高体重各月测试人数
     * @param testSituationQueryDto
     * @return
     */
    TestSituationQueryVo heightAndWeightTestNum(TestSituationQueryDto testSituationQueryDto);

    /**
     * 视力各月测试人数
     * @param testSituationQueryDto
     * @return
     */
    TestSituationQueryVo visionTestNum(TestSituationQueryDto testSituationQueryDto);

    /**
     * 查询历年身高/体重测试结果
     * @param overYearsTestScoreDto
     * @return
     */
    HeightAndWeightVo selOverYearsTestScore(OverYearsTestScoreDto overYearsTestScoreDto);

    /**
     * 查询历年视力测试结果
     * @param overYearsTestScoreDto
     * @return
     */
    VisionTestScoreVo selOverYearsVisionScore(OverYearsTestScoreDto overYearsTestScoreDto);

    /**
     * 查询已测人数男/女占比
     * @param deptId
     * @param optId
     * @param year
     * @return
     */
    int selMeasuredNumber(@Param("deptId") Integer deptId, @Param("optId") Integer optId,@Param("year") Integer year,@Param("sex") Integer sex);

    /**
     * 查询某班级成绩不合格学生
     * @param nearSevenDaysTestDto
     * @return
     */
    List<String> selUnqualifiedUser(NearSevenDaysTestDto nearSevenDaysTestDto);

    /**
     * 近七天视力结果
     * @param nearSevenDaysTestDto
     * @return
     */
    VisionTestScoreVo selNearSevenDaysVision(NearSevenDaysTestDto nearSevenDaysTestDto);

    /**
     * 近七天BMI结果
     * @param nearSevenDaysTestDto
     * @return
     */
    HeightAndWeightVo selNearSevenDaysBMI(NearSevenDaysTestDto nearSevenDaysTestDto);

    /**
     * 视力占比
     * @param nearSevenDaysTestDto
     * @return
     */
    VisionTestScoreVo selVisionRatio(NearSevenDaysTestDto nearSevenDaysTestDto);

    /**
     * BMI占比
     * @param nearSevenDaysTestDto
     * @return
     */
    HeightAndWeightVo selBMIRatio(NearSevenDaysTestDto nearSevenDaysTestDto);

    /**
     * 视力合格率
     * @param nearSevenDaysTestDto
     * @return
     */
    VisionQualifiedRateVo selVisionQualifiedRate(NearSevenDaysTestDto nearSevenDaysTestDto);

    /**
     * 查询视力详情
     * @param userId
     * @param date
     * @return
     */
    EllEyesightParticulars selVisionByUserId(@Param("userId") String userId,@Param("date") String date);

    /**
     * 查询身高体重详情
     * @param userId
     * @param date
     * @return
     */
    EllBmiParticulars selBmiByUserId(@Param("userId") String userId,@Param("date") String date);

    /**
     * 查询最近一次视力测试详情
     * @param userId
     * @return
     */
    EllEyesightParticulars selLastVisionTestById(String userId);

    /**
     * 查询最近一次身高体重测试详情
     * @param userId
     * @return
     */
    EllBmiParticulars selLastBmiTestById(String userId);

    /**
     * 查询日常监测学生信息
     * @param deptId
     * @param optId
     * @return
     */
    List<UserDailyResultVo> selUserTestResult(@Param("deptId") Integer deptId,@Param("optId") Integer optId);

    /**
     * 根据学生id查询学生信息
     * @param userId
     * @return
     */
    PersonalVisionReportVo selUserInfoByUserId(String userId);

    /**
     * 根据部门id查询班级学校
     * @param deptId
     * @return
     */
    PersonalVisionReportVo selClassInfoByDeptId(Integer deptId);

    /**
     * 根据部门id查询学校
     * @param deptId
     * @return
     */
    PersonalVisionReportVo selSchoolInfoByDeptId(Integer deptId);
}
