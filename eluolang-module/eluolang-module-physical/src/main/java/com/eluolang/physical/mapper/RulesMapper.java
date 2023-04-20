package com.eluolang.physical.mapper;

import com.eluolang.common.core.pojo.EllTestRules;
import com.eluolang.common.core.pojo.EllTestRulesScore;
import com.eluolang.physical.model.CreateRuleVo;
import com.eluolang.physical.model.EllScoreRule;
import com.eluolang.physical.model.EllTestRulesVo;
import com.eluolang.physical.model.RulesVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RulesMapper {
    /**
     * 添加每套具体规则
     *
     * @param projectRules
     * @return
     */
    int addRules(@Param("projectRules") List<EllTestRulesScore> projectRules);

    int updataRules(@Param("rulesVo") RulesVo rulesVo, @Param("date") String date);

    List<EllTestRulesScore> findRules(@Param("id") String id, @Param("orgId") String orgId, @Param("proId") String proId, @Param("ruleName") String ruleName,
                                      @Param("parentId") String parentId, @Param("sex") String sex);

    List<EllTestRulesScore> findRulesSys();

    int deleteRuleSys(int id);

    /**
     * 规则组
     *
     * @param ruleVo
     * @return
     */
    int createRule(@Param("ruleVo") CreateRuleVo ruleVo);

    /**
     * 查询规则组
     */
    List<EllTestRulesVo> findEllRule(String id);

    /**
     * 修改组最近使用时间
     */
    int updateGroupUseTime(@Param("useTime") String useTime, @Param("id") String id);

    /**
     * 查找此规则组名称是否已经存在
     */
    int findGroup(String groupName);

    /**
     * 查询部门使用的规则
     */
    List<EllScoreRule> findOrgRule(@Param("planId") String planId, @Param("userId") String userId, @Param("proId") int proId);

    /**
     * 查询得分规则
     */
    List<EllTestRulesScore> selScoreRule(@Param("sex") int sex, @Param("rulesId") int rulesId);

    /**
     * 删除某一组的得分规则
     */
    int delScoreRule(@Param("parentId") int parentId,@Param("sex")int sex);

}
