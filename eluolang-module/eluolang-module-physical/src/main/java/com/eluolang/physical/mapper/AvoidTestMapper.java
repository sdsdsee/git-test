package com.eluolang.physical.mapper;

import com.eluolang.common.core.pojo.EllAvoidProject;
import com.eluolang.common.core.pojo.EllPlan;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.physical.dto.EllAvoidExportDto;
import com.eluolang.physical.model.AvoidApplyVo;
import com.eluolang.physical.model.EllPlanProjectVo;
import com.eluolang.physical.model.EllUpdateAvoid;
import com.eluolang.physical.model.FindAvoidVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AvoidTestMapper {
    /**
     * 添加免测申请
     *
     * @param avoidApply
     * @return
     */
    int addAvoidApply(@Param("avoidApply") AvoidApplyVo avoidApply);

    /**
     * 查询学生
     */
    EllUser selUser(String userId);

    /**
     * 申请免测项目
     */
    int addAvoidProject(@Param("avoid") String avoid, @Param("ellAvoidProjectList") List<EllAvoidProject> ellAvoidProjectList);

    /**
     * 查询免测申请
     */
    List<AvoidApplyVo> findAvoidApply(@Param("findAvoidVo") FindAvoidVo findAvoidVo);

    /**
     * 查询免测申请项目
     */
    List<EllAvoidProject> findAvoidApplyProject(@Param("findAvoidVo") FindAvoidVo findAvoidVo);
    /**
     * 查询免测申请的项目
     */
    List<EllAvoidProject> findAvoidProject(String avoId);

    /**
     * 修改申请状态
     */
    int updateAvoidState(@Param("ellUpdateAvoid") EllUpdateAvoid ellUpdateAvoid);

    /**
     * 检录时候判断该用户是否该项目免测
     */
    int isProTest(@Param("findAvoidVo") FindAvoidVo findAvoidVo);

    /**
     * 查询计划项目
     */
    List<EllPlanProjectVo> findPlanProject(String planId);

    /**
     * 修改
     */
    int updataAvoid(@Param("avoidApply") AvoidApplyVo avoidApply);

    /**
     * 删除免测申请项目
     */
    int deleteAvoid(String avoId);

    /**
     * 查询计划
     */
    EllPlan findEllPlan(String planId);

    /**
     * 查询此人该计划应该测试的项目数量
     */
    int selUserTestNum(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 添加所有项目都免测人员最终分数
     */
    int addPlanEndScore(@Param("userId") String userId, @Param("planId") String planId, @Param("endScore") int endScore, @Param("endComment") String endComment);

    /**
     * 查询免测的信息
     */
    List<EllAvoidExportDto> selAvoid(@Param("findAvoidVo") FindAvoidVo findAvoidVo);

    /**
     * 查询学校名称
     */
    String selSchoolName(String orgId);

    /**
     *
     */
}
