package com.eluolang.physical.service.impl;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllAvoidProject;
import com.eluolang.common.core.pojo.EllPlan;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.physical.dto.EllAvoidExportDto;
import com.eluolang.physical.mapper.AvoidTestMapper;
import com.eluolang.physical.mapper.EllUserMapper;
import com.eluolang.physical.model.*;
import com.eluolang.physical.redis.AvoidRedis;
import com.eluolang.physical.service.EllAvoidTestService;
import com.eluolang.physical.util.CreateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class EllAvoidTestImpl implements EllAvoidTestService {
    @Autowired
    private AvoidTestMapper avoidTestMapper;

    @Autowired
    private AvoidRedis avoidRedis;
    @Autowired
    private EllUserMapper ellUserMapper;

    @Transactional
    @Override
    public int addAvoidApply(AvoidApplyVo avoidApply) {
        //查询学生
        EllUser ellUser = avoidTestMapper.selUser(avoidApply.getUserId());
        avoidApply.setId(IdUtils.fastSimpleUUID());
        avoidApply.setOrgId(Integer.parseInt(ellUser.getOrgId()));
        avoidApply.setCreateTime(CreateTime.getTime());
        avoidApply.setSubmitTime(CreateTime.getTime());
        int i = avoidTestMapper.addAvoidApply(avoidApply);
        avoidApply.getEllAvoidProjectList().forEach(ellAvoidProject ->
                ellAvoidProject.setId(new Date().getTime() + IdUtils.fastSimpleUUID()));
        if (i > 0) {
            avoidTestMapper.addAvoidProject(avoidApply.getId(), avoidApply.getEllAvoidProjectList());
            return i;
        }
        return 0;
    }

    @Override
    public List<AvoidApplyVo> findAvoidApply(FindAvoidVo findAvoidVo) {
        return avoidTestMapper.findAvoidApply(findAvoidVo);
    }

    @Override
    public List<EllReturnScoreVo> findUserById(String id) {
        return ellUserMapper.findUserById(id);
    }

    @Override
    public List<EllAvoidProject> findAvoidProject(String avoId) {
        return avoidTestMapper.findAvoidProject(avoId);
    }

    @Override
    public EllPlan findEllPlan(String planId) {
        return avoidTestMapper.findEllPlan(planId);
    }

    @Override
    public int auditAvoidState(EllUpdateAvoid ellUpdateAvoid) {
        //判断是否为同意，在判断免测的项目是否个数是否等于计划所测试的项目个数是则直接生成最终分数
        FindAvoidVo findAvoidVo = new FindAvoidVo();
        findAvoidVo.setId(ellUpdateAvoid.getId());
        //查询免测申请项目
        List<EllAvoidProject> avoidApplyVos = avoidTestMapper.findAvoidApplyProject(findAvoidVo);
        //该人员应测试项目
        int proNum = avoidTestMapper.selUserTestNum(ellUpdateAvoid.getUserId(), ellUpdateAvoid.getPlanId());
        //判断
        if (avoidApplyVos != null && avoidApplyVos.get(0) != null && avoidApplyVos.size() == proNum) {
            avoidTestMapper.addPlanEndScore(ellUpdateAvoid.getUserId(), ellUpdateAvoid.getPlanId(), 60, "及格");
        }
        //分布式锁加锁
        Boolean success = avoidRedis.getLock(ellUpdateAvoid.getPlanId() + ellUpdateAvoid.getUserId(), 60);
        if (success) {
            avoidTestMapper.updateAvoidState(ellUpdateAvoid);
            avoidRedis.releaseLock(ellUpdateAvoid.getPlanId() + ellUpdateAvoid.getUserId());
            return 1;
        }
        return HttpStatus.HAS_BEEN_OBTAINED;
    }

    @Override
    public int isProTest(FindAvoidVo findAvoidVo) {
        return avoidTestMapper.isProTest(findAvoidVo);
    }

    @Override
    public List<EllPlanProjectVo> findPlanProject(String planId) {
        return avoidTestMapper.findPlanProject(planId);
    }

    @Override
    public int updataAvoid(AvoidApplyVo avoidApply) {
        return avoidTestMapper.updataAvoid(avoidApply);
    }

    @Override
    public int deleteAvoid(String avoId) {
        return avoidTestMapper.deleteAvoid(avoId);
    }

    @Override
    public int addAvoidProject(String avoid, List<EllAvoidProject> ellAvoidProjectList) {
        ellAvoidProjectList.stream().forEach(ellAvoidProject -> ellAvoidProject.setId(new Date().getTime() + IdUtils.fastSimpleUUID()));
        return avoidTestMapper.addAvoidProject(avoid, ellAvoidProjectList);
    }

    @Override
    public List<EllAvoidExportDto> selAvoid(FindAvoidVo findAvoidVo) {
        List<EllAvoidExportDto> ellAvoidExportDtoList = avoidTestMapper.selAvoid(findAvoidVo);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < ellAvoidExportDtoList.size(); i++) {
            //获取项目
            List<EllAvoidProject> avoidProjects = findAvoidProject(ellAvoidExportDtoList.get(i).getId());
            for (int j = 0; j < avoidProjects.size(); j++) {
                if (j == 0) {
                    stringBuffer.append(avoidProjects.get(j).getProName());
                } else {
                    stringBuffer.append("," + avoidProjects.get(j).getProName());
                }
            }
            ellAvoidExportDtoList.get(i).setProName(stringBuffer.toString());
            //清空
            stringBuffer.setLength(0);
            ellAvoidExportDtoList.get(i).setSchoolName(avoidTestMapper.selSchoolName(ellAvoidExportDtoList.get(i).getOrgId()));
            switch (ellAvoidExportDtoList.get(i).getState()) {
                case "1":
                    ellAvoidExportDtoList.get(i).setState("待确认");
                    break;
                case "2":
                    ellAvoidExportDtoList.get(i).setState("确认");
                    break;
                case "3":
                    ellAvoidExportDtoList.get(i).setState("驳回");
                    break;
                case "4":
                    ellAvoidExportDtoList.get(i).setState("驳回且不能重新提交");
                    break;

            }
        }
        return ellAvoidExportDtoList;
    }
}
