package com.eluolang.app.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.eluolang.app.manage.dto.EllUserVo;
import com.eluolang.app.manage.mapper.AppUserMapper;
import com.eluolang.app.manage.service.AppUserService;
import com.eluolang.app.manage.util.CreateTime;
import com.eluolang.app.manage.util.ScoreDisposeUtil;
import com.eluolang.app.manage.vo.*;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 小程序业务逻辑层
 *
 * @author dengrunsen
 */
@Transactional
@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public EllUserVo findUser(String openId) {
        return appUserMapper.findUser(openId);
    }

    @Override
    public EllUser findUserById(String userId) {
        return appUserMapper.findUserById(userId);
    }

    @Override
    public List<String> findUserIdByIdentification(String identification) {
        return appUserMapper.findUserIdByIdentification(identification);
    }

    @Override
    public int updateUserWxOpenId(String userId, String wxOpenId) {
        return appUserMapper.updateUserWxOpenId(userId, wxOpenId);
    }

    @Override
    public String findUserSchoolName(String orgId) {
        return appUserMapper.findUserSchoolName(orgId);
    }

    @Override
    public LinkedHashMap<String, List<EllTestAppointmentVo>> findTimeAppointment(String planId) throws ParseException {
        List<EllTestAppointmentVo> ellTestAppointmentVoList = appUserMapper.findTimeAppointment(planId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //计划开始时间
        Date dateBegin = sdf.parse(ellTestAppointmentVoList.get(0).getDateBegin());
        //计划结束时间
        Date dateEnd = sdf.parse(ellTestAppointmentVoList.get(0).getDateEnd());
        //日历工具
        Calendar calendar = Calendar.getInstance();
        //设置开始时间
        calendar.setTime(dateBegin);
        Date tmp = dateBegin;
        //将预约人数按天数按时间段分
        LinkedHashMap<String, List<EllTestAppointmentVo>> ellMap = new LinkedHashMap<>();
        while (tmp.getTime() <= dateEnd.getTime()) {
            //查询每天每个时间段预约的人数
            List<EllTestAppointmentVo> ellTestAppointmentVo = appUserMapper.findTimeAppointmentNum(sdf.format(tmp), planId);
            //没有人预约的时间段就给默认值
            if (ellTestAppointmentVo.size() != ellTestAppointmentVoList.size()) {
                if (ellTestAppointmentVo.size() <= 0) {
                    ellTestAppointmentVo = new ArrayList<>();
                }//格式化时间
                String dayTime = sdf.format(tmp);
                for (int i = 0; i < ellTestAppointmentVoList.size(); i++) {
                    ellTestAppointmentVoList.get(i).setDayTime(dayTime);
                    ellTestAppointmentVo.add((EllTestAppointmentVo) ellTestAppointmentVoList.get(i).clone());
                }
                //转成set通过set去重,并且通过TreeSet排序,并且通过某属性进行去重
                //设置通过那个字段去重
                Set<EllTestAppointmentVo> ellTestAppointmentVoSet = new TreeSet<>(Comparator.comparing(EllTestAppointmentVo::getBeginTime));
                //把参数放入去重
                ellTestAppointmentVo.stream().forEach(ell -> ellTestAppointmentVoSet.add(ell));
                //又转回list
                ellTestAppointmentVo = new ArrayList<>(ellTestAppointmentVoSet);
                ellMap.put(sdf.format(tmp), ellTestAppointmentVo);
            } else {
                ellMap.put(sdf.format(tmp), ellTestAppointmentVo);
            }
            //天数加上1
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            tmp = calendar.getTime();
        }
        return ellMap;
    }

    @Override
    public List<EllOrderPlanVo> findPlan(String userId) {
        return appUserMapper.findPlan(userId);
    }

    @Override
    public int findAppointmentAllNum(String planId) {
        return appUserMapper.findAppointmentAllNum(planId);
    }

    @Override
    public List<EllTestHistoryVo> findHistory(String userId) {
        //查询的分数
        List<EllTestHistoryVo> ellTestHistoryVoList = appUserMapper.findHistory(userId);
        //每一次的成绩
        for (int i = 0; i < ellTestHistoryVoList.size(); i++) {
            //每次返回的参数
            List<EllAppHistoryVo> ellAppHistoryVoList = new ArrayList<>();
            //判断是否有成绩
            if (ellTestHistoryVoList.get(i).getTesData() != null && ellTestHistoryVoList.get(i).getTesData() != "") {
                Map<String, Object> testDataMap = JSONArray.parseObject(ellTestHistoryVoList.get(i).getTesData());
                for (String key : testDataMap.keySet()) {
                    //把每次数据进行序列化
                    List<EllTestData> testData = JSONArray.parseArray(testDataMap.get(key).toString(), EllTestData.class);
                    //每次的分数返回
                    EllAppHistoryVo ellAppHistoryVo = ScoreDisposeUtil.scoreReturn(testData, testData.get(0).getScore(), ellTestHistoryVoList.get(i).getComment());
                    ellAppHistoryVoList.add(ellAppHistoryVo);
                    //判断是否有多个成绩
                    Boolean aBoolean = ellTestHistoryVoList.get(i).getData().contains(",");
                    if (aBoolean) {
                        String[] data = ellTestHistoryVoList.get(i).getData().split(",");
                        String dataEnd = ScoreDisposeUtil.endScore(data);
                        //保存要显示的最终分数
                        ellTestHistoryVoList.get(i).setData(dataEnd);
                    }
                }
            }
            ellTestHistoryVoList.get(i).setEllAppHistoryVoList(ellAppHistoryVoList);
        }
        return ellTestHistoryVoList;
    }

    @Override
    public int isHasAccomplishPlan(String userId) {
        return appUserMapper.isHasAccomplishPlan(userId);
    }

    @Override
    public int addAppointmentState(AppointmentPlanVo appointmentPlanVo) {
        EllPlanUser ellPlanUser = new EllPlanUser();
        ellPlanUser.setUserId(appointmentPlanVo.getUserId());
        ellPlanUser.setPlanId(appointmentPlanVo.getPlanId());
        //把以前预约的计划判断是否为最后预约的计划设置为否
        appUserMapper.updateAppointment(ellPlanUser);
        ellPlanUser.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        ellPlanUser.setState(1);
        ellPlanUser.setIsLast(1);
        ellPlanUser.setSubDateId(appointmentPlanVo.getSubDateId());
        ellPlanUser.setCreateTime(CreateTime.getTime());
        ellPlanUser.setDayTime(appointmentPlanVo.getDayTime());
        return appUserMapper.addAppointmentState(ellPlanUser);
    }

    @Override
    public int updateAppointmentState(EllPlanUser ellPlanUser) {
        return appUserMapper.updateAppointment(ellPlanUser);
    }

    @Override
    public int findIsHasEndScore(String planId, String userId) {
        return appUserMapper.findIsHasEndScore(planId, userId);
    }

    @Override
    public List<EllPlanVo> findPlanByUserOrgId(String userId, int isExam) {
        return appUserMapper.findPlanByUserOrgId(userId, isExam);
    }

    @Override
    public EllPlanEndScoreVo findUserHistory(String userId, String planId) {
        List<EllUserPlanHistoryVo> ellUserPlanHistoryVoList = appUserMapper.findUserHistory(userId, planId);
        EllPlanEndScoreVo ellPlanEndScore = appUserMapper.findPlanEndScore(userId, planId);
        ellUserPlanHistoryVoList.stream().forEach(ellUserPlanHistory -> {
            //判断是否有多个成绩
            Boolean aBoolean = ellUserPlanHistory.getData().contains(",");
            if (aBoolean) {
                String[] data = ellUserPlanHistory.getData().split(",");
                String dataEnd = ScoreDisposeUtil.endScore(data);
                //保存要显示的最终分数
                ellUserPlanHistory.setData(dataEnd);
            }
        });
        /*for (int i = 0; i < ellUserPlanHistoryVoList.size(); i++) {
            //判断是否有多个成绩
            Boolean aBoolean = ellUserPlanHistoryVoList.get(i).getData().contains(",");
            if (aBoolean) {
                String[] data = ellUserPlanHistoryVoList.get(i).getData().split(",");
                String dataEnd = ScoreDisposeUtil.endScore(data);
                //保存要显示的最终分数
                ellUserPlanHistoryVoList.get(i).setData(dataEnd);
            }
        }*/
        if (ellPlanEndScore == null) {
            ellPlanEndScore = new EllPlanEndScoreVo();
        }
        ellPlanEndScore.setEllScoreList(ellUserPlanHistoryVoList);
        return ellPlanEndScore;
    }

    @Override
    public EllSubscribeNumVo findYetOrderPeopleNumber(int subDateId, String dayTime, String planId) {
        return appUserMapper.findYetOrderPeopleNumber(subDateId, dayTime, planId);
    }

    @Override
    public int selPlanNum(String planId) {
        return appUserMapper.selPlanNum(planId);
    }

    @Override
    public List<PfDepart> selUserPfDepart(String orgId) {
        return appUserMapper.selUserPfDepart(orgId);
    }

    @Override
    public int relieveUserWx(String userId) {
        return appUserMapper.relieveUserWx(userId);
    }
}

