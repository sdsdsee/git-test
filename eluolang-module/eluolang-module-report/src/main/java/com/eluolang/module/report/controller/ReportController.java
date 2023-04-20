package com.eluolang.module.report.controller;

import com.alibaba.fastjson.JSON;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.TestProjectConstant;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.EllBmiParticulars;
import com.eluolang.common.core.pojo.EllEyesightParticulars;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.FileUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.module.report.dto.NearSevenDaysTestDto;
import com.eluolang.module.report.dto.OverYearsTestScoreDto;
import com.eluolang.module.report.dto.TestSituationQueryDto;
import com.eluolang.module.report.model.Hw;
import com.eluolang.module.report.model.Vision;
import com.eluolang.module.report.servier.ReportService;
import com.eluolang.module.report.util.BmiTestResultUtil;
import com.eluolang.module.report.util.PathGeneration;
import com.eluolang.module.report.util.wordTest;
import com.eluolang.module.report.vo.*;
import com.github.pagehelper.PageInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import com.eluolang.common.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import sun.awt.HeadlessToolkit;

import javax.annotation.Resource;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表管理控制器
 *
 * @author dengrunsen
 * @createDate 2022-3-3
 */

@Api("报表管理")
@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private RedisService redisService;

    private static String visionReportPath = "https://qdf.eluolang.cn/Excel/";

    private static String WINDOWS_FACE_PHOTO = "D:\\EllTestImg";

    private static String LINUX_FACE_PHOTO = "/home/EllTestImg";

    /**
     * 学生总人数查询
     *
     * @return Result
     */
    @ApiOperation(value = "学生总人数查询")
    @PostMapping(value = "/selUserCount")
    public Result selUserCount(Integer deptId, Integer optId) {
        SchoolNumberVo schoolNumberVo = reportService.selUserCount(deptId, optId);
        return new Result(HttpStatus.SUCCESS, "查询成功", schoolNumberVo);
    }

    /**
     * 身高体重测试情况
     *
     * @return Result
     */
    @ApiOperation(value = "身高体重测试情况")
    @PostMapping(value = "/selHeightAndWeightTest")
    public Result selHeightAndWeightTest(@RequestBody TestSituationQueryDto testSituationQueryDto) {
        TestSituationQueryVo testSituationQueryVo = reportService.heightAndWeightTestNum(testSituationQueryDto);
        return new Result(HttpStatus.SUCCESS, "查询成功", testSituationQueryVo);
    }

    /**
     * 视力测试情况
     *
     * @return Result
     */
    @ApiOperation(value = "视力测试情况")
    @PostMapping(value = "/selVisionTestNum")
    public Result selVisionTestNum(@RequestBody TestSituationQueryDto testSituationQueryDto) {
        String[] comments = {"近视", "远视", "正常"};
        List<VisionTestVo> visionTestVos = new ArrayList<>();
        for (int i = 0; i < comments.length; i++) {
            testSituationQueryDto.setComment(comments[i]);
            TestSituationQueryVo list = reportService.visionTestNum(testSituationQueryDto);
            VisionTestVo visionTestVo = new VisionTestVo();
            visionTestVo.setComment(comments[i]);
            visionTestVo.setTestSituationQueryVo(list);
            visionTestVos.add(visionTestVo);
        }
        return new Result(HttpStatus.SUCCESS, "查询成功", visionTestVos);
    }

    /**
     * 查询历年身高/体重测试结果
     *
     * @return Result
     */
    @ApiOperation(value = "查询历年身高/体重测试结果")
    @PostMapping(value = "/selOverYearsTestScore")
    public Result selOverYearsTestScore(@RequestBody OverYearsTestScoreDto overYearsTestScoreDto) {
        List<OverYearsTestScoreVo> o = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            OverYearsTestScoreVo overYearsTestScoreVo = new OverYearsTestScoreVo();
            int dateYear = Calendar.getInstance().get(Calendar.YEAR) - (i - 1);
            overYearsTestScoreDto.setYear(dateYear);
            overYearsTestScoreVo.setDateYear(dateYear);
            OverYearsTestScoreVo.DataBean dataBean = new OverYearsTestScoreVo.DataBean();
            HeightAndWeightVo heightAndWeightVo = reportService.selOverYearsTestScore(overYearsTestScoreDto);

            dataBean.setThin(heightAndWeightVo.getThin());
            dataBean.setNormal(heightAndWeightVo.getNormal());
            dataBean.setOverweight(heightAndWeightVo.getOverweight());
            dataBean.setFat(heightAndWeightVo.getFat());
            overYearsTestScoreVo.setDataBean(dataBean);
            o.add(overYearsTestScoreVo);

        }
        return new Result(HttpStatus.SUCCESS, "查询成功", o);
    }

    /**
     * 查询历年视力测试结果
     *
     * @return Result
     */
    @ApiOperation(value = "查询历年视力测试结果")
    @PostMapping(value = "/selOverYearsVisionScore")
    public Result selOverYearsVisionScore(@RequestBody OverYearsTestScoreDto overYearsTestScoreDto) {
        List<OverYearsVisionTestVo> o = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            OverYearsVisionTestVo overYearsVisionTestVo = new OverYearsVisionTestVo();
            int dateYear = Calendar.getInstance().get(Calendar.YEAR) - (i - 1);
            overYearsTestScoreDto.setYear(dateYear);
            overYearsVisionTestVo.setDateYear(dateYear);
            OverYearsVisionTestVo.DataBean dataBean = new OverYearsVisionTestVo.DataBean();
            VisionTestScoreVo visionTestScoreVo = reportService.selOverYearsVisionScore(overYearsTestScoreDto);

            dataBean.setMyopia(visionTestScoreVo.getMyopia());
            dataBean.setHyperopia(visionTestScoreVo.getHyperopia());
            dataBean.setNormal(visionTestScoreVo.getNormal());
            overYearsVisionTestVo.setDataBean(dataBean);
            o.add(overYearsVisionTestVo);
        }
        return new Result(HttpStatus.SUCCESS, "查询成功", o);
    }

    /**
     * BMI测试结果占比
     *
     * @return Result
     */
    @ApiOperation(value = "BMI测试结果占比")
    @PostMapping(value = "/selTestResultProportion")
    public Result selTestResultProportion(@RequestBody OverYearsTestScoreDto overYearsTestScoreDto) {
        HeightAndWeightVo heightAndWeightVo = reportService.selOverYearsTestScore(overYearsTestScoreDto);
        return new Result(HttpStatus.SUCCESS, "查询成功", heightAndWeightVo);
    }

    /**
     * 视力测试结果占比
     *
     * @return Result
     */
    @ApiOperation(value = "视力测试结果占比")
    @PostMapping(value = "/selVisionProportion")
    public Result selVisionProportion(@RequestBody OverYearsTestScoreDto overYearsTestScoreDto) {
        VisionTestScoreVo visionTestScoreVo = reportService.selOverYearsVisionScore(overYearsTestScoreDto);
        return new Result(HttpStatus.SUCCESS, "查询成功", visionTestScoreVo);
    }

    /**
     * 查询已测人数男/女占比
     *
     * @return Result
     */
    @ApiOperation(value = "查询已测人数男/女占比")
    @PostMapping(value = "/selMeasuredNumber")
    public Result selMeasuredNumber(Integer deptId, Integer optId, Integer year) {
        SchoolNumberVo schoolNumberVo = new SchoolNumberVo();
        Integer man = reportService.selMeasuredNumber(deptId, optId, year, 1);
        Integer woman = reportService.selMeasuredNumber(deptId, optId, year, 2);
        Integer total = man + woman;
        schoolNumberVo.setTotal(String.valueOf(total));
        schoolNumberVo.setMan(String.valueOf(man));
        schoolNumberVo.setWoman(String.valueOf(woman));
        return new Result(HttpStatus.SUCCESS, "查询成功", schoolNumberVo);
    }

    /**
     * 视力不合格学生预警
     *
     * @return Result
     */
    @ApiOperation(value = "视力不合格学生预警")
    @PostMapping(value = "/selUnqualifiedUser")
    public Result selUnqualifiedUser(@RequestBody NearSevenDaysTestDto nearSevenDaysTestDto) {
        List<String> list = reportService.selUnqualifiedUser(nearSevenDaysTestDto);
        return new Result(HttpStatus.SUCCESS, "查询成功", list);
    }

    /**
     * 近七天视力结果
     *
     * @return Result
     */
    @ApiOperation(value = "近七天视力结果")
    @PostMapping(value = "/selNearSevenDaysVision")
    public Result selNearSevenDaysVision(@RequestBody NearSevenDaysTestDto nearSevenDaysTestDto) {
        List<NearSevenDaysTestVo> o = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 7; i > 0; i--) {
            NearSevenDaysTestVo nearSevenDaysTestVo = new NearSevenDaysTestVo();
            Date date = DateUtils.addDays(new Date(), -i);
            String formatDate = sdf.format(date);
            nearSevenDaysTestDto.setDay(formatDate);
            nearSevenDaysTestVo.setDateDay(formatDate);
            NearSevenDaysTestVo.DataBean dataBean = new NearSevenDaysTestVo.DataBean();
            VisionTestScoreVo visionTestScoreVo = reportService.selNearSevenDaysVision(nearSevenDaysTestDto);

            dataBean.setMyopia(visionTestScoreVo.getMyopia());
            dataBean.setHyperopia(visionTestScoreVo.getHyperopia());
            dataBean.setNormal(visionTestScoreVo.getNormal());
            nearSevenDaysTestVo.setDataBean(dataBean);
            o.add(nearSevenDaysTestVo);
        }
        return new Result(HttpStatus.SUCCESS, "查询成功", o);
    }

    /**
     * 近七天BMI结果
     *
     * @return Result
     */
    @ApiOperation(value = "近七天BMI结果")
    @PostMapping(value = "/selNearSevenDaysBMI")
    public Result selNearSevenDaysBMI(@RequestBody NearSevenDaysTestDto nearSevenDaysTestDto) {
        List<NearSevenDaysBMIVo> o = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 7; i > 0; i--) {
            NearSevenDaysBMIVo nearSevenDaysBMIVo = new NearSevenDaysBMIVo();
            Date date = DateUtils.addDays(new Date(), -i);
            String formatDate = sdf.format(date);
            nearSevenDaysTestDto.setDay(formatDate);
            nearSevenDaysBMIVo.setDateDay(formatDate);
            NearSevenDaysBMIVo.DataBean dataBean = new NearSevenDaysBMIVo.DataBean();
            HeightAndWeightVo heightAndWeightVo = reportService.selNearSevenDaysBMI(nearSevenDaysTestDto);

            dataBean.setThin(heightAndWeightVo.getThin());
            dataBean.setNormal(heightAndWeightVo.getNormal());
            dataBean.setOverweight(heightAndWeightVo.getOverweight());
            dataBean.setFat(heightAndWeightVo.getFat());
            nearSevenDaysBMIVo.setDataBean(dataBean);
            o.add(nearSevenDaysBMIVo);
        }
        return new Result(HttpStatus.SUCCESS, "查询成功", o);
    }

    /**
     * 视力占比
     *
     * @return Result
     */
    @ApiOperation(value = "视力占比")
    @PostMapping(value = "/selVisionRatio")
    public Result selVisionRatio(@RequestBody NearSevenDaysTestDto nearSevenDaysTestDto) {
        VisionTestScoreVo visionTestScoreVo = reportService.selVisionRatio(nearSevenDaysTestDto);
        return new Result(HttpStatus.SUCCESS, "查询成功", visionTestScoreVo);
    }

    /**
     * BMI占比
     *
     * @return Result
     */
    @ApiOperation(value = "BMI占比")
    @PostMapping(value = "/selBMIRatio")
    public Result selBMIRatio(@RequestBody NearSevenDaysTestDto nearSevenDaysTestDto) {
        HeightAndWeightVo heightAndWeightVo = reportService.selBMIRatio(nearSevenDaysTestDto);
        return new Result(HttpStatus.SUCCESS, "查询成功", heightAndWeightVo);
    }

    /**
     * 视力合格率
     *
     * @return Result
     */
    @ApiOperation(value = "视力合格率")
    @PostMapping(value = "/selVisionQualifiedRate")
    public Result selVisionQualifiedRate(@RequestBody NearSevenDaysTestDto nearSevenDaysTestDto) {
        VisionQualifiedRateVo visionQualifiedRateVo = reportService.selVisionQualifiedRate(nearSevenDaysTestDto);
        return new Result(HttpStatus.SUCCESS, "查询成功", visionQualifiedRateVo);
    }

    /**
     * 学生视力和BMI最近一次检测结果
     *
     * @return Result
     */
    @ApiOperation(value = "学生视力和BMI最近一次检测结果")
    @PostMapping(value = "/visionAndBmiTestResult")
    public Result visionAndBmiTestResult(String userId) {
        TestResultVo testResultVo = new TestResultVo();
        //查询最近一次的视力测试情况
        EllEyesightParticulars ellEyesightParticulars = reportService.selLastVisionTestById(userId);
        if (ellEyesightParticulars != null) {
            testResultVo.setLeftAndRightVision(ellEyesightParticulars.getLeftVision() + "/" + ellEyesightParticulars.getRightVision());
            testResultVo.setLeftAndRightDiopterAx(ellEyesightParticulars.getLeftDiopterAx() + "/" + ellEyesightParticulars.getRightDiopterAx());
            testResultVo.setLeftAndRightDiopterCyl(ellEyesightParticulars.getLeftDiopterCyl() + "/" + ellEyesightParticulars.getRightDiopterCyl());
            testResultVo.setLeftAndRightDiopterSph(ellEyesightParticulars.getLeftDiopterSph() + "/" + ellEyesightParticulars.getRightDiopterSph());
            testResultVo.setPupilDistance(ellEyesightParticulars.getPupilDistance());
            //testResultVo.setLeftAnfRightMirror();
            testResultVo.setLeftAndRightDiopterNormal(ellEyesightParticulars.getLeftDioptricNormal() + "/" + ellEyesightParticulars.getRightDioptricNormal());
            testResultVo.setVisionTestResult(ellEyesightParticulars.getVisionComment());
        }
        //查询最近一次的身高/体重测试情况
        EllBmiParticulars ellBmiParticulars = reportService.selLastBmiTestById(userId);
        if (ellBmiParticulars != null) {
            testResultVo.setHeightAndWeight(ellBmiParticulars.getHeight() + "/" + ellBmiParticulars.getWeight());
            testResultVo.setBmi(ellBmiParticulars.getBmi());
            testResultVo.setBmiTestResult(ellBmiParticulars.getBmiComment());
        }
        return new Result(HttpStatus.SUCCESS, "查询成功", testResultVo);
    }

    /**
     * 学生视力和BMI日常监测(近七天)
     *
     * @return Result
     */
    @ApiOperation(value = "学生视力和BMI日常监测(近七天)")
    @PostMapping(value = "/selVisionAndBmiDaily")
    public Result selVisionAndBmiDaily(String userId) {
        VisionDailyVo visionDailyVo = new VisionDailyVo();
        /** 左眼裸眼视力 */
        List<VisionDailyVo.DataBean> leftVision = new ArrayList<>();
        /** 右眼裸眼视力 */
        List<VisionDailyVo.DataBean> rightVision = new ArrayList<>();
        /** 左眼屈光度(球镜度) */
        List<VisionDailyVo.DataBean> leftDiopterSph = new ArrayList<>();
        /** 右眼屈光度(球镜度) */
        List<VisionDailyVo.DataBean> rightDiopterSph = new ArrayList<>();
        /** 左眼屈光度(柱镜度) */
        List<VisionDailyVo.DataBean> leftDiopterCyl = new ArrayList<>();
        /** 右眼屈光度(柱镜度) */
        List<VisionDailyVo.DataBean> rightDiopterCyl = new ArrayList<>();
        /** 左眼屈光度(轴位) */
        List<VisionDailyVo.DataBean> leftDiopterAx = new ArrayList<>();
        /** 右眼屈光度(轴位) */
        List<VisionDailyVo.DataBean> rightDiopterAx = new ArrayList<>();
        /** 瞳距 */
        List<VisionDailyVo.DataBean> pupilDistance = new ArrayList<>();
        /** 身高 */
        List<VisionDailyVo.DataBean> height = new ArrayList<>();
        /** 体重 */
        List<VisionDailyVo.DataBean> weight = new ArrayList<>();
        /** BMI值 */
        List<VisionDailyVo.DataBean> bmi = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 7; i > 0; i--) {
            Date date = DateUtils.addDays(new Date(), -i);
            String formatDate = sdf.format(date);
            EllEyesightParticulars ellEyesightParticulars = reportService.selVisionByUserId(userId, formatDate);
            EllBmiParticulars ellBmiParticulars = reportService.selBmiByUserId(userId, formatDate);

            /** 左眼裸眼视力 */
            VisionDailyVo.DataBean leftVision1 = new VisionDailyVo.DataBean();
            leftVision1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                leftVision1.setNum(ellEyesightParticulars.getLeftVision());
            } else {
                leftVision1.setNum("0");
            }
            leftVision.add(leftVision1);

            /** 右眼裸眼视力 */
            VisionDailyVo.DataBean rightVision1 = new VisionDailyVo.DataBean();
            rightVision1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                rightVision1.setNum(ellEyesightParticulars.getRightVision());
            } else {
                rightVision1.setNum("0");
            }
            rightVision.add(rightVision1);

            /** 左眼屈光度(球镜度) */
            VisionDailyVo.DataBean leftDiopterSph1 = new VisionDailyVo.DataBean();
            leftDiopterSph1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                leftDiopterSph1.setNum(ellEyesightParticulars.getLeftDiopterSph());
            } else {
                leftDiopterSph1.setNum("0");
            }
            leftDiopterSph.add(leftDiopterSph1);

            /** 右眼屈光度(球镜度) */
            VisionDailyVo.DataBean rightDiopterSph1 = new VisionDailyVo.DataBean();
            rightDiopterSph1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                rightDiopterSph1.setNum(ellEyesightParticulars.getRightDiopterSph());
            } else {
                rightDiopterSph1.setNum("0");
            }
            rightDiopterSph.add(rightDiopterSph1);

            /** 左眼屈光度(柱镜度) */
            VisionDailyVo.DataBean leftDiopterCyl1 = new VisionDailyVo.DataBean();
            leftDiopterCyl1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                leftDiopterCyl1.setNum(ellEyesightParticulars.getLeftDiopterCyl());
            } else {
                leftDiopterCyl1.setNum("0");
            }
            leftDiopterCyl.add(leftDiopterCyl1);

            /** 右眼屈光度(柱镜度) */
            VisionDailyVo.DataBean rightDiopterCyl1 = new VisionDailyVo.DataBean();
            rightDiopterCyl1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                rightDiopterCyl1.setNum(ellEyesightParticulars.getRightDiopterCyl());
            } else {
                rightDiopterCyl1.setNum("0");
            }
            rightDiopterCyl.add(rightDiopterCyl1);

            /** 左眼屈光度(轴位) */
            VisionDailyVo.DataBean leftDiopterAx1 = new VisionDailyVo.DataBean();
            leftDiopterAx1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                leftDiopterAx1.setNum(ellEyesightParticulars.getLeftDiopterAx());
            } else {
                leftDiopterAx1.setNum("0");
            }
            leftDiopterAx.add(leftDiopterAx1);

            /** 右眼屈光度(轴位) */
            VisionDailyVo.DataBean rightDiopterAx1 = new VisionDailyVo.DataBean();
            rightDiopterAx1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                rightDiopterAx1.setNum(ellEyesightParticulars.getRightDiopterAx());
            } else {
                rightDiopterAx1.setNum("0");
            }
            rightDiopterAx.add(rightDiopterAx1);

            /** 瞳距 */
            VisionDailyVo.DataBean pupilDistance1 = new VisionDailyVo.DataBean();
            pupilDistance1.setDateTime(formatDate);
            if (ellEyesightParticulars != null) {
                pupilDistance1.setNum(ellEyesightParticulars.getPupilDistance());
            } else {
                pupilDistance1.setNum("0");
            }
            pupilDistance.add(pupilDistance1);

            /** 身高 */
            VisionDailyVo.DataBean height1 = new VisionDailyVo.DataBean();
            height1.setDateTime(formatDate);
            if (ellBmiParticulars != null) {
                height1.setNum(ellBmiParticulars.getHeight());
            } else {
                height1.setNum("0");
            }
            height.add(height1);

            /** 体重 */
            VisionDailyVo.DataBean weight1 = new VisionDailyVo.DataBean();
            weight1.setDateTime(formatDate);
            if (ellBmiParticulars != null) {
                weight1.setNum(ellBmiParticulars.getWeight());
            } else {
                weight1.setNum("0");
            }
            weight.add(weight1);

            /** BMI值 */
            VisionDailyVo.DataBean bmi1 = new VisionDailyVo.DataBean();
            bmi1.setDateTime(formatDate);
            if (ellBmiParticulars != null) {
                bmi1.setNum(ellBmiParticulars.getBmiComment());
            } else {
                bmi1.setNum("0");
            }
            bmi.add(bmi1);
        }
        visionDailyVo.setLeftVision(leftVision);
        visionDailyVo.setRightVision(rightVision);
        visionDailyVo.setLeftDiopterSph(leftDiopterSph);
        visionDailyVo.setRightDiopterSph(rightDiopterSph);
        visionDailyVo.setLeftDiopterCyl(leftDiopterCyl);
        visionDailyVo.setRightDiopterCyl(rightDiopterCyl);
        visionDailyVo.setLeftDiopterAx(leftDiopterAx);
        visionDailyVo.setRightDiopterAx(rightDiopterAx);
        visionDailyVo.setPupilDistance(pupilDistance);
        visionDailyVo.setHeight(height);
        visionDailyVo.setWeight(weight);
        visionDailyVo.setBmi(bmi);
        return new Result(HttpStatus.SUCCESS, "查询成功", visionDailyVo);
    }

    /**
     * 查询日常监测学生信息
     *
     * @return Result
     */
    @ApiOperation(value = "查询日常监测学生信息")
    @PostMapping(value = "/selUserTestResult")
    public Result selUserTestResult(Integer deptId, Integer optId) {
        List<UserDailyResultVo> userDailyResultVos = reportService.selUserTestResult(deptId, optId);
        return new Result(HttpStatus.SUCCESS, "查询成功", userDailyResultVos);
    }

    /**
     * 导出个人视力报表
     *
     * @return Result
     */
    @ApiOperation(value = "导出个人视力报表")
    @PostMapping(value = "/personalVisionReport")
    public Result personalVisionReport(String userId) {
        PersonalVisionReportVo personalVisionReportVo = reportService.selUserInfoByUserId(userId);
        // 查询最近一次的视力测试情况
        EllEyesightParticulars ellEyesightParticulars = reportService.selLastVisionTestById(userId);
        // 查询最近一次的身高/体重测试情况
        EllBmiParticulars ellBmiParticulars = reportService.selLastBmiTestById(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("name", personalVisionReportVo.getUserName());
        user.put("schoolName", personalVisionReportVo.getSchoolName());
        if (personalVisionReportVo.getUserSex() == 1) {
            user.put("sex", "男");
        } else {
            user.put("sex", "女");
        }
        if (personalVisionReportVo.getFileUrl() != null) {
            String base64;
            if (FileUploadUtil.isLinux() == false) {
                base64 = FileUtils.getBaseImg(WINDOWS_FACE_PHOTO + personalVisionReportVo.getFileUrl());
            } else {
                base64 = FileUtils.getBaseImg(LINUX_FACE_PHOTO + personalVisionReportVo.getFileUrl());
            }
            user.put("facePhoto", base64);
        }
        user.put("className", personalVisionReportVo.getClassName());
        List<Vision> visionList = new ArrayList<>();
        Vision vision = new Vision();
        vision.setName("裸眼视力");
        if (ellEyesightParticulars != null) {
            vision.setLeft(ellEyesightParticulars.getLeftVision());
            vision.setRight(ellEyesightParticulars.getRightVision());
            vision.setComment(ellEyesightParticulars.getVisionComment());
        } else {
            vision.setLeft("0");
            vision.setRight("0");
            vision.setComment("-");
        }
        visionList.add(vision);
        Vision diopterAx = new Vision();
        diopterAx.setName("屈光轴位");
        if (ellEyesightParticulars != null) {
            diopterAx.setLeft(ellEyesightParticulars.getLeftDiopterAx());
            diopterAx.setRight(ellEyesightParticulars.getRightDiopterAx());
            diopterAx.setComment(ellEyesightParticulars.getVisionComment());
        } else {
            diopterAx.setLeft("0");
            diopterAx.setRight("0");
            diopterAx.setComment("-");
        }
        visionList.add(diopterAx);
        Vision diopterSph = new Vision();
        diopterSph.setName("屈光球镜");
        if (ellEyesightParticulars != null) {
            diopterSph.setLeft(ellEyesightParticulars.getLeftDiopterSph());
            diopterSph.setRight(ellEyesightParticulars.getRightDiopterSph());
            diopterSph.setComment(ellEyesightParticulars.getVisionComment());
        } else {
            diopterSph.setLeft("0");
            diopterSph.setRight("0");
            diopterSph.setComment("-");
        }
        visionList.add(diopterSph);
        Vision diopterCyl = new Vision();
        diopterCyl.setName("屈光柱镜");
        if (ellEyesightParticulars != null) {
            diopterCyl.setLeft(ellEyesightParticulars.getLeftDiopterCyl());
            diopterCyl.setRight(ellEyesightParticulars.getRightDiopterCyl());
            diopterCyl.setComment(ellEyesightParticulars.getVisionComment());
        } else {
            diopterCyl.setLeft("0");
            diopterCyl.setRight("0");
            diopterCyl.setComment("-");
        }
        visionList.add(diopterCyl);
        user.put("visionList", visionList);
        if (ellEyesightParticulars != null) {
            user.put("visionRes", ellEyesightParticulars.getVisionComment());
        } else {
            user.put("visionRes", " -");
        }
        String remark;
        if (ellEyesightParticulars != null) {
            remark =
                    "您"
                            + DateUtils.datePath()
                            + "的视力测试结果是"
                            + ellEyesightParticulars.getVisionComment()
                            + "。\n";
        } else {
            remark = "您" + DateUtils.datePath() + "的视力测试结果是 -" + "。\n";
        }
        user.put("visionRemark", remark);
        List<Hw> hwList = new ArrayList<>();
        Hw hw = new Hw();
        if (ellBmiParticulars != null) {
            hw.setHeight(ellBmiParticulars.getHeight());
            hw.setWeight(ellBmiParticulars.getWeight());
            hw.setBmi(ellBmiParticulars.getBmi());
            hw.setComment(ellBmiParticulars.getBmiComment());
        } else {
            hw.setHeight("0");
            hw.setWeight("0");
            hw.setBmi("0");
            hw.setComment("-");
        }
        hwList.add(hw);
        user.put("hwList", hwList);
        if (ellBmiParticulars != null) {
            user.put("hwRes", ellBmiParticulars.getBmiComment());
        } else {
            user.put("hwRes", " -");
        }
        String remark1;
        if (ellBmiParticulars != null) {
            remark1 =
                    "您"
                            + DateUtils.datePath()
                            + "的BMI为"
                            + ellBmiParticulars.getBmi()
                            + "是"
                            + ellBmiParticulars.getBmiComment()
                            + "。\n";
        } else {
            remark1 = "您" + DateUtils.datePath() + "的BMI为 - " + "是 -" + "。\n";
        }
        user.put("hwRemark", remark1);
        user.put("time", DateUtils.nowDate());
        try {
            // 输出文档路径及名称.
            String filePath = "";
            File outFile = null;
            if (FileUploadUtil.isLinux() == false) {
                filePath = WindowsSite.wordStudent + personalVisionReportVo.getUserName() + "视力报表.doc";
                outFile = new File(filePath);

            } else {
                filePath = LinuxSite.wordStudent + personalVisionReportVo.getUserName() + "视力报表.doc";
                outFile = new File(filePath);
            }
            //生成路径
            PathGeneration.createPath(filePath);
            //以utf-8的编码读取ftl文件
            Configuration configuration = new Configuration();

            configuration.setDefaultEncoding("utf-8");
            configuration.setClassicCompatible(true);
            if (FileUploadUtil.isLinux() == false) {
                configuration.setDirectoryForTemplateLoading(
                        new File(WindowsSite.wordStudent));
            } else {
                configuration.setDirectoryForTemplateLoading(
                        new File(LinuxSite.wordStudent));
            }
            configuration.setClassForTemplateLoading(wordTest.class, "/templates");
            Template t = configuration.getTemplate("personal_report.ftl", "utf-8");

            Writer out =
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);

            t.process(user, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(HttpStatus.ERROR, "文件导出报错", e);
        }
        return new Result(HttpStatus.SUCCESS, "导出成功", "word/student/" + personalVisionReportVo.getUserName() + "视力报表.doc");

    }

    /**
     * 导出班级视力报表
     *
     * @return Result
     */
    @ApiOperation(value = "导出班级视力报表")
    @PostMapping(value = "/classVisionReport")
    public Result classVisionReport(Integer deptId, Integer optId) {

        NearSevenDaysTestDto visionDto = new NearSevenDaysTestDto();
        visionDto.setDeptId(deptId);
        visionDto.setOptId(optId);
        visionDto.setProId(TestProjectConstant.VISION);

        NearSevenDaysTestDto bmiDto = new NearSevenDaysTestDto();
        bmiDto.setDeptId(deptId);
        bmiDto.setOptId(optId);
        bmiDto.setProId(TestProjectConstant.HEIGHT_AND_WEIGHT);

        //查询班级信息
        PersonalVisionReportVo personalVisionReportVo = reportService.selClassInfoByDeptId(deptId);
        //视力占比
        VisionTestScoreVo visionTestScoreVo = reportService.selVisionRatio(visionDto);
        //视力测试总人数
        Integer visionNum = Integer.valueOf(visionTestScoreVo.getNormal()) + Integer.valueOf(visionTestScoreVo.getHyperopia()) + Integer.valueOf(visionTestScoreVo.getMyopia());
        //身高体重占比
        HeightAndWeightVo heightAndWeightVo = reportService.selBMIRatio(bmiDto);
        //身高体重测试总人数
        Integer hwNum = Integer.valueOf(heightAndWeightVo.getFat()) + Integer.valueOf(heightAndWeightVo.getNormal()) + Integer.valueOf(heightAndWeightVo.getOverweight()) + Integer.valueOf(heightAndWeightVo.getThin());
        //班级学生信息
        List<UserDailyResultVo> userDailyResultVos = reportService.selUserTestResult(deptId, optId);
        Map<String, Object> user = new HashMap<>();
        StringBuffer stuNameList = new StringBuffer();
        user.put("className", personalVisionReportVo.getClassName());
        user.put("schoolName", personalVisionReportVo.getSchoolName());
        user.put("num", userDailyResultVos.size());
        user.put("nowDate", DateUtils.datePath());
        user.put("normalNum", visionTestScoreVo.getNormal());
        user.put("hyperopiaNum", visionTestScoreVo.getHyperopia());
        user.put("myopiaNum", visionTestScoreVo.getMyopia());
        if (Integer.parseInt(visionTestScoreVo.getHyperopia()) == 0) {
            user.put("hyperopiaRatio", "0.0");
        } else {
            user.put("hyperopiaRatio", (float) visionNum / (float) Integer.parseInt(visionTestScoreVo.getHyperopia()));
        }
        if (Integer.parseInt(visionTestScoreVo.getNormal()) == 0) {
            user.put("normalRatio", "0.0");
        } else {
            user.put("normalRatio", (float) visionNum / (float) Integer.parseInt(visionTestScoreVo.getNormal()));
        }
        if (Integer.parseInt(visionTestScoreVo.getMyopia()) == 0) {
            user.put("myopiaRatio", "0.0");
        } else {
            user.put("myopiaRatio", (float) visionNum / (float) Integer.parseInt(visionTestScoreVo.getMyopia()));
        }
        user.put("fatNum", heightAndWeightVo.getFat());
        user.put("overweightNum", heightAndWeightVo.getOverweight());
        user.put("norNum", heightAndWeightVo.getNormal());
        user.put("thinNum", heightAndWeightVo.getThin());
        if (Integer.parseInt(heightAndWeightVo.getFat()) == 0) {
            user.put("fatRatio", "0.0");
        } else {
            user.put("fatRatio", (float) hwNum / (float) Integer.parseInt(heightAndWeightVo.getFat()));
        }
        if (Integer.parseInt(heightAndWeightVo.getOverweight()) == 0) {
            user.put("overweightRatio", "0.0");
        } else {
            user.put("overweightRatio", (float) hwNum / (float) Integer.parseInt(heightAndWeightVo.getOverweight()));
        }
        if (Integer.parseInt(heightAndWeightVo.getNormal()) == 0) {
            user.put("norRatio", "0.0");
        } else {
            user.put("norRatio", (float) hwNum / (float) Integer.parseInt(heightAndWeightVo.getNormal()));
        }
        if (Integer.parseInt(heightAndWeightVo.getThin()) == 0) {
            user.put("thinRatio", "0.0");
        } else {
            user.put("thinRatio", (float) hwNum / (float) Integer.parseInt(heightAndWeightVo.getThin()));
        }
        user.put("stuList", userDailyResultVos);
        for (int i = 0; i < userDailyResultVos.size(); i++) {
            if (userDailyResultVos.get(i).getVisionComment().equals("近视") || userDailyResultVos.get(i).getVisionComment().equals("远视")) {
                stuNameList.append(userDailyResultVos.get(i).getUserName());
            }
        }
        user.put("stuName", stuNameList);
        user.put("time", DateUtils.nowDate());
        try {

            // 输出文档路径及名称
            String filePath = "";
            File outFile = null;
            if (FileUploadUtil.isLinux() == false) {
                filePath = WindowsSite.wordClass + personalVisionReportVo.getSchoolName() + personalVisionReportVo.getClassName() + "视力报表.doc";
                outFile = new File(filePath);
            } else {
                filePath = LinuxSite.wordClass + personalVisionReportVo.getSchoolName() + personalVisionReportVo.getClassName() + "视力报表.doc";
                outFile = new File(filePath);
            }
            //生成路径
            PathGeneration.createPath(filePath);
            Configuration configuration = new Configuration();

            configuration.setDefaultEncoding("utf-8");
            configuration.setClassicCompatible(true);
            if (FileUploadUtil.isLinux() == false) {
                configuration.setDirectoryForTemplateLoading(
                        new File(WindowsSite.wordClass));
            } else {
                configuration.setDirectoryForTemplateLoading(
                        new File(LinuxSite.wordClass));
            }
            configuration.setClassForTemplateLoading(wordTest.class, "/templates");

            //以utf-8的编码读取ftl文件

            Template t = configuration.getTemplate("class_report.ftl", "utf-8");

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);

            t.process(user, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(HttpStatus.ERROR, "文件导出报错", e);
        }
        return new Result(HttpStatus.SUCCESS, "导出成功", "word/class/" + personalVisionReportVo.getSchoolName() + personalVisionReportVo.getClassName() + "视力报表.doc");
    }

    /**
     * 导出学校视力报表
     *
     * @return Result
     */
    @ApiOperation(value = "导出学校视力报表")
    @PostMapping(value = "/schoolVisionReport")
    public Result schoolVisionReport(Integer deptId, Integer optId) {

        OverYearsTestScoreDto visionDto = new OverYearsTestScoreDto();
        visionDto.setDeptId(deptId);
        visionDto.setOptId(optId);
        visionDto.setProId(TestProjectConstant.VISION);
        visionDto.setYear(Integer.valueOf(DateUtils.getYear()));

        OverYearsTestScoreDto bmiDto = new OverYearsTestScoreDto();
        bmiDto.setDeptId(deptId);
        bmiDto.setOptId(optId);
        bmiDto.setProId(TestProjectConstant.HEIGHT_AND_WEIGHT);
        bmiDto.setYear(Integer.valueOf(DateUtils.getYear()));

        //查询学校信息
        PersonalVisionReportVo personalVisionReportVo = reportService.selSchoolInfoByDeptId(deptId);
        //查询学校人数
        SchoolNumberVo schoolNumberVo = reportService.selUserCount(deptId, optId);
        //当前年份视力测试结果
        VisionTestScoreVo visionTestScoreVo = reportService.selOverYearsVisionScore(visionDto);
        //视力测试总人数
        Integer visionNum = Integer.valueOf(visionTestScoreVo.getNormal()) + Integer.valueOf(visionTestScoreVo.getHyperopia()) + Integer.valueOf(visionTestScoreVo.getMyopia());
        //当前年份身高体重测试结果
        HeightAndWeightVo heightAndWeightVo = reportService.selOverYearsTestScore(bmiDto);
        //身高体重测试总人数
        Integer hwNum = Integer.valueOf(heightAndWeightVo.getFat()) + Integer.valueOf(heightAndWeightVo.getNormal()) + Integer.valueOf(heightAndWeightVo.getOverweight()) + Integer.valueOf(heightAndWeightVo.getThin());
        //调用查询历年视力测试结果接口
        Result result = selOverYearsVisionScore(visionDto);
        if (result.code != HttpStatus.SUCCESS) {
            return new Result(HttpStatus.ERROR, "查询历年视力测试结果错误", null);
        }
        List<OverYearsVisionTestVo> visionTestVos = JSON.parseArray(JSON.toJSONString(result.getData()), OverYearsVisionTestVo.class);
        //调用查询历年身高/体重测试结果接口
        Result result1 = selOverYearsTestScore(bmiDto);
        if (result1.code != HttpStatus.SUCCESS) {
            return new Result(HttpStatus.ERROR, "查询历年身高/体重测试结果错误", null);
        }
        List<OverYearsTestScoreVo> bmiTestVos = JSON.parseArray(JSON.toJSONString(result1.getData()), OverYearsTestScoreVo.class);
        Map<String, Object> user = new HashMap<>();
        user.put("schoolName", personalVisionReportVo.getSchoolName());
        user.put("total", schoolNumberVo.getTotal());
        user.put("manNumber", schoolNumberVo.getMan());
        user.put("womanNumber", schoolNumberVo.getWoman());
        user.put("year", DateUtils.getYear());
        user.put("normalNum", visionTestScoreVo.getNormal());
        user.put("hyperopiaNum", visionTestScoreVo.getHyperopia());
        user.put("myopiaNum", visionTestScoreVo.getMyopia());
        if (Integer.parseInt(visionTestScoreVo.getHyperopia()) == 0) {
            user.put("hyperopiaRatio", "0.0");
        } else {
            user.put("hyperopiaRatio", (float) visionNum / (float) Integer.parseInt(visionTestScoreVo.getHyperopia()));
        }
        if (Integer.parseInt(visionTestScoreVo.getNormal()) == 0) {
            user.put("normalRatio", "0.0");
        } else {
            user.put("normalRatio", (float) visionNum / (float) Integer.parseInt(visionTestScoreVo.getNormal()));
        }
        if (Integer.parseInt(visionTestScoreVo.getMyopia()) == 0) {
            user.put("myopiaRatio", "0.0");
        } else {
            user.put("myopiaRatio", (float) visionNum / (float) Integer.parseInt(visionTestScoreVo.getMyopia()));
        }
        user.put("fatNum", heightAndWeightVo.getFat());
        user.put("overweightNum", heightAndWeightVo.getOverweight());
        user.put("norNum", heightAndWeightVo.getNormal());
        user.put("thinNum", heightAndWeightVo.getThin());
        if (Integer.parseInt(heightAndWeightVo.getFat()) == 0) {
            user.put("fatRatio", "0.0");
        } else {
            user.put("fatRatio", (float) hwNum / (float) Integer.parseInt(heightAndWeightVo.getFat()));
        }
        if (Integer.parseInt(heightAndWeightVo.getOverweight()) == 0) {
            user.put("overweightRatio", "0.0");
        } else {
            user.put("overweightRatio", (float) hwNum / (float) Integer.parseInt(heightAndWeightVo.getOverweight()));
        }
        if (Integer.parseInt(heightAndWeightVo.getNormal()) == 0) {
            user.put("norRatio", "0.0");
        } else {
            user.put("norRatio", (float) hwNum / (float) Integer.parseInt(heightAndWeightVo.getNormal()));
        }
        if (Integer.parseInt(heightAndWeightVo.getThin()) == 0) {
            user.put("thinRatio", "0.0");
        } else {
            user.put("thinRatio", (float) hwNum / (float) Integer.parseInt(heightAndWeightVo.getThin()));
        }
        for (int i = 0; i < visionTestVos.size(); i++) {
            user.put("visionYear" + (i + 1), String.valueOf(visionTestVos.get(i).getDateYear()));
            user.put("hyperopiaNum" + (i + 1), visionTestVos.get(i).getDataBean().getHyperopia());
            user.put("normalNum" + (i + 1), visionTestVos.get(i).getDataBean().getNormal());
            user.put("myopiaNum" + (i + 1), visionTestVos.get(i).getDataBean().getMyopia());
        }
        for (int i = 0; i < bmiTestVos.size(); i++) {
            user.put("bmiYear" + (i + 1), String.valueOf(bmiTestVos.get(i).getDateYear()));
            user.put("fatNum" + (i + 1), bmiTestVos.get(i).getDataBean().getFat());
            user.put("overweightNum" + (i + 1), bmiTestVos.get(i).getDataBean().getOverweight());
            user.put("norNum" + (i + 1), bmiTestVos.get(i).getDataBean().getNormal());
            user.put("thinNum" + (i + 1), bmiTestVos.get(i).getDataBean().getThin());
        }
        user.put("time", DateUtils.nowDate());
        try {

            // 输出文档路径及名称
            String filePath = "";
            File outFile = null;
            if (FileUploadUtil.isLinux() == false) {
                filePath = WindowsSite.wordSchool + personalVisionReportVo.getSchoolName() + "视力报表.doc";
                outFile = new File(filePath);
            } else {
                filePath = LinuxSite.wordSchool + personalVisionReportVo.getSchoolName() + "视力报表.doc";
                outFile = new File(filePath);
            }
            //生成路径
            PathGeneration.createPath(filePath);

            Configuration configuration = new Configuration();

            configuration.setDefaultEncoding("utf-8");
            configuration.setClassicCompatible(true);
            if (FileUploadUtil.isLinux() == false) {
                configuration.setDirectoryForTemplateLoading(
                        new File(WindowsSite.wordSchool ));
            } else {
                configuration.setDirectoryForTemplateLoading(
                        new File(LinuxSite.wordSchool ));
            }
            configuration.setClassForTemplateLoading(wordTest.class, "/templates");
            //以utf-8的编码读取ftl文件

            Template t = configuration.getTemplate("school_report.ftl", "utf-8");

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);

            t.process(user, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(HttpStatus.ERROR, "文件导出报错", e);
        }
        return new Result(HttpStatus.SUCCESS, "导出成功", "word/school/" + personalVisionReportVo.getSchoolName() + "视力报表.doc");
    }
}
