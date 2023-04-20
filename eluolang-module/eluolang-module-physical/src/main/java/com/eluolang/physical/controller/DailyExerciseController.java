package com.eluolang.physical.controller;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllDailyExercisePlan;
import com.eluolang.common.core.pojo.EllDailyPlanUseDepart;
import com.eluolang.common.core.pojo.EllDailyTimeQuantum;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.PageHelpers;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.dto.DailyPlanUseDepartDto;
import com.eluolang.physical.dto.EllDailyMothNumDto;
import com.eluolang.physical.dto.EllDailyRunRankDto;
import com.eluolang.physical.dto.EllDailyTimeNumDto;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.DailyExerciseService;
import com.eluolang.physical.service.EllUserService;
import com.eluolang.physical.util.CreateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Transactional
@Api(tags = "日常锻炼")
@RequestMapping("/daily")
public class DailyExerciseController {
    @Autowired
    public DailyExerciseService dailyExerciseService;
    @Autowired
    private EllUserService ellUserService;

    @PostMapping("/addDailyPlan")
    @ApiOperation("创建日常计划")
    public Result addDailyPlan(@RequestBody EllDailyExercisePlanVo ellDailyPlan) {
     /*   ellDailyPlan.setCreateTime(CreateTime.getTime());
        ellDailyPlan.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        //判断开始时间小于结束时间
        if (ellDailyPlan != null && CreateTime.yearTransitionTimestamp(ellDailyPlan.getTrainStartTime()) < CreateTime.yearTransitionTimestamp(ellDailyPlan.getTrainEndTime())) {
            //查询是否有重复的时间段的计划
            List<EllDailyExercisePlan> ellDailyExercisePlanList = dailyExerciseService.selDailPlan(ellDailyPlan);
            //查询使用部门下的所有权限部门的id
//            List<DailyPlanUseDepartDto> pfDepart = dailyExerciseService.selPlanAllDepart(ellDailyPlan.getUseOrgId(), ellDailyPlan.getAccount());
           //分割前端传来的最后以及部门id
            String[] pfDepart;
            if (ellDailyPlan.getUseOrgId().contains(",")) {
                pfDepart = ellDailyPlan.getUseOrgId().split(",");
            } else {
                pfDepart = new String[]{ellDailyPlan.getUseOrgId()};
            }
            //保存到map中去重判断这个部门这个时间段是否拥有计划
            Map<String, EllDailyPlanUseDepart> pfDepartMap = new HashMap<>();
            //查询此时间段计划使用的所有的部门
            for (int i = 0; i < ellDailyExercisePlanList.size(); i++) {
                List<EllDailyPlanUseDepart> ellDailyPlanUseDepartList = dailyExerciseService.selPlanUseDp(ellDailyExercisePlanList.get(i).getId(), null);
                //保存
                for (int j = 0; j < ellDailyPlanUseDepartList.size(); j++) {
                    pfDepartMap.put(ellDailyPlanUseDepartList.get(j).getOrgId(), ellDailyPlanUseDepartList.get(j));
                }
            }
            //保存
            List<DailyPlanUseDepartDto> pfDepartList = new ArrayList<>();
            //判断这个部门是否拥有计划
            for (int i = 0; i < pfDepart.length; i++) {
                DailyPlanUseDepartDto dailyPlanUseDepartDto = new DailyPlanUseDepartDto(new Date().getTime() + IdUtils.fastSimpleUUID(), ellDailyPlan.getId(), pfDepart[i]);
                pfDepartList.add(dailyPlanUseDepartDto);
                if (ellDailyExercisePlanList != null && ellDailyExercisePlanList.size() > 0 && pfDepartMap.containsKey(pfDepart[i])) {
                    return new Result(HttpStatus.ERROR, "部门计划日期冲突");
                }
            }
            //给第一个时间点生成id
            ellDailyPlan.getDailyTimeList().get(0).setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            //最后一个就不用去做比较了
            for (int i = 0; i < ellDailyPlan.getDailyTimeList().size() - 1; i++) {
//                System.out.println(ellDailyPlan.getDailyTimeList().get(i).getStartHour().compareTo(ellDailyPlan.getDailyTimeList().get(i).getEndHour()));
                //判断开始时间大于结束时间
                if (CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(i).getStartHour()) < CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(i).getEndHour())) {
                    //判断一天的时间段是否有冲突
                    for (int j = i + 1; j < ellDailyPlan.getDailyTimeList().size(); j++) {
                        ellDailyPlan.getDailyTimeList().get(j).setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                        //要么开始时间小于另一个时间段的结束时间，要么结束时间大于另一个的开始时间
                        //开始时间比较
                        Boolean startTimeCompare = CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(i).getStartHour()) <= CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(j).getEndHour());
                        //结束时间比较
                        Boolean endTimeCompare = CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(i).getEndHour()) >= CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(j).getStartHour());
                        if (!(startTimeCompare || endTimeCompare)) {
                            return new Result(HttpStatus.ERROR, "选择的时段有冲突");
                        }
                    }
                } else {
                    return new Result(HttpStatus.ERROR, "开始时间段小于了结束时间");
                }
            }
            //创建计划
            dailyExerciseService.addDailyPlan(ellDailyPlan);
            //添加每天的时间段
            dailyExerciseService.addDailyTime(ellDailyPlan.getDailyTimeList(), ellDailyPlan.getId());
            //添加使用部门
            dailyExerciseService.addPlanUseDp(pfDepartList, ellDailyPlan.getId());
            return new Result(HttpStatus.SUCCESS, "成功");
        }*/
        return new Result(HttpStatus.ERROR, "开始日期小于了结束时间");
    }

    @PostMapping("/selDailyPlan")
    @ApiOperation("查询日常计划")
    public Result selDailyPlan(String orgId, String account) {
        List<EllDailyExercisePlanVo> ellDailyExercisePlanList = dailyExerciseService.selDailyExercisePlanByName(orgId, account);
        for (int i = 0; i < ellDailyExercisePlanList.size(); i++) {
            ellDailyExercisePlanList.get(i).setDailyTimeList(dailyExerciseService.selUseTime(ellDailyExercisePlanList.get(i).getId(), 0));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("daily", ellDailyExercisePlanList);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/selDailyTextContent")
    @ApiOperation("查询日常计划发布的文本内容")
    public Result selDailyTextContent(String dailyId) {
        String text = dailyExerciseService.selDailyTextContent(dailyId);
        return new Result(HttpStatus.SUCCESS, "查询成功", text);
    }

    @PostMapping("/getDailyTextContent")
    @ApiOperation("查询日常计划发布的文本内容设备")
    public Result selDailyTextContentDeviceId(String deviceId) {
        String text = dailyExerciseService.selDailyTextContentDeviceId(deviceId);
        return new Result(HttpStatus.SUCCESS, "查询成功", text);
    }

    @PostMapping("/updataDailyPlan")
    @ApiOperation("修改日常配置")
    @Transactional
    public Result updataDailyPlan(@RequestBody EllDailyExercisePlanVo ellDailyPlan) {
        /*//日常的计划写死
        ellDailyPlan.setId("1665302253342fbdf195e951f4decb11d82ceef6da27d");
        //判断开始时间小于结束时间
        if (ellDailyPlan != null && CreateTime.yearTransitionTimestamp(ellDailyPlan.getTrainStartTime()) < CreateTime.yearTransitionTimestamp(ellDailyPlan.getTrainEndTime())) {
            //查询是否有重复的时间段的计划
            List<EllDailyExercisePlan> ellDailyExercisePlanList = dailyExerciseService.selDailPlan(ellDailyPlan);
            //查询使用部门下的所有权限部门的id
//            List<DailyPlanUseDepartDto> pfDepart = dailyExerciseService.selPlanAllDepart(ellDailyPlan.getUseOrgId(), ellDailyPlan.getAccount());
            //保存到map中去重判断这个部门这个时间段是否拥有计划
            //分割前端传来的最后以及部门id
            String[] pfDepart;
            if (ellDailyPlan.getUseOrgId().contains(",")) {
                pfDepart = ellDailyPlan.getUseOrgId().split(",");
            } else {
                pfDepart = new String[]{ellDailyPlan.getUseOrgId()};
            }
            Map<String, EllDailyPlanUseDepart> pfDepartMap = new HashMap<>();
            //查询此时间段计划使用的所有的部门
            for (int i = 0; i < ellDailyExercisePlanList.size(); i++) {
                List<EllDailyPlanUseDepart> ellDailyPlanUseDepartList = dailyExerciseService.selPlanUseDp(ellDailyExercisePlanList.get(i).getId(), null);
                //保存
                for (int j = 0; j < ellDailyPlanUseDepartList.size(); j++) {
                    pfDepartMap.put(ellDailyPlanUseDepartList.get(j).getOrgId(), ellDailyPlanUseDepartList.get(j));
                }
            }
            //保存
            List<DailyPlanUseDepartDto> pfDepartList = new ArrayList<>();
            //判断这个部门是否拥有计划
            for (int i = 0; i < pfDepart.length; i++) {
                DailyPlanUseDepartDto dailyPlanUseDepartDto = new DailyPlanUseDepartDto(new Date().getTime() + IdUtils.fastSimpleUUID(), ellDailyPlan.getId(), pfDepart[i]);
                pfDepartList.add(dailyPlanUseDepartDto);
                if (ellDailyExercisePlanList != null && ellDailyExercisePlanList.size() > 0 && pfDepartMap.containsKey(pfDepart[i])) {
                    return new Result(HttpStatus.ERROR, "部门计划日期冲突");
                }
            }
            //给第一个时间点生成id
            ellDailyPlan.getDailyTimeList().get(0).setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            //最后一个就不用去做比较了
            for (int i = 0; i < ellDailyPlan.getDailyTimeList().size() - 1; i++) {
//                System.out.println(ellDailyPlan.getDailyTimeList().get(i).getStartHour().compareTo(ellDailyPlan.getDailyTimeList().get(i).getEndHour()));
                //判断开始时间大于结束时间
                if (CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(i).getStartHour()) < CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(i).getEndHour())) {
                    //判断一天的时间段是否有冲突
                    for (int j = i + 1; j < ellDailyPlan.getDailyTimeList().size(); j++) {
                        ellDailyPlan.getDailyTimeList().get(j).setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                        //要么开始时间小于另一个时间段的结束时间，要么结束时间大于另一个的开始时间
                        //开始时间比较
                        Boolean startTimeCompare = CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(i).getStartHour()) <= CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(j).getEndHour());
                        //结束时间比较
                        Boolean endTimeCompare = CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(i).getEndHour()) >= CreateTime.hourTransitionTimestamp(ellDailyPlan.getDailyTimeList().get(j).getStartHour());
                        if (!(startTimeCompare || endTimeCompare)) {
                            return new Result(HttpStatus.ERROR, "选择的时段有冲突");
                        }
                    }
                } else {
                    return new Result(HttpStatus.ERROR, "开始时间段小于了结束时间");
                }
            }
            //删除时间点
            dailyExerciseService.delPlanTime(ellDailyPlan.getId());
            //删除使用部门
            dailyExerciseService.delPlanUseDp(ellDailyPlan.getId());
            //添加每天的时间段
            dailyExerciseService.addDailyTime(ellDailyPlan.getDailyTimeList(), ellDailyPlan.getId());
            //添加使用部门
            dailyExerciseService.addPlanUseDp(pfDepartList, ellDailyPlan.getId());
            //修改
            dailyExerciseService.updataDailyPlan(ellDailyPlan);
        }*/
        //修改时间
        ellDailyPlan.setCreateTime(CreateTime.getTime());
        //查询使用部门下的所有权限部门的id
//        List<DailyPlanUseDepartDto> pfDepart = dailyExerciseService.selPlanAllDepart(ellDailyPlan.getUseOrgId(), "0");
        //查询这个部门是否拥有这个计划
        List<EllDailyExercisePlanVo> ellDailyExercisePlanList = dailyExerciseService.selDailyExercisePlanByName(ellDailyPlan.getUseOrgId(), "0");
        if (ellDailyExercisePlanList.size() <= 0) {
            ellDailyPlan.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            //创建计划
            dailyExerciseService.addDailyPlan(ellDailyPlan);
            //生成id
            ellDailyPlan.getDailyTimeList().stream().forEach(time -> time.setId(new Date().getTime() + IdUtils.fastSimpleUUID()));
            //添加每天的时间段
            dailyExerciseService.addDailyTime(ellDailyPlan.getDailyTimeList(), ellDailyPlan.getId());
            //生成id
//            pfDepart.stream().forEach(pf -> pf.setId(new Date().getTime() + IdUtils.fastSimpleUUID()));
//            //添加使用部门
//            dailyExerciseService.addPlanUseDp(pfDepart, ellDailyPlan.getId());
            return new Result(HttpStatus.SUCCESS, "成功");
        } else {
            //删除时间点
            dailyExerciseService.delPlanTime(ellDailyPlan.getId());
            //删除使用部门
            dailyExerciseService.delPlanUseDp(ellDailyPlan.getId());
            //生成id
            ellDailyPlan.getDailyTimeList().stream().forEach(time -> time.setId(new Date().getTime() + IdUtils.fastSimpleUUID()));
            //添加每天的时间段
            dailyExerciseService.addDailyTime(ellDailyPlan.getDailyTimeList(), ellDailyPlan.getId());
            //生成id
//            pfDepart.stream().forEach(pf -> pf.setId(new Date().getTime() + IdUtils.fastSimpleUUID()));
//            //添加使用部门
//            dailyExerciseService.addPlanUseDp(pfDepart, ellDailyPlan.getId());
            //修改
            dailyExerciseService.updataDailyPlan(ellDailyPlan);
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    //    @Transactional
    @PostMapping("/delDailyPlan")
    @ApiOperation("删除日常计划")
    public Result delDailyPlan(String dailyId) {
        EllDailyExercisePlanVo ellDailyPlan = new EllDailyExercisePlanVo();
        ellDailyPlan.setId(dailyId);
        ellDailyPlan.setIsDelete(1);
        //删除时间点
//        dailyExerciseService.delPlanTime(ellDailyPlan.getId());
        //删除使用部门
//        dailyExerciseService.delPlanUseDp(ellDailyPlan.getId());
        //修改删除标识
        dailyExerciseService.updataDailyPlan(ellDailyPlan);
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @PostMapping("/getPeopleNum")
    @ApiOperation("查询人数")
    public Result getPeopleNum(@RequestBody EllSelDailyDetailsVo ellSelDailyDetailsVo) {
        //日常的计划写死
//        ellSelDailyDetailsVo.setDailyId("1665302253342fbdf195e951f4decb11d82ceef6da27d");
        int all = dailyExerciseService.selDailyNum(ellSelDailyDetailsVo, null);
        int girlNum = dailyExerciseService.selDailyNum(ellSelDailyDetailsVo, "2");
        int boyNum = dailyExerciseService.selDailyNum(ellSelDailyDetailsVo, "1");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("all", all);
        jsonObject.put("girlNum", girlNum);
        jsonObject.put("boyNum", boyNum);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/getRunUser")
    @ApiOperation("查询当前跑步排行榜")
    public Result gerPeopleNum(@RequestBody EllSelDailyDetailsVo ellSelDailyDetailsVo) {
        //日常的计划写死
//        ellSelDailyDetailsVo.setDailyId("1665302253342fbdf195e951f4decb11d82ceef6da27d");
        PageHelper.startPage(ellSelDailyDetailsVo.getPage(), 15);
        List<EllDailyRunRankDto> ellDailyRunRankDtoList = dailyExerciseService.selRankHistory(ellSelDailyDetailsVo);
        PageInfo info = new PageInfo<>(ellDailyRunRankDtoList);
        //这个数字在stream中不会覆盖
        AtomicInteger index = new AtomicInteger((ellSelDailyDetailsVo.getPage() - 1) * 15 + 1);
        ellDailyRunRankDtoList.stream().forEach(ellDailyRunRankDto ->
                //            index.incrementAndGet();是先增加在返回 index.getAndIncrement()先返回在增加
                ellDailyRunRankDto.setRank(index.getAndIncrement()));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", info.getTotal());
        jsonObject.put("rank", ellDailyRunRankDtoList);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/getNotRunUser")
    @ApiOperation("查询未达标跑步排行榜")
    public Result getNotRunUser(@RequestBody EllSelDailyDetailsVo ellSelDailyDetailsVo) throws ParseException {
        //查询这个部门是否拥有这个计划
        List<EllDailyExercisePlanVo> ellDailyExercisePlanList = dailyExerciseService.selDailyExercisePlanByName(ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getAccount());
        //上下半年日常的时间段
        String[] firstMoths = ellDailyExercisePlanList.get(0).getFirstHalfYear().split("-");
        String[] twoMoths = ellDailyExercisePlanList.get(0).getSecondHalfYear().split("-");
        //当前月份
        String month = CreateTime.getMonth();
        String year = CreateTime.getYear();
        Long startTime = 0l;
        Long endtTime = 0l;
        int Type = 0;
        if (Integer.parseInt(month) >= Integer.parseInt(firstMoths[0]) && Integer.parseInt(month) <= Integer.parseInt(firstMoths[1])) {
            Type = 1;
            startTime = CreateTime.monthTransitionTimestamp(year + "-" + firstMoths[0]);
            endtTime = CreateTime.monthTransitionTimestamp(year + "-" + firstMoths[1]);
        } else if (Integer.parseInt(month) >= Integer.parseInt(twoMoths[0]) && Integer.parseInt(month) <= Integer.parseInt(twoMoths[1])) {
            Type = 2;
            startTime = CreateTime.monthTransitionTimestamp(year + "-" + twoMoths[0]);
            //下半年的时间为12月结束时间就是下一年的一月
            if (twoMoths[1].equals("12")) {
                endtTime = CreateTime.monthTransitionTimestamp((Integer.parseInt(year) + 1) + "-01");
            } else {
                startTime = CreateTime.monthTransitionTimestamp(year + "-" + twoMoths[1]);
            }

        }
        //日常的计划写死
//        ellSelDailyDetailsVo.setDailyId("1665302253342fbdf195e951f4decb11d82ceef6da27d");
        PageHelper.startPage(ellSelDailyDetailsVo.getPage(), 15);
        List<EllDailyRunRankDto> ellDailyRunRankDtoList = dailyExerciseService.selNotQualifiedHistoryRank(ellSelDailyDetailsVo, Type, startTime, endtTime);
        PageInfo info = new PageInfo<>(ellDailyRunRankDtoList);
        //这个数字在stream中不会覆盖
        AtomicInteger index = new AtomicInteger((ellSelDailyDetailsVo.getPage() - 1) * 15 + 1);
        ellDailyRunRankDtoList.stream().forEach(ellDailyRunRankDto ->
                //            index.incrementAndGet();是先增加在返回 index.getAndIncrement()先返回在增加
                ellDailyRunRankDto.setRank(index.getAndIncrement()));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", info.getTotal());
        jsonObject.put("rank", ellDailyRunRankDtoList);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/getDayTimeNum")
    @ApiOperation("通过时间查询每个时间点的人数")
    public Result getDayTimeNum(@RequestBody EllSelDailyDetailsVo ellSelDailyDetailsVo) {
        //查询这个部门是否拥有这个计划
        List<EllDailyExercisePlanVo> ellDailyExercisePlanList = dailyExerciseService.selDailyExercisePlanByName(ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getAccount());
        //上下半年日常的时间段
        String[] firstMoths = ellDailyExercisePlanList.get(0).getFirstHalfYear().split("-");
        String[] twoMoths = ellDailyExercisePlanList.get(0).getSecondHalfYear().split("-");
        //获取选择日期的月份
        int moth = CreateTime.getStrTime(ellSelDailyDetailsVo.getDay()).get("month");
        int timeType = 0;
        if (moth >= Integer.parseInt(firstMoths[0]) && moth <= Integer.parseInt(firstMoths[1])) {
            timeType = 1;
        } else if (moth >= Integer.parseInt(twoMoths[0]) && moth <= Integer.parseInt(twoMoths[1])) {
            timeType = 2;
        }
        //获取日常的测试时间点
        List<EllDailyTimeQuantum> dailyTimeQuantums = dailyExerciseService.selUseTime(ellSelDailyDetailsVo.getDailyId(), timeType);
        List<EllDailyTimeNumDto> ellDailyTimeNumDtos = new ArrayList<>();
        for (int i = 0; i < dailyTimeQuantums.size(); i++) {
            EllDailyTimeNumDto ellDailyTimeNumDto = new EllDailyTimeNumDto();
            Long startTime = CreateTime.dateTransitionTimestamp(ellSelDailyDetailsVo.getDay() + "  " + dailyTimeQuantums.get(i).getStartHour() + ":00");
            Long endTime = CreateTime.dateTransitionTimestamp(ellSelDailyDetailsVo.getDay() + "  " + dailyTimeQuantums.get(i).getEndHour() + ":00");
            ellDailyTimeNumDto.setNum(dailyExerciseService.selTimeNum(startTime + "", endTime + "", ellSelDailyDetailsVo.getDailyId(), ellSelDailyDetailsVo.getAccount(), ellSelDailyDetailsVo.getOrgId()));
            ellDailyTimeNumDto.setTimeType(timeType);
            ellDailyTimeNumDto.setTime(dailyTimeQuantums.get(i).getStartHour() + "-" + dailyTimeQuantums.get(i).getEndHour());
            ellDailyTimeNumDtos.add(ellDailyTimeNumDto);
        }
        return new Result(HttpStatus.SUCCESS, "成功", ellDailyTimeNumDtos);
    }

    @PostMapping("/getMothNum")
    @ApiOperation("查询今年的每个月达标的人数")
    public Result getMothNum(@RequestBody EllSelDailyDetailsVo ellSelDailyDetailsVo) {
        List<EllDailyMothNumDto> ellDailyMothNumDtos = new ArrayList<>();
        List<EllDailyExercisePlanVo> ellDailyExercisePlanList = dailyExerciseService.selDailyExercisePlanByName(ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getAccount());
        //上下半年日常的时间段
        String[] firstMoths = ellDailyExercisePlanList.get(0).getFirstHalfYear().split("-");
        String[] twoMoths = ellDailyExercisePlanList.get(0).getSecondHalfYear().split("-");
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String year = CreateTime.getYear();
        //达标
        List<Integer> integerList = new ArrayList<>();
        //不达标
        List<Integer> noAttainList = new ArrayList<>();
        //获取月份的达标人数
        for (int i = 0; i < months.length; i++) {
            int Type = 0;
            if (Integer.parseInt(months[i]) >= Integer.parseInt(firstMoths[0]) && Integer.parseInt(months[i]) <= Integer.parseInt(firstMoths[1])) {
                Type = 1;
            } else if (Integer.parseInt(months[i]) >= Integer.parseInt(twoMoths[0]) && Integer.parseInt(months[i]) <= Integer.parseInt(twoMoths[1])) {
                Type = 2;
            }
            Long monthStart = CreateTime.monthTransitionTimestamp(year + "-" + months[i]);
            Long monthEnd = null;
            if (i == months.length - 1) {
                //最后一个月就加一年
                monthEnd = CreateTime.monthTransitionTimestamp((Integer.parseInt(year) + 1) + "-" + months[0]);
            } else {
                monthEnd = CreateTime.monthTransitionTimestamp(year + "-" + months[i + 1]);
            }
            //达标
            integerList.add(dailyExerciseService.selMonthNum(ellSelDailyDetailsVo.getAccount(), ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getDailyId(), monthStart, monthEnd, Type, null));
            noAttainList.add(dailyExerciseService.selMonthNum(ellSelDailyDetailsVo.getAccount(), ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getDailyId(), monthStart, monthEnd, Type + 2, null));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("noAttain", noAttainList);
        jsonObject.put("attain", integerList);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/getSexNum")
    @ApiOperation("查询男女占比")
    public Result getSexNum(@RequestBody EllSelSexDetailsVo ellSelDailyDetailsVo) {
//        Thread.sleep(10000);
        List<EllDailyExercisePlanVo> ellDailyExercisePlanList = dailyExerciseService.selDailyExercisePlanByName(ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getAccount());
        //上下半年日常的时间段
        String[] firstMoths = ellDailyExercisePlanList.get(0).getFirstHalfYear().split("-");
        String[] twoMoths = ellDailyExercisePlanList.get(0).getSecondHalfYear().split("-");
        //本月
        String month = CreateTime.getMonth();
        String year = CreateTime.getYear();
        //下一个月
        String nextMonth = Integer.parseInt(month) < 10 ? "0" + (Integer.parseInt(month) + 1) : Integer.parseInt(month) + 1 + "";
        if (month.equals("12")) {
            //最后一个月就加一年
            nextMonth = (Integer.parseInt(year) + 1) + "-01";
        } else {
            nextMonth = Integer.parseInt(year) + "-" + (Integer.parseInt(month) + 1);
        }
        int Type = 0;
        if (Integer.parseInt(month) >= Integer.parseInt(firstMoths[0]) && Integer.parseInt(month) <= Integer.parseInt(firstMoths[1])) {
            Type = 1;
        } else if (Integer.parseInt(month) >= Integer.parseInt(twoMoths[0]) && Integer.parseInt(month) <= Integer.parseInt(twoMoths[1])) {
            Type = 2;
        }
        if (ellSelDailyDetailsVo.getAttain() == 2) {
            Type = Type + 2;
        }
        //男女
        int girlNum = dailyExerciseService.selMonthNum(ellSelDailyDetailsVo.getAccount(), ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getDailyId(), CreateTime.monthTransitionTimestamp(year + "-" + month), CreateTime.monthTransitionTimestamp(nextMonth), Type, "2");
        int boyNum = dailyExerciseService.selMonthNum(ellSelDailyDetailsVo.getAccount(), ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getDailyId(), CreateTime.monthTransitionTimestamp(year + "-" + month), CreateTime.monthTransitionTimestamp(nextMonth), Type, "1");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("girlNum", girlNum);
        jsonObject.put("boyNum", boyNum);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/getAttainNum")
    @ApiOperation("查询达标占比")
    public Result getAttainNum(@RequestBody EllSelSexDetailsVo ellSelDailyDetailsVo) {
        List<EllDailyExercisePlanVo> ellDailyExercisePlanList = dailyExerciseService.selDailyExercisePlanByName(ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getAccount());
        //上下半年日常的时间段
        String[] firstMoths = ellDailyExercisePlanList.get(0).getFirstHalfYear().split("-");
        String[] twoMoths = ellDailyExercisePlanList.get(0).getSecondHalfYear().split("-");
        //本月
        String month = CreateTime.getMonth();
        String year = CreateTime.getYear();
        //下一个月
        String nextMonth = Integer.parseInt(month) < 10 ? "0" + (Integer.parseInt(month) + 1) : Integer.parseInt(month) + 1 + "";
        if (month.equals("12")) {
            //最后一个月就加一年
            nextMonth = (Integer.parseInt(year) + 1) + "-01";
        } else {
            nextMonth = Integer.parseInt(year) + "-" + (Integer.parseInt(month) + 1);
        }
        //达标
        int attainType = 0;
        if (Integer.parseInt(month) >= Integer.parseInt(firstMoths[0]) && Integer.parseInt(month) <= Integer.parseInt(firstMoths[1])) {
            attainType = 1;
        } else if (Integer.parseInt(month) >= Integer.parseInt(twoMoths[0]) && Integer.parseInt(month) <= Integer.parseInt(twoMoths[1])) {
            attainType = 2;
        }
        //不达标
        int attainTypeNo = 0;
        if (Integer.parseInt(month) >= Integer.parseInt(firstMoths[0]) && Integer.parseInt(month) <= Integer.parseInt(firstMoths[1])) {
            attainTypeNo = 3;
        } else if (Integer.parseInt(month) >= Integer.parseInt(twoMoths[0]) && Integer.parseInt(month) <= Integer.parseInt(twoMoths[1])) {
            attainTypeNo = 4;
        }
        //达标/未达标
        int attainNum = dailyExerciseService.selMonthNum(ellSelDailyDetailsVo.getAccount(), ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getDailyId(), CreateTime.monthTransitionTimestamp(year + "-" + month), CreateTime.monthTransitionTimestamp(nextMonth), attainType, null);
        int noAttainNum = dailyExerciseService.selMonthNum(ellSelDailyDetailsVo.getAccount(), ellSelDailyDetailsVo.getOrgId(), ellSelDailyDetailsVo.getDailyId(), CreateTime.monthTransitionTimestamp(year + "-" + month), CreateTime.monthTransitionTimestamp(nextMonth), attainTypeNo, null);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("attainNum", attainNum);
        jsonObject.put("noAttainNum", noAttainNum);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/getUserDaily")
    @ApiOperation("任务完成情况")
    public Result getUserDaily(@RequestBody EllDailyUserVo ellDailyUserVo) {
        List<EllDailyExercisePlanVo> ellDailyExercisePlanVos = dailyExerciseService.selDailyById(ellDailyUserVo.getDailyId());
        //上下半年日常的时间段
        String[] firstMoths = ellDailyExercisePlanVos.get(0).getFirstHalfYear().split("-");
        String[] twoMoths = ellDailyExercisePlanVos.get(0).getSecondHalfYear().split("-");
        //获取选择日期的月份
        int month = CreateTime.getStrTime(CreateTime.getTime()).get("month");
        //日常开始时间
        String dailyStart = CreateTime.getYear();
        //日常结束时间
        String dailyEnd = CreateTime.getYear();
        int Type = 0;
        //本周总任务
        Double weeksTask = 1.0;
        //本月总任务
        Double monthTask = 1.0;
        //本学期
        Double semesterTask = 1.0;
        if (month >= Integer.parseInt(firstMoths[0]) && month <= Integer.parseInt(firstMoths[1])) {
            Type = 1;
            //每个阶段的目标
            weeksTask = ellDailyExercisePlanVos.get(0).getFirstWeeks();
            monthTask = ellDailyExercisePlanVos.get(0).getFirstMonth();
            dailyStart = dailyStart + "-" + firstMoths[0];
            dailyEnd = dailyEnd + "-" + firstMoths[1];
            semesterTask = ellDailyExercisePlanVos.get(0).getFirstTrainMileage();
        } else if (month >= Integer.parseInt(twoMoths[0]) && month <= Integer.parseInt(twoMoths[1])) {
            Type = 2;
            //每个阶段的目标
            weeksTask = ellDailyExercisePlanVos.get(0).getSecondWeeks();
            monthTask = ellDailyExercisePlanVos.get(0).getSecondMonth();
            semesterTask = ellDailyExercisePlanVos.get(0).getSecondTrainMileage();
            dailyStart = dailyStart + "-" + twoMoths[0];
            if (Integer.parseInt(twoMoths[1]) == 12) {
                dailyEnd = (Integer.parseInt(dailyEnd) + 1) + "-" + twoMoths[1];
            } else {
                dailyEnd = dailyEnd + "-" + twoMoths[1];
            }

        }
        //获取本周一0点时间时间戳秒级别
        Long MONDAY = CreateTime.nowMonday().getTime() / 1000;
        //获取下周一0点时间时间戳秒级别
        Long NextMONDAY = CreateTime.nextMonday().getTime() / 1000;
        //本月1号0点时间时间戳秒级别
        Long Month = CreateTime.nowMonth().getTime() / 1000;
        //下月1号0点时间时间戳秒级别
        Long nextMonth = CreateTime.nextMonth().getTime() / 1000;
        //本周任务
        int nowWeek = dailyExerciseService.selUserTime(ellDailyUserVo.getUserId(), ellDailyUserVo.getDailyId(),
                MONDAY, NextMONDAY, Type);
        //本月任务
        int nowMonth = dailyExerciseService.selUserTime(ellDailyUserVo.getUserId(), ellDailyUserVo.getDailyId(),
                Month, nextMonth, Type);
        //本学期
        int nowSemester = dailyExerciseService.selUserTime(ellDailyUserVo.getUserId(), ellDailyUserVo.getDailyId(),
                CreateTime.monthTransitionTimestamp(dailyStart), CreateTime.monthTransitionTimestamp(dailyEnd), Type);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nowRunWeek", (int) ((nowWeek / weeksTask) * 100));
        jsonObject.put("nowRunMonth", (int) ((nowMonth / monthTask) * 100));
        jsonObject.put("nowRunSemester", (int) ((nowSemester / semesterTask) * 100));
        jsonObject.put("nowRunWeekNum", nowWeek);
        jsonObject.put("nowRunMonthNum", nowMonth);
        jsonObject.put("nowRunSemesterNum", nowSemester);
        jsonObject.put("nowWeekNum", weeksTask);
        jsonObject.put("nowMonthNum", monthTask);
        jsonObject.put("nowSemesterNum", semesterTask);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/getUserMonthDaily")
    @ApiOperation("跑步里程达标示意图")
    public Result getUserMonthDaily(@RequestBody EllDailyUserVo ellDailyUserVo) {
        return new Result(HttpStatus.SUCCESS, "成功", dailyExerciseService.getUserMonthDaily(ellDailyUserVo));
    }

    @PostMapping("/getUser")
    @ApiOperation("人脸查询user")
    public Result gerUserId(@RequestBody UserFaceRegisterVo userFaceInfo) {
        //人脸检录就不用解密标识
        String userId = dailyExerciseService.finUserId(userFaceInfo.getImage(), userFaceInfo.getDeviceId());
        if (userId == null || userId.equals("")) {
            return new Result(HttpStatus.MATCHING_FAILURE, "未匹配到相识人脸");
        }
        if (userId.equals(HttpStatus.NON_LIVING_BODY + "")) {
            return new Result(HttpStatus.NON_LIVING_BODY, "非活体");
        }
        if (userId.equals(HttpStatus.MATCHING_FAILURE + "")) {
            return new Result(HttpStatus.MATCHING_FAILURE, "未匹配到相识人脸");
        }
        List<EllReturnScoreVo> ellReturnScoreVos = ellUserService.findUserBy(userId, 0);
        if (ellReturnScoreVos == null || ellReturnScoreVos.size() <= 0) {
            return new Result(HttpStatus.ERROR, "未查询到该学生");
        }
        //查询该学生的部门信息
        ellReturnScoreVos.get(0).setOrgName(ellUserService.findDeptNameById(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId())));
        ellReturnScoreVos.get(0).setSchoolName(ellUserService.selSchoolName(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId())));
        return new Result(HttpStatus.SUCCESS, "成功", ellReturnScoreVos.get(0));
    }

    //和上面逻辑相同，前端不想改没办法只能多加接口
    @PostMapping("/getUsers")
    @ApiOperation("人脸查询user")
    public Result gerUserIds(@RequestBody EllGetUsesInfoVo userFaceInfos) {
        //人脸检录就不用解密标识
        List<EllReturnScoreVo> ellReturnScoreVoList = new ArrayList<>();
        for (int i = 0; i < userFaceInfos.getImages().size(); i++) {
            String userId = dailyExerciseService.finUserId(userFaceInfos.getImages().get(i), userFaceInfos.getDeviceId());
            if (userId == null || userId.equals("")) {
                return new Result(HttpStatus.MATCHING_FAILURE, "未匹配到相识人脸");
            }
            if (userId.equals(HttpStatus.NON_LIVING_BODY + "")) {
                return new Result(HttpStatus.NON_LIVING_BODY, "非活体");
            }
            if (userId.equals(HttpStatus.MATCHING_FAILURE + "")) {
                return new Result(HttpStatus.MATCHING_FAILURE, "未匹配到相识人脸");
            }
            List<EllReturnScoreVo> ellReturnScoreVos = ellUserService.findUserBy(userId, 0);
            if (ellReturnScoreVos == null || ellReturnScoreVos.size() <= 0) {
                return new Result(HttpStatus.ERROR, "未查询到该学生");
            }
            //查询该学生的部门信息
            ellReturnScoreVos.get(0).setOrgName(ellUserService.findDeptNameById(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId())));
            ellReturnScoreVos.get(0).setSchoolName(ellUserService.selSchoolName(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId())));
            ellReturnScoreVoList.add(ellReturnScoreVos.get(0));
        }
        return new Result(HttpStatus.SUCCESS, "成功", ellReturnScoreVoList);
    }
}
