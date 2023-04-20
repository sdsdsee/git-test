package com.eluolang.physical.service.impl;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllTestRules;
import com.eluolang.common.core.pojo.EllTestRulesScore;
import com.eluolang.physical.mapper.RulesMapper;
import com.eluolang.physical.model.CreateRuleVo;
import com.eluolang.physical.model.EllTestRulesVo;
import com.eluolang.physical.model.RulesVo;
import com.eluolang.physical.service.RulesService;
import com.eluolang.physical.util.CreateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RulesServiceImpl implements RulesService {
    @Autowired
    private RulesMapper rulesMapper;

    @Override
    public int addRules(List<RulesVo> rulesVo) throws Exception {
        List<EllTestRulesScore> list = new ArrayList<>();
        for (int i = 0; i < rulesVo.size(); i++) {
            EllTestRulesScore rules = new EllTestRulesScore();
            rules.setRule(rulesVo.get(i).getRule());
            rules.setCreateTime(CreateTime.getTime() + "");
            rules.setRuleScore(rulesVo.get(i).getRuleScore());
            rules.setOrgId(rulesVo.get(i).getOrgId() + "");
            rules.setRuleName(rulesVo.get(i).getRuleName());
            rules.setProjectId(rulesVo.get(i).getProjectId());
            rules.setProjectName(rulesVo.get(i).getProjectName());
            rules.setGrade(rulesVo.get(i).getGrade());
            rules.setSex(rulesVo.get(i).getSex());
            rules.setRemark(rulesVo.get(i).getRemark());
            rules.setParentId(rulesVo.get(i).getParentId());
            list.add(rules);
        }

        return rulesMapper.addRules(list);
    }

    @Override
    public int updataRules(RulesVo rulesVo) throws ParseException {

        return rulesMapper.updataRules(rulesVo, CreateTime.getTime());
    }

    @Override
    public List<EllTestRulesScore> findRules(String id, String orgId, String proId, String ruleName, String parentId, String sex) {
        List<EllTestRulesScore> projectRules = rulesMapper.findRules(id, orgId, proId, ruleName, parentId, sex);

        return projectRules;
    }

    @Override
    public List<EllTestRulesScore> findRulesSys(String id, String orgId, String proId, String ruleName, String parentId, String sex) {
        List<EllTestRulesScore> list = rulesMapper.findRules(id, orgId, proId, ruleName, parentId, sex);
        return list;
    }

    @Override
    public int deleteRuleSys(int id) {
        return rulesMapper.deleteRuleSys(id);
    }

    @Override
    public int createRule(CreateRuleVo ruleVo) {
        int i = findGroup(ruleVo.getRuleName());
        if (i > 0) {
            return HttpStatus.ALREADY_EXISTS;
        }
        return rulesMapper.createRule(ruleVo);
    }

    @Override
    public List<EllTestRulesVo> findEllRule(String id) {
        return rulesMapper.findEllRule(id);
    }

    @Override
    public int updateGroupUseTime(String useTime, String id) {
        return updateGroupUseTime(useTime, id);
    }

    @Override
    public int findGroup(String groupName) {
        return rulesMapper.findGroup(groupName);
    }

    @Override
    public List<EllTestRulesScore> selScoreRule(int sex, int rulesId) {
        return rulesMapper.selScoreRule(sex, rulesId);
    }

    @Override
    public int delScoreRule(int parentId,int sex) {
        return rulesMapper.delScoreRule(parentId,sex);
    }
}
