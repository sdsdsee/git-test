package com.eluolang.module.report.servier.impl;

import com.eluolang.common.core.pojo.EllBmiParticulars;
import com.eluolang.common.core.pojo.EllEyesightParticulars;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.module.report.dto.NearSevenDaysTestDto;
import com.eluolang.module.report.dto.OverYearsTestScoreDto;
import com.eluolang.module.report.dto.TestSituationQueryDto;
import com.eluolang.module.report.mapper.ReportMapper;
import com.eluolang.module.report.servier.ReportService;
import com.eluolang.module.report.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 报表模块逻辑实现层
 *
 * @author dengrunsen
 * @createDate 2022-3-3
 */

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Override
    public SchoolNumberVo selUserCount(Integer deptId, Integer optId) {
        return reportMapper.selUserCount(deptId,optId);
    }

    @Override
    public TestSituationQueryVo heightAndWeightTestNum(TestSituationQueryDto testSituationQueryDto) {
        return reportMapper.heightAndWeightTestNum(testSituationQueryDto);
    }

    @Override
    public VisionTestScoreVo selOverYearsVisionScore(OverYearsTestScoreDto overYearsTestScoreDto) {
        return reportMapper.selOverYearsVisionScore(overYearsTestScoreDto);
    }

    @Override
    public TestSituationQueryVo visionTestNum(TestSituationQueryDto testSituationQueryDto) {
        return reportMapper.visionTestNum(testSituationQueryDto);
    }

    @Override
    public HeightAndWeightVo selOverYearsTestScore(OverYearsTestScoreDto overYearsTestScoreDto) {
        return reportMapper.selOverYearsTestScore(overYearsTestScoreDto);
    }

    @Override
    public int selMeasuredNumber(Integer deptId, Integer optId,Integer year,Integer sex) {
        return reportMapper.selMeasuredNumber(deptId, optId,year,sex);
    }

    @Override
    public List<String> selUnqualifiedUser(NearSevenDaysTestDto nearSevenDaysTestDto) {
        return reportMapper.selUnqualifiedUser(nearSevenDaysTestDto);
    }

    @Override
    public VisionTestScoreVo selNearSevenDaysVision(NearSevenDaysTestDto nearSevenDaysTestDto) {
        return reportMapper.selNearSevenDaysVision(nearSevenDaysTestDto);
    }

    @Override
    public HeightAndWeightVo selNearSevenDaysBMI(NearSevenDaysTestDto nearSevenDaysTestDto) {
        return reportMapper.selNearSevenDaysBMI(nearSevenDaysTestDto);
    }

    @Override
    public VisionTestScoreVo selVisionRatio(NearSevenDaysTestDto nearSevenDaysTestDto) {
        return reportMapper.selVisionRatio(nearSevenDaysTestDto);
    }

    @Override
    public HeightAndWeightVo selBMIRatio(NearSevenDaysTestDto nearSevenDaysTestDto) {
        return reportMapper.selBMIRatio(nearSevenDaysTestDto);
    }

    @Override
    public VisionQualifiedRateVo selVisionQualifiedRate(NearSevenDaysTestDto nearSevenDaysTestDto) {
        return reportMapper.selVisionQualifiedRate(nearSevenDaysTestDto);
    }

    @Override
    public EllEyesightParticulars selVisionByUserId(String userId, String date) {
        return reportMapper.selVisionByUserId(userId, date);
    }

    @Override
    public EllBmiParticulars selBmiByUserId(String userId, String date) {
        return reportMapper.selBmiByUserId(userId, date);
    }

    @Override
    public EllEyesightParticulars selLastVisionTestById(String userId) {
        return reportMapper.selLastVisionTestById(userId);
    }

    @Override
    public EllBmiParticulars selLastBmiTestById(String userId) {
        return reportMapper.selLastBmiTestById(userId);
    }

    @Override
    public List<UserDailyResultVo> selUserTestResult(Integer deptId, Integer optId) {
        return reportMapper.selUserTestResult(deptId, optId);
    }

    @Override
    public PersonalVisionReportVo selUserInfoByUserId(String userId) {
        return reportMapper.selUserInfoByUserId(userId);
    }

    @Override
    public PersonalVisionReportVo selClassInfoByDeptId(Integer deptId) {
        return reportMapper.selClassInfoByDeptId(deptId);
    }

    @Override
    public PersonalVisionReportVo selSchoolInfoByDeptId(Integer deptId) {
        return reportMapper.selSchoolInfoByDeptId(deptId);
    }
}

