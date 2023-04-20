package com.eluolang.physical.service.impl;

import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.EllPlan;
import com.eluolang.common.core.pojo.EllPlanProjectChance;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.physical.dto.ClassScoreDto;
import com.eluolang.physical.dto.EllFullScoreDto;
import com.eluolang.physical.dto.EllProAvgScoreDto;
import com.eluolang.physical.dto.EllTotalScoreDto;
import com.eluolang.physical.mapper.PhysicalDataMapper;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.PhysicalDataService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PhysicalDataServiceImpl implements PhysicalDataService {

    @Autowired
    PhysicalDataMapper physicalDataMapper;


    @Override
    public List<PfDepart> selDepartByIdList(List<Integer> deptIdList) {
        return physicalDataMapper.selDepartByIdList(deptIdList);
    }

    @Override
    public List<PfDepartVo> selIsSchoolByIdList(List<Integer> deptIdList) {
        return physicalDataMapper.selIsSchoolByIdList(deptIdList);
    }

    @Override
    public int selSchoolUserCount(String path) {
        return physicalDataMapper.selSchoolUserCount(path);
    }

    @Override
    public int selUserCountByDeptIdList(List<Integer> deptIdList) {
        return physicalDataMapper.selUserCountByDeptIdList(deptIdList);
    }

    @Override
    public int selUserTestSuccessCount(String planId, List<Integer> deptIdList) {
        return physicalDataMapper.selUserTestSuccessCount(planId, deptIdList);
    }

    @Override
    public List<EachProjectScoreVo> selPlanProjectChanceByPlanId(String planId) {
        return physicalDataMapper.selPlanProjectChanceByPlanId(planId);
    }

    @Override
    public EachProjectScoreVo selEachProjectScore(String planId, Integer proId, List<Integer> deptId) {
        return physicalDataMapper.selEachProjectScore(planId, proId, deptId);
    }

    @Override
    public SchoolScoreVo selSchoolScore(List<Integer> deptId, String planId) {
        return physicalDataMapper.selSchoolScore(deptId, planId);
    }

    @Override
    public PlanEachScoreVo selPlanEachScore(List<Integer> deptIdList, String planId) {
        return physicalDataMapper.selPlanEachScore(deptIdList, planId);
    }

    @Override
    public ConfirmAchievementVo selConfirmAchievement(List<Integer> deptIdList, String planId) {
        return physicalDataMapper.selConfirmAchievement(deptIdList, planId);
    }

    @Override
    public int selEachRetestNumber(List<Integer> deptIdList, String planId, Integer proId) {
        return physicalDataMapper.selEachRetestNumber(deptIdList, planId, proId);
    }

    @Override
    public int findNumberOfPeople(int accountId, String path) {
        return physicalDataMapper.findNumberOfPeople(accountId, path);
    }

    @Override
    public int findExamNumberOfPeople(int accountId, String planId) {
        return physicalDataMapper.findExamNumberOfPeople(accountId, planId);
    }

    @Override
    public int findExamFinishNumberOfPeople(int accountId, String planId) {
        return physicalDataMapper.findExamFinishNumberOfPeople(accountId, planId);
    }

    @Override
    public List<String> findRetestNumberOfPeople(int accountId, String planId) {
        return physicalDataMapper.findRetestNumberOfPeople(accountId, planId);
    }

    @Override
    public int findExamSexNumberOfPeople(int accountId, String planId, int sex) {
        return physicalDataMapper.findExamSexNumberOfPeople(accountId, planId, sex);
    }

    @Override
    public int findClassNumberOf(int accountId, String planId) {
        return physicalDataMapper.findClassNumberOf(accountId, planId);
    }

    @Override
    public int findFreeTest(int accountId, String planId) {
        return physicalDataMapper.findFreeTest(accountId, planId);
    }

    @Override
    public List<String> findRetestNumberOf(int accountId, String planId) {
        return physicalDataMapper.findRetestNumberOf(accountId, planId);
    }

    @Override
    public int findDisQualifiedNumberOf(int accountId, String planId) {
        return physicalDataMapper.findDisQualifiedNumberOf(accountId, planId);
    }

    @Override
    public String findPlanTitle(String planId) {
        return physicalDataMapper.findPlanTitle(planId);
    }

    @Override
    public List<EllTestProjectVo> findTestProject(String planId) {
        return physicalDataMapper.findTestProject(planId);
    }

    @Override
    public int findProjectTestNumberOf(int accountId, String planId, int proId, int orgId) {
        return physicalDataMapper.findProjectTestNumberOf(accountId, planId, proId, orgId);
    }

    @Override
    public int findProjectRestTestNumberOf(int accountId, String planId, int proId, int orgId) {
        return physicalDataMapper.findProjectRestTestNumberOf(accountId, planId, proId, orgId);
    }

    @Override
    public int findProjectSexTestNumberOf(int accountId, String planId, int proId, int userSex, int orgId) {
        return physicalDataMapper.findProjectSexTestNumberOf(accountId, planId, proId, userSex, orgId);
    }

    @Override
    public int findTotalityCondition(int accountId, String planId, String scoreCondition, int userSex, int proId, int orgId) {
        return physicalDataMapper.findTotalityCondition(accountId, planId, scoreCondition, userSex, proId, orgId);
    }


    @Override
    public int findProjectNumberOf(int accountId, String planId, int proId, int userSex, int orgId) {
        return physicalDataMapper.findProjectNumberOf(accountId, planId, proId, userSex, orgId);
    }

    @Override
    public List<PfDepart> selSchoolInformation(Integer optId, String planId) {
        return physicalDataMapper.selSchoolInformation(optId, planId);
    }

    @Override
    public List<Integer> termSelectClass(ClassScoreDto classScoreDto) {
        return physicalDataMapper.termSelectClass(classScoreDto);
    }

    @Override
    public List<ClassScoreVo> selEachClassScore(List<Integer> deptList) {
        return physicalDataMapper.selEachClassScore(deptList);
    }

    @Override
    public List<ClassNumberVo> selEachClassNumber(List<Integer> deptList) {
        return physicalDataMapper.selEachClassNumber(deptList);
    }

    @Override
    public SchoolNumberVo selSchoolNumber(List<Integer> deptList) {
        return physicalDataMapper.selSchoolNumber(deptList);
    }

    @Override
    public List<String> selSchoolPath(String planId) {
        return physicalDataMapper.selSchoolPath(planId);
    }

    @Override
    public EllProAvgScoreDto selProAvgScore(String planId, int accountOrgId, int orgId, int proId, int sex) {
        return physicalDataMapper.selProAvgScore(planId, accountOrgId, orgId, proId, sex);
    }

    @Override
    public EllTotalScoreDto selSchoolTotalScore(String planId, int orgId, int accountOrgId, int sex) {
        return physicalDataMapper.selSchoolTotalScore(planId, orgId, accountOrgId, sex);
    }

    @Override
    public List<EllFullScoreDto> selFllScore(int accountId, String planId, int proId, int userSex, int orgId, double score) {
        return physicalDataMapper.selFllScore(accountId, planId, proId, userSex, orgId, score);
    }

    @Override
    public int selPlanOrgIdPeopleNum(int sex, String planId, int orgId, int accountOrgId) {
        return physicalDataMapper.selPlanOrgIdPeopleNum(sex, planId, orgId, accountOrgId);
    }

    @Override
    public int selFullEndScorePeople(int sex, String planId, int orgId, int accountOrgId, double score) {
        return physicalDataMapper.selFullEndScorePeople(sex, planId, orgId, accountOrgId, score);
    }

    @Override
    public List<EllScoreRule> selRuleScore(String evaluation, String planId, int proId, int orgId) {
        return physicalDataMapper.selRuleScore(evaluation, planId, proId, orgId);
    }

    @Override
    public List<EllPlanProjectChance> selPlanProId(String planId, int isMust, String sex, int proId) {
        return physicalDataMapper.selPlanProId(planId, isMust, sex, proId);
    }

    @Override
    public Integer selEndScoreByScore(String planId, double big, double small, int orgId, int accountOrgId, Double notScore, int sex) {
        return physicalDataMapper.selEndScoreByScore(planId, big, small, orgId, accountOrgId, notScore, sex);
    }

    @Override
    public Integer selProProportion(String planId, Integer proId) {
        return physicalDataMapper.selProProportion(planId, proId);
    }

    @Override
    //����üƻ�������
    public double FullMark(EllScoreDetailsVo details, List<EllPlan> ellPlanList, List<EllPlanProjectChance> ellPlanProjectChanceListMust) {
        //��ѯ�ƻ��Ǳ�ѡ��Ŀ
        List<EllPlanProjectChance> ellPlanProjectChanceList = selPlanProId(details.getPlanId(), 2, "1", 0);
        //��ѯ����ķ���
        List<EllScoreRule> scoreRuleListMust = new ArrayList<>();
        double score = 100.0;
        if (ellPlanProjectChanceListMust.size() > 0) {
            scoreRuleListMust = selRuleScore("����", details.getPlanId(), ellPlanProjectChanceListMust.get(0).getProId(), details.getOrgId());
            //����Ĭ�ϴ�С����ĵ������ֵȼ�Ϊ����
            if (scoreRuleListMust != null && scoreRuleListMust.size() >= 3) {
                //�ж��Ƿ�ΪС��
                if (scoreRuleListMust.get(scoreRuleListMust.size() - 1).getRuleScore().contains(".")) {
                    score = Double.parseDouble(scoreRuleListMust.get(scoreRuleListMust.size() - 1).getRuleScore());
                } else {
                    score = Double.parseDouble(scoreRuleListMust.get(scoreRuleListMust.size() - 1).getRuleScore() + ".00");
                }
            }
            //ռ��
            Integer gdp = physicalDataMapper.selProProportion(details.getPlanId(), ellPlanProjectChanceListMust.get(0).getProId());
            gdp = gdp == null || gdp == 0 ? 100 : gdp;
            score = score / 100 * gdp;
        }
//        else {
//            score = 0;
//        }

        //����и���Ŀ���ִ��ڵ���100�־Ͳ��ü�˵�����շ���������ҲΪ100
        if (score < 100.0) {
            //�ѷ����ָ�Ĭ�ϵ�0
            score = 0;
            //��ѯ�Ǳ�ѡ��Ŀ�ķ���
            //��ѯ����ķ���
            List<EllScoreRule> scoreRuleList = new ArrayList<>();
            if (ellPlanProjectChanceList != null && ellPlanProjectChanceList.size() > 0) {
                scoreRuleList = selRuleScore("����", details.getPlanId(), ellPlanProjectChanceList.get(0).getProId(), details.getOrgId());
                //ѡ���ķ���Ϊһ����
                //�����ܷ�
                if (scoreRuleList != null && scoreRuleList.size() >= 3) {
                    score = 0.0;
                    //ռ��
                    Integer gdp = physicalDataMapper.selProProportion(details.getPlanId(), ellPlanProjectChanceList.get(0).getProId());
                    gdp = gdp == null || gdp == 0 ? 100 : gdp;
                    //�ж��Ƿ�ΪС��
                    if (scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore().contains(".")) {
//                        score = score + Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore()) / 100 * gdp * ellPlanList.get(0).getCustomizeTheNumber();
                        score = Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore()) / 100 * gdp * ellPlanList.get(0).getCustomizeTheNumber();
                    } else {
//                        score = score + Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore() + ".00") / 100 * gdp * ellPlanList.get(0).getCustomizeTheNumber();
                        score = Double.parseDouble(scoreRuleList.get(scoreRuleList.size() - 1).getRuleScore() + ".00") / 100 * gdp * ellPlanList.get(0).getCustomizeTheNumber();
                    }
                } else {
                    //û�в�ѯ���͵��ͱ�ѡ����ɼ�����һ��
//                    score = score + score * ellPlanList.get(0).getCustomizeTheNumber();
                    score = score * ellPlanList.get(0).getCustomizeTheNumber();
                }
            }
        } else {
            return 100;
        }
        //�ټ��ϱؿ�ÿ�������
        for (int i = 0; i < ellPlanProjectChanceListMust.size(); i++) {
            //��ѯ��Ŀ����
            scoreRuleListMust = selRuleScore("����", details.getPlanId(), ellPlanProjectChanceListMust.get(i).getProId(), details.getOrgId());
            //ռ��
            Integer gdp = physicalDataMapper.selProProportion(details.getPlanId(), ellPlanProjectChanceListMust.get(0).getProId());
            gdp = gdp == null || gdp == 0 ? 100 : gdp;
            if (scoreRuleListMust != null && scoreRuleListMust.size() > 0) {
                //�ж��Ƿ�ΪС��
                if (scoreRuleListMust.get(scoreRuleListMust.size() - 1).getRuleScore().contains(".")) {
                    score = score + (Double.parseDouble(scoreRuleListMust.get(scoreRuleListMust.size() - 1).getRuleScore()) / 100 * gdp);
                } else {
                    score = score + (Double.parseDouble(scoreRuleListMust.get(scoreRuleListMust.size() - 1).getRuleScore() + ".00") / 100 * gdp);
        
                }
            }
        }
        return score;
    }
}
