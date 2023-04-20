package com.eluolang.physical.mapper;

import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.*;
import com.eluolang.physical.dto.ClassScoreDto;
import com.eluolang.physical.dto.EllFullScoreDto;
import com.eluolang.physical.dto.EllProAvgScoreDto;
import com.eluolang.physical.dto.EllTotalScoreDto;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhysicalDataMapper {
    /**
     * 根据部门id集合查询部门信息
     *
     * @param deptIdList
     * @return
     */
    List<PfDepart> selDepartByIdList(@Param("deptIdList") List<Integer> deptIdList);

    /**
     * 根据部门id集合查询为学校级的部门
     *
     * @param deptIdList
     * @return
     */
    List<PfDepartVo> selIsSchoolByIdList(@Param("idList") List<Integer> deptIdList);

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
    int selUserCountByDeptIdList(@Param("ids") List<Integer> deptIdList);

    /**
     * 根据部门id集合查询学生考试完成人数
     *
     * @param deptIdList
     * @return
     */
    int selUserTestSuccessCount(@Param("planId") String planId, @Param("deptIds") List<Integer> deptIdList);

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
    EachProjectScoreVo selEachProjectScore(@Param("planId") String planId, @Param("proId") Integer proId, @Param("deptId") List<Integer> deptId);

    /**
     * 查询学校得分情况
     *
     * @param deptId
     * @param planId
     * @return
     */
    SchoolScoreVo selSchoolScore(@Param("deptIds") List<Integer> deptId, @Param("planId") String planId);

    /**
     * 根据计划id查询各个分值人数
     *
     * @param planId
     * @return
     */
    PlanEachScoreVo selPlanEachScore(@Param("deptIdList") List<Integer> deptIdList, @Param("planId") String planId);

    /**
     * 根据计划id查询确认成绩人数
     *
     * @param planId
     * @return
     */
    ConfirmAchievementVo selConfirmAchievement(@Param("deptIdList") List<Integer> deptIdList, @Param("planId") String planId);

    /**
     * 根据计划id查询各项目重测人数
     *
     * @param planId
     * @return
     */
    int selEachRetestNumber(@Param("deptIdList") List<Integer> deptIdList, @Param("planId") String planId, @Param("proId") Integer proId);

    /**
     * 查询该账户有权限部门的总人数
     */
    int findNumberOfPeople(@Param("accountId") int accountId, @Param("path") String path);

    /**
     * 查询该账户有权限部门考试总人数
     */
    int findExamNumberOfPeople(@Param("accountId") int accountId, @Param("planId") String planId);

    /**
     * 查询该账户有权限部门已考试完成人数
     */
    int findExamFinishNumberOfPeople(@Param("accountId") int accountId, @Param("planId") String planId);

    /**
     * 查询该账户有权限部门考试重测人数
     */
    List<String> findRetestNumberOfPeople(@Param("accountId") int accountId, @Param("planId") String planId);

    /**
     * 查询该账户有权限部门考试男女人数
     */
    int findExamSexNumberOfPeople(@Param("accountId") int accountId, @Param("planId") String planId, @Param("sex") int sex);

    //体测

    /**
     * 查询该账户在计划下有权限的班级数量
     */
    int findClassNumberOf(@Param("accountId") int accountId, @Param("planId") String planId);

    /**
     * 查询免考人数
     */
    int findFreeTest(@Param("accountId") int accountId, @Param("planId") String planId);

    /**
     * 重考人数
     */
    List<String> findRetestNumberOf(@Param("accountId") int accountId, @Param("planId") String planId);

    /**
     * 考试完成人数的不合格人数
     */
    int findDisQualifiedNumberOf(@Param("accountId") int accountId, @Param("planId") String planId);

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
    int findProjectTestNumberOf(@Param("accountId") int accountId, @Param("planId") String planId, @Param("proId") int proId, @Param("orgId") int orgId);

    /**
     * 单项的项目重测人数
     */
    int findProjectRestTestNumberOf(@Param("accountId") int accountId, @Param("planId") String planId, @Param("proId") int proId, @Param("orgId") int orgId);

    /**
     * 单项的项目分性别测试人数
     */
    int findProjectSexTestNumberOf(@Param("accountId") int accountId, @Param("planId") String planId, @Param("proId") int proId, @Param("userSex") int userSex, @Param("orgId") int orgId);

    /**
     * 得分总体情况
     */
    int findTotalityCondition(@Param("accountId") int accountId, @Param("planId") String planId, @Param("scoreCondition") String scoreCondition, @Param("userSex") int userSex, @Param("proId") int proId, @Param("orgId") int orgId);

    /**
     * 需要测试该项目的人数分男女
     */
    int findProjectNumberOf(@Param("accountId") int accountId, @Param("planId") String planId, @Param("proId") int proId, @Param("userSex") int userSex, @Param("orgId") int orgId);

    /**
     * 查询某一个项目某一分数段的人数
     */
    List<EllFullScoreDto> selFllScore(@Param("accountId") int accountId, @Param("planId") String planId, @Param("proId") int proId, @Param("userSex") int userSex, @Param("orgId") int orgId, @Param("score") double score);

    /**
     * 根据管理员id和计划id查询学校信息
     *
     * @param optId
     * @param planId
     */
    List<PfDepart> selSchoolInformation(@Param("optId") Integer optId, @Param("planId") String planId);

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
     * @return
     */
    List<ClassScoreVo> selEachClassScore(@Param("deptList") List<Integer> deptList);

    /**
     * 查询各个班级人数情况
     *
     * @param deptList
     * @return
     */
    List<ClassNumberVo> selEachClassNumber(@Param("deptList") List<Integer> deptList);

    /**
     * 按条件查询学校男女人数情况
     *
     * @param deptList
     * @return
     */
    SchoolNumberVo selSchoolNumber(@Param("deptList") List<Integer> deptList);

    /**
     * 查询学校path
     */
    List<String> selSchoolPath(String planId);

    /**
     * 查询某个部门的某个项目的平均分
     */
    EllProAvgScoreDto selProAvgScore(@Param("planId") String planId, @Param("accountOrgId") int accountOrgId, @Param("orgId") int orgId, @Param("proId") int proId, @Param("sex") int sex);

    /**
     * 查询部门总分
     */
    EllTotalScoreDto selSchoolTotalScore(@Param("planId") String planId, @Param("orgId") int orgId, @Param("accountOrgId") int accountOrgId, @Param("sex") int sex);

    /**
     * 查询某一部门某账号下参与该计划的人数
     */
    int selPlanOrgIdPeopleNum(@Param("sex") int sex, @Param("planId") String planId, @Param("orgId") int orgId, @Param("accountOrgId") int accountOrgId);

    /**
     * 查询某一部门某账号下最终分高于多少的人
     */
    int selFullEndScorePeople(@Param("sex") int sex, @Param("planId") String planId, @Param("orgId") int orgId, @Param("accountOrgId") int accountOrgId, @Param("score") double score);

    /**
     * 查询规则每个阶段的分数
     */
    List<EllScoreRule> selRuleScore(@Param("evaluation") String evaluation, @Param("planId") String planId, @Param("proId") int proId, @Param("orgId") int orgId);

    /**
     * 查询项目计划id
     */
    List<EllPlanProjectChance> selPlanProId(@Param("planId") String planId, @Param("isMust") int isMust, @Param("sex") String sex, @Param("proId") int proId);

    /**
     * 查询项目占比
     */
    Integer selProProportion(@Param("planId")String planId,@Param("proId")Integer proId);

    /**
     * 查询分数每个阶段的人数
     */
    Integer selEndScoreByScore(@Param("planId") String planId, @Param("big") double big, @Param("small") double small, @Param("orgId") int orgId, @Param("accountOrgId") int accountOrgId, @Param("notScore") Double notScore, @Param("sex") int sex);
}
