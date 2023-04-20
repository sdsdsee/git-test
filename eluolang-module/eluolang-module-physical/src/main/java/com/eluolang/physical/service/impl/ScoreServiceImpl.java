package com.eluolang.physical.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.physical.dto.EllScoreHistoryDto;
import com.eluolang.physical.dto.OneUserScoreDto;
import com.eluolang.physical.feign.ELLSocketServerFeign;
import com.eluolang.physical.mapper.EllUserMapper;
import com.eluolang.physical.mapper.PlanMapper;
import com.eluolang.physical.mapper.RulesMapper;
import com.eluolang.physical.mapper.ScoreMapper;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.*;
import com.eluolang.physical.util.*;
import net.logstash.logback.encoder.org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

@Service
@Transactional
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private RulesService rulesService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PhysicalDataService physicalDataService;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private EllUserMapper ellUserMapper;
    //这些项目数越小分越高
    private final static int[] toTheTrendOfSmallProject = {40, 41, 45, 46, 50, 52, 53, 54, 55, 56, 62, 63, 64, 75, 76, 78, 79, 82, 83, 84, 85};
    //成绩要转换为分钟的项目
    private final static String[] long_distance_race = {"41", "53", "75", "55", "79", "80", "83", "84", "85"};
    @Autowired
    private ELLSocketServerFeign ellSocketServerFeign;

    @Transactional
    @Override
    public int addStudentScoreMapper(EllHistoryVo ellHistoryVo) {
        ellHistoryVo.setId(IdUtils.fastSimpleUUID());
        ellHistoryVo.setCreateTime(CreateTime.TimestampTransitionDate(String.valueOf(ellHistoryVo.getCheckInTimestamp())));
        EllHistoryVo ellHistoryVoFind = new EllHistoryVo();
        ellHistoryVoFind.setPlanId(ellHistoryVo.getPlanId());
        ellHistoryVoFind.setTestProject(ellHistoryVo.getTestProject());
        ellHistoryVoFind.setUserId(ellHistoryVo.getUserId());
        //ellHistoryVo.setCreateTime(CreateTime.getTime());
        //初始化是否重测
        ellHistoryVo.setIsRetest(1);
        //查询该学生
        List<EllReturnScoreVo> ellReturnScoreVos = ellUserMapper.findUserById(ellHistoryVo.getUserId());
        if (ellReturnScoreVos.size() <= 0) {
            return 0;
        }
        //入学年级
        int enGrade = ellReturnScoreVos.get(0).getEnGrade();
        //入学时间
        String enTime = ellReturnScoreVos.get(0).getEnTime();
        //计算入学时间
        String gradeName = calculateGrade(enTime, enGrade);
        ellHistoryVo.setUserGrade(gradeName);
        //去数据查看是否已经存在这个次成绩
        List<EllTestHistory> list = scoreMapper.findUserHistory(ellHistoryVoFind);
        String isRetest = "1";
        JudgmentResultVo judgmentResultVo;
        //判断是否有历史成绩
        Map<String, Object> testDataMap = new HashMap<>();
        if (list.size() > 0) {
            testDataMap = JSONArray.parseObject(list.get(0).getTesData());
            isRetest = "2";
        }
        //这测试时间大于上次测试时间将数据库的历史成绩与新成绩进行拼接保存
        if (list.size() == 0 || (list.size() > 0 && CreateTime.dateTransitionTimestamp(list.get(0).getUpdateTime()) < ellHistoryVo.getCheckInTimestamp())) {
            for (int i = 1; i <= ellHistoryVo.getTestData().size(); i++) {
                ellHistoryVo.getTestData().get(String.valueOf(i)).size();
                ellHistoryVo.getTestData().get(String.valueOf(i)).get(0).setIsRetest(isRetest);
                ellHistoryVo.setIsRetest(Integer.parseInt(isRetest));
                //一次成绩有多中成绩的情况下
                for (int j = 0; j < ellHistoryVo.getTestData().get(String.valueOf(i)).size(); j++) {
                    //设置为重测
                    ellHistoryVo.getTestData().get(String.valueOf(i)).get(j).setIsRetest(isRetest);
                    //设置每次成绩的备注
                    ellHistoryVo.getTestData().get(String.valueOf(i)).get(j).setRemark(ellHistoryVo.getTestData().get(String.valueOf(i)).get(j).getRemark() + "第" + (testDataMap.size() + 1) + "次");
                }
                testDataMap.put((testDataMap.size() + 1) + "", JSONArray.toJSON(ellHistoryVo.getTestData().get(String.valueOf(i))));
            }
        }
        if (list.size() > 0 || isRetest.equals("2")) {
            //判断上传的检录时间大于这次上传成绩的检录时间，大于就是不是最后一次测试的成绩就不用计算分数但是需要重新排序
            testDataMap = dataAssembly(list, ellHistoryVo, testDataMap, isRetest);
            //list.get(0).setScore(list.get(0).getScore() + "," + ellHistoryVo.getScore());
            UpdateScoreVo updateScoreVo = new UpdateScoreVo();
            //将数据
            updateScoreVo.setId(list.get(0).getId());
            //设置目前成绩为重测
            updateScoreVo.setIsRetest(2);
            //拓展数据
            //将以前有的拓展数据转为list并在基础上进行附加新一次的拓展数据
            //由于成绩是会通过测试时间排序的而拓展数据不会所有就会出现拓展数据和成绩不相匹配的情况需要后期优化
            if (list.get(0).getExtraData() != null && !list.get(0).getExtraData().equals("")) {
                List<String> extraDataList = JSONArray.parseArray(JSONArray.toJSON(list.get(0).getExtraData()).toString(), String.class);
                extraDataList.add(ellHistoryVo.getExtraData());
                updateScoreVo.setExtraData(JSONArray.toJSON(extraDataList).toString());
            }
            //如果上传的检录时间小于这次上传的检录时间就重新计算分数，没有就更新历史成绩记录
            if (CreateTime.dateTransitionTimestamp(list.get(0).getUpdateTime()) < ellHistoryVo.getCheckInTimestamp()) {
                judgmentResultVo = judgmentResult(testDataMap, ellHistoryVo.getUserId(), ellHistoryVo.getPlanId(), ellHistoryVo.getTestProject(), "2", ellHistoryVo.getTestData().size(), Integer.valueOf(ellHistoryVo.getUserSex()), gradeName);
                //视力没有分数
                if (judgmentResultVo.getScore() < 0) {
                    updateScoreVo.setScore("-");
                } else {
                    updateScoreVo.setScore(String.valueOf(judgmentResultVo.getScore()));
                }
                updateScoreVo.setData(String.valueOf(judgmentResultVo.getData()));
                updateScoreVo.setTestDateJson(judgmentResultVo.getTestDataMap());
                updateScoreVo.setComment(judgmentResultVo.getComment());
                updateScoreVo.setCheckInTimestamp(ellHistoryVo.getCheckInTimestamp());
                updateScoreVo.setEndTimestamp(ellHistoryVo.getEndTimestamp());
                updateScoreVo.setTimeSpent(ellHistoryVo.getEndTimestamp() - ellHistoryVo.getCheckInTimestamp());
                return scoreMapper.updateScore(updateScoreVo, CreateTime.TimestampTransitionDate(String.valueOf(ellHistoryVo.getCheckInTimestamp())));
            }
            //更新历史成绩记录
            updateScoreVo.setTestDateJson(JSONArray.toJSON(testDataMap).toString());
            return scoreMapper.updateScore(updateScoreVo, list.get(0).getUpdateTime());
        }
        //把map转为json字符传
        String testDataJson = JSONArray.toJSON(ellHistoryVo.getTestData()).toString();
        //把转为json字符传的数据再转为map(String，Object)类型;
        judgmentResultVo = judgmentResult(JSONArray.parseObject(testDataJson), ellHistoryVo.getUserId(), ellHistoryVo.getPlanId(), ellHistoryVo.getTestProject(), "1", ellHistoryVo.getTestData().size(), Integer.valueOf(ellHistoryVo.getUserSex()), gradeName);
        //视力没有分数
        if (judgmentResultVo.getScore() < 0) {
            ellHistoryVo.setScore("-");
        } else {
            ellHistoryVo.setScore(String.valueOf(judgmentResultVo.getScore()));
        }
        ellHistoryVo.setData(String.valueOf(judgmentResultVo.getData()));
        ellHistoryVo.setComment(judgmentResultVo.getComment());
       /* if (ellHistoryVo.getTestData().get((1 + "")).size() > 2) {
            //判断视力等级--正常/近视/远视
        }*/
        //把转为json字符串的数据存入数据库
        ellHistoryVo.setTestDataJson(judgmentResultVo.getTestDataMap());
        //将拓展单条数据变为数组
        List<String> extraDataList = new ArrayList<>();
        extraDataList.add(ellHistoryVo.getExtraData());
        ellHistoryVo.setExtraData(JSONArray.toJSON(extraDataList).toString());
        ellHistoryVo.setTimeSpent(ellHistoryVo.getEndTimestamp() - ellHistoryVo.getCheckInTimestamp());
        return scoreMapper.addStudentScoreMapper(ellHistoryVo);
    }
/*
    public static void main(String[] args) {
        List<String> extraDataList = new ArrayList<>();
        extraDataList.add(null);
        extraDataList.add("222");
        System.out.println(JSONArray.toJSON(extraDataList).toString());
        List<String> listObjectFour = JSONArray.parseArray(JSONArray.toJSON(extraDataList).toString(), String.class);
        System.out.println(listObjectFour);
    }*/

    //判断上传的检录时间大于这次上传成绩的检录时间，大于就是不是最后一次测试的成绩就不用计算分数但是需要重新排序
    public Map<String, Object> dataAssembly(List<EllTestHistory> list, EllHistoryVo ellHistoryVo, Map<String, Object> testDataMap, String isRetest) {
        String s = CreateTime.TimestampTransitionDate(String.valueOf(ellHistoryVo.getCheckInTimestamp()));
        if (CreateTime.dateTransitionTimestamp(list.get(0).getUpdateTime()) > ellHistoryVo.getCheckInTimestamp()) {
            Map<String, List<EllTestData>> stringListHashMap = new HashMap<>();
            int keyNum = 1;
            //冒泡排序
            for (String key : testDataMap.keySet()) {
                List<EllTestData> testData = JSONArray.parseArray(testDataMap.get(key).toString(), EllTestData.class);
                //小于就正常加入，大于就插入后面的顺移1
                if (testData.get(0).getTimestamp() > ellHistoryVo.getCheckInTimestamp() && Integer.parseInt(key) == keyNum) {
                    for (int i = 1; i <= ellHistoryVo.getTestData().size(); i++) {
                        ellHistoryVo.getTestData().get(String.valueOf(i)).size();
                        ellHistoryVo.getTestData().get(String.valueOf(i)).get(0).setIsRetest(isRetest);
                        //一次成绩有多中成绩的情况下
                        for (int j = 0; j < ellHistoryVo.getTestData().get(String.valueOf(i)).size(); j++) {
                            //设置为重测
                            if (stringListHashMap.size() == 0) {
                                isRetest = "1";
                            }
                            ellHistoryVo.getTestData().get(String.valueOf(i)).get(j).setIsRetest(isRetest);
                            //设置每次成绩的备注
                            ellHistoryVo.getTestData().get(String.valueOf(i)).get(j).setRemark(ellHistoryVo.getTestData().get(String.valueOf(i)).get(j).getRemark() + "第" + (stringListHashMap.size() + 1) + "次");
                        }
                        stringListHashMap.put(String.valueOf(keyNum), ellHistoryVo.getTestData().get(String.valueOf(i)));
                        //顺移1
                        keyNum = stringListHashMap.size() + 1;
                    }
                }
                int finalKeyNum = keyNum;
                testData.stream().forEach(data -> {
                    StringBuffer stringBuffer = new StringBuffer(data.getRemark());
                    stringBuffer.replace(stringBuffer.indexOf("第") + 1, stringBuffer.indexOf("次"), String.valueOf(finalKeyNum));
                    if (finalKeyNum == 1) {
                        data.setIsRetest("1");
                    } else {
                        data.setIsRetest("2");
                    }
                    data.setRemark(stringBuffer.toString());
                    stringBuffer.setLength(0);
                });
                stringListHashMap.put(String.valueOf(keyNum), testData);
                keyNum = stringListHashMap.size() + 1;
            }
            testDataMap = JSONArray.parseObject(JSONArray.toJSON(stringListHashMap).toString());
        }
        return testDataMap;
    }

    @Transactional
    public String calculateGrade(String enTime, int enGrade) {
        String gradeName = "";
        //计算年级
        //去掉时间的分隔符
        //入学时间
        String[] enTimes = enTime.split("-");
        StringBuffer stringBuffer = new StringBuffer(enTimes[0] + enTimes[1]);
        int year = 0;
        //判断是不是学生-1不是学生
        if (enGrade != -1) {
            String[] nowTime = new String[0];
            try {
                nowTime = CreateTime.timeYear().split("-");
//                nowTime = "2022-09".split("-");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            StringBuffer stringBufferNow = new StringBuffer(nowTime[0] + nowTime[1]);
//            System.out.println(enTime);
            year = (Integer.parseInt(nowTime[0]) - Integer.parseInt(enTimes[0]));
            //清空
            stringBufferNow.setLength(0);
            stringBuffer.setLength(0);
            //默认
            enGrade = enGrade + year;
            //升级月份
            int upgradePoints = 8;
            //入学时间大于8月就减去一年
            if (year > 0 && Integer.parseInt(enTimes[1]) >= upgradePoints) {
                enGrade = enGrade - 1;
            }
            //只要现在的时间大于8月就加1年级，首先不是今年
            if (year > 0 && Integer.parseInt(nowTime[1]) >= upgradePoints) {
                enGrade = enGrade + 1;
            }
            //今年入学的情况入学时间小于8月的并且现在时间大于8月
            if (year == 0 && (Integer.parseInt(nowTime[1])) >= upgradePoints && Integer.parseInt(enTimes[1]) < upgradePoints) {
                enGrade = enGrade + 1;
            }
        }
        switch (enGrade) {
            case -1:
                gradeName = "其他";
                break;
            case 1:
                gradeName = "一年级";
                break;
            case 2:
                gradeName = "二年级";
                break;
            case 3:
                gradeName = "三年级";
                break;
            case 4:
                gradeName = "四年级";
                break;
            case 5:
                gradeName = "五年级";
                break;
            case 6:
                gradeName = "六年级";
                break;
            case 7:
                gradeName = "初一";
                break;
            case 8:
                gradeName = "初二";
                break;
            case 9:
                gradeName = "初三";
                break;
            case 10:
                gradeName = "高一";
                break;
            case 11:
                gradeName = "高二";
                break;
            case 12:
                gradeName = "高三";
                break;
            case 13:
                gradeName = "大一";
                break;
            case 14:
                gradeName = "大二";
                break;
            case 15:
                gradeName = "大三";
                break;
            case 16:
                gradeName = "大四";
                break;
        }

        return gradeName;
    }

    @Transactional
    @Override
    //添加最终成绩
    public void createEndScore(FindScoreConditionVo findScoreConditionVo, List<EllReturnScoreVo> findUser) {
        //查询计划项目必选个数男
        int numBoy = planMapper.findPro(findScoreConditionVo.getPlanId(), "1");
        //查询计划项目必选个数女
        int numGirl = planMapper.findPro(findScoreConditionVo.getPlanId(), "2");
        //查询计划结束没有
        List<EllPlan> ellPlanList = planMapper.findPlan(findScoreConditionVo.getPlanId(), null, null, null);
        //可选项目数
        int numChoice = 0;
        //该用户应该测试项目个数
        int num = 0;
        //考试情况下要加上用户的选考数
        if (ellPlanList.get(0).getIsExam() == 1) {
            numChoice = ellPlanList.get(0).getCustomizeTheNumber();
        }
        HashMap<String, List<FindScoreVo>> ellTestHistoryHashMap = new HashMap<>();
        for (int j = 0; j < findUser.size(); j++) {
            //最终成绩
            Double endScore = 0.0;
            findScoreConditionVo.setUserId(findUser.get(j).getId());
            //查询用户的免测项目
            List<EllAvoidProject> ellAvoidProjectList = scoreMapper.selAvoidPro(findScoreConditionVo.getUserId(), findScoreConditionVo.getPlanId());//未测试
            List<FindScoreVo> list = scoreMapper.findUserScore(findScoreConditionVo);
            //判断有没有分数
            if (list == null || list.size() <= 0 || !IsIdCardUtil.isDecimalNumber(list.get(0).getScore())) {
                break;
            }
            //将每个学生的成绩放入map
            for (int i = 0; i < list.size(); i++) {
                //判断是否存在
                Boolean isHas = ellTestHistoryHashMap.containsKey(list.get(i).getUserId());
                if (isHas) {
                    ellTestHistoryHashMap.get(list.get(i).getUserId()).add(list.get(i));
                } else {
                    List<FindScoreVo> ellTestHistories = new ArrayList<>();
                    ellTestHistories.add(list.get(i));
                    ellTestHistoryHashMap.put(list.get(i).getUserId(), ellTestHistories);
                }
            }
            for (String userId : ellTestHistoryHashMap.keySet()) {
                //最终分数
                Double endAllScore = 0.0;
                if (ellAvoidProjectList != null && ellAvoidProjectList.size() > 0) {    //未测试
                    //加上免测
                    num = numChoice + ellAvoidProjectList.size();//未测试
                    endAllScore = ellAvoidProjectList.size() * 60.0;//未测试
                } else {
                    num = numChoice;
                }
                //查询该用户的最终成绩
                EllPlanEndScore ellPlanEndScore = scoreMapper.findPlanEndScore(findScoreConditionVo.getPlanId(), userId);
                //查询成绩完成是否核实
                EllScoreVerify ellScoreVerify = scoreMapper.findVerify(userId, findScoreConditionVo.getPlanId());
                //计算分数
                //判断是否有(有最终分数的体考计划将不再计算)
                if ((ellPlanList.get(0).getIsExam() == 2 || ellScoreVerify == null)) {
                    if (ellTestHistoryHashMap.get(userId).get(0).getUserSex().equals("1")) {
                        num = num + numBoy;
                    } else {
                        num = num + numGirl;
                    }//判断是否测试完成
                    if (ellTestHistoryHashMap.get(userId).size() == num) {
                        //项目测试完成计算最终分数
              /*          //获取本年级的项目分数占比
                        Map<Integer, Double> scoreScoreProportion = gradeScoreProportion(findScoreConditionVo);
                        //加分分数
                        int plusScore = 0;
                        //项目测试完成计算最终分数
                        for (int i = 0; i < ellTestHistoryHashMap.get(userId).size(); i++) {
                            if (Integer.parseInt(ellTestHistoryHashMap.get(userId).get(i).getScore()) > 100) {
                                //加分分数
                                plusScore = (Integer.parseInt(ellTestHistoryHashMap.get(userId).get(i).getScore()) - 100) + plusScore;
                                endAllScore = endAllScore + (Double) (100 * scoreScoreProportion.get(ellTestHistoryHashMap.get(userId).get(i).getTestProject()));

                            } else {
                                endAllScore = endAllScore + (Double) (Integer.parseInt(ellTestHistoryHashMap.get(userId).get(i).getScore()) * scoreScoreProportion.get(ellTestHistoryHashMap.get(userId).get(i).getTestProject()));

                            }
                        }*/
                        //加分分数
                        Double plusScore = 0.00;
                        for (int i = 0; i < ellTestHistoryHashMap.get(userId).size(); i++) {
                            Double score = Double.parseDouble(ellTestHistoryHashMap.get(userId).get(i).getScore());
                            //大于100分的都是加分项目
                            if (score > 100) {
                                //加分分数
                                plusScore = (score - 100) + plusScore;
                                endAllScore = endAllScore + 100;
                            } else {
                                endAllScore = endAllScore + score;
                            }
                        } //计划分数占比
                        List<EllPlanScoresOf> scoresOfs = scoreMapper.findPlanScoresOf(findScoreConditionVo.getPlanId());
                        //其他计划最终成绩
                        Double endOtherPlanScore = 0.0;
                        for (int i = 0; i < scoresOfs.size(); i++) {
                            //判断是否为本计划
                            if (scoresOfs.get(i).getIsThis().equals("1")) {
                                //计算本计划占比的分数
                                //判断是否是考试，考试就不用算平均
                                if (ellPlanList.get(0).getIsExam() == 2) {
                                    //体测
                                    //查询本计划必考是否有视力，视力不计算分数
                                    int hasVision = scoreMapper.findHasVision(findScoreConditionVo.getPlanId());
                                    //如果必考项没有计划再查询该学生自选项有没有视力
                                    if (hasVision == 0) {
                                        hasVision = scoreMapper.findHasVisionOptional(findScoreConditionVo.getPlanId(), findScoreConditionVo.getUserId());
                                    }
                                    endScore = (endScore + ((endAllScore / (num - hasVision)) / 100.0 * Double.parseDouble(scoresOfs.get(i).getProportion())));
                                } else {
                                    //考试是不用相除算项目平均分的
                                    endScore = endAllScore / 100.0 * Double.parseDouble(scoresOfs.get(i).getProportion());
                                }
                            } else {
                                //查询其他计划的成绩
                                EllPlanEndScore planEndScore = scoreMapper.findPlanEndScore(scoresOfs.get(i).getPlanId(), findScoreConditionVo.getUserId());
                                if (planEndScore != null) {
                                    //设置其他计划占比
                                    int otherPlanProportion = Integer.parseInt(scoresOfs.get(i).getProportion());
                                    //计算其他计划占比分数（百分制度）
                                    endOtherPlanScore = (planEndScore.getEndScore() / 100.0 * otherPlanProportion) + endOtherPlanScore;
                                }
                            }
                        }
                        //计算本计划最终分数
                          /*  if (otherPlanProportion == 0) {
                                endScore = (endOtherPlanScore / scoresOfs.size() + endScore);
                            }*/
                        endScore = endScore + endOtherPlanScore + plusScore;
                        findUser.get(j).setEndScore(endScore + "");
                        //如果有记录则代表成绩核实过或者是测试计划
                        //然后将最终分数进行存储
                        if ((ellPlanList.get(0).getIsExam() == 2 || ellScoreVerify == null)) {
                            //设置核实
                            findUser.get(j).setVerify(1);
                            EllScoreDetailsVo details = new EllScoreDetailsVo();
                            details.setPlanId(findScoreConditionVo.getPlanId());
                            details.setOrgId(Integer.parseInt(list.get(0).getOrgId()));
                            //查询计划必选项目注意：每个性别测试项目相同数量，分值相同
                            List<EllPlanProjectChance> ellPlanProjectChanceListMust = physicalDataService.selPlanProId(details.getPlanId(), 1, String.valueOf(findUser.get(j).getUserSex()), 0);
                            //该计划的满分
                            double fullScore = physicalDataService.FullMark(details, ellPlanList, ellPlanProjectChanceListMust);
                            //存储最终分数
                            if (ellPlanEndScore != null) {
                                scoreMapper.updataEndScore(findScoreConditionVo.getPlanId(), findScoreConditionVo.getUserId(), endScore, ScoreDisposeUtil.scoreComment(endScore, ellPlanList.get(0).getIsExam(), fullScore));
                            } else {
                                scoreMapper.addPlanEndScore(findScoreConditionVo.getPlanId(), userId, endScore, ScoreDisposeUtil.scoreComment(endScore, ellPlanList.get(0).getIsExam(), fullScore));
                            }
                        }
                    }
                }
            }
        }
    }

    //判断每个年级的项目分数占比
    private Map<Integer, Double> gradeScoreProportion(FindScoreConditionVo findScoreConditionVo) {
        //查询该学生
        List<EllReturnScoreVo> ellReturnScoreVos = ellUserMapper.findUserById(findScoreConditionVo.getUserId());
        int grade = GradeCalculate.gradeNumber(ellReturnScoreVos.get(0).getEnTime(), ellReturnScoreVos.get(0).getEnGrade());
        //项目分数占比
        Map<Integer, Double> scoreProportion = new HashMap<>();
        //身高体重
        scoreProportion.put(35, 0.15);
        //肺活量
        scoreProportion.put(36, 0.15);
        //50米
        scoreProportion.put(56, 0.20);
        switch (grade) {
            case 11:
            case 12:
                //坐位体前屈
                scoreProportion.put(38, 0.30);
                //跳绳
                scoreProportion.put(44, 0.20);
                break;
            case 13:
            case 14:
                //坐位体前屈
                scoreProportion.put(38, 0.20);
                //跳绳
                scoreProportion.put(44, 0.20);
                //仰卧起坐
                scoreProportion.put(42, 0.10);
                break;
            case 15:
            case 16:
                //坐位体前屈
                scoreProportion.put(38, 0.10);
                //跳绳
                scoreProportion.put(44, 0.10);
                //仰卧起坐
                scoreProportion.put(42, 0.20);
                //50*8
                scoreProportion.put(75, 0.10);
                break;
            case 21:
            case 22:
            case 23:
            case 31:
            case 32:
            case 33:
            case 41:
            case 42:
            case 43:
            case 44:
                //坐位体前屈
                scoreProportion.put(38, 0.10);
                //立定跳远
                scoreProportion.put(37, 0.10);
                //仰卧起坐（女）
                scoreProportion.put(42, 0.10);
                //引体向上（男）
                scoreProportion.put(39, 0.10);
                //1000米（男）
                scoreProportion.put(41, 0.20);
                //800米（女）
                scoreProportion.put(53, 0.20);
                break;
        }
        return scoreProportion;
    }

    @Transactional
    @Override
    public int updateScore(EllChangeScoreVo ellChangeScoreVo) {
        EllHistoryVo ellHistoryVo = new EllHistoryVo();
        ellHistoryVo.setId(ellChangeScoreVo.getHistoryId());
        //去数据查看历史成绩
        List<EllTestHistory> list = scoreMapper.findUserHistory(ellHistoryVo);
        //判断是否有历史成绩
        Map<String, Object> testDataMap = new HashMap<>();
        if (list.size() > 0) {
            testDataMap = JSONArray.parseObject(list.get(0).getTesData());
        }
        //修改体测成绩进行格式话算分
        testDataMap = ScoreDisposeUtil.updateScore(ellChangeScoreVo.getData(), testDataMap);
        //计算的分数
        JudgmentResultVo judgmentResultVo = judgmentResult(testDataMap, ellChangeScoreVo.getUserId(), ellChangeScoreVo.getPlanId(), String.valueOf(ellChangeScoreVo.getProId()), "2", 1, list.get(0).getUserSex(), list.get(0).getGrade());
        UpdateScoreVo updateScoreVo = new UpdateScoreVo();
        updateScoreVo.setId(ellChangeScoreVo.getHistoryId());
        updateScoreVo.setData(judgmentResultVo.getData());
        updateScoreVo.setUpdateById(ellChangeScoreVo.getUpdateById());
        updateScoreVo.setScore(String.valueOf(judgmentResultVo.getScore()));
        updateScoreVo.setComment(judgmentResultVo.getComment());
        FindScoreConditionVo findScoreConditionVo = new FindScoreConditionVo();
        findScoreConditionVo.setPlanId(ellHistoryVo.getPlanId());
        findScoreConditionVo.setUserId(ellHistoryVo.getUserId());
        int i = scoreMapper.updateScore(updateScoreVo, CreateTime.getTime());
        if (i > 0) {
            //查询学生成绩
            List<EllReturnScoreVo> findUser = findUserReturn(findScoreConditionVo, null);
            //生成最终分数
            createEndScore(findScoreConditionVo, findUser);
        }

        return i;
    }

    @Override
    public List<EllGradeVideoVo> selGradeVideo(String planId, String userId, int proId) {
        return scoreMapper.selGradeVideo(planId, userId, proId);
    }

    @Override
    public List<String> selFindAvoid(String planId) {
        return scoreMapper.selFindAvoid(planId);
    }

    @Override
    public List<EllScoreListDto> selUserList(EllScoreListVo ellScoreListVo) {
        return scoreMapper.selUserList(ellScoreListVo);
    }

    @Override
    public List<EllScoreListGradeDto> selGradeList(String userId, String planId) {
        return scoreMapper.selGradeList(userId, planId);
    }

    @Override
    public EllPlanEndScore findPlanEndScore(String planId, String userId) {
        return scoreMapper.findPlanEndScore(planId, userId);
    }

    @Override
    public List<EllReturnScoreVo> findUserHistory(FindScoreConditionVo findScoreConditionVo, List<EllReturnScoreVo> findUser) {
        //查询计划项目必选个数男
        int numBoy = planMapper.findPro(findScoreConditionVo.getPlanId(), "1");
        //查询计划项目必选个数女
        int numGirl = planMapper.findPro(findScoreConditionVo.getPlanId(), "2");
        //查询计划结束没有
        List<EllPlan> ellPlanList = planMapper.findPlan(findScoreConditionVo.getPlanId(), null, null, null);
        //可选项目数
        int numChoice = 0;
        //该用户应该测试项目个数
        int num = 0;
        //考试情况下要加上用户的选考数
        if (findScoreConditionVo.getIsExam() == 1) {
            numChoice = ellPlanList.get(0).getCustomizeTheNumber();
        }
        List<EllReturnScoreVo> findUserList = new ArrayList<>();
        for (int j = 0; j < findUser.size(); j++) {
            //最终成绩
            int endScore = 0;
            findScoreConditionVo.setUserId(findUser.get(j).getId());
            List<FindScoreVo> list = scoreMapper.findUserScore(findScoreConditionVo);
            HashMap<String, List<FindScoreVo>> ellTestHistoryHashMap = new HashMap<>();
            //将每个学生的成绩放入map
            for (int i = 0; i < list.size(); i++) {//将历史成绩格
                Map<String, Object> linkedHashMap = JSONArray.parseObject(list.get(i).getTesData());
                //为null或者‘’ 不返回给前端
                list.get(i).setTesData(null);
                List<EllTestData> testData;
                //多次历史成绩
                List<EllHistoricalPerformanceVo> historyData = new ArrayList<>();
                for (int k = 0; k < linkedHashMap.size(); k++) {
                    testData = JSONArray.parseArray(linkedHashMap.get(String.valueOf(k + 1)).toString(), EllTestData.class);
                    //取出每次历史成绩
                    EllAppHistoryVo ellAppHistoryVo = ScoreDisposeUtil.scoreReturn(testData, testData.get(0).getScore(), null);
                    //提取又用的数据
                    EllHistoricalPerformanceVo ellHistoricalPerformanceVo = new EllHistoricalPerformanceVo();
                    ellHistoricalPerformanceVo.setScore(ellAppHistoryVo.getScore());
                    ellHistoricalPerformanceVo.setIsRetest(Integer.parseInt(testData.get(0).getIsRetest()));
                    ellHistoricalPerformanceVo.setData(ellAppHistoryVo.getData());
                    ellHistoricalPerformanceVo.setNumTimes("第" + (k + 1) + "次");
                    historyData.add(ellHistoricalPerformanceVo);
                    list.get(i).setHistoryData(historyData);
                }
                //判断是否存在
                Boolean isHas = ellTestHistoryHashMap.containsKey(list.get(i).getUserId());
                if (isHas) {
                    ellTestHistoryHashMap.get(list.get(i).getUserId()).add(list.get(i));
                } else {
                    List<FindScoreVo> ellTestHistories = new ArrayList<>();
                    ellTestHistories.add(list.get(i));
                    ellTestHistoryHashMap.put(list.get(i).getUserId(), ellTestHistories);
                }
            }
            //查询用户的免测项目/生成默认成绩给前端显示
            List<EllAvoidProject> ellAvoidProjectList = scoreMapper.selAvoidPro(findScoreConditionVo.getUserId(), findScoreConditionVo.getPlanId());//未测试
            if (ellAvoidProjectList != null && ellAvoidProjectList.size() > 0) {
                for (int i = 0; i < ellAvoidProjectList.size(); i++) {
                    FindScoreVo findScoreVo = new FindScoreVo();
                    findScoreVo.setHistoryData(null);
                    findScoreVo.setScore("60");
                    findScoreVo.setData("免测");
                    findScoreVo.setComment("及格");
                    findScoreVo.setProjectName(ellAvoidProjectList.get(i).getProName());
                    findScoreVo.setTestProject(ellAvoidProjectList.get(i).getProId());
                    findScoreVo.setId(IdUtils.fastSimpleUUID());
                    findScoreVo.setUserSex(String.valueOf(findUser.get(j).getUserSex()));
                    findScoreVo.setIsRetest(1);
                    findScoreVo.setIsFreeTest(1);
                    //判断是否存在
                    if (ellTestHistoryHashMap.containsKey(findScoreConditionVo.getUserId())) {
                        ellTestHistoryHashMap.get(findScoreConditionVo.getUserId()).add(findScoreVo);
                    } else {
                        List<FindScoreVo> ellTestHistories = new ArrayList<>();
                        ellTestHistories.add(findScoreVo);
                        ellTestHistoryHashMap.put(findScoreConditionVo.getUserId(), ellTestHistories);
                    }
                }
            }
            for (String userId : ellTestHistoryHashMap.keySet()) {
                num = numChoice;
                //查询该用户的最终成绩
                EllPlanEndScore ellPlanEndScore = scoreMapper.findPlanEndScore(findScoreConditionVo.getPlanId(), userId);
                //查询成绩完成是否核实
                EllScoreVerify ellScoreVerify = scoreMapper.findVerify(userId, findScoreConditionVo.getPlanId());
                //将成绩详细放入实体
                findUser.get(j).setScore(ellTestHistoryHashMap.get(userId));
                //区分身高体重参数和视力测试
                for (int i = 0; i < findUser.get(j).getScore().size(); i++) {
                    //判断是否有多个成绩有逗号就代表有多个参数，身高体重或视力
                    if (findUser.get(j).getScore().get(i).getData().contains(",")) {
                        String datas[] = findUser.get(j).getScore().get(i).getData().split(",");
                        findUser.get(j).getScore().get(i).setData(ScoreDisposeUtil.endScore(datas));
                    }
                }
                //判断是否完成
                if ((ellPlanEndScore == null)) {
                    if (ellTestHistoryHashMap.get(userId).get(0).getUserSex().equals("1")) {
                        num = num + numBoy;
                    } else {
                        num = num + numGirl;
                    }//判断是否测试完成
                    if (ellTestHistoryHashMap.get(userId).size() == num) {
                        findUser.get(j).setIsAccomplish(1);
                        //如果有记录则代表成绩核实过或者是测试计划
                        //然后将最终分数进行存储
                        if ((findScoreConditionVo.getIsExam() == 2 || ellScoreVerify != null)) {
                            //设置核实
                            findUser.get(j).setVerify(1);
                        } else {
                            findUser.get(j).setIsAccomplish(1);
                            findUser.get(j).setVerify(2);
                            //未完成总分数为-
                            findUser.get(j).setEndScore(endScore + "");
                        }
                    } else {
                        findUser.get(j).setIsAccomplish(2);
                        findUser.get(j).setVerify(2);
                        //未完成分数为-1
                        findUser.get(j).setEndScore("-");
                    }
                    findUserList.add(findUser.get(j));
                } else {
                    findUser.get(j).setIsAccomplish(1);
                    //判断有没有最终分数
                    if (ellPlanEndScore != null) {
                        findUser.get(j).setEndScore(ellPlanEndScore.getEndScore() + "");
                    }
                    //如果有记录则代表成绩核实过
                    if (ellScoreVerify != null) {
                        findUser.get(j).setVerify(ellScoreVerify.getIsVerify());
                    } else {
                        findUser.get(j).setVerify(2);
                    }
                    findUserList.add(findUser.get(j));
                }
            }
        }
        return findUserList;
    }

    @Override
    public int selTestProNum(String userId, String planId) {
        return scoreMapper.selTestProNum(userId, planId);
    }

    @Transactional
    @Override
    public List<EllTestHistory> findScore(EllHistoryVo ellHistoryVo) {
        return scoreMapper.findUserHistory(ellHistoryVo);
    }

    @Transactional
    @Override
    public List<EllReturnScoreVo> importHistory(FindScoreConditionVo findScoreConditionVo) {
        List<FindScoreVo> list = scoreMapper.importHistory(findScoreConditionVo);
        HashMap<String, List<FindScoreVo>> ellTestHistoryHashMap = new LinkedHashMap<>();
        //将每个学生的成绩放入map
        for (int i = 0; i < list.size(); i++) {
            Boolean isHas = ellTestHistoryHashMap.containsKey(list.get(i).getUserId());
            if (isHas) {
                ellTestHistoryHashMap.get(list.get(i).getUserId()).add(list.get(i));
            } else {
                List<FindScoreVo> ellTestHistories = new ArrayList<>();
                ellTestHistories.add(list.get(i));
                ellTestHistoryHashMap.put(list.get(i).getUserId(), ellTestHistories);
            }
        }
        List<EllReturnScoreVo> findUserList = new ArrayList<>();
        for (String userId : ellTestHistoryHashMap.keySet()) {
            EllReturnScoreVo ellReturnScoreVo = new EllReturnScoreVo();
            ellReturnScoreVo.setId(ellTestHistoryHashMap.get(userId).get(0).getUserId());
            ellReturnScoreVo.setScore(ellTestHistoryHashMap.get(userId));
            ellReturnScoreVo.setSchoolName(ellUserMapper.selSchoolName(Integer.parseInt(ellTestHistoryHashMap.get(userId).get(0).getOrgId())));
            ellReturnScoreVo.setEnGrade(ellTestHistoryHashMap.get(userId).get(0).getEnGrade());
            ellReturnScoreVo.setEnTime(ellTestHistoryHashMap.get(userId).get(0).getEnTime());
            ellReturnScoreVo.setEthnic(ellTestHistoryHashMap.get(userId).get(0).getEthnic());
            ellReturnScoreVo.setDeptName(ellTestHistoryHashMap.get(userId).get(0).getDeptName());
            ellReturnScoreVo.setSig(ellTestHistoryHashMap.get(userId).get(0).getSig());
            ellReturnScoreVo.setHomeAddress(ellTestHistoryHashMap.get(userId).get(0).getHomeAddress());
            ellReturnScoreVo.setStudentCode(ellTestHistoryHashMap.get(userId).get(0).getStudentCode());
            ellReturnScoreVo.setUserSex(Integer.valueOf(ellTestHistoryHashMap.get(userId).get(0).getUserSex()));
            ellReturnScoreVo.setUserGrade(ellTestHistoryHashMap.get(userId).get(0).getGrade());
            ellReturnScoreVo.setUserName(ellTestHistoryHashMap.get(userId).get(0).getUserName());
            ellReturnScoreVo.setStudentId(ellTestHistoryHashMap.get(userId).get(0).getStudentId());
            ellReturnScoreVo.setUserBirth(ellTestHistoryHashMap.get(userId).get(0).getUserBirth());
            ellReturnScoreVo.setClassRemark(ellTestHistoryHashMap.get(userId).get(0).getClassRemark());
            ellReturnScoreVo.setSourceCode(ellTestHistoryHashMap.get(userId).get(0).getSourceCode());
        /*    if (ellTestHistoryHashMap.get(userId) != null && ellTestHistoryHashMap.get(userId).get(0).getUpdateTime() != null && ellTestHistoryHashMap.get(userId).get(0).getUpdateTime() != "") {
                ellReturnScoreVo.setTime(CreateTime.dayTime(ellTestHistoryHashMap.get(userId).get(0).getUpdateTime()));
            } else {
                ellReturnScoreVo.setTime("未测试");
            }*/

            //最终分数
//            int endScore = 0;
//            List<EllReturnScoreVo> findUser = ellUserMapper.findUserById(userId);
//            findUser.stream().forEach(user -> user.setSchoolName(ellUserMapper.selSchoolName(Integer.parseInt(user.getOrgId()))));
            //判断这个用户删除没有
//            if (findUser.size() > 0) {
//                findUser.get(0).setScore(ellTestHistoryHashMap.get(userId));
       /*     if (ellTestHistoryHashMap.get(userId).get(0).getUserSex().equals("1")) {
                num = numBoy;
            } else {
                num = numGirl;
            }
            if (ellTestHistoryHashMap.get(userId).size() == num) {
                findUser.get(0).setIsAccomplish(1);
            } else {
                findUser.get(0).setIsAccomplish(2);
            }
            for (int i = 0; i < ellTestHistoryHashMap.get(userId).size(); i++) {
                endScore = endScore + Integer.parseInt(ellTestHistoryHashMap.get(userId).get(i).getScore());
            }
            findUser.get(0).setEndScore(endScore / num);*/
//                findUserList.add(findUser.get(0));
//            }
            findUserList.add(ellReturnScoreVo);
        }
        return findUserList;
    }

    @Override
    public int findScoreNum(int proId, String planId) {
        return scoreMapper.findScoreNum(proId, planId);
    }

    @Override
    public String selUserAvg(String userId, String planId) {
        return scoreMapper.selUserAvg(userId, planId);
    }

    @Override
    public List<TestHistoryModel> selUserTestScore(String userId, String planId) {
        return scoreMapper.selUserTestScore(userId, planId);
    }

    @Transactional
    @Override
    public int resultsVerify(EllScoreVerifyVo ellScoreVerifyVo) {
        List<EllUser> ellUserList = ellUserMapper.findUsers(null, null, null, ellScoreVerifyVo.getUserId(), null, null);
        //查询计划项目个数男
        int numBoy = planMapper.findPro(ellScoreVerifyVo.getPlanId(), "1");
        //查询计划项目个数女
        int numGirl = planMapper.findPro(ellScoreVerifyVo.getPlanId(), "2");
        //该用户应该测试项目个数
        int num = 0;
        //查询计划的可选项目数
        List<EllPlan> ellPlanList = planMapper.findPlan(ellScoreVerifyVo.getPlanId(), null, null, null);
        num = num + ellPlanList.get(0).getCustomizeTheNumber();

        //已经测试的项目
        int proHistoryNum = scoreMapper.findProHistory(ellScoreVerifyVo.getUserId(), ellScoreVerifyVo.getPlanId());
        if (ellUserList.get(0).getUserSex().equals("1")) {
            num = num + numBoy;
        } else {
            num = num + numGirl;
        }
        //想等就是该测试的项目都测试了

        if (num == proHistoryNum) {
            String id = IdUtils.fastSimpleUUID();
            ellScoreVerifyVo.setIsVerify(1);
            //进行设置核实
            return scoreMapper.resultsVerify(id, ellScoreVerifyVo);
        }
        return 0;
    }

    @Transactional
    @Override
    public int resultsVerifyFacility(EllScoreVerifyVo ellScoreVerifyVo, MultipartFile imageName, MultipartFile imageFace) {
        List<EllUser> ellUserList = ellUserMapper.findUsers(null, null, null, ellScoreVerifyVo.getUserId(), null, null);
        //查询计划项目个数男
        int numBoy = planMapper.findPro(ellScoreVerifyVo.getPlanId(), "1");
        //查询计划项目个数女
        int numGirl = planMapper.findPro(ellScoreVerifyVo.getPlanId(), "2");
        //该用户应该测试项目个数
        int num = 0;
        //查询计划的可选项目数
        List<EllPlan> ellPlanList = planMapper.findPlan(ellScoreVerifyVo.getPlanId(), null, null, null);
        num = num + ellPlanList.get(0).getCustomizeTheNumber();
        //已经测试的项目
        int proHistoryNum = scoreMapper.findProHistory(ellScoreVerifyVo.getUserId(), ellScoreVerifyVo.getPlanId());
        if (ellUserList.get(0).getUserSex().equals("1")) {
            num = num + numBoy;
        } else {
            num = num + numGirl;
        }
        //想等就是该测试的项目都测试了

        if (num == proHistoryNum) {
            String id = IdUtils.fastSimpleUUID();
            if (!imageName.isEmpty() && !imageFace.isEmpty()) {
                try {
                    String[] typeName = imageName.getContentType().toString().split("/");
                    String[] typeFace = imageFace.getContentType().toString().split("/");
                    //把文件写入到目标位置
                    String fileName = UUID.randomUUID().toString();
                    //姓名截图
                    String pathName = "";
                    String urlName = "";
                    //人脸照片
                    String pathFace = "";
                    String urlFace = "";
                    if (FileUploadUtil.isLinux() == false) {
                        pathName = WindowsSite.Verify + fileName + "." + typeName[1];
                        urlName = WindowsSite.Verify_url;
                        pathFace = WindowsSite.Verify_FACE + fileName + "." + typeFace[1];
                        urlFace = WindowsSite.Verify_url_FACE;
                    } else {
                        pathName = LinuxSite.Verify + fileName + "." + typeName[1];
                        urlName = LinuxSite.Verify_url;
                        pathFace = LinuxSite.Verify_FACE + fileName + "." + typeFace[1];
                        urlFace = LinuxSite.Verify_url_FACE;
                    }
                    //判断是否有文件路径并生成路径
                    PathGeneration.createPath(pathName);
                    PathGeneration.createPath(pathFace);
                    File fileImageName = new File(pathName);
                    imageName.transferTo(fileImageName);//把文件写入目标文件地址
                    File fileImagePath = new File(pathFace);
                    imageFace.transferTo(fileImagePath);//把文件写入目标文件地址
                    //把姓名文件信息存入数据库
                    //把nginx访问图片地址存入数据库
                    //姓名图片id
                    String imgNameId = IdUtils.fastSimpleUUID();
                    //人连照片id
                    String imgFaceId = IdUtils.fastSimpleUUID();
                    urlName = urlName + fileName + "." + typeName[1];
                    ellUserMapper.uploadImage(fileName, String.valueOf(imageName.getSize()), "1", urlName, CreateTime.getTime(), imgNameId);
                    ellUserMapper.uploadImage(pathFace, String.valueOf(imageFace.getSize()), "1", urlFace, CreateTime.getTime(), imgFaceId);
                    //进行设置核实
                    scoreMapper.resultsVerifyFacility(id, ellScoreVerifyVo, imgNameId, imgFaceId);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }

        return 0;
    }

    @Override
    public List<ErrorScreenShotVo> findErrorImage(String planId, String userId) {
        List<ErrorScreenShotVo> screenShotVoList = scoreMapper.findErrorImage(planId, userId);
        for (int i = 0; i < screenShotVoList.size(); i++) {
            //判断是否有多个照片的错误原因
            boolean b = screenShotVoList.get(i).getErrorTraces().contains(",");
            if (b) {
                //将错误原因进行分割
                String[] strings = screenShotVoList.get(i).getErrorTraces().split(",");
                //放入list
                screenShotVoList.get(i).setErrorTraceList(Arrays.asList(strings));
            } else {
                List<String> errorTraceList = new ArrayList<>();
                errorTraceList.add(0, screenShotVoList.get(i).getErrorTraces());
                screenShotVoList.get(i).setErrorTraceList(errorTraceList);
            }
        }
        return screenShotVoList;
    }

    @Override
    public EllScoreVerify findVerify(String userId, String planId) {
        return scoreMapper.findVerify(userId, planId);
    }

    @Override
    public EllReturnScoreVo findResultsVerify(String userId, String planId) {
        return null;
    }

    @Override
    public List<EllReturnScoreVo> findUserReturn(FindScoreConditionVo findScoreConditionVo, List<String> userIds) {
        return scoreMapper.findUserReturn(findScoreConditionVo, userIds);
    }

    @Override
    public List<EllEyesightParticulars> findEyesight(String planId, String userId) {
        return scoreMapper.findEyesight(planId, userId);
    }

    @Override
    public List<EllPlanProjectChance> findEyesightProject(String planId, int parentId) {
        return scoreMapper.findSubsetProject(planId, parentId);
    }


    @Autowired
    private RulesMapper rulesMapper;

    @Transactional
    //每项成绩判断计算最高分  num是计算这次的上传分数的key
    public JudgmentResultVo judgmentResult(Map<String, Object> testDataMap, String userId, String planId, String testProject, String isRetest, int num, Integer sex, String grade) {
        //排序，将每次程序进行排序
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < testDataMap.size(); i++) {
            linkedHashMap.put((i + 1) + "", testDataMap.get(i + 1) + "");
        }
        //格式话数字小数点位数
        DecimalFormat dataFormat = new DecimalFormat("0.00");
        //项目分数占比
        String proGdp = scoreMapper.selProGdp(planId, testProject);
        //为空或者为0就是没占比
        if (proGdp == null || proGdp.equals("0") || proGdp.equals("")) {
            proGdp = "100.00";
        }
        //将占比转换为小数
        if (!proGdp.contains(".")) {
            proGdp = proGdp + ".00";
        }
        //查询规则
        List<EllScoreRule> ruleList = rulesMapper.findOrgRule(planId, userId, Integer.parseInt(testProject));
        HashMap<String, String> scoreMap = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < ruleList.size(); i++) {
            scoreMap.put(ruleList.get(i).getRule(), ruleList.get(i).getRuleScore());
            if (i == 0) {
                stringBuffer.append(ruleList.get(i).getRule());
            } else {
                stringBuffer.append("\n" + ruleList.get(i).getRule());
            }
        }
        Long s = System.currentTimeMillis();
        String data = "-400";
        //  List<List<EllTestData>> ellTestData = new ArrayList<>();
        //最终分数
        Double score = 0.00;
        String comment = "";
        JudgmentResultVo judgmentResultVo = new JudgmentResultVo();
        // ObjectMapper objectMapper = new ObjectMapper();
        //判断最后一次成绩
        int count = 0;
        //本次上传成绩的key值
        int sumCount = (linkedHashMap.size() - num) + 1;
        //成绩把刚上传的成绩每次进行比对取最大值
        for (int key = sumCount; key <= linkedHashMap.size(); key++) {
            count++;
            List<EllTestData> testData = JSONArray.parseArray(linkedHashMap.get(String.valueOf(key)).toString(), EllTestData.class);
            //判断是否成绩是否为无效
            if (testData.get(0).getData().equals("nullity")) {
                score = 0.00;
                data = "无效成绩";
                testData.get(0).setData("无效成绩");
                //本次下面不再执行
                continue;
            }
            //判断是否为体重身高或视力右眼左眼的项目，大于1就是体重身高或视力的项目小于就是普通项目因为这两个项目的测试的成绩多
            if (testData.size() >= 2) {
                if (testData.get(0).getIsRetest().equals(isRetest)) {
                    //身高体重
                    if (testData.size() >= 2 && testData.size() < 4) {
                        //计算身高体重的bmi值
                        Double BMI = Double.parseDouble(dataFormat.format(Double.parseDouble(testData.get(1).getData()) / Math.pow(Double.parseDouble(testData.get(0).getData()) / 100.0, 2)));
                      /*  //判断最后一次成绩
                        if (count == linkedHashMap.size()) {
                            //计算分数
                            score = MainRule.getRule(stringBuffer.toString(), String.valueOf(BMI), scoreMap);
                        }*/
                        //计算分数
                        score = Double.parseDouble(MainRule.getRule(stringBuffer.toString(), String.valueOf(BMI), scoreMap).get("score"));
                        //分数最终占比分数
                        score = Double.parseDouble(dataFormat.format(score / 100.0 * Double.parseDouble(proGdp)));
                        //将计算的bmi值拼接到参数中进行保存
                        EllTestData bmiData = new EllTestData();
                        bmiData.setData(String.valueOf(BMI));
                        bmiData.setScore(String.valueOf(score));
                        bmiData.setUnit("");
                        bmiData.setRemark("BMI第" + (linkedHashMap.size()) + "次");
                        bmiData.setIsRetest(isRetest);
                        bmiData.setTimestamp(new Date().getTime());
                        testData.add(testData.size(), bmiData);
                        //身高体重另存
                        List<EllBmiParticulars> ellBmiParticularsList = scoreMapper.findBmi(planId, userId);
                        EllBmiParticulars ellBmiParticulars = new EllBmiParticulars();
                        ellBmiParticulars.setBmi(String.valueOf(BMI));
                        ellBmiParticulars.setCreateTime(CreateTime.getTime());
                        ellBmiParticulars.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                        ellBmiParticulars.setHeight(testData.get(0).getData());
                        ellBmiParticulars.setWeight(testData.get(1).getData());
                        ellBmiParticulars.setPlanId(planId);
                        ellBmiParticulars.setUserId(userId);
                        ellBmiParticulars.setCountNum(ellBmiParticularsList.size() + 1);
                        ellBmiParticulars.setBmiComment(BmiTestResultUtil.BmiTestResult(sex, grade, Float.parseFloat(String.valueOf(BMI))));
                        //设置评价
                        comment = ellBmiParticulars.getBmiComment();
                        data = testData.get(0).getData() + "," + testData.get(1).getData() + "," + BMI;
                        //把计算的分数放入/体重身高的分数都一样
                        //体重
                        testData.get(1).setScore(score + "");
                        //身高
                        testData.get(0).setScore(score + "");
                        scoreMapper.updataBmi(planId, userId);
                        int i = scoreMapper.createBmi(ellBmiParticulars);
                        if (i <= 0) {
                            throw new RuntimeException("体重另存失败!");
                        }
                    } else {
                        //视力
                        int i = 0;
                        //左眼度数
                        Double left = 0.0;
                        //右眼度数
                        Double right = 0.0;
                        //视力
                        String eyesight = "";
                        for (int j = 0; j < testData.size(); j++) {
                            //初始化数据
                            if (j == 0) {
                                data = testData.get(0).getData();
                            } else {
                                data = data + "," + testData.get(j).getData();
                            }
                            //把计算的分数放入
                            testData.get(j).setScore("-");
                            //判断屈光正不正
                            //并且算出是正常还近视/远视
                            switch (j) {
                                case 2:
                                    EllTestData leftDiopter = new EllTestData();
                                    leftDiopter.setReason("");
                                    leftDiopter.setTimestamp(new Date().getTime());
                                    leftDiopter.setUnit("");
                                    leftDiopter.setIsRetest(isRetest);
                                    leftDiopter.setRemark("左眼屈光正不正" + "第" + (linkedHashMap.size()) + "次");
                                    leftDiopter.setScore("-");
                                    if (testData.get(j).getData() != null && !testData.get(j).getData().equals("")) {
                                        left = Double.parseDouble(testData.get(j).getData()) * 100;
                                        //0正常
                                        leftDiopter.setData("0");
                                        comment = "正常";
                                        if (left > 0) {
                                            if (left > 50) {
                                                leftDiopter.setData("2");
                                                comment = "远视";
                                            }
                                        } else if (left < -50) {
                                            leftDiopter.setData("1");
                                            comment = "近视";
                                        }
                                    } else {
                                        if (Double.parseDouble(testData.get(0).getData()) < 5.0) {
                                            leftDiopter.setData("1");
                                            comment = "近视";
                                        } else {
                                            comment = "正常";
                                            leftDiopter.setData("0");
                                        }
                                    }
                                    testData.add(testData.size(), leftDiopter);
                                    break;
                                case 5:
                                    EllTestData rightDiopter = new EllTestData();
                                    rightDiopter.setReason("");
                                    rightDiopter.setTimestamp(new Date().getTime());
                                    rightDiopter.setUnit("");
                                    rightDiopter.setIsRetest(isRetest);
                                    rightDiopter.setRemark("左眼屈光正不正" + "第" + (linkedHashMap.size()) + "次");
                                    rightDiopter.setScore("-");
                                    if (testData.get(j).getData() != null && !testData.get(j).getData().equals("")) {
                                        right = Double.parseDouble(testData.get(j).getData()) * 100;
                                        //0正常
                                        rightDiopter.setData("0");
                                        if (right > 0) {
                                            if (right > 50) {
                                                rightDiopter.setData("2");
                                                if (eyesightCondition(left, right)) {
                                                    comment = "远视";
                                                } else {
                                                    comment = eyesight;
                                                }
                                            }
                                        } else if (right < -50) {
                                            rightDiopter.setData("1");
                                            if (eyesightCondition(left, right)) {
                                                comment = "近视";
                                            }
                                        }
                                    } else {
                                        if (Double.parseDouble(testData.get(1).getData()) < 5.0) {
                                            rightDiopter.setData("1");
                                            comment = "近视";
                                        } else {
                                            rightDiopter.setData("0");
                                            comment = "正常";
                                        }
                                    }
                                    testData.add(testData.size(), rightDiopter);
                                    break;
                            }
                        }  //视力没的分数
                        score = -1.00;
                        //视力单独在进行储存
                        eyesightStorage(comment, planId, userId, testData);
                    }
                }
            } else {
                //其他项目
                if (testData.get(0).getIsRetest().equals(isRetest)) {
                    if (data.equals("-400")) {
                        data = testData.get(0).getData();
                    }

                    //计算分数
                    Map<String, String> scoreComment = MainRule.getRule(stringBuffer.toString(), testData.get(0).getData(), scoreMap);
                    Double scoreNow = Double.parseDouble(scoreComment.get("score"));
                    //分数最终占比分数
                    scoreNow = Double.parseDouble(dataFormat.format(scoreNow / 100.0 * Double.parseDouble(proGdp)));
                    //判断选取那个分数
                    if (scoreNow >= score) {
                        //判断分数是否一样
                        if (scoreNow == score) {
                            data = theBestData(data, testData.get(0).getData(), Integer.parseInt(testProject));
                        } else {
                            data = testData.get(0).getData();
                        }
                        //是否是需要把成绩转为分的项目
                        boolean i = ArrayUtils.contains(long_distance_race, testProject);
                        if (i) {
                            String zero = "";
                            if (Double.valueOf(data).intValue() % 60 < 10) {
                                zero = "0";
                            }
                            data = Double.valueOf(data).intValue() / 60 + "'" + zero + Double.valueOf(data).intValue() % 60 + "\"";
                            testData.get(0).setData(data);
                            testData.get(0).setUnit("m");
                            //                         System.out.println(data);
                        }
                        score = scoreNow;
                        if (ruleList != null && ruleList.size() > 0) {
                            comment = ruleList.get(Integer.parseInt(scoreComment.get("comment"))).getRemark();
                        } else {
                            comment = "不及格";
                        }

                        //sumCount++;
                    }
                    //把计算的分数放入
                    testData.get(0).setScore(scoreNow + "");
                }
            }
            //重新进行封装map
            linkedHashMap.put(String.valueOf(key), testData);
        }
        //判断除视力以外的所有项目分数的等级
        /*if (score >= 60 && score < 80 && comment == "") {
            comment = "及格";
        } else if (score >= 80 && score < 90 && comment == "") {
            comment = "良";
        } else if (score >= 90 && comment == "") {
            comment = "优秀";
        } else if (score < 60 && score >= 0 && comment == "") {
            comment = "不及格";
        }*/
        count = 0;
        judgmentResultVo.setScore(score);
        judgmentResultVo.setComment(comment);
        judgmentResultVo.setData(data);
        judgmentResultVo.setTestDataMap(JSONArray.toJSON(linkedHashMap).toString());
        Long s1 = System.currentTimeMillis();
        return judgmentResultVo;
    }

    //当分数相同取最好成绩
    public String theBestData(String dataEnd, String dataNow, int proId) {
        //判断项目是越小越好还是越大越好
        boolean i = ArrayUtils.contains(toTheTrendOfSmallProject, proId);
        //i大于0就是越小越好
        if (i) {
            if (Double.parseDouble(dataEnd) >= Double.parseDouble(dataNow)) {
                return dataNow;
            } else {
                return dataEnd;
            }
        } else {
            if (Double.parseDouble(dataEnd) > Double.parseDouble(dataNow)) {
                return dataEnd;
            } else {
                return dataNow;
            }
        }
    }

    //视力单独存储一次
    private void eyesightStorage(String comment, String planId, String userId, List<EllTestData> testData) {
        //视力单独在进行储存
        EllEyesightParticulars ellEyesightParticulars = setEyesight(testData);
        ellEyesightParticulars.setPlanId(planId);
        ellEyesightParticulars.setUserId(userId);
        ellEyesightParticulars.setVisionComment(comment);
        ellEyesightParticulars.setCreateTime(CreateTime.getTime());
        List<EllEyesightParticulars> list = scoreMapper.findEyesight(planId, userId);
        ellEyesightParticulars.setCountNum(list.size() + 1);
        int updateCount = scoreMapper.updataEyesight(ellEyesightParticulars);
        int addCount = scoreMapper.addEyesight(ellEyesightParticulars);
        if (addCount <= 0) {
            throw new RuntimeException("视力另存失败!");
        }
    }
    //视力成绩
   /* private Map<String, String> eyesightConditionGrade(int score, List<EllTestData> testData, String data, String isRetest, LinkedHashMap<String, Object> linkedHashMap, String comment, String userId, String planId) {
        //视力
        int i = 0;
        //左眼度数
        Double left = 0.0;
        //右眼度数
        Double right = 0.0;
        //视力
        String eyesight = "";
        for (int j = 0; j < testData.size(); j++) {
            //初始化数据
            if (j == 0) {
                data = testData.get(0).getData();
            } else {
                data = data + "," + testData.get(j).getData();
            }
            //把计算的分数放入
            testData.get(j).setScore("-");
            //判断屈光正不正
            //并且算出是正常还近视/远视
            switch (j) {
                case 2:
                    EllTestData leftDiopter = new EllTestData();
                    leftDiopter.setReason("");
                    leftDiopter.setTimestamp(new Date().getTime());
                    leftDiopter.setUnit("");
                    leftDiopter.setIsRetest(isRetest);
                    leftDiopter.setRemark("左眼屈光正不正" + "第" + (linkedHashMap.size()) + "次");
                    leftDiopter.setScore("-");
                    if (testData.get(j).getData() != null && !testData.get(j).getData().equals("")) {
                        left = Double.parseDouble(testData.get(j).getData()) * 100;
                        //0正常
                        leftDiopter.setData("0");
                        comment = "正常";
                        if (left > 0) {
                            if (left > 50) {
                                leftDiopter.setData("2");
                                comment = "远视";
                            }
                        } else if (left < -50) {
                            leftDiopter.setData("1");
                            comment = "近视";
                        }
                    } else {
                        if (Double.parseDouble(testData.get(0).getData()) < 5.0) {
                            leftDiopter.setData("1");
                            comment = "近视";
                        } else {
                            comment = "正常";
                            leftDiopter.setData("0");
                        }
                    }
                    testData.add(testData.size(), leftDiopter);
                    break;
                case 5:
                    EllTestData rightDiopter = new EllTestData();
                    rightDiopter.setReason("");
                    rightDiopter.setTimestamp(new Date().getTime());
                    rightDiopter.setUnit("");
                    rightDiopter.setIsRetest(isRetest);
                    rightDiopter.setRemark("左眼屈光正不正" + "第" + (linkedHashMap.size()) + "次");
                    rightDiopter.setScore("-");
                    if (testData.get(j).getData() != null && !testData.get(j).getData().equals("")) {
                        right = Double.parseDouble(testData.get(j).getData()) * 100;
                        //0正常
                        rightDiopter.setData("0");
                        if (right > 0) {
                            if (right > 50) {
                                rightDiopter.setData("2");
                                if (eyesightCondition(left, right)) {
                                    comment = "远视";
                                } else {
                                    comment = eyesight;
                                }
                            }
                        } else if (right < -50) {
                            rightDiopter.setData("1");
                            if (eyesightCondition(left, right)) {
                                comment = "近视";
                            }
                        }
                    } else {
                        if (Double.parseDouble(testData.get(1).getData()) < 5.0) {
                            rightDiopter.setData("1");
                            comment = "近视";
                        } else {
                            rightDiopter.setData("0");
                            comment = "正常";
                        }
                    }
                    testData.add(testData.size(), rightDiopter);
                    break;
            }
        }  //视力没的分数
        score = -1;
        //视力单独在进行储存
        EllEyesightParticulars ellEyesightParticulars = setEyesight(testData);
        ellEyesightParticulars.setPlanId(planId);
        ellEyesightParticulars.setUserId(userId);
        ellEyesightParticulars.setVisionComment(comment);
        ellEyesightParticulars.setCreateTime(CreateTime.getTime());
        List<EllEyesightParticulars> list = scoreMapper.findEyesight(planId, userId);
        ellEyesightParticulars.setCountNum(list.size() + 1);
        int updateCount = scoreMapper.updataEyesight(ellEyesightParticulars);
        int addCount = scoreMapper.addEyesight(ellEyesightParticulars);
        if (addCount <= 0) {
            throw new RuntimeException("视力另存失败!");
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("comment", comment);
        stringMap.put("score", String.valueOf(score));
        stringMap.put("data", data);
        stringMap.put("testDataMap", JSONArray.toJSON(linkedHashMap).toString());
        return stringMap;
    }*/

    //视力判断是以左眼的近视还是远视
    public Boolean eyesightCondition(double leftData, double rightData) {
        //如果左眼度数比右眼度数大就以左眼的视力情况
        if (Math.abs(leftData) >= Math.abs(rightData)) {
            return false;
        }
        return true;
    }

    //视力单独在进行存储一次
    public EllEyesightParticulars setEyesight(List<EllTestData> testDataList) {
        String id = new Date().getTime() + IdUtils.fastSimpleUUID();
        EllEyesightParticulars ellEyesightParticulars = new EllEyesightParticulars();
        ellEyesightParticulars.setId(id);
        ellEyesightParticulars.setIsDelete(0);
        for (int i = 0; i < testDataList.size(); i++) {
            switch (i) {
                case 0:
                    //"左眼裸眼视力"
                    ellEyesightParticulars.setLeftVision(testDataList.get(i).getData());
                    break;
                case 1:
                    //"右眼裸眼视力"
                    ellEyesightParticulars.setRightVision(testDataList.get(i).getData());
                    break;
                case 2:
                    //"左眼屈光度(球镜度)"
                    ellEyesightParticulars.setLeftDiopterSph(testDataList.get(i).getData());
                    break;
                case 3:
                    //"左眼屈光度(柱镜度)"
                    ellEyesightParticulars.setLeftDiopterCyl(testDataList.get(i).getData());
                    break;
                case 4:
                    //"左眼屈光度(轴位)"
                    ellEyesightParticulars.setLeftDiopterAx(testDataList.get(i).getData());
                    break;
                case 5:
                    //"右眼屈光度(球镜度)"
                    ellEyesightParticulars.setRightDiopterSph(testDataList.get(i).getData());
                    break;
                case 6:
                    //"右眼屈光度(柱镜度)"
                    ellEyesightParticulars.setRightDiopterCyl(testDataList.get(i).getData());
                    break;
                case 7:
                    //"右眼屈光度(轴位)"
                    ellEyesightParticulars.setRightDiopterAx(testDataList.get(i).getData());
                    break;
                case 8:
                    //"左眼建议屈光矫正"
                    ellEyesightParticulars.setLeftDiopterCorrect(testDataList.get(i).getData());
                    break;
                case 9:
                    //"右眼建议屈光矫正"
                    ellEyesightParticulars.setRightDiopterCorrect(testDataList.get(i).getData());
                    break;
                case 10:
                    //"左眼角膜曲率半径(水平方向)"
                    ellEyesightParticulars.setLeftCornealCurvatureLevel(testDataList.get(i).getData());
                    break;
                case 11:
                    //"左眼角膜曲率半径(垂直方向)"
                    ellEyesightParticulars.setLeftCornealCurvatureVertical(testDataList.get(i).getData());
                    break;
                case 12:
                    //"左眼角膜曲率半径(水平方向-轴位)"
                    ellEyesightParticulars.setLeftCornealCurvatureLevelAx(testDataList.get(i).getData());
                    break;
                case 13:
                    //"左眼角膜曲率半径(垂直方向-轴位)"
                    ellEyesightParticulars.setLeftCornealCurvatureVerticalAx(testDataList.get(i).getData());
                    break;
                case 14:
                    //"右眼角膜曲率半径(水平方向)"
                    ellEyesightParticulars.setRightCornealCurvatureLevel(testDataList.get(i).getData());
                    break;
                case 15:
                    //"右眼角膜曲率半径(垂直方向)"
                    ellEyesightParticulars.setRightCornealCurvatureVertical(testDataList.get(i).getData());
                    break;
                case 16:
                    //右眼角膜曲率班级(水平方向-轴位)
                    ellEyesightParticulars.setRightCornealCurvatureLevelAx(testDataList.get(i).getData());
                    break;
                case 17:
                    //右眼角膜曲率班级(水平方向-轴位)
                    ellEyesightParticulars.setRightCornealCurvatureVerticalAx(testDataList.get(i).getData());
                    break;
                case 18:
                    //"左眼角膜屈光度(水平方向)"
                    ellEyesightParticulars.setLeftCornealDiopterLevel(testDataList.get(i).getData());
                    break;
                case 19:
                    //"左眼角膜屈光度(垂直方向)"
                    ellEyesightParticulars.setLeftCornealDiopterVertical(testDataList.get(i).getData());
                    break;
                case 20:
                    //左眼角膜屈光度(平均值)
                    ellEyesightParticulars.setLeftCornealDiopterAug(testDataList.get(i).getData());
                    break;
                case 21:
                    //右眼角膜屈光度(水平方向)
                    ellEyesightParticulars.setRightCornealDiopterLevel(testDataList.get(i).getData());
                    break;
                case 22:
                    //右眼角膜屈光度(垂直方向)
                    ellEyesightParticulars.setRightCornealDiopterVertical(testDataList.get(i).getData());
                    break;
                case 23:
                    //右眼角膜屈光度(平均值)
                    ellEyesightParticulars.setRightCornealDiopterAug(testDataList.get(i).getData());
                    break;
                case 24:
                    //左眼角膜散光度
                    ellEyesightParticulars.setLeftCornealAstigmatism(testDataList.get(i).getData());
                    break;
                case 25:
                    //右眼角膜散光度
                    ellEyesightParticulars.setRightCornealAstigmatism(testDataList.get(i).getData());
                    break;
                case 26:
                    //左眼瞳孔直径
                    ellEyesightParticulars.setLeftPupil(testDataList.get(i).getData());
                    break;
                case 27:
                    //右眼瞳孔直径
                    ellEyesightParticulars.setRightPupil(testDataList.get(i).getData());
                    break;
                case 28:
                    //瞳距
                    ellEyesightParticulars.setPupilDistance(testDataList.get(i).getData());
                    break;
                case 29:
                    //左眼屈光正不正(0正常1近视2远视)
                    ellEyesightParticulars.setLeftDioptricNormal(Integer.valueOf(testDataList.get(i).getData()));
                    break;
                case 30:
                    //右眼屈光正不正(0正常1近视2远视)
                    ellEyesightParticulars.setRightDioptricNormal(Integer.valueOf(testDataList.get(i).getData()));
                    break;

            }
        }
        return ellEyesightParticulars;
    }

    @Override
    public List<EllReturnScoreVo> judgeProComplete(String planId, List<EllReturnScoreVo> ellReturnScoreVos) {
        for (int j = 0; j < ellReturnScoreVos.size(); j++) {
            //查询最终分数
            EllPlanEndScore score = findPlanEndScore(planId, ellReturnScoreVos.get(j).getId());
            //最终分数列
            if (score == null) {
                ellReturnScoreVos.get(j).setAppraisalStatus("未测试");
                if (ellReturnScoreVos.get(j).getScore() != null && !(ellReturnScoreVos.get(j).getScore().get(ellReturnScoreVos.get(j).getScore().size() - 1).getData().equals(""))) {
                    ellReturnScoreVos.get(j).setAppraisalStatus("未完成");
                }
                ellReturnScoreVos.get(j).setEndScore("0.00");
            } else {
                DecimalFormat df = new DecimalFormat("#.00");
                ellReturnScoreVos.get(j).setEndScore(df.format(score.getEndScore()));
                ellReturnScoreVos.get(j).setAppraisalStatus("正常");
            }
        }
        return ellReturnScoreVos;


    }

    @Override
    public List<EllUser> selUser(String orgId, String userSex) {
        return scoreMapper.selUser(orgId, userSex);
    }

    @Override
    public List<EllTestProject> selPro(String planId, String useSex) {
        return scoreMapper.selPro(planId, useSex);
    }

    @Override
    public List<String> selProId(String planId, String useSex) {
        return scoreMapper.selProId(planId, useSex);
    }

    @Override
    public List<String> selOptionProId(String planId, String userId) {
        return scoreMapper.selOptionProId(planId, userId);
    }

    @Override
    public List<EllTestHistory> selHistoryById(String planId, String userId, String proId) {
        return scoreMapper.selHistoryById(planId, userId, proId);
    }

    @Override
    public List<EllUser> selScoreless60(String planId, String userSex) {
        return scoreMapper.selScoreless60(planId, userSex);
    }

    @Override
    public List<OneUserScoreDto> getOneUserScore(int account, String orgId, String planId, int proId, String userName, String userId) {
        return scoreMapper.getOneUserScore(account, orgId, planId, proId, userName, userId);
    }

    @Override
    public Map<String, String> imageSaveFiles(MultipartFile faceImage, MultipartFile signatureImage) throws IOException {
        Map<String, String> imageSaveFileMap = new HashMap<>();
        if (!faceImage.isEmpty()) {
            String url = "";
            String[] type = faceImage.getContentType().toString().split("/");
            //把文件写入到目标位置
            String fileName = UUID.randomUUID().toString();
            String path = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.IMG_PATH + fileName + "." + type[1];
            } else {
                path = LinuxSite.IMG_PATH + fileName + "." + type[1];
            }
            //判断是否有文件路径并生成路径
            PathGeneration.createPath(path);
            File file = new File(path);
            faceImage.transferTo(file);//把文件写入目标文件地址
            //把文件信息存入数据库
            //把nginx访问图片地址存入数据库
            url = "/" + fileName + "." + type[1];
            imageSaveFileMap.put("faceImage", url);
        }
        if (!signatureImage.isEmpty()) {
            String url = "";
            String[] type = signatureImage.getContentType().toString().split("/");
            //把文件写入到目标位置
            String fileName = UUID.randomUUID().toString();
            String path = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.IMG_PATH + fileName + "." + type[1];
            } else {
                path = LinuxSite.IMG_PATH + fileName + "." + type[1];
            }
            //判断是否有文件路径并生成路径
            PathGeneration.createPath(path);
            File file = new File(path);
            signatureImage.transferTo(file);//把文件写入目标文件地址
            //把文件信息存入数据库
            //把nginx访问图片地址存入数据库
            url = "/" + fileName + "." + type[1];
            imageSaveFileMap.put("signatureImage", url);
        }
        return imageSaveFileMap;
    }

    @Override
    public List<String> selUserTestPro(String planId, String userId) {
        return scoreMapper.selUserTestPro(planId, userId);
    }

    @Override
    public List<FindScoreVo> selAllProHistory(String planId, String userId, List<String> proId) {
        return scoreMapper.selAllProHistory(planId, userId, proId);
    }

    @Override
    public List<EllScoreHistoryDto> selEndAndProNameScore(String planId, String accountId, String orgId) {
        return scoreMapper.selEndAndProNameScore(planId, accountId, orgId);
    }

    @Override
    public List<EllTestHistory> selHistory(String planId) {
        return scoreMapper.selHistory(planId);
    }

    @Override
    public EllScoreVerify selVerifyByUserId(String planId, String userId) {
        return scoreMapper.selVerifyByUserId(planId, userId);
    }

}
