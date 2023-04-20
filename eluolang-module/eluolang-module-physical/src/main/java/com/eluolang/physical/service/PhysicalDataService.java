package com.eluolang.physical.service;

import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.EllPlan;
import com.eluolang.common.core.pojo.EllPlanProjectChance;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.physical.dto.ClassScoreDto;
import com.eluolang.physical.dto.EllFullScoreDto;
import com.eluolang.physical.dto.EllProAvgScoreDto;
import com.eluolang.physical.dto.EllTotalScoreDto;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PhysicalDataService {
    /**
     * 根据部门id集合查询部门信息
     *
     * @param deptIdList
     * @return
     */
    List<PfDepart> selDepartByIdList(List<Integer> deptIdList);

    /**
     * 根据部门id集合查询为学校级的部门
     *
     * @param deptIdList
     * @return
     */
    List<PfDepartVo> selIsSchoolByIdList(List<Integer> deptIdList);

    /**
     * 根据path查询学校总人数
     *
     * @param path
     * @return
     */
    int selSchoolUserCount(String path);

    /**
     * 根据部门id集合查询学生考试人数
     *
     * @param deptIdList
     * @return
     */
    int selUserCountByDeptIdList(List<Integer> deptIdList);

    /**
     * 根据部门id集合查询学生考试完成人数
     *
     * @param deptIdList
     * @return
     */
    int selUserTestSuccessCount(String planId, List<Integer> deptIdList);

    /**
     * 根据计划id查询该计划使用项目
     *
     * @param planId
     * @return
     */
    List<EachProjectScoreVo> selPlanProjectChanceByPlanId(String planId);

    /**
     * 根据计划id查询考点各项目分值情况
     *
     * @param planId
     * @return
     */
    EachProjectScoreVo selEachProjectScore(String planId, Integer proId, List<Integer> deptId);

    /**
     * 查询学校得分情况
     *
     * @param deptId
     * @param planId
     * @return
     */
    SchoolScoreVo selSchoolScore(List<Integer> deptId, String planId);

    /**
     * 根据计划id查询各个分值人数
     *
     * @param planId
     * @return
     */
    PlanEachScoreVo selPlanEachScore(List<Integer> deptIdList, String planId);

    /**
     * 根据计划id查询确认成绩人数
     *
     * @param planId
     * @return
     */
    ConfirmAchievementVo selConfirmAchievement(List<Integer> deptIdList, String planId);

    /**
     * 根据计划id查询各项目重测人数
     *
     * @param planId
     * @return
     */
    int selEachRetestNumber(List<Integer> deptIdList, String planId, Integer proId);

    /**
     * 查询该账户有权限部门的总人数
     */
    int findNumberOfPeople(int accountId, String path);

    /**
     * 查询该账户有权限部门考试总人数
     */
    int findExamNumberOfPeople(int accountId, String planId);

    /**
     * 查询该账户有权限部门已考试完成人数
     */
    int findExamFinishNumberOfPeople(int accountId, String planId);

    /**
     * 查询该账户有权限部门考试重测人数
     */
    List<String> findRetestNumberOfPeople(int accountId, String planId);

    /**
     * 查询该账户有权限部门考试男女人数
     */
    int findExamSexNumberOfPeople(int accountId, String planId, int sex);

    /**
     * 查询该账户在计划下有权限的班级数量
     */
    int findClassNumberOf(int accountId, String planId);

    /**
     * 查询免考人数
     */
    int findFreeTest(int accountId, String planId);

    /**
     * 重考人数
     */
    List<String> findRetestNumberOf(int accountId, String planId);

    /**
     * 考试完成人数的不合格人数
     */
    int findDisQualifiedNumberOf(int accountId, String planId);

    /**
     * 计划标题
     */
    String findPlanTitle(String planId);

    /**
     * 查询测试项目
     */
    List<EllTestProjectVo> findTestProject(String planId);

    /**
     * 单项的项目已测试人数
     */
    int findProjectTestNumberOf(int accountId, String planId, int proId, int orgId);

    /**
     * 单项的项目重测人数
     */
    int findProjectRestTestNumberOf(int accountId, String planId, int proId, int orgId);

    /**
     * 单项的项目分性别测试人数
     */
    int findProjectSexTestNumberOf(int accountId, String planId, int proId, int userSex, int orgId);

    /**
     * 得分总体情况
     */
    int findTotalityCondition(int accountId, String planId, String scoreCondition, int userSex, int proId, int orgId);

    /**
     * 需要测试该项目的人数分男女
     */
    int findProjectNumberOf(int accountId, String planId, int proId, int userSex, int orgId);

    /**
     * 根据管理员id和计划id查询学校信息
     *
     * @param optId
     * @param planId
     * @return
     */
    List<PfDepart> selSchoolInformation(Integer optId, String planId);

    /**
     * 按条件查询班级
     *
     * @param classScoreDto
     * @return
     */
    List<Integer> termSelectClass(ClassScoreDto classScoreDto);

    /**
     * 查询各个班级得分情况
     *
     * @param deptList
     * @return
     */
    List<ClassScoreVo> selEachClassScore(List<Integer> deptList);

    /**
     * 查询各个班级人数情况
     *
     * @param deptList
     * @return
     */
    List<ClassNumberVo> selEachClassNumber(List<Integer> deptList);

    /**
     * 按条件查询学校男女人数情况
     *
     * @param deptList
     * @return
     */
    SchoolNumberVo selSchoolNumber(List<Integer> deptList);

    /**
     * 查询学校path
     */
    List<String> selSchoolPath(String planId);

    /**
     * 查询某个部门的某个项目的平均分
     */
    EllProAvgScoreDto selProAvgScore(String planId, int accountOrgId, int orgId, int proId, int sex);

    /**
     * 查询部门总分
     */
    EllTotalScoreDto selSchoolTotalScore(String planId, int orgId, int accountOrgId, int sex);

    /**
     * 查询某一个项目某一分数段的人数
     */
    List<EllFullScoreDto> selFllScore(int accountId, String planId, int proId, int userSex, int orgId, double score);

    /**
     * 查询某一部门某账号下参与该计划的人数
     */
    int selPlanOrgIdPeopleNum(int sex, String planId, int orgId, int accountOrgId);

    /**
     * 查询某一部门某账号下最终分高于多少的人
     */
    int selFullEndScorePeople(int sex, String planId, int orgId, int accountOrgId, double score);

    /**
     * 查询规则每个阶段的分数
     */
    List<EllScoreRule> selRuleScore(String evaluation, String planId, int proId, int orgId);

    /**
     * 查询项目计划id
     */
    List<EllPlanProjectChance> selPlanProId(String planId, int isMust, String sex, int proId);

    /**
     * 查询分数每个阶段的人数
     */
    Integer selEndScoreByScore(String planId, double big, double small, int orgId, int accountOrgId, Double notScore, int sex);

    /**
     * 查询项目占比
     */
    Integer selProProportion( String planId, Integer proId);

    /**
     * 计算成绩满分为多少
     */
    double FullMark(EllScoreDetailsVo details, List<EllPlan> ellPlanList, List<EllPlanProjectChance> ellPlanProjectChanceListMust);
}
