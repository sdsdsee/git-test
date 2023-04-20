package com.eluolang.physical.service.impl;

import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.physical.dto.DepartTreeDto;
import com.eluolang.physical.mapper.PlanMapper;
import com.eluolang.physical.mapper.ProjectMapper;
import com.eluolang.physical.mapper.RulesMapper;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.PlanService;
import com.eluolang.physical.util.CreateTime;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

@Transactional
@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private RulesMapper rulesMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Transactional
    @Override
    public int createPlan(PlanVo planVo, List<CreateChanceVo> createChanceVos) throws ParseException {
        EllPlan ellPlan = new EllPlan();
        //创建时间
        ellPlan.setCreateTime(CreateTime.timeDay());
        //创建人id
        ellPlan.setCreateById(planVo.getCreateById());
        //创建人路径
        ellPlan.setOrgPath(planVo.getOrgId());
        //计划id
        String id = IdUtils.fastSimpleUUID();
        ellPlan.setId(id);
        //实施地点
        ellPlan.setPlanAddr(planVo.getPlanAddr());
        //计划的状态
        ellPlan.setPlanState(1);
        ellPlan.setOrgId(planVo.getOrgId());
        //计划的说明
        ellPlan.setPlanRemark(planVo.getPlanRemark());
        //计划开始时间
        ellPlan.setDateBegin(planVo.getDateBegin());
        //计划结束时间
        ellPlan.setDateEnd(planVo.getDateEnd());
        //计划标题
        ellPlan.setPlanTitle(planVo.getPlanTitle());
        //重测次数
        ellPlan.setAuthNum(planVo.getAuthNum());
        //是否可以重测
        ellPlan.setIsResurvey(planVo.getIsResurvey());
        //重测是否需要管理员认证
        ellPlan.setIsAuthentication(planVo.getIsAuthentication());
        //计划需要测试的项目
        ellPlan.setProId(planVo.getProId());
        //测试项目计分规则
        ellPlan.setUseRules(planVo.getUseRules());
        //是否需要预约
        ellPlan.setIsSubscribe(planVo.getIsSubscribe());
        //是否是考试
        ellPlan.setIsExam(planVo.getIsExam());
        //设置可选项目个数
        ellPlan.setCustomizeTheNumber(planVo.getCusTheNumber());
        ellPlan.setIsView(planVo.getIsView());

        //查询项目名称设置默认测试机会
        for (int i = 0; i < createChanceVos.size(); i++) {
            createChanceVos.get(i).setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            createChanceVos.get(i).setPlanId(id);
        }
        defaultChance(createChanceVos);
        //设置本次计划的占比，赋值id
        for (int i = 0; i < planVo.getEllPlanScoresOfs().size(); i++) {
            if (planVo.getEllPlanScoresOfs().get(i).getIsThis() == 1) {
                planVo.getEllPlanScoresOfs().get(i).setProportionPlanId(id);
            }
        }
        planMapper.addPlanScoreOf(id, planVo.getEllPlanScoresOfs(), null, CreateTime.getTime());
        //如果是测试计划就进行添加预约时间
        if (ellPlan.getIsExam() == 2 && ellPlan.getIsSubscribe().equals("1")) {
            for (int i = 0; i < planVo.getEllTestAppointments().size(); i++) {
                planVo.getEllTestAppointments().get(i).setPlanId(id);
                planVo.setAuthNum(planVo.getAuthNum() + planVo.getEllTestAppointments().get(i).getNum());
            }
            planMapper.addEllTestAppointment(planVo.getEllTestAppointments());
            //预约总人数
            ellPlan.setAuthNum(0);
        }
        return planMapper.createPlan(ellPlan);
    }

    @Override
    public int findProceedPlan(String planState, String dateBegin, String createByPath) {
        return planMapper.findProceedPlan(planState, dateBegin, createByPath);
    }

    @Override
    public List<EllPlan> findPlan(String id, String orgId, String planState, String isSubscribe) {
        return planMapper.findPlan(id, orgId, planState, isSubscribe);
    }

    @Override
    public List<EllPlan> findPlanVideo(String id, String orgId, String planState, String isSubscribe, int year, int toYear) {
        return planMapper.findPlanVideo(id, orgId, planState, isSubscribe, year, toYear);
    }

    @Transactional
    @Override
    public int updatePlan(UpdatePlanVo updatePlanVo) throws ParseException, SQLException {
        //修改了计划的项目，把删除的项目的默认机会删除，新增的进行增加
        updateChaneNum(updatePlanVo);
        if (updatePlanVo.getIsExam() == 2) {
            //删除
            planMapper.deleteEllTestAppointment(updatePlanVo.getId());
            //修改
            if (updatePlanVo.getIsSubscribe().equals("1")) {
                updatePlanVo.getEllTestAppointments().stream().forEach(ellTestAppointment -> ellTestAppointment.setPlanId(updatePlanVo.getId()));
                updatePlanVo.getEllTestAppointments().stream().forEach(ellTestAppointment -> ellTestAppointment.setCreateBy(updatePlanVo.getUpdateById()));
                planMapper.addEllTestAppointment(updatePlanVo.getEllTestAppointments());
            }
        } else {
            //修改计划占比，先删除然后再重新添加
            //删除
            planMapper.deletePlanScoreOf(updatePlanVo.getId());
            //添加
            planMapper.addPlanScoreOf(updatePlanVo.getId(), updatePlanVo.getEllPlanScoresOfs(), null, CreateTime.getTime());
        }
        //删除预约
        planMapper.deletePlanUser(updatePlanVo.getId());
        return planMapper.updatePlan(updatePlanVo, CreateTime.getTime());
    }

    @Transactional
    //修改了计划的项目，把删除的项目的默认机会删除，新增的进行增加
    public void updateChaneNum(UpdatePlanVo updatePlanVo) {
        //第一种
        //查询到原先此项目需要测试项目
        List<EllPlanProjectChance> yList = planMapper.findProChance(updatePlanVo.getId(), true);
        Map<Integer, EllPlanProjectChance> ellDeleteChanceMap = new HashMap<>();
        //把查询的机会放入map中
        for (int i = 0; i < yList.size(); i++) {
            ellDeleteChanceMap.put(yList.get(i).getProId(), yList.get(i));
        }

        for (int i = 0; i < updatePlanVo.getCreateChanceVos().size(); i++) {
            //遍历传的项目机会
            int s = updatePlanVo.getCreateChanceVos().get(i).getProId();
            //判断原先数据库是否拥有这个项目以及机会
            Boolean isHsChance = ellDeleteChanceMap.containsKey(s);
            if (!isHsChance) {
                //如果新传上来的没有就进行添加
                UpdatePlanVo createPlanVo = new UpdatePlanVo();
                List<CreateChanceVo> createChanceVos = new ArrayList<>();
                //设置id
                updatePlanVo.getCreateChanceVos().get(i).setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                createChanceVos.add(updatePlanVo.getCreateChanceVos().get(i));
                planMapper.createProChance(createChanceVos);
            } else {
                //有就进行修改
                planMapper.updateProChance(ellDeleteChanceMap.get(s).getId(), updatePlanVo.getCreateChanceVos().get(i).getChance(), updatePlanVo.getUpdateById(), updatePlanVo.getCreateChanceVos().get(i).getUseSex(), updatePlanVo.getCreateChanceVos().get(i).getTime(), updatePlanVo.getCreateChanceVos().get(i).getEssential(), updatePlanVo.getCreateChanceVos().get(i).getProGdp());
            }
            //然后把和原先一样拥有的项目从map中移除掉留在map中的就是要进行原先有现在没有的项目
            ellDeleteChanceMap.remove(updatePlanVo.getCreateChanceVos().get(i).getProId());
        }
        //把现在没有的项目进行删除
        int i = 0;
        for (Integer key : ellDeleteChanceMap.keySet()) {

            planMapper.deleteProChance(ellDeleteChanceMap.get(key).getId());
        }
        //第二种就是把这个表相关的数据全部删除再添加
    }

    @Transactional
    @Override
    public int deletePlanSys(int id) {
        return planMapper.deletePlanSys(id);
    }

    @Transactional
    @Override
    public int deletePlan(String id, String createById) {
        //删除分数占比
        planMapper.deletePlanScoreOf(id);
        //计划预约
        planMapper.deletePlanUser(id);
        //删除项目机会
        planMapper.deleteAllProChance(id);
        //删除项目使用部门
        planMapper.deleteUseDep(id);
        //删除免测申请项目
        planMapper.deleteAvoidPro(id);
        //删除免测项目
        planMapper.deleteAvoid(id);
        return planMapper.deletePlan(id, createById);
    }

    @Transactional
    @Override
    public int finishPlan(String id, String planState, String createById, String orgId) throws ParseException {

        UpdatePlanVo updatePlanVo = new UpdatePlanVo();
        updatePlanVo.setId(id);
        updatePlanVo.setUpdateById(createById);
        updatePlanVo.setUpdateState(planState);
        updatePlanVo.setCusTheNumber(-1);
        return planMapper.updatePlan(updatePlanVo, CreateTime.getTime());
    }

    @Transactional
    @Override
    public int updateUseOrg(List<EllUseOrgVo> ellUseOrgs) {
        //先全部删除再添加
        planMapper.deleteAllUseOrg(ellUseOrgs.get(0).getPlanId());
        if (ellUseOrgs != null && ellUseOrgs.size() > 0) {
            for (int i = 0; i < ellUseOrgs.size(); i++) {
                List<EllUseOrgVo> ellUseOrgVoList = planMapper.selPfDepart(ellUseOrgs.get(i).getOrgId());
                int k = i;
                ellUseOrgVoList.get(0).setIsThis(1);
                ellUseOrgVoList.stream().forEach(ellUseOrgVo -> {
                    ellUseOrgVo.setPlanId(ellUseOrgs.get(k).getPlanId());
                    ellUseOrgVo.setUseRule(ellUseOrgs.get(k).getUseRule());
                    ellUseOrgVo.setCreateId(ellUseOrgs.get(k).getCreateId());
                });
                //当i大于0的时候就才进行删除
                //将有下级有单独选择规则的部门把前面的添加的删除添加
                if (i > 0) {
                    planMapper.deleteUseOrgId(ellUseOrgs.get(i).getPlanId(), ellUseOrgVoList);
                }
                planMapper.addBatchUseOrg(ellUseOrgVoList);
            }

        }
        return 1;
    }
   /* @Transactional
    @Override
    public int updateUseOrg(List<EllUseOrgVo> ellUseOrgs) {
        int count = 0;
        if (ellUseOrgs != null) {
            List<EllUseOrg> list = planMapper.findPlanUseOrg(ellUseOrgs.get(0).getPlanId());
            List<EllUseOrgVo> ellUseOrgMysql = new ArrayList<>();
            Map<Integer, EllUseOrg> ellUseOrgMap = new HashMap<>();
            //封装进hashMap
            for (int i = 0; i < list.size(); i++) {
                ellUseOrgMap.put(list.get(i).getOrgId(), list.get(i));
            }
            for (int i = 0; i < ellUseOrgs.size(); i++) {
                //查看是否已经拥有
                boolean isHas = ellUseOrgMap.containsKey(ellUseOrgs.get(i).getOrgId());
                if (!isHas) {
                    //为拥有就放入list进行批量添加
                    ellUseOrgMysql.add(ellUseOrgs.get(i));
                   *//* //未拥有就进行添加
                    addUseOrg(ellUseOrgs.get(i).getPlanId(), String.valueOf(ellUseOrgs.get(i).getOrgId()), ellUseOrgs.get(i).getCreateId());*//*
                } else {
                    //已经拥有就进行修改
                    upPlanUseOrgRule(ellUseOrgMap.get(ellUseOrgs.get(i).getOrgId()).getId(), ellUseOrgs.get(i).getUseRule());
                }
                //把现在和以前拥有的从map中移除
                ellUseOrgMap.remove(ellUseOrgs.get(i).getOrgId());

            }
            //把以前有现在没有的进行删除
            for (Integer key : ellUseOrgMap.keySet()) {
                planMapper.deleteUseOrg(ellUseOrgMap.get(key).getPlanId(),
                        String.valueOf(ellUseOrgMap.get(key).getOrgId()));
            }
            //批量添加
            if (ellUseOrgMysql.size() > 0) {
                planMapper.addBatchUseOrg(ellUseOrgMysql);
            }
            count++;
            int authNum = planMapper.selClassPeopleNum(ellUseOrgs);
            UpdatePlanVo updatePlanVo = new UpdatePlanVo();
            updatePlanVo.setUpdateById(ellUseOrgs.get(0).getCreateId());
            updatePlanVo.setId(ellUseOrgs.get(0).getPlanId());
            updatePlanVo.setAuthNum(String.valueOf(authNum));
            updatePlanVo.setCusTheNumber(-1);
            planMapper.updatePlan(updatePlanVo, CreateTime.getTime());
        } else {
            //传为空就全部删除
            count++;
            planMapper.deleteAllProChance(ellUseOrgs.get(0).getPlanId());
        }

        return count;
    }*/

    @Transactional
    @Override
    public int addUseOrg(String planId, String orgId, String createById, String useRule, String parentId) {
        int isHas = planMapper.findIsHas(planId, orgId);
        if (isHas == 0) {
            int j = planMapper.addUseOrg(planId, orgId, useRule, parentId);
            if (j < 0) {
                return 0;
            }
        }


        return 1;
    }


    @Override
    public List<EllPlanVo> findOrgPlan(int page, String orgId, int isExam, int accountOrgId, Integer status) {
        //判断是会否查所有
        if (page >= 0) {
            PageHelper.startPage(page, 10);
        }
        return planMapper.findOrgPlan(orgId, isExam, accountOrgId, status);
    }

    @Override
    public List<EllPlanStatusNumVo> selPlanStatusNum(String orgId, int isExam, int accountOrgId, Integer status) {
        return planMapper.selPlanStatusNum(orgId, isExam, accountOrgId, status);
    }

    @Override
    public int updateProChance(String id, int chance, String updateById, String useSex, int time, String essential, String proportion) {
        return planMapper.updateProChance(id, chance, updateById, useSex, time, essential, proportion);
    }

    @Override
    public List<EllPlanProjectChance> findProChance(String planId, Boolean isFindEyes) {
        return planMapper.findProChance(planId, isFindEyes);
    }

    @Override
    public List<EllUseOrg> findPlanUseOrg(String planId, int type) {
        return planMapper.findPlanUseOrg(planId, type);
    }


    @Override
    public int upPlanUseOrgRule(int id, String useRule) {
        return planMapper.upPlanUseOrgRule(id, useRule);
    }

    @Override
    public int findPlanIssue(String planId, String orgId) {
        return planMapper.findPlanIssue(planId, orgId);
    }

    @Override
    public List<EllTestAppointment> findAppointment(String planId) {
        return planMapper.findAppointment(planId);
    }

    @Override
    public List<EllPlanScoresOfVo> findEllPlanScoreOf(String planId) {
        return planMapper.findEllPlanScoreOf(planId);
    }

    @Override
    public List<FindOptionalProjectVo> findOptionalProject(String planId, String sex, int proId) {
        return planMapper.findOptionalProject(planId, sex, proId);
    }

    @Override
    public int addOptionalProject(List<BatchOptionalVo> optionalVoList, String planId) {
        return planMapper.addOptionalProject(optionalVoList, planId, CreateTime.getTime());
    }

    @Override
    public List<EllFindSelectedPro> findProjectOptional(String planId, String userId, int orgId) {
        return planMapper.findProjectOptional(planId, userId, orgId);
    }

    @Override
    public int findSelectedNum(String planId, String userId) {
        return planMapper.findSelectedNum(planId, userId);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public int timingUpdatePlanState() throws ParseException {
        return planMapper.timingUpdatePlanState(CreateTime.timeDay());
    }

    @Override
    public List<DepartTreeDto> selPlanDepart(String accountId, String planId, Integer orgId) {
        return planMapper.selPlanDepart(accountId, planId, orgId);
    }

    @Override
    public List<EllUserPlanDepartment> selPlanUseDepartment(String accountId, String planId) {
        return planMapper.selPlanUseDepartment(accountId, planId);
    }

    @Override
    public EllPlan selPlan(String planId) {
        return planMapper.selPlan(planId);
    }

    @Override
    public int selOptionPeopleNum(String planId, String proId) {
        return planMapper.selOptionPeopleNum(planId, proId);
    }

    @Override
    public List<EllPlanProjectChance> findMustProject(String planId, String userSex) {
        return planMapper.findMustProject(planId, userSex);
    }

    @Override
    public int findPro(String planId, String useSex) {
        return planMapper.findPro(planId, useSex);
    }

    @Override
    public List<EllTestHistory> selHistory(String planId) {
        return planMapper.selHistory(planId);
    }

    @Override
    public List<EllVideoVo> selVideoUser(String planId, String userId) {
        return planMapper.selVideoUser(planId, userId);
    }

    @Override
    public DepartTreeDto selDepartById(int id) {
        return planMapper.selDepartById(id);
    }

    @Override
    public List<EllUseOrgVo> selPfDepart(int orgId) {
        return planMapper.selPfDepart(orgId);
    }

    @Override
    public int selPlanTotalNum(String plan) {
        return planMapper.selPlanTotalNum(plan);
    }

    @Transactional
    //设置默认测试机会
    public void defaultChance(List<CreateChanceVo> createChanceVos) {
        if (createChanceVos.get(0).getCreateById() != null && createChanceVos.get(0).getCreateById() != "") {
            planMapper.createProChance(createChanceVos);
        } else {
            throw new NullPointerException("参数错误参数不全！");
        }
    }
}
