package com.eluolang.physical.controller;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.EllPlan;
import com.eluolang.common.core.pojo.EllPlanProjectChance;
import com.eluolang.common.core.pojo.EllUserPlanDepartment;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.dto.*;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.EllUserService;
import com.eluolang.physical.service.PhysicalDataService;
import com.eluolang.physical.service.PlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Api(tags = "BI接口管理")
/**
 * @命名规则:前端liaoguojian
 */
@RestController
@Transactional
public class    PhysicalDataController {

    @Autowired
    PhysicalDataService physicalDataService;
    @Autowired
    EllUserService ellUserService;
    @Autowired
    PlanService planService;

    /**
     * 根据部门id集合查询学校名称和人数
     *
     * @return Result
     */
    @ApiOperation(value = "根据部门id集合查询学校名称和人数")
    @PostMapping(value = "/selSchoolAndNumber")
    public Result selSchoolAndNumber(@RequestBody SchoolNumDto schoolNumDto) {
        List<PfDepart> pfDepartList = physicalDataService.selDepartByIdList(schoolNumDto.getDeptIdList());
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < pfDepartList.size(); i++) {
            //去掉path的第一个“/”，避免分割出现“”字符串
            String jie = pfDepartList.get(i).getPath().substring(1, pfDepartList.get(i).getPath().length() - 1);
            //根据“/”分割字符
            String[] items = jie.split("/");
            //转为list
            List<Integer> deptIds = Stream.of(items).map(Integer::parseInt).collect(Collectors.toList());
            ids.addAll(deptIds);
        }
        //id去重
        List<Integer> idList = ids.stream().distinct().collect(Collectors.toList());
        List<PfDepartVo> pfDeparts = physicalDataService.selIsSchoolByIdList(idList);
        if (pfDeparts.size() != 0) {
            for (int i = 0; i < pfDeparts.size(); i++) {
                List<Integer> id = new ArrayList<>();
                for (int j = 0; j < pfDepartList.size(); j++) {
                    boolean flag = pfDepartList.get(j).getPath().contains(pfDeparts.get(i).getPath());
                    if (flag == true) {
                        id.add(pfDepartList.get(j).getId());
                    }
                }
                Integer total = physicalDataService.selSchoolUserCount(pfDeparts.get(i).getPath());
                Integer number = physicalDataService.selUserCountByDeptIdList(id);
                Integer test = physicalDataService.selUserTestSuccessCount(schoolNumDto.getPlanId(), id);
                pfDeparts.get(i).setTotal(total);
                pfDeparts.get(i).setNumber(number);
                pfDeparts.get(i).setTestSuccessNum(test);
            }
            return new Result(HttpStatus.SUCCESS, "查询成功", pfDeparts);
        }
        return new Result(HttpStatus.ERROR, "该计划下暂无学校", pfDeparts);
    }

    /**
     * 根据计划id查询考点各项目分值情况
     *
     * @return Result
     */
    @ApiOperation(value = "根据计划id查询考点各项目分值情况")
    @PostMapping(value = "/selEachProjectScore")
    public Result selEachProjectScore(@RequestParam("planId") String planId, @RequestParam("deptId") List<Integer> deptId) {
        List<EachProjectScoreVo> eachProjectScoreVoList = physicalDataService.selPlanProjectChanceByPlanId(planId);
        if (eachProjectScoreVoList.size() != 0) {
            for (int i = 0; i < eachProjectScoreVoList.size(); i++) {
                EachProjectScoreVo eachProjectScoreVo = physicalDataService.selEachProjectScore(planId, eachProjectScoreVoList.get(i).getProId(), deptId);
                eachProjectScoreVoList.get(i).setOneScore(eachProjectScoreVo.getOneScore());
                eachProjectScoreVoList.get(i).setTwoScore(eachProjectScoreVo.getTwoScore());
                eachProjectScoreVoList.get(i).setThreeScore(eachProjectScoreVo.getThreeScore());
            }
            return new Result(HttpStatus.SUCCESS, "查询成功", eachProjectScoreVoList);
        }
        return new Result(HttpStatus.ERROR, "该计划下未查询到项目", null);
    }

    /**
     * 查询学校得分情况
     *
     * @return Result
     */
    @ApiOperation(value = "查询学校得分情况")
    @PostMapping(value = "/selSchoolScore")
    public Result selSchoolScore(@RequestParam("deptIdList") List<Integer> deptIdList, @RequestParam("planId") String planId) {
        List<PfDepart> pfDepartList = physicalDataService.selDepartByIdList(deptIdList);
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < pfDepartList.size(); i++) {
            //去掉path的第一个“/”，避免分割出现“”字符串
            String jie = pfDepartList.get(i).getPath().substring(1, pfDepartList.get(i).getPath().length() - 1);
            //根据“/”分割字符
            String[] items = jie.split("/");
            //转为list
            List<Integer> deptIds = Stream.of(items).map(Integer::parseInt).collect(Collectors.toList());
            ids.addAll(deptIds);
        }
        //id去重
        List<Integer> idList = ids.stream().distinct().collect(Collectors.toList());
        //查询学校级部门
        List<PfDepartVo> pfDeparts = physicalDataService.selIsSchoolByIdList(idList);
        List<SchoolScoreVo> schoolScoreVos = new ArrayList<>();
        if (pfDeparts.size() != 0) {
            for (int i = 0; i < pfDeparts.size(); i++) {
                List<Integer> id = new ArrayList<>();
                for (int j = 0; j < pfDepartList.size(); j++) {
                    //将该帐号有权限的部门path与学校的path匹配，相同则写入
                    boolean flag = pfDepartList.get(j).getPath().contains(pfDeparts.get(i).getPath());
                    if (flag == true) {
                        id.add(pfDepartList.get(j).getId());
                    }
                }
                SchoolScoreVo schoolScoreVo = physicalDataService.selSchoolScore(id, planId);
                schoolScoreVo.setDeptId(pfDeparts.get(i).getId());
                schoolScoreVo.setDeptName(pfDeparts.get(i).getDeptName());
                schoolScoreVos.add(schoolScoreVo);
            }
            return new Result(HttpStatus.SUCCESS, "查询成功", schoolScoreVos);
        }
        return new Result(HttpStatus.ERROR, "该计划下暂无学校", schoolScoreVos);
    }

    /**
     * 根据计划id查询各个分值人数
     *
     * @return Result
     */
    @ApiOperation(value = "根据计划id查询各个分值人数")
    @PostMapping(value = "/selPlanEachScore")
    public Result selPlanEachScore(@RequestParam("deptIdList") List<Integer> deptIdList, @RequestParam("planId") String planId) {
        PlanEachScoreVo planEachScoreVo = physicalDataService.selPlanEachScore(deptIdList, planId);
        return new Result(HttpStatus.SUCCESS, "查询成功", planEachScoreVo);
    }

    /**
     * 根据计划id查询确认成绩人数
     *
     * @return Result
     */
    @ApiOperation(value = "根据计划id查询确认成绩人数")
    @PostMapping(value = "/selConfirmAchievement")
    public Result selConfirmAchievement(@RequestParam("deptIdList") List<Integer> deptIdList, @RequestParam("planId") String planId) {
        ConfirmAchievementVo confirmAchievementVo = physicalDataService.selConfirmAchievement(deptIdList, planId);
        return new Result(HttpStatus.SUCCESS, "查询成功", confirmAchievementVo);
    }

    /**
     * 根据计划id查询各项目重测人数
     *
     * @return Result
     */
    @ApiOperation(value = "根据计划id查询各项目重测人数")
    @PostMapping(value = "/selEachRetestNumber")
    public Result selEachRetestNumber(@RequestParam("deptIdList") List<Integer> deptIdList, @RequestParam("planId") String planId) {
        List<EachProjectScoreVo> eachProjectScoreVoList = physicalDataService.selPlanProjectChanceByPlanId(planId);
        if (eachProjectScoreVoList.size() != 0) {
            List<RetestNumberVo> retestNumberVoList = new ArrayList<>();
            for (int i = 0; i < eachProjectScoreVoList.size(); i++) {
                RetestNumberVo retestNumberVo = new RetestNumberVo();
                Integer num = physicalDataService.selEachRetestNumber(deptIdList, planId, eachProjectScoreVoList.get(i).getProId());
                retestNumberVo.setProId(eachProjectScoreVoList.get(i).getProId());
                retestNumberVo.setProName(eachProjectScoreVoList.get(i).getProName());
                retestNumberVo.setRetestNumber(String.valueOf(num));
                retestNumberVoList.add(retestNumberVo);
            }
            return new Result(HttpStatus.SUCCESS, "查询成功", retestNumberVoList);
        }
        return new Result(HttpStatus.ERROR, "该计划下未查询到项目", null);
    }

    /**
     * 查询该账户有权限部门的总人数/考试总人数/已完成考试/重测人数/男女人数
     */
    @ApiOperation("查询该账户有权限部门的总人数/考试总人数/已完成考试/重测人数/男女人数")
    @GetMapping("/selNumberOfPeople")
    public Result selNumberOfPeople(int accountId, String planId) {
        //查询学校path
        List<String> pathList = physicalDataService.selSchoolPath(planId);
        //总人数
        int numberOfPeople = 0;
        for (int i = 0; i < pathList.size(); i++) {
            numberOfPeople = numberOfPeople + physicalDataService.findNumberOfPeople(accountId, pathList.get(i));
        }
        //考试人数
        int examNumberOfPeople = physicalDataService.findExamNumberOfPeople(accountId, planId);
        //考试完成人数
        int examFinishNumberOfPeople = physicalDataService.findExamFinishNumberOfPeople(accountId, planId);
        //考试重测人数
        List<String> retestNumberOfPeople = physicalDataService.findRetestNumberOfPeople(accountId, planId);
        //考试男生人数
        int examBoyNumberOfPeople = physicalDataService.findExamSexNumberOfPeople(accountId, planId, 1);
        //考试女生人数
        int examGirlNumberOfPeople = physicalDataService.findExamSexNumberOfPeople(accountId, planId, 2);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("numberOfPeople", numberOfPeople);
        jsonObject.put("examNumberOfPeople", examNumberOfPeople);
        jsonObject.put("examFinishNumberOfPeople", examFinishNumberOfPeople);
        jsonObject.put("retestNumberOfPeople", retestNumberOfPeople.size());
        jsonObject.put("examBoyNumberOfPeople", examBoyNumberOfPeople);
        jsonObject.put("examGirlNumberOfPeople", examGirlNumberOfPeople);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }
    //体测

    /**
     * 体测Bi合格率/完成测试人数/免考人数/重考人数/班级总数/测试人数
     */
    @ApiOperation("体测Bi合格率/完成测试人数/免考人数/重考人数/班级总数/测试人数")
    @GetMapping("/selTestNumberOfPeople")
    public Result selTestNumberOfPeople(int accountId, String planId) {
        //计划名
        String planTile = physicalDataService.findPlanTitle(planId);
        //体测人数
        int testNumberOfPeople = physicalDataService.findExamNumberOfPeople(accountId, planId);
        //班级数量
        int classNumberOf = physicalDataService.findClassNumberOf(accountId, planId);
        //重考人数
        List<String> retestNumberOf = physicalDataService.findRetestNumberOf(accountId, planId);
        //完成测试人数
        int finishNumberOfPeople = physicalDataService.findExamFinishNumberOfPeople(accountId, planId);
        //完成的不合格人数
        int disQualifiedNumberOf = physicalDataService.findDisQualifiedNumberOf(accountId, planId);
        //免考人数
        int freeTest = physicalDataService.findFreeTest(accountId, planId);
        Double yield = disQualifiedNumberOf / Double.parseDouble(finishNumberOfPeople + "") * 100;
        if (yield.isNaN()) {
            yield = 0.00;
        } else {
            //格式化数字为两位
            DecimalFormat sdf = new DecimalFormat("###.00");
            yield = Double.valueOf(sdf.format(yield));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("planTtle", planTile);
        jsonObject.put("testNumberOfPeople", testNumberOfPeople);
        jsonObject.put("classNumberOf", classNumberOf);
        jsonObject.put("retestNumberOf", retestNumberOf.size());
        jsonObject.put("finishNumberOfPeople", finishNumberOfPeople);
        jsonObject.put("yield", yield + "%");
        jsonObject.put("freeTest", freeTest);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    /**
     * 查询测试项目
     */
    @ApiOperation("查询测试项目")
    @GetMapping("/selTestProject")
    public Result selTestProject(String planId) {
        List<EllTestProjectVo> descData = physicalDataService.findTestProject(planId);

        return new Result(HttpStatus.SUCCESS, "查询成功", descData);
    }

    /**
     * 根据管理员id和计划id查询学校信息
     *
     * @return Result
     */
    @ApiOperation(value = "根据管理员id和计划id查询学校信息")
    @PostMapping(value = "/selSchoolInformation")
    public Result selSchoolInformation(Integer optId, String planId) {
        List<PfDepart> pfDepartList = physicalDataService.selSchoolInformation(optId, planId);
        return new Result(HttpStatus.SUCCESS, "查询成功", pfDepartList);
    }

    /**
     * 单个项目测试数据
     */
    @ApiOperation("单个项目测试数据")
    @GetMapping("/selProTestProjectData")
    public Result selProTestProjectData(String planId, int accountId, Integer proId, int orgId) {
        //已测试人数
        int one = 0;
        //重测人数
        int two = 0;
        //男已测试人数
        int three = 0;
        //女已测试人数
        int four = 0;
        //需要测试的人数
        int sum = 0;
        //需要测试的人数男
        int sumBoy = 0;
        //需要测试的人数女
        int sumGirl = 0;
        //优秀默认为100分不为100分的就以最后一个为满分
        double score = 100.0;
        //满分人数
        List<EllFullScoreDto> fullMark = new ArrayList<>();
        //该项目得分情况占比分女
        int excellentBoy, goodBoy, passBoy, failBoy, excellentGirl, goodGirl, passGirl, failGirl = 0;
        //该项目得分情况
        int excellent, good, pass, fail = 0;
        //良好//满分的90%为优秀//满分的60%为及格
        double goodScore, excellentScore, passScore = 0;
        //所有项目和单个项目的情况
        if (proId <= 0 || proId == null) {
            EllFullScoreDto ellFullScoreDto = new EllFullScoreDto();
            //查询计划
            List<EllPlan> ellPlanList = planService.findPlan(planId, null, null, null);
            //查询计划必选项目注意：每个性别测试项目相同数量，分值相同
            List<EllPlanProjectChance> ellPlanProjectChanceListMust = physicalDataService.selPlanProId(planId, 1, "1", 0);
            EllScoreDetailsVo details = new EllScoreDetailsVo(accountId, orgId, planId, proId);
            //该计划的满分
            score = physicalDataService.FullMark(details, ellPlanList, ellPlanProjectChanceListMust);
            //总分满分人数
            int fullEndScore = physicalDataService.selFullEndScorePeople(0, planId, orgId, accountId, score);
            ellFullScoreDto.setNum(fullEndScore);
            fullMark.add(ellFullScoreDto);
            //测试完成人数
            EllTotalScoreDto ellTotalScoreDto = physicalDataService.selSchoolTotalScore(details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), 0);
            one = ellTotalScoreDto.getReferenceNum();
            //男测试完成人数
//            EllTotalScoreDto ellTotalScoreDtoBoy = physicalDataService.selSchoolTotalScore(details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), 1);
//            three = ellTotalScoreDtoBoy.getReferenceNum();
            //女测试完成人数
//            EllTotalScoreDto ellTotalScoreDtoGirl = physicalDataService.selSchoolTotalScore(details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), 2);
//            four = ellTotalScoreDtoGirl.getReferenceNum();
            //需要测试人数
            sum = physicalDataService.selPlanOrgIdPeopleNum(0, planId, orgId, accountId);
            //需要测试的人数男
            sumBoy = physicalDataService.selPlanOrgIdPeopleNum(1, planId, orgId, accountId);
            //需要测试的人数女
            sumGirl = physicalDataService.selPlanOrgIdPeopleNum(2, planId, orgId, accountId);
            //满分的80%为良好
            goodScore = score * 0.8;
            //满分的90%为优秀
            excellentScore = score * 0.9;
            //满分的60%为及格
            passScore = score * 0.6;
            //不能包括最大，满分和零分除外
            //优秀
            excellentBoy = physicalDataService.selEndScoreByScore(details.getPlanId(), score, excellentScore, details.getOrgId(), details.getAccountOrgId(), null, 1);
            //良好
            goodBoy = physicalDataService.selEndScoreByScore(details.getPlanId(), excellentScore, goodScore, details.getOrgId(), details.getAccountOrgId(), excellentScore, 1);
            //及格
            passBoy = physicalDataService.selEndScoreByScore(details.getPlanId(), goodScore, passScore, details.getOrgId(), details.getAccountOrgId(), goodScore, 1);
            //不及格
            failBoy = physicalDataService.selEndScoreByScore(details.getPlanId(), passScore, 0, details.getOrgId(), details.getAccountOrgId(), null, 1);
            //优秀女
            excellentGirl = physicalDataService.selEndScoreByScore(details.getPlanId(), score, excellentScore, details.getOrgId(), details.getAccountOrgId(), null, 2);
            //良好女
            goodGirl = physicalDataService.selEndScoreByScore(details.getPlanId(), excellentScore, goodScore, details.getOrgId(), details.getAccountOrgId(), excellentScore, 2);
            //及格女
            passGirl = physicalDataService.selEndScoreByScore(details.getPlanId(), goodScore, passScore, details.getOrgId(), details.getAccountOrgId(), goodScore, 2);
            //不及格女
            failGirl = physicalDataService.selEndScoreByScore(details.getPlanId(), passScore, 0, details.getOrgId(), details.getAccountOrgId(), null, 2);
            //优秀
            excellent = physicalDataService.selEndScoreByScore(details.getPlanId(), score, excellentScore, details.getOrgId(), details.getAccountOrgId(), null, 0);
            //良好
            good = physicalDataService.selEndScoreByScore(details.getPlanId(), excellentScore, goodScore, details.getOrgId(), details.getAccountOrgId(), excellentScore, 0);
            //及格
            pass = physicalDataService.selEndScoreByScore(details.getPlanId(), goodScore, passScore, details.getOrgId(), details.getAccountOrgId(), goodScore, 0);
            //不及格
            fail = physicalDataService.selEndScoreByScore(details.getPlanId(), passScore, 0, details.getOrgId(), details.getAccountOrgId(), null, 0);
        } else {
            //单项的项目已测试人数
            one = physicalDataService.findProjectTestNumberOf(accountId, planId, proId, orgId);
            //单项的项目重测人数
            two = physicalDataService.findProjectRestTestNumberOf(accountId, planId, proId, orgId);
            //男项目已测试人数
            three = physicalDataService.findProjectSexTestNumberOf(accountId, planId, proId, 1, orgId);
            //女项目已测试人数
            four = physicalDataService.findProjectSexTestNumberOf(accountId, planId, proId, 2, orgId);
            //需要测试该项目的人数
            sum = physicalDataService.findProjectNumberOf(accountId, planId, proId, 0, orgId);
            //需要测试该项目的人数男
            sumBoy = physicalDataService.findProjectNumberOf(accountId, planId, proId, 1, orgId);
            //需要测试该项目的人数女
            sumGirl = physicalDataService.findProjectNumberOf(accountId, planId, proId, 2, orgId);
            //查询优秀的分数
            List<EllScoreRule> scoreRuleList = physicalDataService.selRuleScore("优秀", planId, proId, orgId);
            //查询单个项目满分
            if (scoreRuleList != null && scoreRuleList.size() > 0 && score > Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore())) {
                score = Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore());
            }
            //占比
            Integer gdp = physicalDataService.selProProportion(planId, proId);
            gdp = gdp == null || gdp == 0 ? 100 : gdp;
            score = score / 100 * gdp;
            //单个项目满分人数
            fullMark = physicalDataService.selFllScore(accountId, planId, proId, 0, orgId, score);
            //满分的80%为良好
            goodScore = score * 0.8;
            //满分的90%为优秀
            excellentScore = score * 0.9;
            //满分的60%为及格
            passScore = score * 0.6;
            //该项目得分情况占比分男
            excellentBoy = physicalDataService.findTotalityCondition(accountId, planId, "优秀", 1, proId, orgId);
            goodBoy = physicalDataService.findTotalityCondition(accountId, planId, "良", 1, proId, orgId);
            passBoy = physicalDataService.findTotalityCondition(accountId, planId, "及格", 1, proId, orgId);
            failBoy = physicalDataService.findTotalityCondition(accountId, planId, "不及格", 1, proId, orgId);
            //该项目得分情况占比分女
            excellentGirl = physicalDataService.findTotalityCondition(accountId, planId, "优秀", 2, proId, orgId);
            goodGirl = physicalDataService.findTotalityCondition(accountId, planId, "良", 2, proId, orgId);
            passGirl = physicalDataService.findTotalityCondition(accountId, planId, "及格", 2, proId, orgId);
            failGirl = physicalDataService.findTotalityCondition(accountId, planId, "不及格", 2, proId, orgId);
            //该项目得分情况
            excellent = physicalDataService.findTotalityCondition(accountId, planId, "优秀", 0, proId, orgId);
            good = physicalDataService.findTotalityCondition(accountId, planId, "良", 0, proId, orgId);
            pass = physicalDataService.findTotalityCondition(accountId, planId, "及格", 0, proId, orgId);
            fail = physicalDataService.findTotalityCondition(accountId, planId, "不及格", 0, proId, orgId);
        }


        ProjectData projectData = new ProjectData();
        projectData.setFullMark(fullMark.get(0).getNum());
        projectData.setOne(one);
        projectData.setTwo(two);
        projectData.setThree(three);
        projectData.setFour(four);
        projectData.setSum(sum);
        projectData.setSumBoy(sumBoy);
        projectData.setSumGirl(sumGirl);
        EllScoreDetails details = new EllScoreDetails();
        details.setExcellent(excellent);
        details.setPass(pass);
        details.setGood(good);
        details.setFail(fail);
        details.setExcellentBoy(excellentBoy);
        details.setGoodBoy(goodBoy);
        details.setPassBoy(passBoy);
        details.setFailBoy(failBoy);
        details.setExcellentGirl(excellentGirl);
        details.setGoodGirl(goodGirl);
        details.setPassGirl(passGirl);
        details.setFailGirl(failGirl);
        DecimalFormat dataFormat = new DecimalFormat("0.00");
        //分数等级
        EllScoreGradeDto gradeDto = new EllScoreGradeDto(">=" + dataFormat.format(excellentScore), dataFormat.format(excellentScore) + "-" + dataFormat.format(goodScore), dataFormat.format(goodScore) + "-" + dataFormat.format(passScore), dataFormat.format(passScore));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("projectData", projectData);
        jsonObject.put("details", details);
        jsonObject.put("scoreGrade", gradeDto);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    /**
     * 按条件查询班级得分情况
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询班级得分情况")
    @PostMapping(value = "/selClassScore")
    public Result selClassScore(@RequestBody ClassScoreDto classScoreDto) {
        List<Integer> list = physicalDataService.termSelectClass(classScoreDto);
        if (list.size() > 0) {
            List<ClassScoreVo> classScoreVos = physicalDataService.selEachClassScore(list);
            return new Result(HttpStatus.SUCCESS, "查询成功", classScoreVos);
        }
        return new Result(HttpStatus.ERROR, "未查询到班级", null);
    }

    /**
     * 按条件查询班级人数情况
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询班级人数情况")
    @PostMapping(value = "/selClassNumber")
    public Result selClassNumber(@RequestBody ClassScoreDto classScoreDto) {
        List<Integer> list = physicalDataService.termSelectClass(classScoreDto);
        if (list.size() > 0) {
            List<ClassNumberVo> classNumberVos = physicalDataService.selEachClassNumber(list);
            return new Result(HttpStatus.SUCCESS, "查询成功", classNumberVos);
        }
        return new Result(HttpStatus.ERROR, "未查询到班级", null);
    }

    /**
     * 按条件查询学校男女人数情况
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询学校男女人数情况")
    @PostMapping(value = "/selSchoolNumber")
    public Result selSchoolNumber(@RequestBody ClassScoreDto classScoreDto) {
        List<Integer> list = physicalDataService.termSelectClass(classScoreDto);
        if (list.size() > 0) {
            SchoolNumberVo schoolNumberVo = physicalDataService.selSchoolNumber(list);
            return new Result(HttpStatus.SUCCESS, "查询成功", schoolNumberVo);
        }
        return new Result(HttpStatus.ERROR, "未查询到班级", null);
    }

    /**
     * 考试得分详情
     */
   /* @ApiImplicitParams({
            @ApiImplicitParam(name = "accountOrgId", value = "账号id", dataType = "int", required = true),
            @ApiImplicitParam(name = "orgId", value = "部门id", dataType = "int", required = true),
            @ApiImplicitParam(name = "planId", value = "计划id", dataType = "string", required = true),
            @ApiImplicitParam(name = "proId", value = "项目id", dataType = "int", required = true)
    })*/
    @ApiOperation("项目平均分")
    @PostMapping("/getScoreDetails")
    public Result getScoreDetails(@RequestBody EllScoreDetailsVo details) {
        EllProAvgScoreDto ellProAvgScoreDto = physicalDataService.selProAvgScore(details.getPlanId(), details.getAccountOrgId(), details.getOrgId(), details.getProId(), 0);
        if (ellProAvgScoreDto != null && (ellProAvgScoreDto.getAvgScore() == null)) {
            ellProAvgScoreDto.setAvgScore(0.0);
        }
        DecimalFormat dataFormat = new DecimalFormat("0.00");

        return new Result(HttpStatus.SUCCESS, "成功", dataFormat.format(ellProAvgScoreDto.getAvgScore()));
    }

    @ApiOperation("学校总分情况")
    @PostMapping("/getTotalScore")
    public Result getTotalScore(@RequestBody EllScoreDetailsVo details) {
        //查询子部门
        List<PfDepart> schoolList = ellUserService.selPlanSchool(details.getPlanId(), 4, String.valueOf(details.getOrgId()));
        List<EllTotalScoreDto> ellTotalScoreDtoList = new ArrayList<>();
        DecimalFormat dataFormat = new DecimalFormat("0.00");
        for (int i = 0; i < schoolList.size(); i++) {
            EllTotalScoreDto ellTotalScoreDto = physicalDataService.selSchoolTotalScore(details.getPlanId(), schoolList.get(i).getId(), details.getAccountOrgId(), 0);
            if (ellTotalScoreDto != null && (ellTotalScoreDto.getTotalScore() == null || ellTotalScoreDto.getTotalAvgScore() == null)) {
                ellTotalScoreDto.setTotalAvgScore(0.0);
                ellTotalScoreDto.setTotalScore(0.0);
            }
            ellTotalScoreDto.setSchoolName(schoolList.get(i).getDeptName());
            ellTotalScoreDto.setTotalAvgScore(Double.parseDouble(dataFormat.format(ellTotalScoreDto.getTotalAvgScore())));
            ellTotalScoreDtoList.add(ellTotalScoreDto);
        }
        //排序大到小
        ellTotalScoreDtoList.sort((EllTotalScoreDto a, EllTotalScoreDto b) -> b.getTotalAvgScore().compareTo(a.getTotalAvgScore()));
        return new Result(HttpStatus.SUCCESS, "成功", ellTotalScoreDtoList);
    }

    @ApiOperation("男女项目平均分")
    @PostMapping("/getSexScoreDetails")
    public Result getSexScoreDetails(@RequestBody EllScoreDetailsVo details) {
        EllProAvgScoreDto ellProAvgScoreBoy = new EllProAvgScoreDto(), ellProAvgScoreGirl = new EllProAvgScoreDto();

        if (details.getProId() > 0 && details.getProId() != 1) {
            ellProAvgScoreBoy = physicalDataService.selProAvgScore(details.getPlanId(), details.getAccountOrgId(), details.getOrgId(), details.getProId(), 1);
            if (ellProAvgScoreBoy != null && (ellProAvgScoreBoy.getAvgScore() == null)) {
                ellProAvgScoreBoy.setAvgScore(0.0);
            }
            ellProAvgScoreGirl = physicalDataService.selProAvgScore(details.getPlanId(), details.getAccountOrgId(), details.getOrgId(), details.getProId(), 2);
            if (ellProAvgScoreGirl != null && (ellProAvgScoreGirl.getAvgScore() == null)) {
                ellProAvgScoreGirl.setAvgScore(0.0);
            }
        } else {
            //总分平均分男
            EllTotalScoreDto ellTotalScoreDtoBoy = physicalDataService.selSchoolTotalScore(details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), 1);
            if (ellTotalScoreDtoBoy == null || ellTotalScoreDtoBoy.getTotalAvgScore() == null) {
                ellTotalScoreDtoBoy.setTotalAvgScore(0.0);
            }
            ellProAvgScoreBoy.setNum(ellTotalScoreDtoBoy.getReferenceNum());
            ellProAvgScoreBoy.setAvgScore(ellTotalScoreDtoBoy.getTotalAvgScore());
            //总分平均分女
            EllTotalScoreDto ellTotalScoreDtoGirl = physicalDataService.selSchoolTotalScore(details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), 2);
            if (ellTotalScoreDtoGirl == null || ellTotalScoreDtoGirl.getTotalAvgScore() == null) {
                ellTotalScoreDtoGirl.setTotalAvgScore(0.0);
            }
            ellProAvgScoreGirl.setNum(ellTotalScoreDtoGirl.getReferenceNum());
            ellProAvgScoreGirl.setAvgScore(ellTotalScoreDtoGirl.getTotalAvgScore());
        }

        DecimalFormat dataFormat = new DecimalFormat("0.00");
        ellProAvgScoreBoy.setAvgScore(Double.valueOf(dataFormat.format(ellProAvgScoreBoy.getAvgScore())));
        ellProAvgScoreGirl.setAvgScore(Double.valueOf(dataFormat.format(ellProAvgScoreGirl.getAvgScore())));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boy", ellProAvgScoreBoy);
        jsonObject.put("girl", ellProAvgScoreGirl);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("学校总数/项目考试总数/项目平均分/项目总人数")
    @PostMapping("/getProPlanDatNum")
    public Result getProPlanDatNum(@RequestBody EllScoreDetailsVo details) {
        //查询学校
        List<PfDepart> schoolList = ellUserService.selPlanSchool(details.getPlanId(), 3, String.valueOf(details.getOrgId()));
        if (schoolList == null || schoolList.size() <= 0) {
            //查询班级
            List<PfDepart> classList = ellUserService.selPlanSchool(details.getPlanId(), 1, String.valueOf(details.getOrgId()));
            //查询学校
            schoolList = ellUserService.selPlanSchool(details.getPlanId(), 2, String.valueOf(classList.get(0).getId()));
        }
        //考试总人数
        int sum = physicalDataService.findProjectNumberOf(details.getAccountOrgId(), details.getPlanId(), details.getProId(), 0, details.getOrgId());

        //优秀默认为100分不为100分的就以最后一个为满分
        double score = 100.0;
        //满分人数
        int fullEndScore = 0;
        //平均分
        double avgScore = 0.0;
        //单个项目的情况
        if (details.getProId() > 0 && details.getProId() != 1) {
            //查询优秀的分数
            List<EllScoreRule> scoreRuleList = physicalDataService.selRuleScore("优秀", details.getPlanId(), details.getProId(), details.getOrgId());
            if (scoreRuleList != null && scoreRuleList.size() > 0 && score > Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore())) {
                score = Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore());
            }
            //占比
            Integer gdp = physicalDataService.selProProportion(details.getPlanId(), details.getProId());
            gdp = gdp == null || gdp == 0 ? 100 : gdp;
            score = score / 100 * gdp;
            List<EllFullScoreDto> ellFullScoreDtoListGirl = physicalDataService.selFllScore(details.getAccountOrgId(), details.getPlanId(), details.getProId(), 0, details.getOrgId(), score);
            fullEndScore = ellFullScoreDtoListGirl.get(0).getNum();
            //平均分
            EllProAvgScoreDto ellProAvgScoreDto = physicalDataService.selProAvgScore(details.getPlanId(), details.getAccountOrgId(), details.getOrgId(), details.getProId(), 0);
            if (ellProAvgScoreDto != null && (ellProAvgScoreDto.getAvgScore() == null)) {
                ellProAvgScoreDto.setAvgScore(0.0);
            }
            avgScore = ellProAvgScoreDto.getAvgScore();
        } else {
            //没选项目就是最终分满分
            //查询计划
            List<EllPlan> ellPlanList = planService.findPlan(details.getPlanId(), null, null, null);
            //查询计划必选项目注意：每个性别测试项目相同数量，分值相同
            List<EllPlanProjectChance> ellPlanProjectChanceListMust = physicalDataService.selPlanProId(details.getPlanId(), 1, "1", 0);
            //该计划的满分
            score = physicalDataService.FullMark(details, ellPlanList, ellPlanProjectChanceListMust);
            fullEndScore = physicalDataService.selFullEndScorePeople(0, details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), score);
            //总分平均分
            EllTotalScoreDto ellTotalScoreDto = physicalDataService.selSchoolTotalScore(details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), 0);
            if (ellTotalScoreDto == null || ellTotalScoreDto.getTotalAvgScore() == null) {
                ellTotalScoreDto.setTotalAvgScore(0.0);
            }
            avgScore = ellTotalScoreDto.getTotalAvgScore();
        }

        DecimalFormat dataFormat = new DecimalFormat("0.00");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalNum", sum);
        jsonObject.put("fullEndScore", fullEndScore);
        jsonObject.put("schoolNum", schoolList.size());
        jsonObject.put("totalEndScore", dataFormat.format(avgScore));
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    //目前没用
    @ApiOperation("学校总数/考试总数/平均分/满分总人数")
    @PostMapping("/getPlanDatNum")
    public Result getPlanDatNum(@RequestBody EllScoreDetailsVo details) {
        //查询计划
        List<EllPlan> ellPlanList = planService.findPlan(details.getPlanId(), null, null, null);
        //查询计划必选项目注意：每个性别测试项目相同数量，分值相同
        List<EllPlanProjectChance> ellPlanProjectChanceListMust = physicalDataService.selPlanProId(details.getPlanId(), 1, "1", 0);
        //查询学校
        List<PfDepart> schoolList = ellUserService.selPlanSchool(details.getPlanId(), 3, String.valueOf(details.getOrgId()));
        if (schoolList == null || schoolList.size() <= 0) {
            //查询班级
            List<PfDepart> classList = ellUserService.selPlanSchool(details.getPlanId(), 1, String.valueOf(details.getOrgId()));
            //查询学校
            schoolList = ellUserService.selPlanSchool(details.getPlanId(), 2, String.valueOf(classList.get(0).getId()));
        }
        //考试总人数
        int totalNum = physicalDataService.selPlanOrgIdPeopleNum(0, details.getPlanId(), details.getOrgId(), details.getAccountOrgId());
        //每人选考数量
//        int num =planService.selPlan(details.getPlanId()).getCustomizeTheNumber();
        //该计划的满分
        double score = physicalDataService.FullMark(details, ellPlanList, ellPlanProjectChanceListMust);
        //满分人数
        int fullEndScore = physicalDataService.selFullEndScorePeople(0, details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), score);
        //平均分
        EllTotalScoreDto ellTotalScoreDto = physicalDataService.selSchoolTotalScore(details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), 0);
        if (ellTotalScoreDto == null || ellTotalScoreDto.getTotalAvgScore() == null) {
            ellTotalScoreDto.setTotalAvgScore(0.0);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalNum", totalNum);

        jsonObject.put("fullEndScore", fullEndScore);
        jsonObject.put("schoolNum", schoolList.size());
        //平均分
        DecimalFormat dataFormat = new DecimalFormat("0.00");
        jsonObject.put("totalEndScore", dataFormat.format(ellTotalScoreDto.getTotalAvgScore()));
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }


    @ApiOperation("查询每个项目满分人数")
    @PostMapping("/getProFullScoreNum")
    public Result getProFullScoreNum(@RequestBody EllScoreDetailsVo details) {
        List<EllFullScoreSexDto> ellFullScoreDtos = new ArrayList<>();
        //男女所有项目满分次数
        int totalBoy = 0;
        int totalGirl = 0;
        List<EllPlanProjectChance> ellPlanProjectChanceList = physicalDataService.selPlanProId(details.getPlanId(), 0, null, details.getProId());
        for (int i = 0; i < ellPlanProjectChanceList.size(); i++) {
            //查询优秀的分数
            List<EllScoreRule> scoreRuleList = physicalDataService.selRuleScore("优秀", details.getPlanId(), ellPlanProjectChanceList.get(i).getProId(), details.getOrgId());
            //优秀默认为100分不为100分的就以最后一个为满分
            double score = 100.0;
            if (scoreRuleList != null && scoreRuleList.size() > 0 && score > Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore())) {
                score = Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore());
            }
            //占比
            Integer gdp = physicalDataService.selProProportion(details.getPlanId(), details.getProId());
            gdp = gdp == null || gdp == 0 ? 100 : gdp;
            score = score / 100 * gdp;
            List<EllFullScoreDto> ellFullScoreDtoListGirl = physicalDataService.selFllScore(details.getAccountOrgId(), details.getPlanId(), ellPlanProjectChanceList.get(i).getProId(), 2, details.getOrgId(), score);
            List<EllFullScoreDto> ellFullScoreDtoListBoy = physicalDataService.selFllScore(details.getAccountOrgId(), details.getPlanId(), ellPlanProjectChanceList.get(i).getProId(), 1, details.getOrgId(), score);
            EllFullScoreSexDto ellFullScoreSexDto = new EllFullScoreSexDto(ellFullScoreDtoListBoy.get(0).getNum(), ellFullScoreDtoListGirl.get(0).getNum(), ellPlanProjectChanceList.get(i).getProName());
            ellFullScoreDtos.add(ellFullScoreSexDto);
            totalBoy += ellFullScoreDtoListBoy.get(0).getNum();
            totalGirl += ellFullScoreDtoListGirl.get(0).getNum();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("proFullMark", ellFullScoreDtos);
        jsonObject.put("girlNum", totalGirl);
        jsonObject.put("boyNum", totalBoy);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("每个项目达标情况")
    @PostMapping("/getProReach")
    public Result getProReach(@RequestBody EllScoreDetailsVo details) {
        List<EllFullScoreDto> ellFullScoreDtos = new ArrayList<>();
        //查询计划项目
        List<EllPlanProjectChance> ellPlanProjectChanceList = physicalDataService.selPlanProId(details.getPlanId(), 0, null, details.getProId());
        for (int i = 0; i < ellPlanProjectChanceList.size(); i++) {
            //查询及格的分数
            List<EllScoreRule> scoreRuleList = physicalDataService.selRuleScore("及格", details.getPlanId(), ellPlanProjectChanceList.get(i).getProId(), details.getOrgId());
            double score = 60.0;
            if (scoreRuleList != null && scoreRuleList.size() >= 1) {        //判断是否为小数
                if (scoreRuleList.get(0).getRuleScore().contains(".")) {
                    score = Double.parseDouble(scoreRuleList.get(0).getRuleScore());
                } else {
                    score = Double.parseDouble(scoreRuleList.get(0).getRuleScore() + ".00");
                }
            }
            //占比
            Integer gdp = physicalDataService.selProProportion(details.getPlanId(), ellPlanProjectChanceList.get(i).getProId());
            gdp = gdp == null || gdp == 0 ? 100 : gdp;
            score = score / 100 * gdp;
            //需要测试该项目的人数
            int sum = physicalDataService.findProjectNumberOf(details.getAccountOrgId(), details.getPlanId(), ellPlanProjectChanceList.get(i).getProId(), 0, details.getOrgId());
            List<EllFullScoreDto> ellFullScoreDtoList = physicalDataService.selFllScore(details.getAccountOrgId(), details.getPlanId(), ellPlanProjectChanceList.get(i).getProId(), 0, details.getOrgId(), score);
            double percent = ellFullScoreDtoList.get(0).getNum() / Double.parseDouble(sum + ".00") * 100.0;
            DecimalFormat dataFormat = new DecimalFormat("0.00");
            ellFullScoreDtoList.get(0).setPercent(dataFormat.format(percent));
            ellFullScoreDtoList.get(0).setProName(ellPlanProjectChanceList.get(i).getProName());
            ellFullScoreDtos.add(ellFullScoreDtoList.get(0));
        }

        return new Result(HttpStatus.SUCCESS, "成功", ellFullScoreDtos);
    }

    @ApiOperation("每个项目平均分")
    @PostMapping("/getProAvgScore")
    public Result getProAvgScore(@RequestBody EllScoreDetailsVo details) {
        List<EllProAvgScoreDto> ellProAvgScoreDtoList = new ArrayList<>();
        DecimalFormat dataFormat = new DecimalFormat("0.00");
        //查询计划项目
        List<EllPlanProjectChance> ellPlanProjectChanceList = physicalDataService.selPlanProId(details.getPlanId(), 0, null, 0);
        //查询项目平均分
        for (int i = 0; i < ellPlanProjectChanceList.size(); i++) {
            EllProAvgScoreDto ellProAvgScoreDto = physicalDataService.selProAvgScore(details.getPlanId(), details.getAccountOrgId(), details.getOrgId(), ellPlanProjectChanceList.get(i).getProId(), 0);
            if (ellProAvgScoreDto != null && (ellProAvgScoreDto.getAvgScore() == null)) {
                ellProAvgScoreDto.setAvgScore(0.0);
            }
            ellProAvgScoreDto.setAvgScore(Double.parseDouble(dataFormat.format(ellProAvgScoreDto.getAvgScore())));
            ellProAvgScoreDto.setProName(ellPlanProjectChanceList.get(i).getProName());
            ellProAvgScoreDtoList.add(ellProAvgScoreDto);
        }
        //总分平均分
        EllTotalScoreDto ellTotalScoreDto = physicalDataService.selSchoolTotalScore(details.getPlanId(), details.getOrgId(), details.getAccountOrgId(), 0);
        if (ellTotalScoreDto == null || ellTotalScoreDto.getTotalAvgScore() == null) {
            ellTotalScoreDto.setTotalAvgScore(0.0);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("proAvg", ellProAvgScoreDtoList);
        jsonObject.put("totalAvg", dataFormat.format(ellTotalScoreDto.getTotalAvgScore()));
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("查询总分等级占比")
    @PostMapping("/getScoreGrade")
    public Result getScoreGrade(@RequestBody EllScoreDetailsVo details) {
        //查询计划
        List<EllPlan> ellPlanList = planService.findPlan(details.getPlanId(), null, null, null);
        //查询计划必选项目注意：每个性别测试项目相同数量，分值相同
        List<EllPlanProjectChance> ellPlanProjectChanceListMust = physicalDataService.selPlanProId(details.getPlanId(), 1, "1", 0);
        //该计划的满分
        double score = physicalDataService.FullMark(details, ellPlanList, ellPlanProjectChanceListMust);
        //满分的80%为良好
        double goodScore = score * 0.8;
        //满分的90%为优秀
        double excellentScore = score * 0.9;
        //满分的60%为及格
        double passScore = score * 0.6;
        //不能包括最大，满分和零分除外
        //优秀
        int excellentNum = physicalDataService.selEndScoreByScore(details.getPlanId(), score, excellentScore, details.getOrgId(), details.getAccountOrgId(), null, 0);
        //良好
        int goodNum = physicalDataService.selEndScoreByScore(details.getPlanId(), excellentScore, goodScore, details.getOrgId(), details.getAccountOrgId(), excellentScore, 0);
        //及格
        int passNum = physicalDataService.selEndScoreByScore(details.getPlanId(), goodScore, passScore, details.getOrgId(), details.getAccountOrgId(), goodScore, 0);
        //不及格
        int notPassNum = physicalDataService.selEndScoreByScore(details.getPlanId(), passScore, 0, details.getOrgId(), details.getAccountOrgId(), null, 0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("excellentNum", excellentNum);
        jsonObject.put("goodNum", goodNum);
        jsonObject.put("passNum", passNum);
        jsonObject.put("notPassNum", notPassNum);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }
}
