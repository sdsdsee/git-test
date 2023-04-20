package com.eluolang.physical.service;

import com.eluolang.common.core.pojo.*;
import com.eluolang.physical.dto.DepartTreeDto;
import com.eluolang.physical.model.CreateChanceVo;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface PlanService {
    int createPlan(PlanVo planVo, List<CreateChanceVo> createChanceVos) throws ParseException;

    /**
     * 通过条件进行条数查询个数
     *
     * @param planState    状态 1待发布2正在进行3已结束
     * @param dateBegin    开始时间
     * @param createByPath 创建人路径
     * @return
     */
    int findProceedPlan(String planState, String dateBegin, String createByPath);

    /**
     * 通过条件进行条数查询
     *
     * @param id        计划id
     * @param orgId     部门id
     * @param planState 状态 1待发布2正在进行3已结束
     * @return
     */
    List<EllPlan> findPlan(String id, String orgId, String planState, String isSubscribe);

    List<EllPlan> findPlanVideo(String id, String orgId, String planState, String isSubscribe, int year, int toYear);

    /**
     * 修改条件
     *
     * @param updatePlanVo 修改
     * @return
     */
    int updatePlan(UpdatePlanVo updatePlanVo) throws ParseException, SQLException;

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
    int deletePlan(String id, String createById);

    /**
     * 结束计划（与修改计划用的同一个sql）/修改计划状态
     *
     * @param id        计划id
     * @param planState 状态1待发布，2正在进行，3结束
     * @return
     * @throws ParseException
     */
    int finishPlan(String id, String planState, String createById, String orgId) throws ParseException;

    /**
     * 删除/修改/新增计划选择使用的部门以及其使用的规则
     */
    int updateUseOrg(List<EllUseOrgVo> ellUseOrgs);

    /**
     * 新增计划使用部门单个
     */
    int addUseOrg(String planId, String orgId, String createById, String useRule, String parentId);

    /**
     * 查询计划
     */
    List<EllPlanVo> findOrgPlan(int page, String orgId, int isExam, int accountOrgId, Integer status);
    /**
     * 查询计划各个状态的人数
     */
    List<EllPlanStatusNumVo> selPlanStatusNum(String orgId,int isExam, int accountOrgId, Integer status);
    /**
     * 修改这个计划的项目测试机会和男女使用
     */
    int updateProChance(String id, int chance, String updateById, String useSex, int time, String essential, String proportion);

    /**
     * 查询这个计划下项目的测试机会
     */
    List<EllPlanProjectChance> findProChance(String planId, Boolean isFindEyes);

    /**
     * 查询已经计划使用的的部门
     */
    List<EllUseOrg> findPlanUseOrg(String planId, int type);

    /**
     * 修改计划使用部门使用的规则
     */
    int upPlanUseOrgRule(int id, String useRule);

    /**
     * 查询部门是否拥有发布计划
     */
    int findPlanIssue(String planId, String orgId);

    /**
     * 查询预约时间
     */
    List<EllTestAppointment> findAppointment(String planId);

    /**
     * 查询这个计划占比
     *
     * @param planId
     * @return
     */
    List<EllPlanScoresOfVo> findEllPlanScoreOf(String planId);

    /**
     * 查询自选项
     * 分男女性别为空就都查
     */
    List<FindOptionalProjectVo> findOptionalProject(String planId, String sex, int proId);

    /**
     * 添加自选项目
     */
    int addOptionalProject(List<BatchOptionalVo> optionalVoList, String planId);

    /**
     * 查询已经选择的自选项
     */
    List<EllFindSelectedPro> findProjectOptional(String planId, String userId, int orgId);

    /**
     * 查询已选择项目个数
     */
    int findSelectedNum(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 定时修改时间到期的比赛状态
     */
    int timingUpdatePlanState() throws ParseException;

    /**
     * 查询计划使用部门
     */
    List<DepartTreeDto> selPlanDepart(String accountId, String planId, Integer orgId);

    /**
     * 查询计划选择的部门
     */
    List<EllUserPlanDepartment> selPlanUseDepartment(String accountId, String planId);

    /**
     * 通过id查询计划
     */
    EllPlan selPlan(String planId);

    /**
     * 查询每个项目选择多少人
     */
    int selOptionPeopleNum(String planId, String proId);

    /**
     * 查询计划项目必选项
     */
    List<EllPlanProjectChance> findMustProject(String planId, String userSex);

    /**
     * 查询这个计划下的项目个数/可分男女
     */
    int findPro(String planId, String useSex);

    /**
     * 通过计划id查询有成绩的学生id
     */
    List<EllTestHistory> selHistory(String planId);

    /**
     * 查询用户改计划的所有视频
     */
    List<EllVideoVo> selVideoUser(String planId, String userId);

    /**
     * 通过id查询部门
     */
    DepartTreeDto selDepartById(int id);

    /**
     * 查询到下级部门
     */
    List<EllUseOrgVo> selPfDepart(int orgId);

    /**
     * 查询计划总人数
     */
    int selPlanTotalNum(String plan);
}
