package com.eluolang.app.manage.service;

import com.eluolang.app.manage.dto.EllUserVo;
import com.eluolang.app.manage.vo.*;
import com.eluolang.common.core.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 定义业务层的小程序接口方法
 *
 * @author dengrunsen
 */
public interface AppUserService {
    /**
     * 查看此微信是否绑定了用户
     */
    EllUserVo findUser(String openId);

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
    int updateUserWxOpenId(String userId, String wxOpenId);

    /**
     * 查询此用户的学校名称
     */
    String findUserSchoolName(String orgId);

    /**
     * 查询预约时间段
     */
    LinkedHashMap<String, List<EllTestAppointmentVo>> findTimeAppointment(String planId) throws ParseException;

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
     * 查询成绩
     */
    List<EllTestHistoryVo> findHistory(String userId);

    /**
     * 查询计划是否完成
     */
    int isHasAccomplishPlan(String userId);

    /**
     * 添加预约计划
     */
    int addAppointmentState(AppointmentPlanVo appointmentPlanVo);

    /**
     * 修改计划预约
     */
    int updateAppointmentState(EllPlanUser ellPlanUser);

    /**
     * 查询计划是否测试完毕
     */
    int findIsHasEndScore(String planId, String userId);

    /**
     * 查询该学生测试过的所有计划通过部门
     */
    List<EllPlanVo> findPlanByUserOrgId(String userId,int isExam);
    /**
     * 查询用户成绩
     */
    EllPlanEndScoreVo findUserHistory(String userId, String planId);
    /**
     * 查询这天此时间段的已预约人数
     */
    EllSubscribeNumVo findYetOrderPeopleNumber( int subDateId,String dayTime,String planId);

    /***
     * c查询计划总人数
     */
    int selPlanNum(String planId);

    /**
     * 查询用户的部门
     */
    List<PfDepart> selUserPfDepart(String orgId);
    /**
     * 解除绑定
     */
    int relieveUserWx(String userId);

}
