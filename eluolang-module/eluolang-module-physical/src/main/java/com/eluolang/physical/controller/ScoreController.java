package com.eluolang.physical.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.Listener.ScoreListener;
import com.eluolang.physical.dto.EllScoreHistoryDto;
import com.eluolang.physical.dto.OneUserScoreDto;
import com.eluolang.physical.feign.ELLSocketServerFeign;
import com.eluolang.physical.feign.PlayGroundServiceFeign;
import com.eluolang.physical.model.*;
import com.eluolang.physical.redis.AvoidRedis;
import com.eluolang.physical.service.*;
import com.eluolang.physical.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.jmx.snmp.Timestamp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRange;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "分数管理")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RulesService rulesService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PlanService planService;
    @Autowired
    private EllUserService ellUserService;
    @Autowired
    private AvoidRedis avoidRedis;
    @Autowired
    private ELLSocketServerFeign ellSocketServerFeign;
    @Autowired
    private PlayGroundServiceFeign playGroundServiceFeign;
    //没有分数的项目(子项目)
    private static int[] notHaveScorePro = {67, 68, 69, 70, 71, 72, 73, 74};

    @ApiOperation("提交上传成绩")
    @PostMapping("/submitScores")
    @Transactional
    public Result submitScores(@RequestBody EllHistoryVo ellHistoryVo) throws IOException {
        //减少测试人数,
        avoidRedis.reduceTestNum(Integer.parseInt(ellHistoryVo.getUserOrgId()));
        if (ellHistoryVo.getMode() == 3 || ellHistoryVo.getMode() == 0) {
            try {
                if (ellHistoryVo.getTestProject() == "" && ellHistoryVo.getTestProject() == null) {
                    if (ellHistoryVo.getUserId() == "" && ellHistoryVo.getUserId() == null) {
                        return new Result(HttpStatus.ERROR, "请上传正确的参数");
                    }
                }
                EllScoreVerify ellScoreVerify = scoreService.findVerify(ellHistoryVo.getUserId(), ellHistoryVo.getPlanId());
                if (ellScoreVerify == null) {
                    Boolean lock = avoidRedis.getScoreLock(ellHistoryVo.getPlanId() + ":" + ellHistoryVo.getTestProject() + ":" + ellHistoryVo.getUserId(), 1);
                    while (!lock) {
                        lock = avoidRedis.getScoreLock(ellHistoryVo.getPlanId() + ":" + ellHistoryVo.getTestProject() + ":" + ellHistoryVo.getUserId(), 1);
                    }
                    int i = scoreService.addStudentScoreMapper(ellHistoryVo);
                    if (i > 0) {
                        FindScoreConditionVo findScoreConditionVo = new FindScoreConditionVo();
                        findScoreConditionVo.setPlanId(ellHistoryVo.getPlanId());
                        findScoreConditionVo.setUserId(ellHistoryVo.getUserId());
                        //查询学生成绩
                        List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, null);
                        //生成最终分数
                        scoreService.createEndScore(findScoreConditionVo, findUser);
                        //通知bi更改
                        ellSocketServerFeign.sendToAll("修改");
                        //删除锁
                        avoidRedis.releaseScoreLock(ellHistoryVo.getPlanId() + ":" + ellHistoryVo.getTestProject() + ":" + ellHistoryVo.getUserId());
                        return new Result(HttpStatus.SUCCESS, "成绩提交成功");
                    }
                    //删除锁
                    avoidRedis.releaseScoreLock(ellHistoryVo.getPlanId() + ":" + ellHistoryVo.getTestProject() + ":" + ellHistoryVo.getUserId());
                    return new Result(HttpStatus.ERROR, "成绩提交失败");
                }
            } catch (Exception e) {
                //删除锁
                avoidRedis.releaseScoreLock(ellHistoryVo.getPlanId() + ":" + ellHistoryVo.getTestProject() + ":" + ellHistoryVo.getUserId());
                System.out.println(e.getMessage());
                return new Result(HttpStatus.ERROR, "成绩提交失败");
            }
            return new Result(HttpStatus.NO_MODIFICATION_ALLOWED, "成绩已核实，不能修改");
        } else {
            List<EllClassHistoryVo> ellClassHistoryVoList = new ArrayList<>();
            EllClassHistoryVo ellClassHistoryVo = new EllClassHistoryVo();
            ellClassHistoryVo.setUserId(ellHistoryVo.getUserId());
            ellClassHistoryVo.setCourseId(ellHistoryVo.getCourseId());
            ellClassHistoryVo.setCreateByDevice(ellHistoryVo.getCreateByDevice());
            ellClassHistoryVo.setTeacherId(ellHistoryVo.getTeacherId());
            List<EllChancesVo> chancesVos = new ArrayList<>();
            EllChancesVo ellChancesVo = new EllChancesVo();
            List<String> scoreList = new ArrayList<>();
            //获取最好成绩
            for (int i = 0; i < ellHistoryVo.getTestData().get(ellHistoryVo.getBestGradeKey()).size(); i++) {
                scoreList.add(ellHistoryVo.getTestData().get(ellHistoryVo.getBestGradeKey()).get(i).getData());
            }
            ellChancesVo.setScores(scoreList);
            ellChancesVo.setTimestamp(ellHistoryVo.getEndTimestamp());
            chancesVos.add(ellChancesVo);
            ellClassHistoryVo.setChances(chancesVos);
            ellClassHistoryVo.setUserSex(Integer.parseInt(ellHistoryVo.getUserSex()));
            ellClassHistoryVo.setOrgId(ellHistoryVo.getUserOrgId());
            ellClassHistoryVo.setProId(Integer.parseInt(ellHistoryVo.getTestProject()));
            ellClassHistoryVo.setProName(ellHistoryVo.getProjectName());
            ellClassHistoryVo.setIsRetest(ellHistoryVo.getIsRetest());
            ellClassHistoryVo.setType(ellHistoryVo.getMode());
            ellClassHistoryVo.setExtraData(ellHistoryVo.getExtraData());
            ellClassHistoryVo.setCheckInTimestamp(ellHistoryVo.getCheckInTimestamp());
            ellClassHistoryVo.setEndTimestamp(ellHistoryVo.getEndTimestamp());
            ellClassHistoryVoList.add(ellClassHistoryVo);
            return playGroundServiceFeign.addHistory(ellClassHistoryVoList);
        }
    }

    @ApiOperation("批量提交上传成绩")
    @PostMapping("/submitBatchScores")
    @Transactional
    public Result submitBatchScores(@RequestBody List<EllHistoryVo> ellHistoryVoList) {
        //判断模式成绩
        if (ellHistoryVoList.get(0).getMode() == 3 || ellHistoryVoList.get(0).getMode() == 0) {
            Map<String, EllHistoryVo> ellHistoryVoMap = new HashMap<>();
            List<SubmitBatchErrorVo> submitBatchErrorVoList = new ArrayList<>();
            StringBuffer key = new StringBuffer();
            try {
                for (int i = 0; i < ellHistoryVoList.size(); i++) {
                    //生成key
                    key.append(ellHistoryVoList.get(i).getPlanId() + ":" + ellHistoryVoList.get(i).getTestProject() + ":" + ellHistoryVoList.get(i).getUserId());
                    Boolean lock = avoidRedis.getScoreLock(ellHistoryVoList.get(i).getPlanId() + ":" + ellHistoryVoList.get(i).getTestProject() + ":" + ellHistoryVoList.get(i).getUserId(), 1);
                    while (!lock) {
                        lock = avoidRedis.getScoreLock(ellHistoryVoList.get(i).getPlanId() + ":" + ellHistoryVoList.get(i).getTestProject() + ":" + ellHistoryVoList.get(i).getUserId(), 1);
                    }
                    scoreService.addStudentScoreMapper(ellHistoryVoList.get(i));
                    FindScoreConditionVo findScoreConditionVo = new FindScoreConditionVo();
                    findScoreConditionVo.setPlanId(ellHistoryVoList.get(i).getPlanId());
                    findScoreConditionVo.setUserId((ellHistoryVoList.get(i).getUserId()));
                    List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, null);
                    //生成最终分数
                    scoreService.createEndScore(findScoreConditionVo, findUser);
                    //删除锁
                    avoidRedis.releaseScoreLock(ellHistoryVoList.get(i).getPlanId() + ":" + ellHistoryVoList.get(i).getTestProject() + ":" + ellHistoryVoList.get(i).getUserId());
                    key.setLength(0);
                }
                //通知bi更改
                ellSocketServerFeign.sendToAll("修改");
            } catch (Exception e) {
                SubmitBatchErrorVo submitBatchErrorVo = new SubmitBatchErrorVo();
                submitBatchErrorVo.setCode(HttpStatus.PARTIAL_DATA_ERROR);
                submitBatchErrorVo.setMessage("部分数据出错");
                submitBatchErrorVo.setCheckInTimestamp(System.currentTimeMillis());
                submitBatchErrorVoList.add(submitBatchErrorVo);
                //删除锁
                avoidRedis.releaseScoreLock(key.toString());
            }
            if (ellHistoryVoMap.size() > 0) {
                return new Result(HttpStatus.PARTIAL_DATA_ERROR, "部分数据出错", submitBatchErrorVoList);
            }
        } else {
            //减少测试人数
            List<EllClassHistoryVo> ellClassHistoryVoList = new ArrayList<>();
            for (int i = 0; i < ellHistoryVoList.size(); i++) {
                avoidRedis.reduceTestNum(Integer.parseInt(ellHistoryVoList.get(i).getUserOrgId()));
                EllClassHistoryVo ellClassHistoryVo = new EllClassHistoryVo();
                ellClassHistoryVo.setUserId(ellHistoryVoList.get(i).getUserId());
                ellClassHistoryVo.setCourseId(ellHistoryVoList.get(i).getCourseId());
                ellClassHistoryVo.setCreateByDevice(ellHistoryVoList.get(i).getCreateByDevice());
                ellClassHistoryVo.setTeacherId(ellHistoryVoList.get(i).getTeacherId());
                List<EllChancesVo> chancesVos = new ArrayList<>();
                EllChancesVo ellChancesVo = new EllChancesVo();
                List<String> scoreList = new ArrayList<>();
                //获取最好成绩
                for (int j = 0; i < ellHistoryVoList.get(i).getTestData().get(ellHistoryVoList.get(i).getBestGradeKey()).size(); j++) {
                    scoreList.add(ellHistoryVoList.get(i).getTestData().get(ellHistoryVoList.get(i).getBestGradeKey()).get(j).getData());
                }
                ellChancesVo.setScores(scoreList);
                ellChancesVo.setTimestamp(ellHistoryVoList.get(i).getEndTimestamp());
                chancesVos.add(ellChancesVo);
                ellClassHistoryVo.setChances(chancesVos);
                ellClassHistoryVo.setUserSex(Integer.parseInt(ellHistoryVoList.get(i).getUserSex()));
                ellClassHistoryVo.setOrgId(ellHistoryVoList.get(i).getUserOrgId());
                ellClassHistoryVo.setProId(Integer.parseInt(ellHistoryVoList.get(i).getTestProject()));
                ellClassHistoryVo.setProName(ellHistoryVoList.get(i).getProjectName());
                ellClassHistoryVo.setIsRetest(ellHistoryVoList.get(i).getIsRetest());
                ellClassHistoryVo.setType(ellHistoryVoList.get(i).getMode());
                ellClassHistoryVo.setExtraData(ellHistoryVoList.get(i).getExtraData());
                ellClassHistoryVo.setCheckInTimestamp(ellHistoryVoList.get(i).getCheckInTimestamp());
                ellClassHistoryVo.setEndTimestamp(ellHistoryVoList.get(i).getEndTimestamp());
                ellClassHistoryVoList.add(ellClassHistoryVo);
            }
            return playGroundServiceFeign.addHistory(ellClassHistoryVoList);
        }
        return Result.SUCCESS();
    }


    @Transactional
    @ApiOperation("查询成绩")
    @PostMapping("/findScores")
    public Result findScores(@RequestBody FindScoreConditionVo findScoreConditionVo) {
        List<String> userIds = scoreService.selFindAvoid(findScoreConditionVo.getPlanId());
        if (userIds == null && userIds.size() == 0) {
            userIds = null;
        }
        //long a = new Date().getTime();
        //查询有成绩的学生的
        PageHelper.startPage(findScoreConditionVo.getPage(), 10);
        List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, userIds);
        //System.out.println("a:" + (new Date().getTime() - a));
        PageInfo info = new PageInfo<>(findUser);
        //long b = new Date().getTime();
        List<EllReturnScoreVo> ellReturnScoreVos = scoreService.findUserHistory(findScoreConditionVo, findUser);
        //System.out.println("b:" + (new Date().getTime() - b));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("score", ellReturnScoreVos);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "", jsonObject);
    }

    @Transactional
    @ApiOperation("查询用户成绩(平板)")
    @PostMapping("/getScores")
    public Result getScores(@RequestBody FindScoreConditionVo findScoreConditionVo) {
        List<String> userIds = scoreService.selFindAvoid(findScoreConditionVo.getPlanId());
        if (userIds == null && userIds.size() == 0) {
            userIds = null;
        }
        //long a = new Date().getTime();
        //查询有成绩的学生的
        PageHelper.startPage(findScoreConditionVo.getPage(), 10);
        List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, userIds);
        //System.out.println("a:" + (new Date().getTime() - a));
        PageInfo info = new PageInfo<>(findUser);
        //long b = new Date().getTime();
        List<EllReturnScoreVo> ellReturnScoreVos = scoreService.findUserHistory(findScoreConditionVo, findUser);
        //System.out.println("b:" + (new Date().getTime() - b));
        return new Result(HttpStatus.SUCCESS, "成功", ellReturnScoreVos);
    }

    @ApiOperation("查询单个项目成绩/分数")
    @PostMapping("/getOneScore")
    public Result getOneScore(@RequestBody OneScoreVo oneScoreVo) {
        PageHelper.startPage(oneScoreVo.getPage(), 20);
        List<OneUserScoreDto> oneUserScoreDtos = scoreService.getOneUserScore(oneScoreVo.getAccount(), oneScoreVo.getOrgId(), oneScoreVo.getPlanId(), oneScoreVo.getProId(), oneScoreVo.getUserName(), oneScoreVo.getUserId());
        PageInfo info = new PageInfo<>(oneUserScoreDtos);
        //判断项目是否身高体重
        if (oneScoreVo.getProId() == 35) {
            for (int i = 0; i < oneUserScoreDtos.size(); i++) {
                if (oneUserScoreDtos.get(i).getData() != null && oneUserScoreDtos.get(i).getData().contains(",")) {
                    String[] data = oneUserScoreDtos.get(i).getData().split(",");
                    oneUserScoreDtos.get(i).setData("身高:" + data[0] + "cm,体重:" + data[1] + "kg" + ",BMI:" + data[2]);
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", oneUserScoreDtos);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("查询单个项目成绩/分数2")
    @PostMapping("/getOneProScore")
    public Result getOneProScore(@RequestBody OneScoreVo oneScoreVo) {
        PageHelper.startPage(oneScoreVo.getPage(), 20);
        List<OneUserScoreDto> oneUserScoreDtos = scoreService.getOneUserScore(oneScoreVo.getAccount(), oneScoreVo.getOrgId(), oneScoreVo.getPlanId(), oneScoreVo.getProId(), oneScoreVo.getUserName(), oneScoreVo.getUserId());
        PageInfo info = new PageInfo<>(oneUserScoreDtos);
        //判断项目是否身高体重
        if (oneScoreVo.getProId() == 35) {
            for (int i = 0; i < oneUserScoreDtos.size(); i++) {
                if (oneUserScoreDtos.get(i).getData() != null && oneUserScoreDtos.get(i).getData().contains(",")) {
                    String[] data = oneUserScoreDtos.get(i).getData().split(",");
                    oneUserScoreDtos.get(i).setData("身高:" + data[0] + "cm,体重:" + data[1] + "kg" + ",BMI:" + data[2]);
                }
            }
        }
        return new Result(HttpStatus.SUCCESS, "成功", oneUserScoreDtos.get(0));
    }

    @ApiOperation("班级成绩列表")
    @PostMapping("/scoreList")
    public Result scoreList(@RequestBody EllScoreListVo ellScore) {
        //查询男女生的必考项目
        List<String> proIdBoys = scoreService.selProId(ellScore.getPlanId(), "1");
        List<String> proIdGirls = scoreService.selProId(ellScore.getPlanId(), "2");
        PageHelper.startPage(ellScore.getPage(), ellScore.getSize());
        List<EllScoreListDto> list = scoreService.selUserList(ellScore);
        PageInfo info = new PageInfo<>(list);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSex().equals("1")) {
                list.get(i).setGrade(ScoreDisposeUtil.scoreList(scoreService.selGradeList(list.get(i).getUserId(), ellScore.getPlanId()), proIdBoys, scoreService.selOptionProId(ellScore.getPlanId(), list.get(i).getUserId())));
            } else {
                list.get(i).setGrade(ScoreDisposeUtil.scoreList(scoreService.selGradeList(list.get(i).getUserId(), ellScore.getPlanId()), proIdGirls, scoreService.selOptionProId(ellScore.getPlanId(), list.get(i).getUserId())));
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", info.getTotal());
        jsonObject.put("ellScore", list);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("成绩导出")
    @PostMapping("/importExcel")
    public Result importExcel(@RequestBody FindScoreConditionVo findScoreConditionVo, HttpServletResponse response) {
        List<EllReturnScoreVo> ellReturnScoreVos = scoreService.importHistory(findScoreConditionVo);
        //需要测试项目
        //查询视力项目
        List<EllPlanProjectChance> ellPlanProject = planService.findProChance(findScoreConditionVo.getPlanId(), true);
        //父项目id和子项目id关联保存map
        Map<Integer, List<EllPlanProjectChance>> integerListMap = new HashMap<>();
        for (int i = 0; i < ellPlanProject.size(); i++) {
            //判断是否有视力/视力查询的是子项目的成绩所有要删除父项目
            if (ellPlanProject.get(i).getProId() == 43 || ellPlanProject.get(i).getProName().equals("视力")) {
                List<EllPlanProjectChance> ellPlanProjectChanceList = scoreService.findEyesightProject(findScoreConditionVo.getPlanId(), ellPlanProject.get(i).getProId());
                //父项目id和子项目关联
                integerListMap.put(ellPlanProject.get(i).getProId(), ellPlanProjectChanceList);
                //在测试项目中删除父项目
                //删除父项目
                ellPlanProject.remove(i);
                //添加子项目
                ellPlanProject.addAll(ellPlanProjectChanceList);
            }
            if (ellPlanProject.get(i).getProId() == 43 || ellPlanProject.get(i).getProName().equals("身高体重")) {
                List<EllPlanProjectChance> ellPlanProjectChanceList = scoreService.findEyesightProject(findScoreConditionVo.getPlanId(), ellPlanProject.get(i).getProId());
                //父项目id和子项目关联
                integerListMap.put(ellPlanProject.get(i).getProId(), ellPlanProjectChanceList);
                //在测试项目中删除父项目
                //删除父项目
                ellPlanProject.remove(i);
                //添加子项目
                ellPlanProject.addAll(ellPlanProjectChanceList);
            }
        }
        String fileName = dynamicImportScoreExcel(ellPlanProject, ellReturnScoreVos, integerListMap);
        String Path = "";
        if (FileUploadUtil.isLinux() == false) {
            Path = WindowsSite.Path;
        } else {
            Path = LinuxSite.Path;
        }
        //生成文件路径
        PathGeneration.createPath(Path);
        File file = new File(Path + fileName);
        try (InputStream inputStream = new FileInputStream(file); OutputStream outputStream = response.getOutputStream();) {
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(HttpStatus.SUCCESS, "导出成功");
    }

    @ApiOperation("核实成绩查询")
    @GetMapping("/findResultsVerify")
    public Result findResultsVerify(String userId, String planId) {
        FindScoreConditionVo findScoreConditionVo = new FindScoreConditionVo();
        findScoreConditionVo.setUserId(userId);
        findScoreConditionVo.setPlanId(planId);
        findScoreConditionVo.setPage(-1);
        List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, null);
        if (findUser.size() <= 0) {
            return new Result(HttpStatus.NOT_FOUND, "未找到");
        }
        //查询应测项目id
        List<String> proIds = scoreService.selUserTestPro(planId, userId);
        //c查询对应项目的成绩
        List<FindScoreVo> findScoreVoList = scoreService.selAllProHistory(planId, userId, proIds);
        findUser.get(0).setScore(findScoreVoList);
        //查询照片
        String userImage = ellUserService.findUserImage(userId);
        //查询学校名称
        String schoolName = ellUserService.findOrgName(findUser.get(0).getOrgId(), 1, userId);
        //查询班级名称
        String className = ellUserService.findOrgName(findUser.get(0).getOrgId(), 0, userId);
        //查询计划
        List<EllPlan> ellPlans = planService.findPlan(planId, null, null, null);
        EllScoreVerify scoreVerify = scoreService.selVerifyByUserId(planId, userId);
        findUser.get(0).setOrgName(className);
        findUser.get(0).setFileUrl(userImage);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", findUser);
        jsonObject.put("begin", ellPlans.get(0).getDateBegin());
        jsonObject.put("examNumber", null);
        jsonObject.put("end", ellPlans.get(0).getDateEnd());
        jsonObject.put("schoolName", schoolName);
        jsonObject.put("scoreVerify", scoreVerify);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("纸质成绩核实")
    @PostMapping("/resultsVerify")
    @Transactional
    public Result resultsVerify(@RequestBody EllScoreVerifyVo ellScoreVerifyVo) throws IOException {
        //查询成绩完成是否核实
        EllScoreVerify ellScoreVerify = scoreService.findVerify(ellScoreVerifyVo.getUserId(), ellScoreVerifyVo.getPlanId());
        if (ellScoreVerify != null) {
            return new Result(HttpStatus.SUCCESS);
        }
        int i = scoreService.resultsVerify(ellScoreVerifyVo);
        if (i > 0) {
            //通知bi更改
            ellSocketServerFeign.sendToAll("修改");
            return new Result(HttpStatus.SUCCESS, "核实成功！");
        }
        return new Result(HttpStatus.ERROR, "核实失败有可能是项目还没测试完成!");
    }

    @ApiOperation("app成绩核实")
    @PostMapping("/resultsVerifyApp")
    @Transactional
    public Result resultsVerifyApp(EllScoreVerifyVo ellScoreVerifyVo, @RequestParam("faceImage") MultipartFile faceImage, @RequestParam("signatureImage") MultipartFile signatureImage) throws IOException {
        //查询成绩完成是否核实
        EllScoreVerify ellScoreVerify = scoreService.findVerify(ellScoreVerifyVo.getUserId(), ellScoreVerifyVo.getPlanId());
        if (ellScoreVerify != null) {
            return new Result(HttpStatus.SUCCESS);
        }
        Map<String, String> imageSaveFiles = scoreService.imageSaveFiles(faceImage, signatureImage);
        ellScoreVerifyVo.setFaceImageId(imageSaveFiles.get("faceImage"));
        ellScoreVerifyVo.setSignatureImageId(imageSaveFiles.get("signatureImage"));
        int i = scoreService.resultsVerify(ellScoreVerifyVo);
        if (i > 0) {
            //通知bi更改
            ellSocketServerFeign.sendToAll("修改");
            return new Result(HttpStatus.SUCCESS, "核实成功！");
        }
        return new Result(HttpStatus.ERROR, "核实失败有可能是项目还没测试完成!");
    }

    @ApiOperation("设备成绩核实")
    @PostMapping("/resultsVerifyFacility")
    @Transactional
    public Result resultsVerifyFacility(@RequestBody EllScoreVerifyVo ellScoreVerifyVo, @RequestParam("imageName") MultipartFile imageName, @RequestParam("imageFace") MultipartFile imageFace) throws IOException {
        //查询成绩完成是否核实
        EllScoreVerify ellScoreVerify = scoreService.findVerify(ellScoreVerifyVo.getUserId(), ellScoreVerifyVo.getPlanId());
        if (ellScoreVerify != null) {
            return new Result(HttpStatus.ALREADY_EXISTS, "已经核实过！");
        }
        int i = scoreService.resultsVerifyFacility(ellScoreVerifyVo, imageName, imageFace);
        if (i > 0) {
            //通知bi更改
            ellSocketServerFeign.sendToAll("修改");
            return new Result(HttpStatus.SUCCESS, "核实成功！");
        }
        return new Result(HttpStatus.ERROR, "核实失败有可能是项目还没测试完成!");
    }

    //成绩动态表格
    public String dynamicImportScoreExcel(List<EllPlanProjectChance> ellPlanProject, List<EllReturnScoreVo> ellReturnScoreVos, Map<Integer, List<EllPlanProjectChance>> integerListMap) {
        String file = IdUtils.fastSimpleUUID() + ".xlsx";
        Long sss = System.currentTimeMillis();
        //查询最终分数和判断用户的测试状态
        ellReturnScoreVos = scoreService.judgeProComplete(ellPlanProject.get(0).getPlanId(), ellReturnScoreVos);
        System.out.println("dynamicImportScoreExcelStart:" + sss);
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        //固定列的个数
        int subscript = 13;
//        int subscript=9;
        //score项目分数个数
        int scoreScript = 0;
        //后面固定列数
        int endScript = 3;
        Boolean isTrue = false;
//        int subscript=9;
        //存储每个成绩的列数（在数组中的下标）
        for (int i = 0; i < ellPlanProject.size(); i++) {
            //看前一个项目是否需要有分数第一个不用判断
            if (!isTrue && i != 0) {
                //不需要就获取项目列表上一个项目的下表加1
                map.put(ellPlanProject.get(i).getProId(), map.get(ellPlanProject.get(i - 1).getProId()) + 1);
            } else {
                map.put(ellPlanProject.get(i).getProId(), subscript + map.size() + i);
            }
            if (ellPlanProject.get(i).getProNameAbbreviation().equals("sg") || ellPlanProject.get(i).getProNameAbbreviation().equals("tz") || integerListMap.containsKey(ellPlanProject.get(i).getProId())) {
                isTrue = false;
            } else {
                isTrue = true;
                scoreScript++;
            }
        }
        Long ssss = System.currentTimeMillis();
        System.out.println("dynamicImportScoreExcelMap:" + (ssss - sss));
        try {
            // 所有行的集合
            List<List<Object>> list = new ArrayList<List<Object>>();
            for (int i = 0; i < ellReturnScoreVos.size(); i++) {
                // 第 n 行的数据
                //存储参数，位数是前面固定几位和项目的个数+项目的分数的个数+最后固定几列
//                Object[] row = new Object[9 + map.size() + 1];
                Object[] row = new Object[subscript + map.size() + 1 + scoreScript + endScript];
                //前两行是年级编码和班级编码是在国家体质平台上生成的
                //学生信息
                row[0] = ellReturnScoreVos.get(i).getSchoolName();
                //把年级放入
//                ellReturnScoreVos.get(i).setUserGrade(ellReturnScoreVos.get(i).getScore().get(0).getGrade());
                if (ellReturnScoreVos.get(i).getUserGrade().equals("未测试")) {
                    row[1] = "未测试";
                } else {
                    row[1] = GradeCalculate.gradeCode(ellReturnScoreVos.get(i).getUserGrade());
                }
                row[2] = ellReturnScoreVos.get(i).getSig();
                row[3] = ellReturnScoreVos.get(i).getDeptName();
                row[4] = ellReturnScoreVos.get(i).getStudentCode();
                row[5] = ellReturnScoreVos.get(i).getEthnic();
                row[6] = ellReturnScoreVos.get(i).getUserName();
                row[7] = ellReturnScoreVos.get(i).getUserSex();
                row[8] = ellReturnScoreVos.get(i).getUserBirth();
                row[9] = ellReturnScoreVos.get(i).getHomeAddress();
                row[10] = ellReturnScoreVos.get(i).getScore().get(0).getGrade();
                row[11] = GradeCalculate.gradeNumber(ellReturnScoreVos.get(i).getEnTime(), ellReturnScoreVos.get(i).getEnGrade());
                row[12] = ellReturnScoreVos.get(i).getSourceCode();
                for (int j = 0; j < ellReturnScoreVos.get(i).getScore().size(); j++) {
                    //判断成绩中是否有多个成绩
                    if (ellReturnScoreVos.get(i).getScore().get(j).getData() == null || ellReturnScoreVos.get(i).getScore().get(j).getData() == "") {
                        continue;
                    }
                  /*  //要注释这是把数子变整数
                    if (ellReturnScoreVos.get(i).getScore().get(j).getTestProject() == 36) {
                        ellReturnScoreVos.get(i).getScore().get(j).setData(Double.valueOf(ellReturnScoreVos.get(i).getScore().get(j).getData()).intValue()+"");
                    }
                    //要注释这是把数子保留两位
                    if (ellReturnScoreVos.get(i).getScore().get(j).getTestProject() == 56) {
                        ellReturnScoreVos.get(i).getScore().get(j).setData(Double.valueOf(ellReturnScoreVos.get(i).getScore().get(j).getData()).intValue()+"0");
                    }*/
                    Boolean datas = ellReturnScoreVos.get(i).getScore().get(j).getData().contains(",");
                    if (datas) {
                        //将成绩进行分割
                        String dataArray[] = ellReturnScoreVos.get(i).getScore().get(j).getData().split(",");
                        //将有子项目的成绩进行处理
                        List<FindScoreVo> findScoreVoList = DataProcessingUtil.scoreProcessingUtil(dataArray, ellReturnScoreVos.get(i).getScore().get(j), integerListMap);
                        ellReturnScoreVos.get(i).getScore().addAll(findScoreVoList);
                        //下面不执行
                        continue;
                    }
                    //判断计划是否有这个项目
                    if (map.get(ellReturnScoreVos.get(i).getScore().get(j).getTestProject()) != null) {
                        //把成绩和项目列数进行绑定
                        row[map.get(ellReturnScoreVos.get(i).getScore().get(j).getTestProject())] = ellReturnScoreVos.get(i).getScore().get(j).getData();

                        //排序
                        Arrays.sort(notHaveScorePro);
                        //判断：这是判断没有分数的项目中是否含有这个项目
                        if (Arrays.binarySearch(notHaveScorePro, ellReturnScoreVos.get(i).getScore().get(j).getTestProject()) < 0) {
                            row[map.get(ellReturnScoreVos.get(i).getScore().get(j).getTestProject()) + 1] = ellReturnScoreVos.get(i).getScore().get(j).getScore();
                        }

                    }

                }
                //最终分数
                Double score = Double.parseDouble(ellReturnScoreVos.get(i).getEndScore());
                row[row.length - 4] = ellReturnScoreVos.get(i).getEndScore();
                //标准赋分
                if (score != null && score != 0.00) {
                    if (score >= 80) {
                        row[row.length - 3] = 10;
                    } else if (score < 80 && score >= 75) {
                        row[row.length - 3] = 8.5;
                    } else if (score < 75 && score >= 70) {
                        row[row.length - 3] = 8;
                    } else if (score < 70 && score >= 65) {
                        row[row.length - 3] = 7.5;
                    } else if (score < 65 && score >= 60) {
                        row[row.length - 3] = 7;
                    } else if (score < 60) {
                        row[row.length - 3] = 5.5;
                    }
                }
                //用户成绩状态
                row[row.length - 2] = ellReturnScoreVos.get(i).getAppraisalStatus();
                System.out.println(row.length);
                list.add(Arrays.asList(row));
            }
            String path = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.Path;
            } else {
                path = LinuxSite.Path;
            }
            //生成文件路径
            PathGeneration.createPath(path + file);
            ExcelWriter excelWriter = EasyExcelFactory.getWriter(new FileOutputStream(path + file));
            // 表单
            Sheet sheet = new Sheet(1, 0);
            sheet.setSheetName("模板");
            // 创建一个表格
            Table table = new Table(1);
            // 动态添加 表头 headList --> 所有表头行集合
            List<List<String>> headList = new ArrayList<List<String>>();
            List<String> excelTitle = new ArrayList<>();
            excelTitle.add(0, "学校名称");
//            excelTitle.add(1, "年级编码");
            excelTitle.add(1, "年级编号");
//            excelTitle.add(2, "班级编码");
            excelTitle.add(2, "班级编号");
            excelTitle.add(3, "班级名称");
            excelTitle.add(4, "学籍号");
            excelTitle.add(5, "民族代码");
            excelTitle.add(6, "姓名");
            excelTitle.add(7, "性别");
            excelTitle.add(8, "出生日期");
            excelTitle.add(9, "家庭住址");
            excelTitle.add(10, "年级");
            excelTitle.add(11, "当前年级");
            excelTitle.add(12, "生源地代码");
            for (int i = 0; i < ellPlanProject.size(); i++) {
                excelTitle.add(map.get(ellPlanProject.get(i).getProId()), ellPlanProject.get(i).getProName());
                //在每项成绩后面加上分数列，这几项没有分数所以不用加
                if (!ellPlanProject.get(i).getProNameAbbreviation().equals("sg") && !ellPlanProject.get(i).getProNameAbbreviation().equals("tz") && !integerListMap.containsKey(ellPlanProject.get(i).getProId())) {
                    excelTitle.add(map.get(ellPlanProject.get(i).getProId()) + 1, "分数");
                }
            }
            excelTitle.add(excelTitle.size(), "最终分数");
            excelTitle.add(excelTitle.size(), "标准赋分");
            excelTitle.add(excelTitle.size(), "分数状态");
            //判断是否有视力这个项目
           /* if (map.containsKey(43)) {

            }*/
            // 第 n 行 的表头
            for (int i = 0; i < excelTitle.size(); i++) {
                List<String> headTitle0 = new ArrayList<String>();
                //在生成表格的title的时候相同的list在table模板中有多少个就会占多少个列
                //例如list1向表头list中添加了两次就会在excel中占两列
                headTitle0.add(excelTitle.get(i));
                headList.add(headTitle0);
                table.setHead(headList);

            }
            excelWriter.write(list, sheet, table);
            // 记得 释放资源
            excelWriter.finish();
            System.out.println("ok");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Long ss = System.currentTimeMillis();
        System.out.println("dynamicImportScoreExcelEnd:" + (ss - sss));
        return file;
    }

    @ApiOperation("查询考试肢体错误截图")
    @PostMapping("/findErrorImage")
    public Result findErrorImage(String planId, String userId) {
        List<ErrorScreenShotVo> screenShotVoList = scoreService.findErrorImage(planId, userId);
        boolean isHasError = false;
        if (screenShotVoList.size() > 0) {
            isHasError = true;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("screenShotVoList", screenShotVoList);
        jsonObject.put("isHasError", isHasError);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);

    }

    @ApiOperation("修改成绩")
    @PostMapping("/updateScore")
    public Result updateScore(@RequestBody EllChangeScoreVo ellChangeScoreVo) throws IOException {
        //查询是否认证核实过
        EllScoreVerify ellScoreVerify = scoreService.findVerify(ellChangeScoreVo.getUserId(), ellChangeScoreVo.getPlanId());
        if (ellScoreVerify == null) {
            int i = scoreService.updateScore(ellChangeScoreVo);
            if (i > 0) {
                //通知bi更改
                ellSocketServerFeign.sendToAll("修改");
                return new Result(HttpStatus.SUCCESS, "修改成功");
            } else {
                return new Result(HttpStatus.NOT_FOUND, "此学生没有该成绩，无法修改，请测试以后在进行修改");
            }
        }
        return new Result(HttpStatus.NO_MODIFICATION_ALLOWED, "成绩已核实，不能修改");
    }

    @ApiOperation("查看视频")
    @GetMapping("/selGradeVideo")
    public Result selGradeVideo(String planId, String userId, int proId) throws IOException {
        return new Result(HttpStatus.SUCCESS, "成功", scoreService.selGradeVideo(planId, userId, proId));
    }

    @ApiOperation("随机生成成绩")
    @GetMapping("/randomGeneration")
    public Result randomGeneration(String planId, String orgId) throws IOException {
        List<EllUser> userListBoy = scoreService.selUser(orgId, "1");
        List<EllUser> userListGirl = scoreService.selUser(orgId, "2");
        List<EllTestProject> projectListBoy = scoreService.selPro(planId, "1");
        List<EllTestProject> projectListGirl = scoreService.selPro(planId, "2");
        /*List<EllUser> userList = scoreService.selScoreless60(planId, "1");
        for (int i = 0; i < userList.size(); i++) {
            //
            if (userList.get(i).getUserSex().equals(1)) {
                EllHistoryVo ellHistoryVo1 = new EllHistoryVo();
                ellHistoryVo1.setUserId(userList.get(i).getId());
                ellHistoryVo1.setPlanId(planId);
                ellHistoryVo1.setTestProject("39");
                ellHistoryVo1.setCheckInTimestamp(new Date().getTime());
                ellHistoryVo1.setEndTimestamp(new Date().getTime());
                ellHistoryVo1.setIsRetest(1);
                ellHistoryVo1.setProjectName("引体向上");
                ellHistoryVo1.setUserSex(userList.get(i).getUserSex() + "");
                ellHistoryVo1.setEndTime(CreateTime.getTime());
                ellHistoryVo1.setUserOrgId(userList.get(i).getOrgId());
                RandomGeneration.proYtxs(userList.get(i), null, ellHistoryVo1);
                submitScores(ellHistoryVo1);
            } else {
                EllHistoryVo ellHistoryVo = new EllHistoryVo();
                ellHistoryVo.setUserId(userList.get(i).getId());
                ellHistoryVo.setPlanId(planId);
                ellHistoryVo.setTestProject("37");
                ellHistoryVo.setCheckInTimestamp(new Date().getTime());
                ellHistoryVo.setEndTimestamp(new Date().getTime());
                ellHistoryVo.setIsRetest(1);
                ellHistoryVo.setProjectName("立定跳远");
                ellHistoryVo.setUserSex(userList.get(i).getUserSex() + "");
                ellHistoryVo.setEndTime(CreateTime.getTime());
                ellHistoryVo.setUserOrgId(userList.get(i).getOrgId());
                RandomGeneration.proLdty(userList.get(i), null, ellHistoryVo);
                submitScores(ellHistoryVo);
            }
        }*/

        //男
        for (int i = 0; i < projectListBoy.size(); i++) {
            for (int j = 0; j < userListBoy.size(); j++) {
                List<EllTestHistory> testHistoryList = scoreService.selHistoryById(planId, userListBoy.get(j).getId(), "35");
               /* if (testHistoryList != null && testHistoryList.size() > 0) {
                    EllHistoryVo ellHistoryVo = new EllHistoryVo();
                    ellHistoryVo.setUserId(userListBoy.get(j).getId());
                    ellHistoryVo.setPlanId(planId);
                    ellHistoryVo.setTestProject(String.valueOf(projectListBoy.get(i).getId()));
                    ellHistoryVo.setCheckInTimestamp(new Date().getTime());
                    ellHistoryVo.setEndTimestamp(new Date().getTime());
                    ellHistoryVo.setIsRetest(1);
                    ellHistoryVo.setProjectName(projectListBoy.get(i).getProName());
                    ellHistoryVo.setUserSex(userListBoy.get(j).getUserSex() + "");
                    ellHistoryVo.setEndTime(CreateTime.getTime());
                    ellHistoryVo.setUserOrgId(userListBoy.get(j).getOrgId());
                    DecimalFormat dataFormat = new DecimalFormat("0.0");


                    String [] aa=testHistoryList.get(0).getData().split(",");
                    Double BMI = Double.parseDouble(dataFormat.format(Double.parseDouble(aa[1]) / Math.pow(Double.parseDouble(aa[0]) / 100.0, 2)));
                    Map<String, List<EllTestData>> testData = new HashMap<>();
                    ellHistoryVo.setData(aa[0]+ "," + aa[1] + "," + BMI);
                    List<EllTestData> ellTestDataList = new ArrayList<>();
                    EllTestData ellTestDataHeight = new EllTestData();
                    ellTestDataHeight.setData(aa[0]);
                    ellTestDataHeight.setIsRetest("1");
                    ellTestDataHeight.setRemark("身高第1次");
                    ellTestDataHeight.setTimestamp(new Date().getTime());
                    ellTestDataList.add(ellTestDataHeight);
                    EllTestData ellTestDataWeight = new EllTestData();
                    ellTestDataWeight.setData(aa[1]);
                    ellTestDataWeight.setIsRetest("1");
                    ellTestDataWeight.setRemark("体重第1次");
                    ellTestDataWeight.setTimestamp(new Date().getTime());
                    ellTestDataList.add(ellTestDataWeight);
                    EllTestData ellTestDataBmi = new EllTestData();
                    ellTestDataBmi.setData(String.valueOf(BMI));
                    ellTestDataBmi.setIsRetest("1");
                    ellTestDataBmi.setRemark("BMI第1次");
                    ellTestDataBmi.setTimestamp(new Date().getTime());
                    ellTestDataList.add(ellTestDataBmi);
                    testData.put("1", ellTestDataList);
                    ellHistoryVo.setTestData(testData);
                    submitScores(ellHistoryVo);
                }*/
                if ((testHistoryList == null || testHistoryList.size() <= 0) && userListBoy.get(j).getAppHeight() != null && userListGirl.get(j).getWxOpenId() != null && !userListGirl.get(j).getWxOpenId().equals("")) {
                    EllHistoryVo ellHistoryVo = new EllHistoryVo();
                    ellHistoryVo.setUserId(userListBoy.get(j).getId());
                    ellHistoryVo.setPlanId(planId);
                    ellHistoryVo.setTestProject(String.valueOf(projectListBoy.get(i).getId()));
                    ellHistoryVo.setCheckInTimestamp(new Date().getTime());
                    ellHistoryVo.setEndTimestamp(new Date().getTime());
                    ellHistoryVo.setIsRetest(1);
                    ellHistoryVo.setProjectName(projectListBoy.get(i).getProName());
                    ellHistoryVo.setUserSex(userListBoy.get(j).getUserSex() + "");
                    ellHistoryVo.setEndTime(CreateTime.getTime());
                    ellHistoryVo.setUserOrgId(userListBoy.get(j).getOrgId());
                    switch (projectListBoy.get(i).getId()) {
                        case 35:
                            System.out.println(i);
                            RandomGeneration.heightAndWeight(userListBoy.get(j), projectListBoy.get(i), ellHistoryVo);
                            submitScores(ellHistoryVo);
                            break;
                        case 56:
                            System.out.println(i);
                            RandomGeneration.pro50(userListBoy.get(j), projectListBoy.get(i), ellHistoryVo);
                            Result result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 41:
                            System.out.println(i);
                            RandomGeneration.pro1000(userListBoy.get(j), projectListBoy.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 37:
                            System.out.println(i);
                            RandomGeneration.proLdty(userListBoy.get(j), projectListBoy.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 36:
                            System.out.println(i);
                            RandomGeneration.proFeihuoliang(userListBoy.get(j), projectListBoy.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 38:
                            System.out.println(i);
                            RandomGeneration.proTqq(userListBoy.get(j), projectListBoy.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 39:
                            System.out.println(i);
                            RandomGeneration.proYtxs(userListBoy.get(j), projectListBoy.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                    }
                }
            }
        }
        System.out.println("男生完成");
        //女
        for (int i = 0; i < projectListGirl.size(); i++) {
            for (int j = 0; j < userListGirl.size(); j++) {
                List<EllTestHistory> testHistoryList = scoreService.selHistoryById(planId, userListGirl.get(j).getId(), String.valueOf(projectListGirl.get(i).getId()));
               /* if (testHistoryList != null && testHistoryList.size() > 0) {
                    EllHistoryVo ellHistoryVo = new EllHistoryVo();
                    ellHistoryVo.setUserId(userListGirl.get(j).getId());
                    ellHistoryVo.setPlanId(planId);
                    ellHistoryVo.setTestProject(String.valueOf(projectListGirl.get(i).getId()));
                    ellHistoryVo.setCheckInTimestamp(new Date().getTime());
                    ellHistoryVo.setEndTimestamp(new Date().getTime());
                    ellHistoryVo.setIsRetest(1);
                    ellHistoryVo.setProjectName(projectListGirl.get(i).getProName());
                    ellHistoryVo.setUserSex(userListGirl.get(j).getUserSex() + "");
                    ellHistoryVo.setEndTime(CreateTime.getTime());
                    ellHistoryVo.setUserOrgId(userListGirl.get(j).getOrgId());
                    DecimalFormat dataFormat = new DecimalFormat("0.0");

                    String [] aa=testHistoryList.get(0).getData().split(",");
                    Double BMI = Double.parseDouble(dataFormat.format(Double.parseDouble(aa[1]) / Math.pow(Double.parseDouble(aa[0]) / 100.0, 2)));
                    Map<String, List<EllTestData>> testData = new HashMap<>();
                    ellHistoryVo.setData(aa[0]+ "," + aa[1] + "," + BMI);
                    List<EllTestData> ellTestDataList = new ArrayList<>();
                    EllTestData ellTestDataHeight = new EllTestData();
                    ellTestDataHeight.setData(aa[0]);
                    ellTestDataHeight.setIsRetest("1");
                    ellTestDataHeight.setRemark("身高第1次");
                    ellTestDataHeight.setTimestamp(new Date().getTime());
                    ellTestDataList.add(ellTestDataHeight);
                    EllTestData ellTestDataWeight = new EllTestData();
                    ellTestDataWeight.setData(aa[1]);
                    ellTestDataWeight.setIsRetest("1");
                    ellTestDataWeight.setRemark("体重第1次");
                    ellTestDataWeight.setTimestamp(new Date().getTime());
                    ellTestDataList.add(ellTestDataWeight);
                    EllTestData ellTestDataBmi = new EllTestData();
                    ellTestDataBmi.setData(String.valueOf(BMI));
                    ellTestDataBmi.setIsRetest("1");
                    ellTestDataBmi.setRemark("BMI第1次");
                    ellTestDataBmi.setTimestamp(new Date().getTime());
                    ellTestDataList.add(ellTestDataBmi);
                    testData.put("1", ellTestDataList);
                    ellHistoryVo.setTestData(testData);
                    submitScores(ellHistoryVo);
                }*/
                if ((testHistoryList == null || testHistoryList.size() <= 0) && userListGirl.get(j).getWxOpenId() != null && !userListGirl.get(j).getWxOpenId().equals("")) {
                    EllHistoryVo ellHistoryVo = new EllHistoryVo();
                    ellHistoryVo.setUserId(userListGirl.get(j).getId());
                    ellHistoryVo.setPlanId(planId);
                    ellHistoryVo.setTestProject(String.valueOf(projectListGirl.get(i).getId()));
                    ellHistoryVo.setCheckInTimestamp(new Date().getTime());
                    ellHistoryVo.setEndTimestamp(new Date().getTime());
                    ellHistoryVo.setIsRetest(1);
                    ellHistoryVo.setProjectName(projectListGirl.get(i).getProName());
                    ellHistoryVo.setUserSex(userListGirl.get(j).getUserSex() + "");
                    ellHistoryVo.setEndTime(CreateTime.getTime());
                    ellHistoryVo.setUserOrgId(userListGirl.get(j).getOrgId());
                    switch (projectListGirl.get(i).getId()) {
                        case 35:
                            System.out.println(i);
                            RandomGeneration.heightAndWeight(userListGirl.get(j), projectListGirl.get(i), ellHistoryVo);
                            submitScores(ellHistoryVo);
                            break;
                        case 56:
                            System.out.println(i);
                            RandomGeneration.pro50(userListGirl.get(j), projectListGirl.get(i), ellHistoryVo);
                            submitScores(ellHistoryVo);
                            break;
                        case 53:
                            System.out.println(i);
                            RandomGeneration.pro800(userListGirl.get(j), projectListGirl.get(i), ellHistoryVo);
                            Result result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 37:
                            System.out.println(i);
                            RandomGeneration.proLdty(userListGirl.get(j), projectListGirl.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 36:
                            System.out.println(i);
                            RandomGeneration.proFeihuoliang(userListGirl.get(j), projectListGirl.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 38:
                            System.out.println(i);
                            RandomGeneration.proTqq(userListGirl.get(j), projectListGirl.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                        case 42:
                            System.out.println(i);
                            RandomGeneration.proYwqz(userListGirl.get(j), projectListGirl.get(i), ellHistoryVo);
                            result = submitScores(ellHistoryVo);
                            System.out.println(result);
                            break;
                    }
                }
            }
        }
        System.out.println("女生完成");
        return new Result();
    }

    @ApiOperation("生成最终成绩")
    @GetMapping("/creatEndScore")
    public Result creatEndScore(String planId, String orgId) throws IOException {
        List<EllUser> userListBoy = scoreService.selUser(orgId, "1");
        List<EllUser> userListGirl = scoreService.selUser(orgId, "2");
        for (int j = 0; j < userListBoy.size(); j++) {
            FindScoreConditionVo findScoreConditionVo = new FindScoreConditionVo();
            findScoreConditionVo.setPlanId(planId);
            findScoreConditionVo.setUserId(userListBoy.get(j).getId());
            //查询学生成绩
            List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, null);
            //生成最终分数
            scoreService.createEndScore(findScoreConditionVo, findUser);
        }
        for (int j = 0; j < userListGirl.size(); j++) {
            FindScoreConditionVo findScoreConditionVo = new FindScoreConditionVo();
            findScoreConditionVo.setPlanId(planId);
            findScoreConditionVo.setUserId(userListGirl.get(j).getId());
            //查询学生成绩
            List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, null);
            //生成最终分数
            scoreService.createEndScore(findScoreConditionVo, findUser);
        }
        return null;
    }

    @ApiOperation("某一项目的规则更改重新录入")
    @GetMapping("/updateProScore")
    public Result updateProScore(String planId, String orgId, Integer proId) throws IOException {
        List<EllTestHistory> list = scoreService.selHistoryById(planId, null, String.valueOf(proId));
        for (int i = 0; i < list.size(); i++) {
            EllHistoryVo ellHistoryVo = new EllHistoryVo();
            ellHistoryVo.setUserId(list.get(i).getUserId());
            ellHistoryVo.setPlanId(list.get(i).getPlanId());
            ellHistoryVo.setCreateByExaminer(list.get(i).getCreateByExaminer());
            ellHistoryVo.setCreateByDevice(list.get(i).getCreateByDevice());
            Map<String, List<EllTestData>> testData = new HashMap<>();
            List<EllTestData> ellTestDataList = new ArrayList<>();
            //多个成绩
            String data[] = list.get(i).getData().split(",");
            for (int j = 0; j < data.length; j++) {
                EllTestData ellTestData = new EllTestData();
                //将分钟转为秒
                if (data[j].contains("'")) {
                    String minutes[] = data[j].split("'");
                    //去掉字符串的符号
                    ellTestData.setData(String.valueOf(Integer.parseInt(minutes[0]) * 60 + Integer.valueOf(minutes[1].replace("\"", ""))));
                } else {
                    ellTestData.setData(data[j]);
                }
                ellTestData.setIsRetest("2");
                ellTestData.setTimestamp(new Date().getTime());
                ellTestData.setScore("");
                ellTestDataList.add(ellTestData);
            }
            testData.put("1", ellTestDataList);
            ellHistoryVo.setTestData(testData);
            ellHistoryVo.setUserSex(String.valueOf(list.get(i).getUserSex()));
            ellHistoryVo.setUserOrgId(list.get(i).getOrgId());
            ellHistoryVo.setTestProject(String.valueOf(list.get(i).getTestProject()));
            ellHistoryVo.setProjectName(list.get(i).getProjectName());
            ellHistoryVo.setCheckInTimestamp(new Date().getTime());
            ellHistoryVo.setEndTimestamp(new Date().getTime());
            ellHistoryVo.setEndTime(CreateTime.getTime());
            ellHistoryVo.setIsRetest(2);
            scoreService.addStudentScoreMapper(ellHistoryVo);
            FindScoreConditionVo findScoreConditionVo = new FindScoreConditionVo();
            findScoreConditionVo.setPlanId(ellHistoryVo.getPlanId());
            findScoreConditionVo.setUserId(ellHistoryVo.getUserId());
            //查询学生成绩
            List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, null);
            //生成最终分数
            scoreService.createEndScore(findScoreConditionVo, findUser);
        }

        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("生成excel考试")
    @PostMapping("/createExamExcel")
    public Result createExamExcel(@RequestBody EllCreateExamExcelVo excelVo) throws FileNotFoundException {
        //查询计划项目必选个数男
        int numBoy = planService.findPro(excelVo.getPlanId(), "1");
        //查询计划项目必选个数女
        int numGirl = planService.findPro(excelVo.getPlanId(), "2");
        //查询计划结束没有
        List<EllPlan> ellPlanList = planService.findPlan(excelVo.getPlanId(), null, null, null);
        //成绩
        List<EllScoreHistoryDto> list = scoreService.selEndAndProNameScore(excelVo.getPlanId(), excelVo.getAccountId(), excelVo.getOrgId());
        //最终生成的项目个数
        int num = numGirl + ellPlanList.get(0).getCustomizeTheNumber();
        if (numBoy >= numGirl) {
            num = numBoy + ellPlanList.get(0).getCustomizeTheNumber();
        }
        //表头
        List<String> headTables = new ArrayList<>();
        headTables.add("编号");
        headTables.add("准考证号");
        headTables.add("姓名");
        headTables.add("性别");
        headTables.add("班级");
        headTables.add("总分");
        for (int i = 0; i < num; i++) {
            headTables.add("项目");
            headTables.add("成绩");
            headTables.add("分数");
        }
        //内容
        Map<String, String> contentMap = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            if (contentMap.containsKey(list.get(i).getId())) {
                contentMap.put(list.get(i).getId(), contentMap.get(list.get(i).getId()) + list.get(i).toMerge().replace("null", ""));
            } else {
                contentMap.put(list.get(i).getId(), list.get(i).toString().replace("null", ""));
            }
        }
        //把map转为list
        List<String> contentItem = contentMap.values().stream().collect(Collectors.toList());
        String url = ExcelUtil.createExcel(headTables, contentItem);
        return new Result(HttpStatus.SUCCESS, "成功", url);
    }

    @Autowired
    private ScoreController scoreController;

    @ApiOperation("导入成绩")
    @PostMapping("/importScore")
    @Transactional
    public Result importScore(@RequestParam("scoreFile") MultipartFile scoreFile, String planId) throws IOException {
        ScoreListener scoreListener = new ScoreListener(scoreController, ellUserService, scoreService, planId);
        EasyExcel.read(scoreFile.getInputStream(), scoreListener).sheet().doRead();
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("导入成绩模板")
    @PostMapping("/importScoreModel")
    public Result importScoreModel(String planId) throws IOException {
        List<EllTestProject> ellTestProjectList = scoreService.selPro(planId, "1,2");
        List<String> stringList = new ArrayList<>();
        stringList.add("姓名");
        stringList.add("学籍号");
        stringList.add("性别");
        for (int i = 0; i < ellTestProjectList.size(); i++) {
            stringList.add(ellTestProjectList.get(i).getProName());
        }
        List<String> content = new ArrayList<>();
        content.add("测试学生");
        content.add("xxxxx");
        content.add("1");
        String URL = ExcelUtil.createExcel(stringList, content);
        return new Result(HttpStatus.SUCCESS, "成功", URL);
    }

    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        stringList.add("35");
        stringList.add("56");
        stringList.add("77");
        System.out.println(stringList.contains("77"));

    }
}
