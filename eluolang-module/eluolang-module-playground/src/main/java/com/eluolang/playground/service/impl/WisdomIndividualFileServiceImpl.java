package com.eluolang.playground.service.impl;

import com.eluolang.common.core.pojo.EllClassHistory;
import com.eluolang.common.core.pojo.EllTestRulesScore;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.mapper.WisdomClassMapper;
import com.eluolang.playground.mapper.WisdomIndividualFileMapper;
import com.eluolang.playground.service.WisdomIndividualFileService;
import com.eluolang.playground.util.GradeCalculate;
import com.eluolang.playground.util.MainRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WisdomIndividualFileServiceImpl implements WisdomIndividualFileService {
    @Autowired
    private WisdomClassMapper wisdomClassMapper;
    @Autowired
    private WisdomIndividualFileMapper wisdomIndividualFileMapper;
    //上肢项目 代表1
    @Value("${UpperLimbProIds}")
    private  String UpperLimbProIds;
    //下肢项目 代表2
    @Value("${LowerLimbProIds}")
    private  String LowerLimbProIds;
    //肺部 代表3
    @Value("${lungProIds}")
    private  String LungProIds;
    //腹部 代表4
    @Value("${abdomenProIds}")
    private  String AbdomenProIds;
    //手部 代表5
    @Value("${HandProIds}")
    private  String HandProIds;
    //协调性项目 代表6
    @Value("${coordinateProIds}")
    private  String CoordinateProIds;
    private  Map<Integer, String> bodyId=new HashMap<>();

    WisdomIndividualFileServiceImpl() {
        bodyId.put(1, UpperLimbProIds);
        bodyId.put(2, LowerLimbProIds);
        bodyId.put(3, LungProIds);
        bodyId.put(4, AbdomenProIds);
        bodyId.put(5, HandProIds);
        bodyId.put(6, CoordinateProIds);
    }

    @Override
    public EllBodyProblemPositionDto calculationBodyError(String userId) {
        bodyId.put(1, UpperLimbProIds);
        bodyId.put(2, LowerLimbProIds);
        bodyId.put(3, LungProIds);
        bodyId.put(4, AbdomenProIds);
        bodyId.put(5, HandProIds);
        bodyId.put(6, CoordinateProIds);
        //查询学生信息
        EllUser ellUser = wisdomIndividualFileMapper.selUserById(userId);
        //计算年级
        int grade = GradeCalculate.gradeCode(ellUser.getEnTime(), Integer.parseInt(ellUser.getEnGrade()));
        //查询学校id
        PfDepart school = wisdomClassMapper.selSchoolByClassId(Integer.parseInt(ellUser.getOrgId()));
        //查询每个项目最后的日常成绩
        List<EllClassHistory> histories = wisdomIndividualFileMapper.selLastOneProHistory(userId);
        //规则信息保存
        Map<Integer, HashMap<String, String>> proRulesMap = new HashMap<>();
        //计算字符串保存
        Map<Integer, String> scoreCalculateMap = new HashMap<>();
        //成绩分数保存
        Map<Integer, List<Map<String, String>>> scoreGradeMap = new HashMap<>();
        //问题部位及建议
        EllBodyProblemPositionDto bodyProblemPositionDto = new EllBodyProblemPositionDto();
        //初始化
        bodyProblemPositionDto.setBodyList(new ArrayList<>());
        bodyProblemPositionDto.setSuggestion("");
        bodyProblemPositionDto.setHistories(new ArrayList<>());
        //问题部位避免同一部位重复出现
        Map<Integer, List<Integer>> problemBodyMap = new HashMap<>();
        for (int i = 0; i < histories.size(); i++) {
            //查询规则
            if (!proRulesMap.containsKey(histories.get(i).getProId())) {
                List<EllTestRulesScore> rulesScores = wisdomIndividualFileMapper.selTestRulesScore(grade, school.getId(), histories.get(i).getProId());
                //规则信息
                HashMap<String, String> scoreMap = new HashMap<>();
                //计算信息
                StringBuffer stringBuffer = new StringBuffer();
                //将规则进行存储
                for (int j = 0; j < rulesScores.size(); j++) {
                    scoreMap.put(rulesScores.get(j).getRule(), rulesScores.get(j).getRuleScore());
                    if (j == 0) {
                        stringBuffer.append(rulesScores.get(j).getRule());
                    } else {
                        stringBuffer.append("\n" + rulesScores.get(j).getRule());
                    }
                }
                //保存
                proRulesMap.put(histories.get(i).getProId(), scoreMap);
                scoreCalculateMap.put(histories.get(i).getProId(), stringBuffer.toString());
            }
            //计算后的信息
            Map<String, String> calculateInfo = new HashMap<>();
            //身高体重
            if (histories.get(i).getData().contains(",")) {
                //格式话数字小数点位数
                DecimalFormat dataFormat = new DecimalFormat("0.00");
                String[] datas = histories.get(i).getData().split(",");
                Double BMI = Double.parseDouble(dataFormat.format(Double.parseDouble(datas[1]) / Math.pow(Double.parseDouble(datas[1]) / 100.0, 2)));
                calculateInfo = MainRule.getRule(scoreCalculateMap.get(histories.get(i).getProId()), String.valueOf(BMI), proRulesMap.get(histories.get(i).getProId()));
            } else {
                calculateInfo = MainRule.getRule(scoreCalculateMap.get(histories.get(i).getProId()), histories.get(i).getData(), proRulesMap.get(histories.get(i).getProId()));
            }
            EllScoreProDto scoreProDto = new EllScoreProDto(histories.get(i).getProName(), calculateInfo.get("score"));
            //返回成绩
            bodyProblemPositionDto.getHistories().add(scoreProDto);
            //保存成绩
            if (!scoreGradeMap.containsKey(histories.get(i).getProId())) {
                List<Map<String, String>> scoreList = new ArrayList<>();
                scoreList.add(calculateInfo);
                scoreGradeMap.put(histories.get(i).getProId(), scoreList);
            } else {
                scoreGradeMap.get(histories.get(i).getProId()).add(calculateInfo);
            }
            //及格和不及格就代表某一部位有问题
            switch (calculateInfo.get("comment")) {
                case "不及格":
                case "及格":
                case "0":
                    for (int j = 1; j <= bodyId.size(); j++) {
                        //判断问题部位在哪里，重复的部位不在添加
                        if (bodyId.get(j).contains("/" + histories.get(i).getProId() + "/") && !problemBodyMap.containsKey(j)) {
                            List<Integer> proIds = new ArrayList<>();
                            proIds.add(histories.get(i).getProId());
                            problemBodyMap.put(j, proIds);
                            //问题部位
                            bodyProblemPositionDto.getBodyList().add(j);
                        }
                    }
                default:
                    //查询配置好的建议规则
                    //查询建议规则
                    List<EllSuggestionDto> suggestionList = wisdomIndividualFileMapper.selSuggestion(histories.get(i).getProId());
                    //建议评判
                    for (int j = 0; j < suggestionList.size(); j++) {
                        //判断comment是否相同
                        if (suggestionList.get(j).getComment().equals(calculateInfo.get("comment"))) {
                            //保存建议
                            bodyProblemPositionDto.setSuggestion(bodyProblemPositionDto.getSuggestion() + histories.get(i).getProName() + ":" + suggestionList.get(j).getGuide() + ",");
                        }
                    }
            }
        }
        //生成图表需要3个项目不住就填上
        for (int i = bodyProblemPositionDto.getHistories().size(); i < 3; i++) {
            EllScoreProDto scoreProDto = new EllScoreProDto();
            scoreProDto.setProName("项目" + i);
            scoreProDto.setScore("0");
            bodyProblemPositionDto.getHistories().add(scoreProDto);
        }

        return bodyProblemPositionDto;
    }

    @Override
    public List<EllProScoreDto> selStudentData(String userId, Integer dayNum) {
        return wisdomIndividualFileMapper.selStudentData(userId, dayNum);
    }

    @Override
    public Integer selBetterThanGradeNum(Integer grade, int proId, Integer type) {
        return wisdomIndividualFileMapper.selBetterThanGradeNum(grade, proId, type);
    }

    @Override
    public EllStudentGradeDto selRunMileage(String userId) {
        return wisdomIndividualFileMapper.selRunMileage(userId);
    }

    @Override
    public Integer selWeekRun(String userId, long time) {
        return wisdomIndividualFileMapper.selWeekRun(userId, time);
    }

    @Override
    public EllIndividualUserDto selUser(String userId) {
        return wisdomIndividualFileMapper.selUser(userId);
    }

    @Override
    public List<EllEndScoreDto> selPlanScore(String userId) {
        return wisdomIndividualFileMapper.selPlanScore(userId);
    }

    @Override
    public List<EllClassGradeDto> selClassGrade(String userId, List<EllClassGradeDayDto> proIds) {
        return wisdomIndividualFileMapper.selClassGrade(userId, proIds);
    }

    @Override
    public List<EllClassGradeDayDto> selTestPro(String userId) {
        return wisdomIndividualFileMapper.selTestPro(userId);
    }

    @Override
    public List<EllClassHistory> selLastOneProHistory(String userId) {
        return wisdomIndividualFileMapper.selLastOneProHistory(userId);
    }

    @Override
    public List<EllEyesInfoDto> selEyesInfo(String userId) {
        return wisdomIndividualFileMapper.selEyesInfo(userId);
    }
}
