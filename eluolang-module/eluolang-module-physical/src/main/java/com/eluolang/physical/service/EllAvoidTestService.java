package com.eluolang.physical.service;

import com.eluolang.common.core.pojo.EllAvoidProject;
import com.eluolang.common.core.pojo.EllPlan;
import com.eluolang.physical.dto.EllAvoidExportDto;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EllAvoidTestService {
    /**
     * 添加免测申请
     *
     * @param avoidApply
     * @return
     */
    int addAvoidApply(AvoidApplyVo avoidApply);
    /**
     * 查询免测申请
     */
    List<AvoidApplyVo> findAvoidApply(FindAvoidVo findAvoidVo);
    /**
     * 根据用户id查询用户
     *
     * @param id
     * @return
     */
    List<EllReturnScoreVo> findUserById( String id);
    /**
     * 查询免测申请的项目
     */
    List<EllAvoidProject> findAvoidProject(String avoId);
    /**
     * 查询计划
     */
    EllPlan findEllPlan(String planId);
    /**
     * 修改申请状态
     */
    int auditAvoidState(@Param("ellUpdateAvoid") EllUpdateAvoid ellUpdateAvoid);
    /**
     * 检录时候判断该用户是否该项目免测
     */
    int isProTest(FindAvoidVo findAvoidVo);
    /**
     * 查询计划项目
     */
    List<EllPlanProjectVo> findPlanProject(String planId);
    /**
     * 修改计划
     */
    int updataAvoid(AvoidApplyVo avoidApply);
    /**
     * 删除免测申请项目
     */
    int deleteAvoid(String avoId);
    /**
     * 申请免测项目
     */
    int addAvoidProject(String avoid,List<EllAvoidProject> ellAvoidProjectList);
    /**
     * 查询免测的信息
     */
    List<EllAvoidExportDto> selAvoid(FindAvoidVo findAvoidVo);
}
