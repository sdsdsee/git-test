package com.eluolang.app.manage.mapper;

import com.eluolang.app.manage.dto.EllUserVo;
import com.eluolang.app.manage.vo.*;
import com.eluolang.common.core.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定义数据访问层小程序接口方法
 *
 * @author renzhixing
 */
@Mapper
public interface AppUserMapper {
    /**
     * 查看此微信是否绑定了用户
     */
    EllUserVo findUser(String openId);

    /**
     * 查询用户的部门
     */
    List<PfDepart> selUserPfDepart(String orgId);

    /**
     * 解除绑定
     */
    int relieveUserWx(String userId);

    /**
     * 查询此用户的学校名称
     */
    String findUserSchoolName(String orgId);

    /**
     * 通过id查找是否拥有这个用户
     */
    EllUser findUserById(String userId);

    /**
     * 通过身份证标识查找相同标识的用户id
     */
    List<String> findUserIdByIdentification(String identification);

    /**
     * 绑定微信的openId
     */
    int updateUserWxOpenId(@Param("userId") String userId, @Param("wxOpenId") String wxOpenId);

    /**
     * 查询预约时间段
     */
    List<EllTestAppointmentVo> findTimeAppointment(String planId);

    /**
     * 查询预约时间段的已预约的人数及计划信息
     */
    List<EllTestAppointmentVo> findTimeAppointmentNum(@Param("timeDay") String timeDay, @Param("planId") String planId);

    /**
     * 通过部门查询计划
     */
    List<EllOrderPlanVo> findPlan(String userId);

    /**
     * 查询计划已预约人数
     *
     * @param planId
     * @return
     */
    int findAppointmentAllNum(String planId);

    /**
     * 查询最近一次成绩
     */
    List<EllTestHistoryVo> findHistory(String userId);

    /**
     * 查询计划是否完成
     */
    int isHasAccomplishPlan(String userId);

    /**
     * 添加预约计划
     */
    int addAppointmentState(@Param("ellPlanUser") EllPlanUser ellPlanUser);

    /**
     * 修改计划预约
     */
    int updateAppointment(@Param("ellPlanUser") EllPlanUser ellPlanUser);

    /**
     * 查询计划是否测试完毕
     */
    int findIsHasEndScore(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 查询该学生测试过的所有计划通过部门
     */
    List<EllPlanVo> findPlanByUserOrgId(@Param("userId") String userId, @Param("isExam") int isExam);

    /**
     * 查询用户成绩
     */
    List<EllUserPlanHistoryVo> findUserHistory(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 查询计划最终分数
     */
    EllPlanEndScoreVo findPlanEndScore(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 查询这个时间段可预约多少人
     */
    int findCanOderHavePeopleNumber(@Param("subDateId") int subDateId);

    /**
     * 查询这天此时间段的已预约人数/查询这个时间段可预约多少人
     */
    EllSubscribeNumVo findYetOrderPeopleNumber(@Param("subDateId") int subDateId, @Param("dayTime") String dayTime, @Param("planId") String planId);

    /***
     * c查询计划总人数
     */
    int selPlanNum(String planId);
}
