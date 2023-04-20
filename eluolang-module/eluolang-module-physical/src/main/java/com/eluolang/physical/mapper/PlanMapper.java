package com.eluolang.physical.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.physical.dto.DepartTreeDto;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanMapper {
    int createPlan(@Param("ellPlan") EllPlan ellPlan);

    /**
     * 通过条件进行计数
     */
    int findProceedPlan(@Param("planState") String planState, @Param("dateBegin") String dateBegin, @Param("orgId") String orgId);

    /**
     * 通过条件1.通过id 2.通过测试机构查询到该机构下所有下级都使用该计划 3.通过状态 4.是否需要预约
     */
    List<EllPlan> findPlan(@Param("id") String id, @Param("orgId") String orgId, @Param("planState") String planState, @Param("isSubscribe") String isSubscribe);

    List<EllPlan> findPlanVideo(@Param("id") String id, @Param("orgId") String orgId, @Param("planState") String planState, @Param("isSubscribe") String isSubscribe, @Param("year") int year, @Param("toYear") int toYear);

    /**
     * 查询计划
     */
    List<EllPlanVo> findOrgPlan(@Param("orgId") String orgId, @Param("isExam") int isExam, @Param("accountOrgId") int accountOrgId, @Param("status") Integer status);

    /**
     * 查询计划各个状态的人数
     */
    List<EllPlanStatusNumVo> selPlanStatusNum(@Param("orgId") String orgId, @Param("isExam") int isExam, @Param("accountOrgId") int accountOrgId, @Param("status") Integer status);

    /**
     * 判断计划名是否重复
     */
    int findOrgPlanByName(String planName);

    /**
     * 查询部门是否拥有发布计划
     */
    int findPlanIssue(@Param("planId") String planId, @Param("orgId") String orgId);

    /**
     * 修改计划
     */
    int updatePlan(@Param("updatePlanVo") UpdatePlanVo updatePlanVo, @Param("updateTime") String updateTime);

    /**
     * 物理删除计划
     */
    int deletePlanSys(int id);

    /**
     * 逻辑删除计划
     *
     * @param id
     * @return
     */
    int deletePlan(@Param("id") String id, @Param("createById") String createById);

    /**
     * 添加计划使用部门
     */
    int addUseOrg(@Param("planId") String planId, @Param("orgId") String orgId, @Param("useRule") String useRule, @Param("parentId") String parentId);

    /**
     * 批量添加计划使用部门sql
     */
    int addBatchUseOrg(@Param("ellUseOrgMysql") List<EllUseOrgVo> ellUseOrgMysql);

    /**
     * 修改使用部门计划
     */
    int updateUseOrg(@Param("planId") String planId, @Param("orgId") String orgId, @Param("createById") String createById, @Param("useOrgPlan") String useOrgPlan);

    /**
     * 删除使用部门
     */
    int deleteUseOrg(@Param("planId") String planId, @Param("orgId") String orgId);

    /**
     * 删除计划所有的使用部门
     */
    int deletePlanUseOrg(@Param("planId") String planId);

    /**
     * 查询是否已经拥有该部门
     */
    int findIsHas(@Param("planId") String planId, @Param("orgId") String orgId);

    /**
     * 查询已经计划使用的的部门
     */
    List<EllUseOrg> findPlanUseOrg(@Param("planId") String planId, @Param("type") int type);

    /**
     * 修改计划使用部门使用的规则
     */
    int upPlanUseOrgRule(@Param("id") int id, @Param("useRule") String useRule);

    /**
     * 设置这个计划的项目测机会
     */
    int createProChance(@Param("createChanceVos") List<CreateChanceVo> createChanceVos);

    /**
     * 修改这个计划的项目测试机会
     */
    int updateProChance(@Param("id") String id, @Param("chance") int chance, @Param("updateById") String updateById, @Param("useSex") String useSex, @Param("time") int time, @Param("essential") String essential, @Param("proGdp") String proGdp);

    /**
     * 查询这个计划下项目的测试机会/可不查视力项目
     */
    List<EllPlanProjectChance> findProChance(@Param("planId") String planId, @Param("isFindEyes") Boolean isFindEyes);

    /**
     * 查询这个计划下的项目个数/可分男女
     */
    int findPro(@Param("planId") String planId, @Param("useSex") String useSex);

    /**
     * 查询这个计划下项目的测试机会
     */
    List<EllPlanProjectChance> isHasPlanChance(@Param("planId") String planId, @Param("proId") String proId);

    /**
     * 减少项目，把关联计划的该项目的测试机会也进行删除
     *
     * @param id
     * @return
     */
    int deleteProChance(String id);

    /**
     * 删除这个计划所有的项目测试机会
     */
    int deleteAllProChance(String planId);

    /**
     * 删除这个计划某些部门的使用规则
     */
    int deleteUseOrgId(@Param("planId") String planId, @Param("ellUseOrgs") List<EllUseOrgVo> ellUseOrgs);

    /**
     * 删除使用部门
     */
    int deleteAllUseOrg(@Param("planId") String planId);

    /**
     * 添加预约时间
     */
    int addEllTestAppointment(@Param("ellTestAppointments") List<EllTestAppointment> ellTestAppointments);

    /**
     * 删除预约时间
     */
    int deleteEllTestAppointment(String planId);

    /**
     * 查询预约时间
     */
    List<EllTestAppointment> findAppointment(String planId);

    /**
     * 添加计划算分占比
     */
    int addPlanScoreOf(@Param("planId") String planId, @Param("ellPlanScoresOfs") List<EllPlanScoresOfVo> ellPlanScoresOfs, @Param("createTime") String createTime, @Param("updateTime") String updateTime);

    /**
     * 删除计划算分占比
     */
    int deletePlanScoreOf(String planId);

    /**
     * 查询这个计划占比
     *
     * @param planId
     * @return
     */
    List<EllPlanScoresOfVo> findEllPlanScoreOf(String planId);

    /**
     * 查询可自选项
     * 分男女性别为空就都查
     */
    List<FindOptionalProjectVo> findOptionalProject(@Param("planId") String planId, @Param("sex") String sex, @Param("proId") int proId);

    /**
     * 添加自选项目
     */
    int addOptionalProject(@Param("optionalVoList") List<BatchOptionalVo> optionalVoList, @Param("planId") String planId, @Param("createTime") String createTime);

    /**
     * 查询已经选择的自选项
     */
    List<EllFindSelectedPro> findProjectOptional(@Param("planId") String planId, @Param("userId") String userId, @Param("proId") int proId);

    /**
     * 查询已选择项目个数
     */
    int findSelectedNum(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 查询计划项目必选项
     */
    List<EllPlanProjectChance> findMustProject(@Param("planId") String planId, @Param("userSex") String userSex);

    /**
     * 查询计划预约总人数(计划使用班级总人数)
     */
    int selClassPeopleNum(@Param("ellUseOrgs") List<EllUseOrgVo> ellUseOrgs);

    /**
     * 删除预约
     */
    int deletePlanUser(String planId);

    /**
     * 删除项目所有使用部门
     */
    int deleteUseDep(String planId);

    /**
     * 删除免测申请
     */
    int deleteAvoid(String planId);

    /**
     * 删除免测申请的项目
     */
    int deleteAvoidPro(String planId);

    /**
     * 定时修改时间到期的比赛状态
     */
    int timingUpdatePlanState(String dateNow);

    /**
     * 查询到下级部门
     */
    List<EllUseOrgVo> selPfDepart(int orgId);

    /**
     * 查询计划使用部门
     */
    List<DepartTreeDto> selPlanDepart(@Param("accountId") String accountId, @Param("planId") String planId, @Param("orgId") Integer orgId);

    /**
     * 查询计划选择的部门
     */
    List<EllUserPlanDepartment> selPlanUseDepartment(@Param("accountId") String accountId, @Param("planId") String planId);

    /**
     * 通过id查询计划
     */
    EllPlan selPlan(String planId);

    /**
     * 查询每个项目选择多少人
     */
    int selOptionPeopleNum(@Param("planId") String planId, @Param("proId") String proId);

    /**
     * 通过计划id查询有成绩的学生id
     */
    List<EllTestHistory> selHistory(String planId);

    /**
     * 查询用户改计划的所有视频
     */
    List<EllVideoVo> selVideoUser(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 通过id查询部门
     */
    DepartTreeDto selDepartById(int id);

    /**
     * 查询计划总人数
     */
    int selPlanTotalNum(String plan);
}
