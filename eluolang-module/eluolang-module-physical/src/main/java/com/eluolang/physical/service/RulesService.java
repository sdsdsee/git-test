package com.eluolang.physical.service;


import com.eluolang.common.core.pojo.EllTestRules;
import com.eluolang.common.core.pojo.EllTestRulesScore;
import com.eluolang.physical.model.CreateRuleVo;
import com.eluolang.physical.model.EllTestRulesVo;
import com.eluolang.physical.model.RulesVo;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;

public interface RulesService {
    // 具体每条规则

    /**
     * 增加规则
     */
    int addRules(List<RulesVo> rulesVo) throws Exception;

    /**
     * 修改规则
     */
    int updataRules(RulesVo rulesVo) throws ParseException;

    /**
     * 用户查询规则
     */
    List<EllTestRulesScore>  findRules(String id, String orgId, String proId, String ruleName, String parentId, String sex);

    /**
     * 超级管理员查询规则
     */
    List<EllTestRulesScore> findRulesSys(String id, String orgId, String proId, String ruleName, String parentId, String sex);

    /**
     * 管理员删除规则
     *
     * @param id
     * @return
     */
    int deleteRuleSys(int id);

    //  规则组

    /**
     * 创建规则组
     *
     * @param ruleVo
     * @return
     */
    int createRule(CreateRuleVo ruleVo);

    /**
     * 查询规则组
     *
     * @param id
     * @return
     */
    List<EllTestRulesVo> findEllRule(String id);

    /**
     * 修改最近使用时间
     */
    int updateGroupUseTime(String useTime, String id);

    /**
     * 查找此规则组名称是否已经存在
     */
    int findGroup(String groupName);
    /**
     * 查询得分规则
     */
    List<EllTestRulesScore> selScoreRule(int sex,int rulesId);
    /**
     * 删除某一组的得分规则
     */
    int delScoreRule( int parentId,int sex);
}
