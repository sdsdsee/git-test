package com.eluolang.playground.controller;


import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.hardware.dto.PhysicalTrainDto;
import com.eluolang.common.core.hardware.vo.FaceDetectData;
import com.eluolang.common.core.hardware.vo.NowRunDataVo;
import com.eluolang.common.core.pojo.EllDailyExercisePlan;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.service.PlayGroundService;
import com.eluolang.playground.service.feign.TcpServerRemoteService;
import com.eluolang.playground.subsribe.MQTTConfig;
import com.eluolang.playground.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * TODO:
 *
 * @Author: ZHANG
 * @create: 2021/8/27 16:16
 */
@Api(tags = "智慧操场")
@RestController
@RequestMapping("/playground")
public class PlayGroundController {
    @Autowired
    private PlayGroundService playGroundService;

    @Autowired
    private RedisService redisService;

//    @Autowired
//    private MQTTSubsribe mqttSubsribe;

    @Autowired
    private MQTTConfig config;

    @Autowired
    private TcpServerRemoteService tcpServerRemoteService;



    private String start = "RUNNING_START";
    private String end = "RUNNING_END";

//    @ApiOperation(value = "开始录制")
//    @GetMapping(value = "/Start")
//    public void Start(Integer id,String FileName) {
//        String ffmpegPath="ffmpeg";
//        String streamUrl="rtsp://admin:ell85353507@192.168.3.78:554/ch0/sub/av_stream";
//        String FilePath="C:\\Users\\Administrator\\Desktop\\opencv_jar_so\\"+FileName;
//        Process process = rtspToMP4.StartRecord(ffmpegPath, streamUrl, FilePath);
//        if(null!=process){
//            map.put(id,process);
////            return Result.success();
//        }
////       return Result.failed();
//    }

//    @ApiOperation(value = "结束录制")
//    @PostMapping(value = "/stop")
//    public void stop() {
//        redisService.setCacheObject("test","11111",5, TimeUnit.MINUTES);
//    }
//    @ApiOperation(value = "ssss")
//    @PostMapping(value = "/start")
//    public Long start() {
//        return redisService.getExpire("test");
//    }


    @ApiOperation(value = "当前跑步人员详情")
    @PostMapping(value = "/currentRunningDetails")
    public Result currentRunningDetails(String dailyId) {
        List<CurrentRunningDetailsVo> currentRunningDetailsVos = new ArrayList<>();
        List<String> listRun = redisService.getCacheByPrefix(CacheConstant.RUNNING_FREE_KEY+dailyId+"*");
        if (listRun.size()!=0){
            for (int i = 0; i < listRun.size(); i++) {
                PhysicalTrainDto dto = redisService.getCacheObject(listRun.get(i));
                CurrentRunningDetailsVo currentRunningDetailsVo = playGroundService.selCurrentRunningDetails(dto);
                currentRunningDetailsVos.add(currentRunningDetailsVo);
            }
        }

        return new Result(HttpStatus.SUCCESS,"查询成功",currentRunningDetailsVos);
    }

    @ApiOperation(value = "跑步每周人数统计")
    @PostMapping(value = "/selRunningNumberCensus")
    public Result selRunningNumberCensus(@RequestBody PlanTotalNumDto planTotalNumDto) {
        StandardNumVo standardNumVo = playGroundService.selRunningNumberCensus(planTotalNumDto);
        return new Result(HttpStatus.SUCCESS,"查询成功",standardNumVo);
    }

    @ApiOperation(value = "近30天每日跑步人数和里程")
    @PostMapping(value = "/selRunningData")
    public Result selRunningData(String deviceId) {
        Map<String,Object> map = new HashMap<>();

        List<EllDeviceInfoVo> ellDeviceInfoVos = playGroundService.findDeviceInfo(deviceId);
        List<String> fileUrl = new ArrayList<>();
        if (ellDeviceInfoVos.size() != 0){
            for(int i = 0; i < ellDeviceInfoVos.size(); i++) {
                fileUrl.add(ellDeviceInfoVos.get(i).getFileUrl());
            }
        }
        Map<String,Object> videos = new HashMap<>();
        videos.put("show",1);
        videos.put("list",fileUrl);
        map.put("videos",videos);

        List<RunningDataVo> data = new ArrayList<>();
        EllDailyExercisePlan exercisePlan = playGroundService.selExercisePlan(deviceId);
        List<Integer> deptList = new ArrayList<>();
        deptList.add(Integer.valueOf(exercisePlan.getUseOrgId()));
        if (exercisePlan != null){
            List<RunningDataVo> runningDataVos = playGroundService.selRunningData(deptList);
            for(int i = 0; i < 30; i++) {
                boolean flag = true;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = DateUtils.addDays(new Date(), -(i+1));
                String formatDate = sdf.format(date);

                if (runningDataVos.size() != 0){
                    for(int j = 0; j < runningDataVos.size(); j++) {
                        RunningDataVo runningData= new RunningDataVo();
                        if (formatDate.equals(runningDataVos.get(j).getDate())){
                            runningData.setTimes(runningDataVos.get(j).getTimes());
                            runningData.setMileage(runningDataVos.get(j).getMileage());
                            runningData.setDate(runningDataVos.get(j).getDate());
                            data.add(runningData);
                            flag = false;
                        }
                    }
                }
                if (flag == true){
                    RunningDataVo runningDataVo = new RunningDataVo();
                    runningDataVo.setTimes(0);
                    runningDataVo.setMileage("0");
                    runningDataVo.setDate(formatDate);
                    data.add(runningDataVo);
                }
            }
            Map<String,Object> records = new HashMap<>();
            records.put("show",1);
            records.put("list",data);
            map.put("records",records);
            return new Result(HttpStatus.SUCCESS,"查询成功",map);
        }
        return new Result(HttpStatus.ERROR,"未查询到日常锻炼计划",null);
    }

    @ApiOperation(value = "累计运动人数")
    @PostMapping(value = "/selTotalSportNumber")
    public Result selTotalSportNumber(String dailyId) {
        List<String> dateList = DateUtils.getNearSevenDay();
        List<TotalSportNumberVo> totalSportNumberVos = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            TotalSportNumberVo totalSportNumberVo = playGroundService.selTotalSportNumber(dateList.get(i),dailyId);
            totalSportNumberVos.add(totalSportNumberVo);
        }
        return new Result(HttpStatus.SUCCESS,"查询成功",totalSportNumberVos);
    }

    @ApiOperation(value = "本月跑步数据")
    @PostMapping(value = "/thisMonthRunData")
    public Result thisMonthRunData(@RequestBody MonthRunDataDto monthRunDataDto) {
        /** 日常锻炼计划总人数 */
        Integer dailyTotalNum = playGroundService.selPlanTotalNum(monthRunDataDto.getDailyId());
        MonthRunDataVo monthRunDataVo = playGroundService.selRankByUserId(monthRunDataDto);
        if (monthRunDataVo == null){
            return new Result(HttpStatus.ERROR,"本月未查询到此人跑步数据",null);
        }
        monthRunDataVo.setTotal(String.valueOf(dailyTotalNum));
        /** 根据计划id查询计划详情 */
        EllDailyExercisePlan ellDailyExercisePlan = playGroundService.selDailyPlanById(monthRunDataDto.getDailyId());
        //本周需要完成的里程数
        Double monthMileage = 0.0;
        //需要查询的月份
        Integer month = Integer.valueOf(monthRunDataDto.getMonth().split("-")[1]);
        //日常锻炼计划上半年划分的月份
        String[] firstHalfYear = ellDailyExercisePlan.getFirstHalfYear().split("-");
        //日常锻炼计划下半年划分的月份
        String[] secondHalfYear = ellDailyExercisePlan.getSecondHalfYear().split("-");
        //判断当前开始时间处于上半年还是下半年，以此写入本周需要完成的里程数
        if (Integer.valueOf(firstHalfYear[0]) >= month || Integer.valueOf(firstHalfYear[1]) <= month){
            monthMileage = ellDailyExercisePlan.getFirstMonth();
        }else if(Integer.valueOf(secondHalfYear[0]) >= month || Integer.valueOf(secondHalfYear[1]) <= month){
            monthMileage = ellDailyExercisePlan.getSecondMonth();
        }
        Double surplusMileage = monthMileage - Double.parseDouble(monthRunDataVo.getFinishMileage());
        monthRunDataVo.setMonthMileage(String.valueOf(monthMileage));
        monthRunDataVo.setSurplusMileage(String.valueOf(surplusMileage));
        //查询此人员跑步累计里程数
        CurrentRunningDetailsVo currentRunningDetailsVo = playGroundService.selMileageSumByUserId(monthRunDataDto.getDailyId(),monthRunDataDto.getUserId());
        monthRunDataVo.setTotalMileage(currentRunningDetailsVo.getMileageSum());
        return new Result(HttpStatus.SUCCESS,"查询成功",monthRunDataVo);
    }

    @ApiOperation(value = "每月跑步里程达标")
    @PostMapping(value = "/runMileageStandard")
    public Result runMileageStandard(@RequestBody MonthRunDataDto monthRunDataDto) {
        List<MileageStandardVo> mileageStandardVos = new ArrayList<>();
        /** 根据计划id查询计划详情 */
        EllDailyExercisePlan ellDailyExercisePlan = playGroundService.selDailyPlanById(monthRunDataDto.getDailyId());
        //日常锻炼计划上半年划分的月份
        String[] firstHalfYear = ellDailyExercisePlan.getFirstHalfYear().split("-");
        //日常锻炼计划下半年划分的月份
        String[] secondHalfYear = ellDailyExercisePlan.getSecondHalfYear().split("-");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        for (int i = 1; i <= 12; i++) {
            MileageStandardVo mileageStandardVo = new MileageStandardVo();
            mileageStandardVo.setMonth(i+"月");
            monthRunDataDto.setMonth(year+"-"+ StringUtils.geFourNumber(i));
            //本周需要完成的里程数
            Double monthMileage = 0.0;
            //判断当前开始时间处于上半年还是下半年，以此写入本周需要完成的里程数
            if (Integer.valueOf(firstHalfYear[0]) >= i || Integer.valueOf(firstHalfYear[1]) <= i){
                monthMileage = ellDailyExercisePlan.getFirstMonth();
            }else if(Integer.valueOf(secondHalfYear[0]) >= i || Integer.valueOf(secondHalfYear[1]) <= i){
                monthMileage = ellDailyExercisePlan.getSecondMonth();
            }
            Integer finishMileage = playGroundService.selMileageStandard(monthRunDataDto);
            mileageStandardVo.setMonthMileage(String.valueOf(monthMileage));
            mileageStandardVo.setFinishMileage(String.valueOf(Float.valueOf(finishMileage)/1000));
            mileageStandardVos.add(mileageStandardVo);
        }
        return new Result(HttpStatus.SUCCESS,"查询成功",mileageStandardVos);
    }

    @ApiOperation(value = "近七天跑步心率区间")
    @PostMapping(value = "/selHeartRateInterval")
    public Result selHeartRateInterval(String dailyId,String userId) {
        List<String> dateList = DateUtils.getNearSevenDay();
        List<HeartRateIntervalVo> heartRateIntervalVos = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            HeartRateIntervalVo heartRateIntervalVo = new HeartRateIntervalVo();
            Integer time = 0;
            Integer mileage = 0;
            List<EllDailyHistoryVo> ellDailyHistoryVoList = playGroundService.selHeartRateInterval(dailyId,userId,dateList.get(i));
            if (ellDailyHistoryVoList.size() > 1){
                for (int j = 0; j < ellDailyHistoryVoList.size(); j++) {
                    if (j+1<ellDailyHistoryVoList.size()){
                        Integer s = DateUtils.getDatePoorMin(new Date(Long.valueOf(ellDailyHistoryVoList.get(j+1).getLocationTime())),new Date(Long.valueOf(ellDailyHistoryVoList.get(j).getLocationTime()))).multiply(BigDecimal.valueOf(60)).intValue();
                        if (s <= 60*5){
                            time += s;
                        }
                    }
                    mileage += ellDailyHistoryVoList.get(j).getMileage();
                }
            }

            heartRateIntervalVo.setDate(dateList.get(i));
            heartRateIntervalVo.setTime(DateUtils.getSecToMin(time));
            heartRateIntervalVo.setSumMileage(String.valueOf(Float.valueOf(mileage)/1000));
            heartRateIntervalVos.add(heartRateIntervalVo);
        }
        return new Result(HttpStatus.SUCCESS,"查询成功",heartRateIntervalVos);
    }

    @ApiOperation(value = "当前跑步人员信息")
    @PostMapping(value = "/selCurrentUserRunData")
    public Result selCurrentUserRunData(@RequestBody RunningDataDto runningDataDto) {
        NowRunDataVo nowRunDataVo = redisService.getCacheObject(CacheConstant.NOW_RUNNING_TIME+runningDataDto.getDailyId()+":"+runningDataDto.getUserId());
        if (nowRunDataVo == null){
            return new Result(HttpStatus.ERROR,"未查询到数据",null);
        }
        runningDataDto.setStartTime(nowRunDataVo.getStartTime());
        runningDataDto.setEndTime(nowRunDataVo.getEndTime());
        CurrentUserRunDataVo currentUserRunDataVo = playGroundService.selCurrentUserRunData(runningDataDto);
        currentUserRunDataVo.setMileage(currentUserRunDataVo.getMileage()/1000);
        Integer s = DateUtils.getDatePoorMin(new Date(Long.valueOf(currentUserRunDataVo.getEndTime())),new Date(Long.valueOf(currentUserRunDataVo.getStartTime()))).multiply(BigDecimal.valueOf(60)).intValue();
        currentUserRunDataVo.setTime(DateUtils.getSecToMin(s));
        currentUserRunDataVo.setAverageSpeed(DateUtils.getSecToMin((int) (s/(currentUserRunDataVo.getMileage()))));

        List<EllDailyHistoryVo> ellDailyHistoryVoList = playGroundService.selCurrentRunData(runningDataDto);
        if (ellDailyHistoryVoList.size() != 0){
            List<Integer> timeList = new ArrayList<>();
            Integer mileage = 0;
            String locationTime = ellDailyHistoryVoList.get(0).getLocationTime();
          for (int i = 0; i < ellDailyHistoryVoList.size(); i++) {
              mileage += ellDailyHistoryVoList.get(i).getMileage();
              if (mileage >= 400){
                  Integer time = DateUtils.getDatePoorMin(new Date(Long.valueOf(ellDailyHistoryVoList.get(i).getLocationTime())),new Date(Long.valueOf(locationTime))).multiply(BigDecimal.valueOf(60)).intValue();
                  timeList.add(time);
                  locationTime = ellDailyHistoryVoList.get(i).getLocationTime();
                  mileage = 0;
              }
          }
          if (timeList.size() != 0){
            //升序排列
            Collections.sort(timeList, new Comparator<Integer>() {
                public int compare(Integer s1, Integer s2) {
                    return s1.compareTo(s2);
                }
            });
            currentUserRunDataVo.setBestResult(DateUtils.getSecToMin(timeList.get(0))+"/圈");
          }
        }
//        List<String> list = MileageUtil.Mileage(currentUserRunDataVo.getMileage().intValue());
//        List<GetLocationTimeVo> getLocationTimeVos = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            runningDataDto.setRank(list.get(i));
//            GetLocationTimeVo getLocationTimeVo = playGroundService.selLocationTime(runningDataDto);
//            getLocationTimeVos.add(getLocationTimeVo);
//        }
//        //升序排列
//        Collections.sort(getLocationTimeVos, new Comparator<GetLocationTimeVo>() {
//            public int compare(GetLocationTimeVo s1, GetLocationTimeVo s2) {
//                return s1.getLocationTime().compareTo(s2.getLocationTime());
//            }
//        });
//        List<Integer> time = new ArrayList<>();
//        if (getLocationTimeVos.size() == 0 || getLocationTimeVos.size() == 1) {
//            currentUserRunDataVo.setBestResult("");
//        }else {
//            for (int i = 0; i < getLocationTimeVos.size(); i++) {
//                if (i + 1 < getLocationTimeVos.size()) {
//                    Integer miao =
//                            DateUtils.getDatePoorMin(
//                                    new Date(Long.valueOf(getLocationTimeVos.get(i + 1).getLocationTime())),
//                                    new Date(Long.valueOf(getLocationTimeVos.get(i).getLocationTime())))
//                                    .multiply(BigDecimal.valueOf(60))
//                                    .intValue();
//                    time.add(miao);
//                }
//            }
//            //升序排列
//            Collections.sort(time, new Comparator<Integer>() {
//                public int compare(Integer s1, Integer s2) {
//                    return s1.compareTo(s2);
//                }
//            });
//            currentUserRunDataVo.setBestResult(String.valueOf(time.get(0)));
//        }

        return new Result(HttpStatus.SUCCESS,"查询成功",currentUserRunDataVo);
    }

    @ApiOperation(value = "跑步人员区间配速")
    @PostMapping(value = "/selCurrentRunDataDetails")
    public Result selCurrentRunDataDetails(@RequestBody RunningDataDto runningDataDto) {
        NowRunDataVo nowRunDataVo = redisService.getCacheObject(CacheConstant.NOW_RUNNING_TIME+runningDataDto.getDailyId()+":"+runningDataDto.getUserId());
        if (nowRunDataVo == null){
            return new Result(HttpStatus.ERROR,"未查询到数据",null);
        }
        runningDataDto.setStartTime(nowRunDataVo.getStartTime());
        runningDataDto.setEndTime(nowRunDataVo.getEndTime());
        List<EllDailyHistoryVo> ellDailyHistoryVoList = playGroundService.selCurrentRunData(runningDataDto);
        Integer count = ellDailyHistoryVoList.size() / 4;
        List<SectionSpeedVo> sectionSpeedVoList = new ArrayList<>();
        if (count > 0) {
          List<EllDailyHistoryVo> list = ellDailyHistoryVoList.subList(0, count * 1+1);
          List<EllDailyHistoryVo> list2 = ellDailyHistoryVoList.subList(count * 1, count * 2+1);
          List<EllDailyHistoryVo> list3 = ellDailyHistoryVoList.subList(count * 2, count * 3+1);
          List<EllDailyHistoryVo> list4 = ellDailyHistoryVoList.subList(count * 3, ellDailyHistoryVoList.size());

            Integer s = DateUtils.getDatePoorMin(new Date(Long.valueOf(list.get(list.size()-1).getLocationTime())),new Date(Long.valueOf(list.get(0).getLocationTime()))).multiply(BigDecimal.valueOf(60)).intValue();
            Integer s2 = DateUtils.getDatePoorMin(new Date(Long.valueOf(list2.get(list2.size()-1).getLocationTime())),new Date(Long.valueOf(list2.get(0).getLocationTime()))).multiply(BigDecimal.valueOf(60)).intValue();
            Integer s3 = DateUtils.getDatePoorMin(new Date(Long.valueOf(list3.get(list3.size()-1).getLocationTime())),new Date(Long.valueOf(list3.get(0).getLocationTime()))).multiply(BigDecimal.valueOf(60)).intValue();
            Integer s4 = 0;
            if (list4.size() > 1) {
                s4 =
                    DateUtils.getDatePoorMin(
                            new Date(Long.valueOf(list4.get(list4.size() - 1).getLocationTime())),
                            new Date(Long.valueOf(list4.get(0).getLocationTime())))
                        .multiply(BigDecimal.valueOf(60))
                        .intValue();
              }
//
//          // 升序排列
//          Collections.sort(
//              list,
//              new Comparator<EllDailyHistoryVo>() {
//                public int compare(EllDailyHistoryVo s1, EllDailyHistoryVo s2) {
//                  return Integer.valueOf(s1.getHeartRate()).compareTo(Integer.valueOf(s2.getHeartRate()));
//                }
//              });
//          // 升序排列
//          Collections.sort(
//              list2,
//              new Comparator<EllDailyHistoryVo>() {
//                public int compare(EllDailyHistoryVo s1, EllDailyHistoryVo s2) {
//                  return Integer.valueOf(s1.getHeartRate()).compareTo(Integer.valueOf(s2.getHeartRate()));
//                }
//              });
//          // 升序排列
//          Collections.sort(
//              list3,
//              new Comparator<EllDailyHistoryVo>() {
//                public int compare(EllDailyHistoryVo s1, EllDailyHistoryVo s2) {
//                  return Integer.valueOf(s1.getHeartRate()).compareTo(Integer.valueOf(s2.getHeartRate()));
//                }
//              });
//      if (list4.size() > 1) {
//        // 升序排列
//        Collections.sort(
//            list4,
//            new Comparator<EllDailyHistoryVo>() {
//              public int compare(EllDailyHistoryVo s1, EllDailyHistoryVo s2) {
//                return Integer.valueOf(s1.getHeartRate()).compareTo(Integer.valueOf(s2.getHeartRate()));
//              }
//            });
//          }
//               String heart = list.get(0).getHeartRate()+"-"+list.get(list.size()-1).getHeartRate();
//                String heart2 = list2.get(0).getHeartRate()+"-"+list2.get(list2.size()-1).getHeartRate();
//                String heart3 = list3.get(0).getHeartRate()+"-"+list3.get(list3.size()-1).getHeartRate();
//                String heart4 = "0";
//                if (list4.size() > 1) {
//                    heart4 =
//                    list4.get(0).getHeartRate() + "-" + list4.get(list4.size() - 1).getHeartRate();
//                }
                SectionSpeedVo sectionSpeedVo = new SectionSpeedVo();
                sectionSpeedVo.setTime(DateUtils.getSecToMin(s));
//                sectionSpeedVo.setHeart(heart);
                sectionSpeedVoList.add(sectionSpeedVo);

                SectionSpeedVo sectionSpeedVo2 = new SectionSpeedVo();
                sectionSpeedVo2.setTime(DateUtils.getSecToMin(s2));
//                sectionSpeedVo2.setHeart(heart2);
                sectionSpeedVoList.add(sectionSpeedVo2);

                SectionSpeedVo sectionSpeedVo3 = new SectionSpeedVo();
                sectionSpeedVo3.setTime(DateUtils.getSecToMin(s3));
//                sectionSpeedVo3.setHeart(heart3);
                sectionSpeedVoList.add(sectionSpeedVo3);
                if (list4.size() > 1){
                    SectionSpeedVo sectionSpeedVo4 = new SectionSpeedVo();
                    sectionSpeedVo4.setTime(DateUtils.getSecToMin(s4));
//                    sectionSpeedVo.setHeart(heart4);
                    sectionSpeedVoList.add(sectionSpeedVo4);
                }
            }
        return new Result(HttpStatus.SUCCESS,"查询成功",sectionSpeedVoList);
    }

    @ApiOperation(value = "查询某人跑步排名")
    @PostMapping(value = "/selRunningRank")
    public Result selRunningRank(@RequestBody RunningRankDto runningRankDto) {
        List<String> dateList = DateUtils.getNearTenDay();
        List<RunningRankVo> runningRankVoList = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
          runningRankDto.setDate(dateList.get(i));
          RunningRankVo runningRankVo = playGroundService.selUserRunRank(runningRankDto);

          if (runningRankVo != null) {
            RunningRankVo runningRankVo1 = playGroundService.selUserRunRankBySchool(runningRankDto);
            runningRankVo.setSchoolRank(runningRankVo1.getRank());
            runningRankVoList.add(runningRankVo);
          }
        }
        return new Result(HttpStatus.SUCCESS,"查询成功",runningRankVoList);
    }

    @ApiOperation(value = "跑步情况分析")
    @PostMapping(value = "/runningSituationAnalyse")
    public Result runSituationAnalyse(String dailyId,String userId,String date) {
        RunningAnalyseVo runningAnalyseVo = new RunningAnalyseVo();
        List<EllDailyHistoryVo> ellDailyHistoryVoList = playGroundService.runSituationAnalyse(dailyId, userId, date);
        if (ellDailyHistoryVoList.size() > 1){
            runningAnalyseVo.setDate(date);
            int mileageSum = ellDailyHistoryVoList.stream().mapToInt(EllDailyHistoryVo::getMileage).sum();
            Integer s = DateUtils.getDatePoorMin(new Date(Long.valueOf(ellDailyHistoryVoList.get(ellDailyHistoryVoList.size()-1).getLocationTime())),new Date(Long.valueOf(ellDailyHistoryVoList.get(0).getLocationTime()))).multiply(BigDecimal.valueOf(60)).intValue();
            runningAnalyseVo.setAverageSpeed(DateUtils.getSecToMin(s/(mileageSum/1000)));
            List<RunningAnalyseVo.DataBean> dataBeanList = new ArrayList<>();
            for (int i = 0; i < ellDailyHistoryVoList.size(); i++) {
                if (i != ellDailyHistoryVoList.size() - 1) {
                    RunningAnalyseVo.DataBean dataBean = new RunningAnalyseVo.DataBean();
                    Integer miao = DateUtils.getDatePoorMin(new Date(Long.valueOf(ellDailyHistoryVoList.get(i+1).getLocationTime())),new Date(Long.valueOf(ellDailyHistoryVoList.get(i).getLocationTime()))).multiply(BigDecimal.valueOf(60)).intValue();
                    dataBean.setSectionSpeed(DateUtils.getSecToMin(miao));
                    dataBean.setHeart(ellDailyHistoryVoList.get(i+1).getHeartRate());
                    dataBeanList.add(dataBean);
                }
            }
            runningAnalyseVo.setDataBeanList(dataBeanList);
            return new Result(HttpStatus.SUCCESS,"查询成功",runningAnalyseVo);
        }
        return new Result(HttpStatus.ERROR,"未有当日跑步数据",null);
    }

//    @ApiOperation(value = "自由跑检录")
//    @PostMapping(value = "/freeRunningCheck")
//    public Result freeRunningCheck(@RequestBody PhysicalTrainDto physicalTrainDto) {
//        List<String> testList = redisService.getCacheByPrefix(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getDailyId()+"*");
//        if (testList.size()!=0){
//            for (int i = 0; i < testList.size(); i++) {
//                PhysicalTrainDto dto = redisService.getCacheObject(testList.get(i));
//                if (dto.getMac().equals(physicalTrainDto.getMac())){
//                    return new Result(HttpStatus.ERROR,"该手环已绑定考核模式，清解绑后重试",null);
//                }
//            }
//        }
//        List<String> listRun = redisService.getCacheByPrefix(CacheConstant.RUNNING_FREE_KEY+physicalTrainDto.getDailyId()+"*");
//        if (listRun.size()!=0){
//            for (int i = 0; i < listRun.size(); i++) {
//                PhysicalTrainDto dto = redisService.getCacheObject(listRun.get(i));
//                if (dto.getMac().equals(physicalTrainDto.getMac())){
//                    return new Result(HttpStatus.ERROR,"该手环已绑定",null);
//                }
//            }
//        }
//        String date = DateUtils.getTime();
//        physicalTrainDto.setStartTime(date);
//        redisService.setCacheObject(CacheConstant.RUNNING_FREE_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId(),physicalTrainDto);
//        UserCallVo userCallVo = playGroundService.selUserMessageById(physicalTrainDto.getUserId());
//        userCallVo.setMac(physicalTrainDto.getMac());
//        //检录成功后人员的个人跑步信息，存入redis
//        redisService.setCacheObject(CacheConstant.RUNNING_CALL_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId(),userCallVo);
//        List<String> list = new ArrayList<>();
//        list.add(physicalTrainDto.getMac());
//        playGroundService.issueInstruction(list);
//
//        return new Result(HttpStatus.SUCCESS,"检录成功",null);
//    }

//    @ApiOperation(value = "自由跑结束")
//    @PostMapping(value = "/freeRunningEnd")
//    public Result freeRunningEnd(String dailyId,String userId) {
//        PhysicalTrainDto physicalTrainDto = redisService.getCacheObject(CacheConstant.RUNNING_FREE_KEY+dailyId+":"+userId);
//
//        List<String> list = new ArrayList<>();
//        list.add(physicalTrainDto.getMac());
//        playGroundService.removeIssued(list);
//
//        redisService.deleteObject(CacheConstant.RUNNING_FREE_KEY+dailyId+":"+userId);
//        redisService.deleteObject(CacheConstant.RUNNING_CALL_KEY+physicalTrainDto.getDailyId()+":"+userId);
//
//        return new Result(HttpStatus.SUCCESS,"已结束",null);
//    }

    @ApiOperation(value = "测试跑检录")
    @PostMapping(value = "/testRunningCheck")
    public Result testRunningCheck(@RequestBody PhysicalTrainDto physicalTrainDto) {
        List<String> listRun = redisService.getCacheByPrefix(CacheConstant.RUNNING_FREE_KEY+physicalTrainDto.getDailyId()+"*");
        if (listRun.size()!=0){
            for (int i = 0; i < listRun.size(); i++) {
                PhysicalTrainDto dto = redisService.getCacheObject(listRun.get(i));
                if (dto.getMac().equals(physicalTrainDto.getMac())){
                    return new Result(HttpStatus.ERROR,"该手环已绑定锻炼模式，清解绑后重试",null);
                }
            }
        }
        List<String> list = redisService.getCacheByPrefix(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getDailyId()+"*");
        if (list.size()!=0){
            for (int i = 0; i < list.size(); i++) {
                PhysicalTrainDto dto = redisService.getCacheObject(list.get(i));
                if (dto.getMac().equals(physicalTrainDto.getMac())){
                    return new Result(HttpStatus.ERROR,"该手环已绑定",null);
                }
            }
        }
//        String date = DateUtils.getTime();
//        physicalTrainDto.setStartTime(date);
        //测试跑手环绑定信息，存入redis
        redisService.setCacheObject(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId(),physicalTrainDto);
        UserCallVo userCallVo = playGroundService.selUserMessageById(physicalTrainDto.getUserId());
        userCallVo.setMac(physicalTrainDto.getMac());
        userCallVo.setProName(physicalTrainDto.getProName());

        //检录成功后人员的个人跑步信息，存入redis
        redisService.setCacheObject(CacheConstant.RUNNING_CALL_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId(),userCallVo);
        return new Result(HttpStatus.SUCCESS,"检录成功",null);
    }

//    @ApiOperation(value = "开始测试")
//    @PostMapping(value = "/issueInstruction")
//    public Result issueInstruction(String dailyId) throws Exception {
//        String date = DateUtils.getTime();
//        List<String> runList = redisService.getCacheByPrefix(CacheConstant.RUNNING_TEST_KEY+dailyId+"*");
//        if (runList.size()!=0){
//            for (int i = 0; i < runList.size(); i++) {
//                PhysicalTrainDto dto = redisService.getCacheObject(runList.get(i));
//                dto.setStartTime(date);
//                //测试跑手环绑定信息，存入redis
//                redisService.setCacheObject(CacheConstant.RUNNING_TEST_KEY+dailyId+":"+dto.getUserId(),dto);
//            }
//        }
//        List<String> list = new ArrayList<>();
//        List<String> userList = redisService.getCacheByPrefix(CacheConstant.RUNNING_CALL_KEY+dailyId+"*");
//        if (userList.size()!=0){
//            for (int i = 0; i < userList.size(); i++) {
//                UserCallVo userCallVo = redisService.getCacheObject(userList.get(i));
//                list.add(userCallVo.getMac());
//            }
//            playGroundService.issueInstruction(list);
//        }
//        //给大屏通过socket推送消息
//        List<String> dpDevice = redisService.getCacheByPrefix(CacheConstant.LARGE_SCREEN_DEVICE+"*");
//        if (dpDevice.size()!=0){
//            for (int i = 0; i < dpDevice.size(); i++) {
//                String dp = redisService.getCacheObject(dpDevice.get(i));
//                tcpServerRemoteService.sendTo(start,dp);
//            }
//        }
//        redisService.setCacheObject(CacheConstant.RUNNING_TEST_BUTTON_KEY,"1");
//        return new Result(HttpStatus.SUCCESS,"检录成功",null);
//    }

//    @ApiOperation(value = "结束测试")
//    @PostMapping(value = "/runTestEnd")
//    public Result runTestEnd(String dailyId) throws Exception {
//        List<String> list = new ArrayList<>();
//        List<String> macList = redisService.getCacheByPrefix(CacheConstant.RUNNING_TEST_KEY+dailyId+"*");
//        if (macList.size() != 0){
//        for (int i = 0; i < macList.size(); i++) {
//            PhysicalTrainDto physicalTrainDto = redisService.getCacheObject(macList.get(i));
//            playGroundService.longRaceResult(physicalTrainDto);
//            redisService.deleteObject(CacheConstant.RUNNING_TEST_KEY+dailyId+":"+physicalTrainDto.getUserId());
//        }
//        }
//        List<String> userList = redisService.getCacheByPrefix(CacheConstant.RUNNING_CALL_KEY+dailyId+"*");
//        if (userList.size()!=0){
//            for (int i = 0; i < userList.size(); i++) {
//                UserCallVo userCallVo = redisService.getCacheObject(userList.get(i));
//                list.add(userCallVo.getMac());
//                redisService.deleteObject(userList.get(i));
//            }
//            playGroundService.removeIssued(list);
//        }
//        //给大屏通过socket推送消息
//        List<String> dpDevice = redisService.getCacheByPrefix(CacheConstant.LARGE_SCREEN_DEVICE+"*");
//        if (dpDevice.size()!=0){
//            for (int i = 0; i < dpDevice.size(); i++) {
//                String dp = redisService.getCacheObject(dpDevice.get(i));
//                tcpServerRemoteService.sendTo(end,dp);
//            }
//        }
//        redisService.deleteObject(CacheConstant.RUNNING_TEST_BUTTON_KEY);
//        return new Result(HttpStatus.SUCCESS,"结束成功",null);
//    }

    @ApiOperation(value = "查询自由跑计划id")
    @PostMapping(value = "/selDailyIdByOrgId")
    public Result selDailyIdByOrgId(Integer orgId) throws Exception {
        String dailyId = playGroundService.selDailyIdByOrgId(orgId);
        return new Result(HttpStatus.SUCCESS,"查询成功",dailyId);
    }

    @ApiOperation(value = "跑步平板模式切换")
    @PostMapping(value = "/modeSwitch")
    public Result modeSwitch(@RequestBody ModeSwitchDto modeSwitchDto) throws Exception {
        redisService.setCacheObject(CacheConstant.RUNNING_MODE_KEY,modeSwitchDto);
        List<String> list = redisService.getCacheByPrefix(CacheConstant.LARGE_SCREEN_DEVICE+"*");
        if (list.size()!=0){
            for (int i = 0; i < list.size(); i++) {
               String id = redisService.getCacheObject(list.get(i));
               tcpServerRemoteService.sendTo(JSONObject.toJSONString(modeSwitchDto),id);
            }
        }
        return new Result(HttpStatus.SUCCESS,"切换成功",null);
    }

    @ApiOperation(value = "查询测试跑按钮状态")
    @PostMapping(value = "/selTestRunningButton")
    public Result selTestRunningButton() {
        String type = redisService.getCacheObject(CacheConstant.RUNNING_TEST_BUTTON_KEY);
        return new Result(HttpStatus.SUCCESS,"查询成功",type);
    }

    @ApiOperation(value = "跑步检录人员信息查询")
    @PostMapping(value = "/runCheckData")
    public Result runCheckData(String dailyId) throws Exception {
        List<String> list = redisService.getCacheByPrefix(CacheConstant.RUNNING_CALL_KEY+dailyId+"*");
        List<UserCallVo> userCallVoList = new ArrayList<>();
        if (list.size()!=0){
            for (int i = 0; i < list.size(); i++) {
                UserCallVo userCallVo = redisService.getCacheObject(list.get(i));
                userCallVoList.add(userCallVo);
            }
            return new Result(HttpStatus.SUCCESS,"查询成功",userCallVoList);
        }
        return new Result(HttpStatus.ERROR,"未查询到测试人员信息",null);
    }

//    @ApiOperation(value = "获取服务器当前时间")
//    @GetMapping(value = "/gainServerCurrentTime")
//    public Result gainServerCurrentTime() {
//        String nowDate = DateUtils.getTime();
//        return new Result(HttpStatus.SUCCESS,"获取成功",nowDate);
//    }
//
//    @ApiOperation(value = "添加检录人员开跑时间")
//    @GetMapping(value = "/addStartRunTime")
//    public Result addStartRunTime(String date) {
//        List<String> list = redisService.getCacheByPrefix(CacheConstant.RUNNING_TEST_KEY+"*");
//        for (int i = 0; i < list.size(); i++) {
//            PhysicalTrainDto physicalTrainDto = redisService.getCacheObject(list.get(i));
//            physicalTrainDto.setStartTime(date);
//            //测试跑手环绑定信息，存入redis
//            redisService.setCacheObject(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getUserId(),physicalTrainDto);
//        }
//        return new Result(HttpStatus.SUCCESS,"修改成功",null);
//    }

        @ApiOperation(value = "获取某人脸信息")
        @PostMapping(value = "/faceToken")
        public Result faceToken(String faceToken){
            String imgId = null;
            LargeScreenVo largeScreenVo = new LargeScreenVo();


            try{
                RestTemplate restTemplate = new RestTemplate();
                //get请求
                //方法一：getForEntity(String url, Class<T> responseType, Object... uriVariables),没有参数
                String url = "http://192.168.3.211/v1/MEGBOX/faces/faceToken/{faceToken}";
                ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class, faceToken);
                Result result = JSONObject.parseObject(forEntity.getBody(),Result.class);
                if (result.getCode() == 0){
                    FaceDataVo faceDataVo = JSONObject.parseObject(result.getData().toString(),FaceDataVo.class);
                    imgId = faceDataVo.getImageId();
                    EllUser ellUser = playGroundService.selSignUserByCode(imgId);
                    if (ellUser==null){
                        return new Result(HttpStatus.ERROR,"未查询到该人员信息",null);
                    }
                    EllDailyExercisePlan ellDailyExercisePlan = playGroundService.selPlanDataById(Integer.valueOf(ellUser.getOrgId()));
                    if (ellDailyExercisePlan==null){
                        return new Result(HttpStatus.ERROR,"未查询到锻炼计划",null);
                    }
                    largeScreenVo.setDailyId(ellDailyExercisePlan.getId());
                    largeScreenVo.setUserId(ellUser.getId());
                    largeScreenVo.setDeptId(Integer.valueOf(ellDailyExercisePlan.getUseOrgId()));
                }
            }catch (Exception e){
                return new Result(HttpStatus.ERROR,"查询失败",null);
            }
            return new Result(HttpStatus.SUCCESS,"查询成功",largeScreenVo);
        }
}
