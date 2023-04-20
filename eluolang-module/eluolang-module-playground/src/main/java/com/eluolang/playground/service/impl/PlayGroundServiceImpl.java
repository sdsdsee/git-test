package com.eluolang.playground.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.hardware.dto.PhysicalTrainDto;
import com.eluolang.common.core.hardware.vo.FaceDetectData;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.FileUtils;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.dto.device.WristbandConnectDto;
import com.eluolang.playground.dto.device.WristbandTimingDto;
import com.eluolang.playground.mapper.PlayGroundMapper;
import com.eluolang.playground.service.PlayGroundService;
import com.eluolang.playground.service.feign.PhysicalRemoteService;
import com.eluolang.playground.subsribe.MQTTConfig;
import com.eluolang.playground.util.RunningConfig;
import com.eluolang.playground.util.SpringUtil;
import com.eluolang.playground.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智慧操场逻辑实现层
 *
 * @author dengrunsen
 * @date 2022年08月19日 13:57
 */
@Service
@Transactional
public class PlayGroundServiceImpl implements PlayGroundService {
    @Autowired
    private PlayGroundMapper playGroundMapper;

    @Autowired
    private RedisService redisService;
    @Autowired
    private PhysicalRemoteService physicalRemoteService;

    @Autowired
    private RunningConfig runningConfig;

//    @Autowired
//    private MQTTSubsribe mqttSubsribe;

    @Autowired
    private MQTTConfig config;

//    private String project = "cp800";
//    private static int ju = 200;

    /**
     * 智慧操场redis已存入的人脸识别信息处理后存入数据库（按日常锻炼配置计算里程，未在配置时间中默认只算一半里程）
     * @param physicalTrainDto
     * @return
     */
    @Override
    @Async
    public int physicalTrainAnalysis(PhysicalTrainDto physicalTrainDto) {
        List<FaceDetectData> faceDetectDataList = new ArrayList<>();
        String startTime = DateUtils.date3TimeStamp(physicalTrainDto.getStartTime(),DateUtils.YYYY_MM_DD_HH_MM_SS);
//        String endTime = DateUtils.date2TimeStamp(physicalTrainDto.getEndTime(),DateUtils.YYYY_MM_DD_HH_MM_SS);
        List<String> list = redisService.getCacheByPrefix(CacheConstant.FACE_KEY+physicalTrainDto.getMac()+"*");
        if (list.size() > 1){
        for (int i = 0; i < list.size(); i++) {
            FaceDetectData faceDetectData = redisService.getCacheObject(list.get(i));
            if (Long.valueOf(faceDetectData.getTimestamp()) >= Long.valueOf(startTime)){
                faceDetectDataList.add(faceDetectData);
            }
        }
        if (faceDetectDataList.size() > 1){
        //升序排列
        Collections.sort(faceDetectDataList, new Comparator<FaceDetectData>() {
            public int compare(FaceDetectData s1, FaceDetectData s2) {
                return String.valueOf(s1.getTimestamp()).compareTo(String.valueOf(s2.getTimestamp()));
            }
        });
//        List<EllWristbandData> ellWristbandDatas = ellWristbandDataList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getLocationTime()))), ArrayList::new));
//        int index = Arrays.binarySearch(aa,0,aa.length-1,ellWristbandDataList.get(0).getLocation());
//        String[] a = Arrays.copyOfRange(aa,index,aa.length);
//        String[] b = Arrays.copyOfRange(aa,0,index);
//        String[] triggerPoint = FileUtils.add(a,b);
        List<EllDailyHistory> ellDailyHistoryList = new ArrayList<>();
        //本次跑步触发所有触发点的数据
        for (int i = 0; i < faceDetectDataList.size(); i++) {
            if (i+1<faceDetectDataList.size()){
            int index = FileUtils.findNum(config.getAa(),faceDetectDataList.get(i).getChannelName());
            int index1 = FileUtils.findNum(config.getAa(),faceDetectDataList.get(i+1).getChannelName());
            int num = 0;
            if(index < 0 || index1 < 0){
                return -1;
            }
            if (index > index1){
                num = config.getAa().length-index + index1;
            }
            if (index < index1){
                num = index1 - index;
            }
            if (index == index1){
                faceDetectDataList.remove(i+1);
                i--;
                continue;
            }
//            if (num > 3){
//                return -2;
//            }
            int mileage = config.getJu()*num;
            if (i == 0) {
              int count =
              playGroundMapper.selLocationDataIsExist(
                      faceDetectDataList.get(i).getImageId(),
                      String.valueOf(faceDetectDataList.get(i).getTimestamp()));
               if (count < 1) {
                   EllDailyHistory ellDailyHistory = new EllDailyHistory();
                   ellDailyHistory.setId(IdUtils.fastSimpleUUID()+ new Date().getTime());
                   ellDailyHistory.setMileage(0);
                   ellDailyHistory.setDailyId(physicalTrainDto.getDailyId());
                   ellDailyHistory.setType(1);
                   ellDailyHistory.setMac(faceDetectDataList.get(i).getImageId());
                   ellDailyHistory.setUserId(physicalTrainDto.getUserId());
                   ellDailyHistory.setLocation(faceDetectDataList.get(i).getChannelName());
                   ellDailyHistory.setLocationTime(String.valueOf(faceDetectDataList.get(i).getTimestamp()));
//                   ellDailyHistory.setHeartRate(ellWristbandDataList.get(i).getHeart());
//                   ellDailyHistory.setTemperature(ellWristbandDataList.get(i).getTemperature());
//                   ellDailyHistory.setOxygen(ellWristbandDataList.get(i).getOxygen());
//                   ellDailyHistory.setMeasureTime(ellWristbandDataList.get(i).getMeasureTime());
//                   ellDailyHistory.setStep(ellWristbandDataList.get(i).getStep());
//                   ellDailyHistory.setBattery(ellWristbandDataList.get(i).getBattery());
//                   ellDailyHistory.setSos(Integer.valueOf(ellWristbandDataList.get(i).getSos()));
//                   ellDailyHistory.setWristbandStatus(Integer.valueOf(ellWristbandDataList.get(i).getWristbandStatus()));
                   ellDailyHistoryList.add(ellDailyHistory);

               }
            }
            int count1 = playGroundMapper.selLocationDataIsExist(faceDetectDataList.get(i+1).getImageId(),String.valueOf(faceDetectDataList.get(i+1).getTimestamp()));
                if (count1 < 1){
                    EllDailyHistory ellDailyHistory = new EllDailyHistory();
                    ellDailyHistory.setId(IdUtils.fastSimpleUUID()+ new Date().getTime());
                    List<EllDailyTimeQuantum> ellDailyTimeQuantums = playGroundMapper.selDailyTimeQuantum(physicalTrainDto.getDailyId());
                    if (ellDailyTimeQuantums.size() != 0){
                        for (int j = 0; j < ellDailyTimeQuantums.size(); j++) {
                         if (DateUtils.compTime(ellDailyTimeQuantums.get(j).getStartHour(),DateUtils.getToDayTime1(DateUtils.timeStamp3Date(String.valueOf(faceDetectDataList.get(i+1).getTimestamp()),"HH:mm:ss"))) == false
                         && DateUtils.compTime(DateUtils.getToDayTime1(DateUtils.timeStamp3Date(String.valueOf(faceDetectDataList.get(i+1).getTimestamp()),"HH:mm:ss")),ellDailyTimeQuantums.get(j).getEndHour()) == false
                         ){
                             ellDailyHistory.setMileage(mileage);
                             break;
                         }
                         if (j+1 == ellDailyTimeQuantums.size()){
                             ellDailyHistory.setMileage(mileage/2);
                         }
                       }
                    }

                    ellDailyHistory.setDailyId(physicalTrainDto.getDailyId());
                    ellDailyHistory.setType(1);
                    ellDailyHistory.setMac(faceDetectDataList.get(i+1).getImageId());
                    ellDailyHistory.setUserId(physicalTrainDto.getUserId());
                    ellDailyHistory.setLocation(faceDetectDataList.get(i+1).getChannelName());
                    ellDailyHistory.setLocationTime(String.valueOf(faceDetectDataList.get(i+1).getTimestamp()));
//                    ellDailyHistory.setHeartRate(ellWristbandDataList.get(i+1).getHeart());
//                    ellDailyHistory.setTemperature(ellWristbandDataList.get(i+1).getTemperature());
//                    ellDailyHistory.setOxygen(ellWristbandDataList.get(i+1).getOxygen());
//                    ellDailyHistory.setMeasureTime(ellWristbandDataList.get(i+1).getMeasureTime());
//                    ellDailyHistory.setStep(ellWristbandDataList.get(i+1).getStep());
//                    ellDailyHistory.setBattery(ellWristbandDataList.get(i+1).getBattery());
//                    ellDailyHistory.setSos(Integer.valueOf(ellWristbandDataList.get(i+1).getSos()));
//                    ellDailyHistory.setWristbandStatus(Integer.valueOf(ellWristbandDataList.get(i+1).getWristbandStatus()));
                    ellDailyHistoryList.add(ellDailyHistory);
                    //获取跑过俩个点位所耗时间（s）
                    Integer s = DateUtils.getDatePoorMin(new Date(faceDetectDataList.get(i+1).getTimestamp()),new Date(faceDetectDataList.get(i).getTimestamp())).multiply(BigDecimal.valueOf(60)).intValue();
                    //获取redis存储的所有人员累计跑步时间，如果未获取到则新增，否则累加
                    Integer time = redisService.getCacheObject(CacheConstant.All_USER_RUNNING_TIME+":"+physicalTrainDto.getDailyId());
                    if (time == null){
                        //新增
                        redisService.setCacheObject(CacheConstant.All_USER_RUNNING_TIME+":"+physicalTrainDto.getDailyId(),s);
                    }else {
                        //累加
                        time += time + s;
                        redisService.setCacheObject(CacheConstant.All_USER_RUNNING_TIME+":"+physicalTrainDto.getDailyId(),time);
                    }
                }
        }
    }
            if (ellDailyHistoryList.size()!=0){
                 int row = playGroundMapper.addDailyHistoryList(ellDailyHistoryList);
                 System.out.println("----------------添加成功行------"+row);
            }
            return 1;
        }
        return -1;
        }
        return -1;
    }

    @Override
    public int selLocationDataIsExist(String mac, String locationTime) {
        return playGroundMapper.selLocationDataIsExist(mac, locationTime);
    }

    @Override
    public int addDailyHistoryList(List<EllDailyHistory> ellDailyHistoryList) {
        return playGroundMapper.addDailyHistoryList(ellDailyHistoryList);
    }

    @Override
    public CurrentRunningDetailsVo selCurrentRunningDetails(PhysicalTrainDto physicalTrainDto) {
        CurrentRunningDetailsVo currentRunningDetailsVo = playGroundMapper.selMileageSumByUserId(physicalTrainDto.getDailyId(),physicalTrainDto.getUserId());
        String date = DateUtils.date3TimeStamp(physicalTrainDto.getStartTime(),DateUtils.YYYY_MM_DD_HH_MM_SS);
        List<EllDailyHistoryVo> ellDailyHistoryList = playGroundMapper.selDailyHistoryByLocationTime(physicalTrainDto.getDailyId(),physicalTrainDto.getUserId(),date);
        if (ellDailyHistoryList.size() > 1) {
              // 升序排列
              Collections.sort(
                  ellDailyHistoryList,
                  new Comparator<EllDailyHistoryVo>() {
                    public int compare(EllDailyHistoryVo s1, EllDailyHistoryVo s2) {
                      return s1.getLocationTime().compareTo(s2.getLocationTime());
                    }
                  });
              Integer s = DateUtils.getDatePoorMin(new Date(Long.valueOf(ellDailyHistoryList.get(ellDailyHistoryList.size()-1).getLocationTime())),new Date(Long.valueOf(ellDailyHistoryList.get(0).getLocationTime()))).multiply(BigDecimal.valueOf(60)).intValue();
              // list集合中获取heart集合
//              List<String> heartList =
//                  ellDailyHistoryList.stream().map(i -> i.getHeartRate()).collect(Collectors.toList());
              currentRunningDetailsVo.setTime(DateUtils.getSecToMin(s));
            }
        return currentRunningDetailsVo;
    }

    @Override
    public List<EllDailyHistoryVo> selDailyHistoryByLocationTime(String dailyId,String userId, String locationTime) {
        return playGroundMapper.selDailyHistoryByLocationTime(dailyId,userId,locationTime);
    }

    @Override
    public CurrentRunningDetailsVo selMileageSumByUserId(String dailyId, String userId) {
        return playGroundMapper.selMileageSumByUserId(dailyId, userId);
    }

    @Override
    public StandardNumVo selRunningNumberCensus(PlanTotalNumDto planTotalNumDto) {
        /** 日常锻炼计划总人数 */
        Integer dailyTotalNum = playGroundMapper.selPlanTotalNum(planTotalNumDto.getDailyId());
        /** 根据计划id查询计划详情 */
        EllDailyExercisePlan ellDailyExercisePlan = playGroundMapper.selDailyPlanById(planTotalNumDto.getDailyId());
        if (ellDailyExercisePlan != null) {

          Integer month = 0;
          // 本周需要完成的里程数
          Double weeks = 0.0;
          // 获取月份
          if (ellDailyExercisePlan.getFirstHalfYear() != null) {
            month = Integer.valueOf(planTotalNumDto.getStartTime().substring(5, 7));
          }
          // 日常锻炼计划上半年划分的月份
          String[] firstHalfYear = ellDailyExercisePlan.getFirstHalfYear().split("-");
          // 日常锻炼计划下半年划分的月份
          String[] secondHalfYear = ellDailyExercisePlan.getSecondHalfYear().split("-");
          // 判断当前开始时间处于上半年还是下半年，以此写入本周需要完成的里程数
          if (Integer.valueOf(firstHalfYear[0]) >= month
              || Integer.valueOf(firstHalfYear[1]) <= month) {
            weeks = ellDailyExercisePlan.getFirstWeeks();
          } else if (Integer.valueOf(secondHalfYear[0]) >= month
              || Integer.valueOf(secondHalfYear[1]) <= month) {
            weeks = ellDailyExercisePlan.getSecondWeeks();
          }
          planTotalNumDto.setMileage(String.valueOf(weeks));
          planTotalNumDto.setStartTime(
              DateUtils.date2TimeStamp(planTotalNumDto.getStartTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
          planTotalNumDto.setEndTime(
              DateUtils.date2TimeStamp(planTotalNumDto.getEndTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
          StandardNumVo standardNumVo = playGroundMapper.selStandardNum(planTotalNumDto);
          standardNumVo.setTotalNum(dailyTotalNum);
          return standardNumVo;
        }
        return null;
    }

    @Override
    public List<EllDeviceInfoVo> findDeviceInfo(String deviceId) {
        return playGroundMapper.findDeviceInfo(deviceId);
    }

    @Override
    public EllDailyExercisePlan selExercisePlan(String deviceId) {
        return playGroundMapper.selExercisePlan(deviceId);
    }

    @Override
    public List<RunningDataVo> selRunningData(List<Integer> deptIdList) {
        return playGroundMapper.selRunningData(deptIdList);
    }

    @Override
    public Integer selPlanTotalNum(String dailyId) {
        return playGroundMapper.selPlanTotalNum(dailyId);
    }

    @Override
    public EllDailyExercisePlan selDailyPlanById(String id) {
        return playGroundMapper.selDailyPlanById(id);
    }

    @Override
    public List<EllDailyTimeQuantum> selDailyTimeQuantum(String dailyId) {
        return playGroundMapper.selDailyTimeQuantum(dailyId);
    }

    @Override
    public TotalSportNumberVo selTotalSportNumber(String date,String dailyId) {
        return playGroundMapper.selTotalSportNumber(date,dailyId);
    }

    @Override
    public MonthRunDataVo selRankByUserId(MonthRunDataDto monthRunDataDto) {
        return playGroundMapper.selRankByUserId(monthRunDataDto);
    }

    @Override
    public Integer selMileageStandard(MonthRunDataDto monthRunDataDto) {
        return playGroundMapper.selMileageStandard(monthRunDataDto);
    }

    @Override
    public List<EllDailyHistoryVo> selHeartRateInterval(String dailyId, String userId, String day) {
        return playGroundMapper.selHeartRateInterval(dailyId, userId, day);
    }

    @Override
    public CurrentUserRunDataVo selCurrentUserRunData(RunningDataDto runningDataDto) {
        return playGroundMapper.selCurrentUserRunData(runningDataDto);
    }

    @Override
    public GetLocationTimeVo selLocationTime(RunningDataDto runningDataDto) {
        return playGroundMapper.selLocationTime(runningDataDto);
    }

    @Override
    public List<EllDailyHistoryVo> selCurrentRunData(RunningDataDto runningDataDto) {
        return playGroundMapper.selCurrentRunData(runningDataDto);
    }

    @Override
    public RunningRankVo selUserRunRank(RunningRankDto runningRankDto) {
        return playGroundMapper.selUserRunRank(runningRankDto);
    }

    @Override
    public RunningRankVo selUserRunRankBySchool(RunningRankDto runningRankDto) {
        return playGroundMapper.selUserRunRankBySchool(runningRankDto);
    }

//    @Override
//    @Async
//    public int longRacePhysicalAdd(PhysicalTrainDto physicalTrainDto) throws IOException {
//        List<EllWristbandData> ellWristbandDataList = new ArrayList<>();
//        if (physicalTrainDto.getStartTime() != null){
//        String startTime = DateUtils.date3TimeStamp(physicalTrainDto.getStartTime(),DateUtils.YYYY_MM_DD_HH_MM_SS);
//        //获取redis缓存的人员跑步信息
//        UserCallVo userCallVo = redisService.getCacheObject(CacheConstant.RUNNING_CALL_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId());
//        List<String> list = redisService.getCacheByPrefix(CacheConstant.WRISTBAND_KEY+physicalTrainDto.getMac()+"*");
//        if (list.size() > 1){
//        for (int i = 0; i < list.size(); i++) {
//            EllWristbandData ellWristbandData = redisService.getCacheObject(list.get(i));
//            if (Long.valueOf(ellWristbandData.getLocationTime()) >= Long.valueOf(startTime)){
//                ellWristbandDataList.add(ellWristbandData);
//            }
//        }
//        if (ellWristbandDataList.size() > 1){
//        //升序排列
//        Collections.sort(ellWristbandDataList, new Comparator<EllWristbandData>() {
//            public int compare(EllWristbandData s1, EllWristbandData s2) {
//                return s1.getLocationTime().compareTo(s2.getLocationTime());
//            }
//        });
//            //累计里程
//            int mileage = 0;
//            //本次跑步触发所有触发点的数据
//            for (int i = 0; i < ellWristbandDataList.size(); i++) {
//                if (i+1<ellWristbandDataList.size()){
//                    int index = FileUtils.findNum(config.getAa(),ellWristbandDataList.get(i).getLocation());
//                    int index1 = FileUtils.findNum(config.getAa(),ellWristbandDataList.get(i+1).getLocation());
//                    int num = 0;
//                    if(index < 0 || index1 < 0){
//                        return -1;
//                    }
//                    if (index > index1){
//                        num = config.getAa().length-index + index1;
//                    }
//                    if (index < index1){
//                        num = index1 - index;
//                    }
//                    if (index == index1){
//                        num = 2;
//                    }
////            if (num > 3){
////                return -2;
////            }
//                    mileage += config.getJu()*num;
//                    }
//            }
//         //如果未完成测试则返回最新的心率，如果完成则返回平均心率
//        String heart = null;
//            if (ellWristbandDataList.size() != 0){
//                heart = ellWristbandDataList.get(ellWristbandDataList.size()-1).getHeart();
//            }
//        //本次跑步测试完成耗时
//        Integer s = 0;
//        if (physicalTrainDto.getProName().equals("cp800")){
//            List<EllWristbandData> list1 = ellWristbandDataList.stream().filter(EllWristbandData -> EllWristbandData.getLocation().equals(runningConfig.getCp800Location())).collect(Collectors.toList());
//            if (list1.size() == runningConfig.getCp800Num()){
////                String maxLocationTime = list1.stream().mapToInt(Comparator.comparing(EllWristbandData::getLocationTime) ).average().orElse(0d);
//                //测试完成平均心率
//                double heartAvg = ellWristbandDataList.stream().mapToInt(EllWristbandData -> Integer.parseInt(EllWristbandData.getHeart())).average().getAsDouble();
//                heart = String.valueOf((int) heartAvg);
//                String maxLocationTime = list1.stream().max(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                String minLocationTime = list1.stream().min(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                s = DateUtils.getDatePoorMin(new Date(Long.valueOf(maxLocationTime)),new Date(Long.valueOf(minLocationTime))).multiply(BigDecimal.valueOf(60)).intValue();
//                userCallVo.setTime(DateUtils.getSecToMin(s));
//                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(s));
//                List<String> mac = new ArrayList<>();
//                mac.add(physicalTrainDto.getMac());
//                removeIssued(mac);
//                redisService.deleteObject(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId());
//            }
//        }
//        if (physicalTrainDto.getProName().equals("cp1000")){
//            List<EllWristbandData> startList = ellWristbandDataList.stream().filter(EllWristbandData -> EllWristbandData.getLocation().equals(runningConfig.getCp1000StartLocation())).collect(Collectors.toList());
//            List<EllWristbandData> endList = ellWristbandDataList.stream().filter(EllWristbandData -> EllWristbandData.getLocation().equals(runningConfig.getCp1000EndLocation())).collect(Collectors.toList());
//            if (endList.size() == runningConfig.getCp1000EndNum()){
//                double heartAvg = ellWristbandDataList.stream().mapToInt(EllWristbandData -> Integer.parseInt(EllWristbandData.getHeart())).average().getAsDouble();
//                heart = String.valueOf((int) heartAvg);
//                String maxLocationTime = endList.stream().max(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                String minLocationTime = startList.stream().min(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                s = DateUtils.getDatePoorMin(new Date(Long.valueOf(maxLocationTime)),new Date(Long.valueOf(minLocationTime))).multiply(BigDecimal.valueOf(60)).intValue();
//                userCallVo.setTime(DateUtils.getSecToMin(s));
//                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(s));
//                List<String> mac = new ArrayList<>();
//                mac.add(physicalTrainDto.getMac());
//                removeIssued(mac);
//                redisService.deleteObject(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId());
//            }
//        }
//        if (physicalTrainDto.getProName().equals("cp2000")){
//            List<EllWristbandData> list1 = ellWristbandDataList.stream().filter(EllWristbandData -> EllWristbandData.getLocation().equals(runningConfig.getCp2000Location())).collect(Collectors.toList());
//            if (list1.size() == runningConfig.getCp2000Num()){
//                double heartAvg = ellWristbandDataList.stream().mapToInt(EllWristbandData -> Integer.parseInt(EllWristbandData.getHeart())).average().getAsDouble();
//                heart = String.valueOf((int) heartAvg);
//                String maxLocationTime = list1.stream().max(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                String minLocationTime = list1.stream().min(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                s = DateUtils.getDatePoorMin(new Date(Long.valueOf(maxLocationTime)),new Date(Long.valueOf(minLocationTime))).multiply(BigDecimal.valueOf(60)).intValue();
//                userCallVo.setTime(DateUtils.getSecToMin(s));
//                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(s));
//                List<String> mac = new ArrayList<>();
//                mac.add(physicalTrainDto.getMac());
//                removeIssued(mac);
//                redisService.deleteObject(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId());
//            }
//        }
//        if (physicalTrainDto.getProName().equals("cp3000")){
//            List<EllWristbandData> startList = ellWristbandDataList.stream().filter(EllWristbandData -> EllWristbandData.getLocation().equals(runningConfig.getCp3000StartLocation())).collect(Collectors.toList());
//            List<EllWristbandData> endList = ellWristbandDataList.stream().filter(EllWristbandData -> EllWristbandData.getLocation().equals(runningConfig.getCp3000EndLocation())).collect(Collectors.toList());
//            if (endList.size() == runningConfig.getCp3000EndNum()){
//                double heartAvg = ellWristbandDataList.stream().mapToInt(EllWristbandData -> Integer.parseInt(EllWristbandData.getHeart())).average().getAsDouble();
//                heart = String.valueOf((int) heartAvg);
//                String maxLocationTime = endList.stream().max(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                String minLocationTime = startList.stream().min(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                s = DateUtils.getDatePoorMin(new Date(Long.valueOf(maxLocationTime)),new Date(Long.valueOf(minLocationTime))).multiply(BigDecimal.valueOf(60)).intValue();
//                userCallVo.setTime(DateUtils.getSecToMin(s));
//                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(s));
//                List<String> mac = new ArrayList<>();
//                mac.add(physicalTrainDto.getMac());
//                removeIssued(mac);
//                redisService.deleteObject(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId());
//            }
//        }
//        if (physicalTrainDto.getProName().equals("cp5000")){
//            List<EllWristbandData> startList = ellWristbandDataList.stream().filter(EllWristbandData -> EllWristbandData.getLocation().equals(runningConfig.getCp5000StartLocation())).collect(Collectors.toList());
//            List<EllWristbandData> endList = ellWristbandDataList.stream().filter(EllWristbandData -> EllWristbandData.getLocation().equals(runningConfig.getCp5000EndLocation())).collect(Collectors.toList());
//            if (endList.size() == runningConfig.getCp5000EndNum()){
//                String maxLocationTime = endList.stream().max(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                String minLocationTime = startList.stream().min(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
//                s = DateUtils.getDatePoorMin(new Date(Long.valueOf(maxLocationTime)),new Date(Long.valueOf(minLocationTime))).multiply(BigDecimal.valueOf(60)).intValue();
//                userCallVo.setTime(DateUtils.getSecToMin(s));
//                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(s));
//                List<String> mac = new ArrayList<>();
//                mac.add(physicalTrainDto.getMac());
//                removeIssued(mac);
//                redisService.deleteObject(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId());
//            }
//        }
//            userCallVo.setMileage(String.valueOf(mileage));
//            userCallVo.setHeart(heart);
//            //将最新的人员跑步信息写入redis
//            redisService.setCacheObject(CacheConstant.RUNNING_CALL_KEY+physicalTrainDto.getDailyId()+":"+physicalTrainDto.getUserId(),userCallVo);
//           return 1;
//        }
//        return -1;
//        }
//        return -1;
//        }
//        return -1;
//    }

    @Override
    public int longRaceResult(PhysicalTrainDto physicalTrainDto) throws IOException {
        List<EllWristbandData> ellWristbandDataList = new ArrayList<>();
        if (physicalTrainDto.getStartTime() != null){
        String startTime = DateUtils.date3TimeStamp(physicalTrainDto.getStartTime(),DateUtils.YYYY_MM_DD_HH_MM_SS);
        List<String> list = redisService.getCacheByPrefix(CacheConstant.WRISTBAND_KEY+physicalTrainDto.getMac()+"*");
        if (list.size() > 1){
            for (int i = 0; i < list.size(); i++) {
                EllWristbandData ellWristbandData = redisService.getCacheObject(list.get(i));
                if (Long.valueOf(ellWristbandData.getLocationTime()) >= Long.valueOf(startTime)){
                    ellWristbandDataList.add(ellWristbandData);
                }
            }
            if (ellWristbandDataList.size() > 1){
            //升序排列
            Collections.sort(ellWristbandDataList, new Comparator<EllWristbandData>() {
                public int compare(EllWristbandData s1, EllWristbandData s2) {
                    return s1.getLocationTime().compareTo(s2.getLocationTime());
                }
            });
            //累计里程
            int mileage = 0;
            //本次跑步触发所有触发点的数据
            for (int i = 0; i < ellWristbandDataList.size(); i++) {
                if (i+1<ellWristbandDataList.size()){
                    int index = FileUtils.findNum(config.getAa(),ellWristbandDataList.get(i).getLocation());
                    int index1 = FileUtils.findNum(config.getAa(),ellWristbandDataList.get(i+1).getLocation());
                    int num = 0;
                    if(index < 0 || index1 < 0){
                        return -1;
                    }
                    if (index > index1){
                        num = config.getAa().length-index + index1;
                    }
                    if (index < index1){
                        num = index1 - index;
                    }
                    if (index == index1){
                        num = 2;
                    }
//            if (num > 3){
//                return -2;
//            }
                    mileage += config.getJu()*num;
                }
            }

            //本次跑步测试完成耗时
            String maxLocationTime = ellWristbandDataList.stream().max(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
            String minLocationTime = ellWristbandDataList.stream().min(Comparator.comparing(EllWristbandData::getLocationTime)).get().getLocationTime();
            Integer s = DateUtils.getDatePoorMin(new Date(Long.valueOf(maxLocationTime)),new Date(Long.valueOf(minLocationTime))).multiply(BigDecimal.valueOf(60)).intValue();
            if (physicalTrainDto.getProName().equals("cp800")){
                    Double time = s/((double)mileage/1000)*0.8;
                    addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(time.intValue()));
            }
            if (physicalTrainDto.getProName().equals("cp1000")){
                Double time = s/((double)mileage/1000);
                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(time.intValue()));
            }
            if (physicalTrainDto.getProName().equals("cp2000")){
                Double time = s/((double)mileage/1000)*2;
                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(time.intValue()));
            }
            if (physicalTrainDto.getProName().equals("cp3000")){
                Double time = s/((double)mileage/1000)*3;
                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(time.intValue()));
            }
            if (physicalTrainDto.getProName().equals("cp5000")){
                Double time = s/((double)mileage/1000)*5;
                addScore(physicalTrainDto.getDailyId(),physicalTrainDto.getProName(),physicalTrainDto.getUserId(),String.valueOf(time.intValue()));
            }
            return 1;
          }
            return -1;
        }
        return -1;
        }
//        List<String> mac = new ArrayList<>();
//        mac.add(physicalTrainDto.getMac());
//        removeIssued(mac);
//        redisService.deleteObject(CacheConstant.RUNNING_TEST_KEY+physicalTrainDto.getUserId());
        return -1;
    }

    @Override
    public EllUser selUserById(String userId) {
        return playGroundMapper.selUserById(userId);
    }

    @Override
    public EllTestProject selTestProject(String name) {
        return playGroundMapper.selTestProject(name);
    }

    @Override
    public List<EllDailyHistoryVo> runSituationAnalyse(String dailyId,String userId,String date) {
        return playGroundMapper.runSituationAnalyse(dailyId, userId, date);
    }

//    @Override
//    public void issueInstruction(List<String> list) {
//        WristbandConnectDto wristbandConnectDto = new WristbandConnectDto();
//        //0为断开连接，1为连接
//        wristbandConnectDto.setCmd("1");
//        wristbandConnectDto.setBleNum("3");
//        wristbandConnectDto.setMacNum(String.valueOf(list.size()));
//        wristbandConnectDto.setMac(list);
//
//        WristbandTimingDto wristbandTimingDto = new WristbandTimingDto();
//        wristbandTimingDto.setType("6");
//        wristbandTimingDto.setTime(String.valueOf(DateUtils.getCurrentTime()));
//
//        Map<String, String> map = config.getDefault_topic();
//        //将手环连接指令和校时指令下发到mqtt服务器
//        for (String key:map.keySet()) {
//            String value = map.get(key);
//            String topic = "/cmd/"+value+"/wristband/bind";
//            String topic2 = "/cmd/"+value+"/wristband/message";
//            mqttSubsribe.sendMQTTMessage(topic, JSONObject.toJSONString(wristbandConnectDto), 0);
//            mqttSubsribe.sendMQTTMessage(topic2, JSONObject.toJSONString(wristbandTimingDto), 0);
//        }
//    }

//    @Override
//    public void removeIssued(List<String> list) {
//        WristbandConnectDto wristbandConnectDto = new WristbandConnectDto();
//        //0为断开连接，1为连接
//        wristbandConnectDto.setCmd("0");
//        wristbandConnectDto.setBleNum("3");
//        wristbandConnectDto.setMacNum(String.valueOf(list.size()));
//        wristbandConnectDto.setMac(list);
//        Map<String, String> map = config.getDefault_topic();
//        for (String key:map.keySet()) {
//            String value = map.get(key);
//            String topic = "/cmd/"+value+"/wristband/bind";
//            mqttSubsribe.sendMQTTMessage(topic, JSONObject.toJSONString(wristbandConnectDto), 0);
//        }
//    }

    @Override
    public UserCallVo selUserMessageById(String userId) {
        return playGroundMapper.selUserMessageById(userId);
    }

    @Override
    public String selDailyIdByOrgId(Integer orgId) {
        return playGroundMapper.selDailyIdByOrgId(orgId);
    }

    @Override
    public EllUser selSignUserByCode(String imgId) {
        return playGroundMapper.selSignUserByCode(imgId);
    }

    @Override
    public EllDailyExercisePlan selPlanDataById(Integer deptId) {
        return playGroundMapper.selPlanDataById(deptId);
    }

    public int addScore(String planId,String proName,String userId,String data) throws IOException {
        EllUser ellUser = playGroundMapper.selUserById(userId);
        EllTestProject ellTestProject = playGroundMapper.selTestProject(proName);
        EllHistoryVo ellHistoryVo = new EllHistoryVo();
        ellHistoryVo.setUserId(userId);
        ellHistoryVo.setPlanId(planId);
        ellHistoryVo.setUserSex(String.valueOf(ellUser.getUserSex()));
        ellHistoryVo.setUserOrgId(ellUser.getOrgId());
        ellHistoryVo.setTestProject(String.valueOf(ellTestProject.getId()));
        ellHistoryVo.setProjectName(ellTestProject.getProName());
        ellHistoryVo.setCheckInTimestamp(new Date().getTime());
        ellHistoryVo.setEndTimestamp(new Date().getTime());
        ellHistoryVo.setEndTime(String.valueOf(new Date().getTime()));
        Map<String, List<EllTestData>> map = new HashMap<>();
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData = new EllTestData();
        ellTestData.setRemark(ellTestProject.getProName());
        ellTestData.setIsRetest("1");
        ellTestData.setData(data);
        ellTestData.setScore("0");
        ellTestData.setUnit(ellTestProject.getProUnit());
        ellTestData.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData);
        map.put("1",ellTestDataList);
        ellHistoryVo.setTestData(map);
        Result result = physicalRemoteService.submitScores(ellHistoryVo);
        if (result.getCode() != HttpStatus.SUCCESS){
            return -1;
        }
        return  1;
    }

//    @Scheduled(cron = "0/5 * * * * *")
//    public void cron() throws IOException {
//        RedisService redis = (RedisService) SpringUtil.getBean(RedisService.class);
//        PlayGroundService playGround = (PlayGroundService)SpringUtil.getBean(PlayGroundService.class);
//        List<String> list = redis.getCacheByPrefix(CacheConstant.RUNNING_TEST_KEY+"*");
//    System.out.println("每五秒执行................");
//        for (int i = 0; i < list.size(); i++) {
//            PhysicalTrainDto physicalTrainDto = redis.getCacheObject(list.get(i));
//            playGround.longRacePhysicalAdd(physicalTrainDto);
//        }
//    }

    @Scheduled(cron = "0/10 * * * * *")
    public void freeRunning() {
        RedisService redis = (RedisService) SpringUtil.getBean(RedisService.class);
        PlayGroundService playGround = (PlayGroundService)SpringUtil.getBean(PlayGroundService.class);
        List<String> list = redis.getCacheByPrefix(CacheConstant.RUNNING_FREE_KEY+"*");
//        System.out.println("每十秒执行................");
        for (int i = 0; i < list.size(); i++) {
            PhysicalTrainDto physicalTrainDto = redis.getCacheObject(list.get(i));
            playGround.physicalTrainAnalysis(physicalTrainDto);
        }
    }
}
