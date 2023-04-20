package com.eluolang.playground.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.hardware.dto.EllDeviceInfoStateDto;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.service.WisdomClassService;
import com.eluolang.playground.service.feign.TcpServerRemoteService;
import com.eluolang.playground.util.CreateTime;
import com.eluolang.playground.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RestController
@Api(tags = "智慧课程")
@RequestMapping("/classroom")
public class WisdomClassController {
    @Autowired
    private WisdomClassService wisdomClassService;
    //#成绩越大越好的项目
    //maxScorePro: 36,37,38,39,42,44,46,48,49,51,59,60,61,65,80,81,90
    @Value("${maxScorePro}")
    private static String maxScorePro;
    //#成绩越小越好的项目
    //minScorePro: 40,41,45,47,50,52,53,54,55,56,62,63,64,66,75,76,78,79,82,83,84,85,89
    @Value("${minScorePro}")
    private static String minScorePro;
    //#总数最大越好的
    //sumScorePro:
    @Value("${sumScorePro}")
    private static String sumScorePro;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TcpServerRemoteService tcpServerRemoteService;

    @ApiOperation("添加成绩")
    @PostMapping("/submitScores")
    public Result addHistory(@RequestBody List<EllClassHistoryVo> historys) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.addHisClass(historys));
    }

    @ApiOperation("查询成绩(平板)")
    @PostMapping("/getScores")
    public Result getUserHistory(@RequestBody EllSelClassHistory history) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.selHisClass(history));
    }

    @ApiOperation("上传视频")
    @PostMapping("/uploadTestingVideo")
    public Result addVideo(EllClassVideoVo classVideo, @RequestParam("videoFile") MultipartFile video) throws IOException {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.addVideo(classVideo, video));
    }

    @ApiOperation("查询视频")
    @PostMapping("/getVideo")
    public Result selVideo(@RequestBody EllClassVideo classVideo) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.selVideo(classVideo));
    }

    @ApiOperation("添加课程时间")
    @PostMapping("/addClassTime")
    public Result addClassTime(@RequestBody List<EllSmartClassTime> classTimes) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.addClassTime(classTimes));
    }

    @ApiOperation("查询课程时间")
    @GetMapping("/getClassTime")
    public Result getClassTime(int orgId) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.selCourseTimeByClassId(orgId));
    }


    @ApiOperation("添加课程")
    @PostMapping("/addCourseSchedule")
    public Result addClassSchedule(@RequestBody EllSmartClassScheduleVo classSchedule) throws Exception {
        int i = wisdomClassService.addClassSchedule(classSchedule);
        if (i == 0) {
            return new Result(HttpStatus.DATE_NOT_FOUNT, "此时间段没有此星期，请重新选择");
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("查询课程表")
    @GetMapping("/test/coursesTimeTable")
    public Result getClassSchedule(int teacherId, int weekNum) throws Exception {
        String[] weekDay = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        //先从上往下查询
        List<PfDepart> departList = wisdomClassService.selSchools(teacherId, true);
        //没有学校就从下往上查询
        if (departList == null || departList.size() <= 0) {
            departList = wisdomClassService.selSchools(teacherId, false);
        }
        if (departList == null || departList.size() <= 0) {
            return new Result(HttpStatus.ERROR, "账号没有绑定部门");
        }
        List<EllSmartClassTimeDto> ellSmartClassTimeDtos = wisdomClassService.selCourseTimeByCourseId(departList.get(0).getId());
        if (ellSmartClassTimeDtos == null || ellSmartClassTimeDtos.size() <= 0) {
            return new Result(HttpStatus.ERROR, "学校还没有设置课程时间");
        }
        //获取这一周的起止日期
        Long[] weekTimeStamp = CreateTime.getCurrentWeekTimeFrame(CreateTime.weekMonday(weekNum));
        List<String> day = CreateTime.findEveryDay(CreateTime.TimestampTransitionDate(String.valueOf(weekTimeStamp[0])), CreateTime.TimestampTransitionDate(String.valueOf(weekTimeStamp[1])));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("courseIndex", ellSmartClassTimeDtos);
        for (int i = 0; i < 7; i++) {
            Map<String, Object> map = new HashMap<>();
            List<EllSmartClassScheduleDto> classScheduleDtoList = wisdomClassService.selSmartClassWeek(weekTimeStamp[0], weekTimeStamp[1], teacherId, i + 1, null);
            if (classScheduleDtoList == null || classScheduleDtoList.size() <= 0) {
                classScheduleDtoList = new ArrayList<>();
            }
            map.put("data", day.get(i));
            map.put("courses", classScheduleDtoList);
            jsonObject.put(weekDay[i], map);
        }
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/delCourseById")
    public Result delCourseById(String courseId) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(courseId);
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.delCourseById(stringList));
    }

    @ApiOperation("修改课程单课程")
    @PostMapping("/updateCourseSingle")
    public Result updateCourseSingle(@RequestBody EllSmartClassScheduleUpDataVo classScheduleVo) {
        //查询课程时间
        EllSmartClassTime ellSmartClassTime = wisdomClassService.selClassTimeById(classScheduleVo.getClassTimeId());
        EllSmartClassSchedule classSchedule = new EllSmartClassSchedule();
        classSchedule.setId(classScheduleVo.getId());
        classSchedule.setClassName(classScheduleVo.getClassName());
        classSchedule.setTeacherId(classScheduleVo.getTeacherId());
        classSchedule.setClassTimeId(classScheduleVo.getClassTimeId());
        //将日期和时间拼接
        classSchedule.setConcreteStart(classScheduleVo.getConcreteStartDate() + " " + ellSmartClassTime.getTimeStart());
        classSchedule.setConcreteEnd(classScheduleVo.getConcreteStartDate() + " " + ellSmartClassTime.getTimeEnd());
        //将具体时间转为时间戳
        classSchedule.setConcreteStampEnd(CreateTime.dateTransitionTimestamp(classSchedule.getConcreteStart()));
        classSchedule.setConcreteStampStart(CreateTime.dateTransitionTimestamp(classSchedule.getConcreteEnd()));
        classSchedule.setCreateBy(classScheduleVo.getTeacherId());
        classSchedule.setClassId(classScheduleVo.getClassId());
        classSchedule.setSchoolId(classScheduleVo.getSchoolId());
        classSchedule.setCreateTime(CreateTime.getTime());
        classSchedule.setUpdateTime(CreateTime.getTime());
        classSchedule.setDateTime(classScheduleVo.getConcreteStartDate());
        classSchedule.setLocation(classScheduleVo.getLocation());
        classSchedule.setPhone(classScheduleVo.getPhone());
        classSchedule.setWeek(classSchedule.getWeek());
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.updateCourseById(classSchedule));
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/delCourseByIdBatch")
    public Result delCourseByIdBatch(String[] courseIds) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.delCourseById(Arrays.asList(courseIds)));
    }

    @ApiOperation("查询课程")
    @GetMapping("/getCourse")
    //type 0是所有 1是正在进行 2已结束 3是未开始
    public Result getCourse(int accountId, int type, int page, String courseName) throws Exception {
        List<EllSmartClassScheduleDto> classScheduleDtoList = null;
        PageHelper.startPage(page, 10);
        switch (type) {
            case 0:
                classScheduleDtoList = wisdomClassService.selSmartClassWeek(null, null, accountId, null, courseName);
                break;
            case 1:
                classScheduleDtoList = wisdomClassService.selSmartClassIng(CreateTime.getHourTransitionTimestamp(), accountId, courseName);
                break;
            case 2:
                classScheduleDtoList = wisdomClassService.selSmartClassWeek(null, new Date().getTime(), accountId, null, courseName);
                break;
            case 3:
                classScheduleDtoList = wisdomClassService.selSmartClassWeek(new Date().getTime(), null, accountId, null, courseName);
                break;
        }
        PageInfo info = new PageInfo<>(classScheduleDtoList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coursewares", classScheduleDtoList);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("查询学校")
    @GetMapping("/getSchool")
    public Result getSchool(int accountId) {
        //先从上往下查询
        List<PfDepart> departList = wisdomClassService.selSchools(accountId, true);
        //没有学校就从下往上查询
        if (departList == null || departList.size() <= 0) {
            departList = wisdomClassService.selSchools(accountId, false);
        }
        return new Result(HttpStatus.SUCCESS, "成功", departList);
    }

    @ApiOperation("查询老师")
    @GetMapping("/getSchoolTeacher")
    public Result getSchoolTeacher(int accountId, int schoolId) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.selTeacher(schoolId, accountId));
    }

    @ApiOperation("添加课件")
    @PostMapping("/addCourseware")
    public Result addCourseware(EllCourseware ellCourseware, @RequestParam("file") MultipartFile file) throws ParseException, IOException {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.addCourseware(file, ellCourseware));
    }

    @ApiOperation("删除课件")
    @DeleteMapping("/delCourseware")
    public Result delCourseware(String coursewareId) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(coursewareId);
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.delCourseware(stringList));
    }

    @ApiOperation("批量删除课件")
    @DeleteMapping("/delCoursewareBatch")
    public Result delCoursewareBatch(String[] coursewareIds) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.delCourseware(Arrays.asList(coursewareIds)));
    }

    @ApiOperation("查询课件")
    @PostMapping("/test/coursewares")
    public Result getCoursewares(String coursewareName, int teacherId, int page) {
        PageHelper.startPage(page, 10);
        List<EllCourseware> coursewares = wisdomClassService.selCourse(coursewareName, teacherId);
        PageInfo info = new PageInfo<>(coursewares);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coursewares", coursewares);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("查询班级成绩")
    @GetMapping("/getClassGrade")
    public Result getClassGrade(Integer classId, String courseId) {
        //每个项目的成绩
        List<EllSmartGradeDto> dtoList = wisdomClassService.selGradeAvgAndMax(classId, courseId);
        //课堂记录文件
        String url = null;
        //考勤人数
        int num = 0;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("proGrade", dtoList);
        jsonObject.put("courseRecord", url);
        jsonObject.put("checkAttendance", num);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("通过课程id查询学生")
    @GetMapping("/getUsersByCourseId")
    public Result getUsersByCourseId(String courseId, int page, String userName) {
        PageHelper.startPage(page, 10);
        List<EllGetUserByCourseIdDto> dtoList = wisdomClassService.selUserByCourseId(courseId, userName);
        PageInfo info = new PageInfo<>(dtoList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userList", dtoList);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("查询个人成绩")
    @GetMapping("/getUserGrade")
    public Result getUserGrade(String userId, String courseId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInfo", wisdomClassService.selUserAndCourse(courseId, userId));
        jsonObject.put("dataGrade", wisdomClassService.selUserGrade(userId, courseId));
        jsonObject.put("video", wisdomClassService.selVideoByUserId(userId, null, courseId));
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }


    @ApiOperation("平板拉取体测体考计划")
    @GetMapping("/test/plansWithOverview")
    public Result plansWithOverview(Integer page, Integer isExam, Integer teacherId, Integer status) {
        PageHelper.startPage(page, 10);
        List<EllFlatPlatePlanDto> planDtos = wisdomClassService.selPlanFlatPlate(isExam, teacherId, status);
        PageInfo info = new PageInfo<>(planDtos);
        List<EllPlanStatusNumVo> statusNum = wisdomClassService.selPlanStatusNum(isExam, teacherId, null);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("plans", planDtos);
        Map<String, Integer> map = new HashMap<>();
        map.put("created", statusNum.get(0).getNum());
        map.put("started", statusNum.get(1).getNum());
        map.put("finished", statusNum.get(2).getNum());
        jsonObject.put("overview", map);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("大屏排名查询")
    @GetMapping("/test/rankOfPerProject")
    public Result rankOfPerProject(String deviceId) {
        EllDevice ellDevice = wisdomClassService.selDevice(deviceId);
        PageHelper.startPage(0, 6);
        //#成绩越大越好的项目
        String[] sumPro = sumScorePro.split(",");
        //#成绩越小越好的项目
        String[] minPro = minScorePro.split(",");
        //#总数越大越好的项目
        String[] maxPro = maxScorePro.split(",");
        Map<Integer, String[]> map = new HashMap<>();
        //key为查询sql时候的状态
        map.put(1, minPro);
        map.put(2, maxPro);
        map.put(3, sumPro);
        //排名
        List<EllProRankDto> list = new ArrayList<>();
        for (int key = 1; key < map.size(); key++) {
//            if (map.get(key) != null && map.get(key).length > 0) {
            for (int i = 0; i < map.get(key).length; i++) {
                List<EllProScoreDto> dtoList = wisdomClassService.selProScore(ellDevice.getDeptId(), map.get(key)[i], key);
                //有成绩的才返回
                if (dtoList.size() > 0) {
                    EllProRankDto proRankDto = new EllProRankDto();
                    proRankDto.setProName(dtoList.get(0).getProName());
                    proRankDto.setUnit(dtoList.get(0).getUnit());
                    //学生
                    List<EllProRankUserDto> userDtos = new ArrayList<>();
                    for (int j = 0; j < dtoList.size(); j++) {
                        EllProRankUserDto ellProRankUserDto = new EllProRankUserDto();
                        ellProRankUserDto.setRank(j + 1);
                        ellProRankUserDto.setUserName(dtoList.get(j).getUserName());
                        ellProRankUserDto.setUserSex(dtoList.get(i).getUserSex());
                        switch (key) {
                            case 1:
                                //越小越好
                                ellProRankUserDto.setScore(dtoList.get(j).getMinScore());
                                break;
                            case 2:
                                //越大越好
                                ellProRankUserDto.setScore(dtoList.get(j).getMaxScore());
                                break;
                            case 3:
                                ellProRankUserDto.setScore(dtoList.get(i).getSumScore());
                                break;
                        }
                        userDtos.add(ellProRankUserDto);
                        proRankDto.setUsers(userDtos);
                    }
                    list.add(proRankDto);
                }
                //保存到redis
                redisTemplate.opsForList().rightPush("rank:individual:" + map.get(key)[i], dtoList);
            }
//            }
        }
        return new Result(HttpStatus.SUCCESS, "成功", list);
    }

    @ApiOperation("大屏每周测试人数")
    @GetMapping("/test/testedNumberOfTheWeek")
    public Result testedNumberOfTheWeek(String deviceId) throws Exception {
        //获取本周的起始时间
        Long[] weekStartAndEnd = CreateTime.getCurrentWeekTimeFrame(CreateTime.timeDay());
        List<String> weekDays = CreateTime.findEveryDay(CreateTime.TimestampTransitionDate(String.valueOf(weekStartAndEnd[0])), CreateTime.TimestampTransitionDate(String.valueOf(weekStartAndEnd[1])));
        EllDevice ellDevice = wisdomClassService.selDevice(deviceId);
        List<Map> mapList = new ArrayList<>();
        for (int i = 0; i < weekDays.size(); i++) {
            Map<String, Object> integerMap = new HashMap<>();
            integerMap.put("date", weekDays.get(i));
            integerMap.put("count", wisdomClassService.selTestPeoNum(weekDays.get(i), ellDevice.getDeptId()));
            mapList.add(integerMap);
        }
        return new Result(HttpStatus.SUCCESS, "成功", mapList);
    }

    @ApiOperation(" 大屏(各个项目)累计测试人数（月）")
    @GetMapping("/test/testedNumberOfPerProject")
    public Result testedNumberOfPerProject(String deviceId) throws ParseException {
        EllDevice ellDevice = wisdomClassService.selDevice(deviceId);
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.selProTestNum(CreateTime.timeDay(), ellDevice.getDeptId()));
    }

    @ApiOperation(" 大屏(各个项目)累计测试人数（月）")
    @GetMapping("/test/testedNumberOfPerProjectOfToday")
    public Result testedNumberOfPerProjectOfToday(String deviceId) throws ParseException {
        EllDevice ellDevice = wisdomClassService.selDevice(deviceId);
        int manNumber = wisdomClassService.selProSexNumDay(CreateTime.timeDay(), ellDevice.getDeptId(), 1);
        int womanNumber = wisdomClassService.selProSexNumDay(CreateTime.timeDay(), ellDevice.getDeptId(), 2);
        List<EllProTestNumDto> dtoList = wisdomClassService.selProTestNumDay(CreateTime.timeDay(), ellDevice.getDeptId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalNumber", manNumber + womanNumber);
        jsonObject.put("manNumber", manNumber);
        jsonObject.put("womanNumber", womanNumber);
        jsonObject.put("projects", dtoList);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @GetMapping("/test/projectsWordsCould")
    @ApiOperation("词云生成")
    public Result projectsWordsCould(String deviceId) {
        String url = wisdomClassService.createWordCloud(deviceId);
        if (url == null) {
            return new Result(HttpStatus.ERROR, "未查询到有测试数据");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("show", 1);
        return new Result(HttpStatus.SUCCESS, "成功", url);
    }

    @ApiOperation("基本参数")
    @GetMapping("/test/bigscreenStats")
    public Result bigscreenStats(String deviceId) {
        EllDevice ellDevice = wisdomClassService.selDevice(deviceId);
        Long createTime = CreateTime.getDayTransitionTimestamp(ellDevice.getCreateTime());
        Long useTime = new Date().getTime() - createTime;
        //服务时间
        int serviceDays = (int) (useTime / 86400);
        int userNumber = wisdomClassService.selSchoolNum(ellDevice.getDeptId(), null);
        int manNumber = wisdomClassService.selSchoolNum(ellDevice.getDeptId(), 1);
        int womanNumber = wisdomClassService.selSchoolNum(ellDevice.getDeptId(), 2);
        //查询学校
        //先从上往下查询
        List<PfDepart> departList = wisdomClassService.selSchoolsByOrgId(ellDevice.getDeptId(), true);
        //没有学校就从下往上查询
        if (departList == null || departList.size() <= 0) {
            departList = wisdomClassService.selSchoolsByOrgId(ellDevice.getDeptId(), false);
        }
        //时间
        int accumulativeTestHours = 0;
        //里程
        int accumulativeTestKilometres = 0;
        if (departList != null && departList.size() != 0) {
            for (int i = 0; i < departList.size(); i++) {
                EllDailyExercisePlan ellDailyExercisePlan = wisdomClassService.selDailyPlan(departList.get(i).getId());
                accumulativeTestHours = accumulativeTestHours + (Integer) redisTemplate.opsForValue().get(CacheConstant.All_USER_RUNNING_TIME + ellDailyExercisePlan.getId());
                accumulativeTestKilometres = accumulativeTestKilometres + wisdomClassService.selDailyRunMileage(ellDailyExercisePlan.getId());
            }
        }
        accumulativeTestHours = accumulativeTestHours / 86400;
        accumulativeTestKilometres = accumulativeTestKilometres / 1000;
        //测试人数
        int accumulativeTestNumber = wisdomClassService.selSmartTestNum(ellDevice.getDeptId());
        //测试人次
        int accumulativeTestTimes = wisdomClassService.selSmartTestTimes(ellDevice.getDeptId());
        int currTestNumber = 0;
        List<String> classIds = wisdomClassService.selClassId(ellDevice.getDeptId());
        //获取在场测试人数
        for (int i = 0; i < classIds.size(); i++) {
            if (redisTemplate.opsForValue().get("testNumber:" + classIds.get(i)) != null) {
                currTestNumber = currTestNumber + (Integer) redisTemplate.opsForValue().get("testNumber:" + classIds.get(i));
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serviceDays", serviceDays);
        jsonObject.put("userNumber", userNumber);
        jsonObject.put("manNumber", manNumber);
        jsonObject.put("womanNumber", womanNumber);
        jsonObject.put("accumulativeTestHours", accumulativeTestHours);
        jsonObject.put("accumulativeTestMileage", accumulativeTestKilometres);
        jsonObject.put("accumulativeTestNumber", accumulativeTestNumber);
        jsonObject.put("accumulativeTestTimes", accumulativeTestTimes);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @GetMapping("/test/publishInfo")
    @ApiOperation("发布信息查询")
    public Result publishInfo(String deviceId, String type) {
        //查询设备的id
        EllDevice ellDevice = wisdomClassService.selDevice(deviceId);
        List<EllDeviceInfoDto> infoDtoList = wisdomClassService.selInfoDevice(ellDevice.getId(), type);
        for (int i = 0; i < infoDtoList.size(); i++) {
            if (infoDtoList.get(i).getInfoType() != 3) {
                List<String> fileList = wisdomClassService.selInfoFile(infoDtoList.get(i).getInfoId());
                infoDtoList.get(i).setFileList(fileList);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("show", 1);
        jsonObject.put("list", infoDtoList);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("个人运动月排行榜")
    @GetMapping("/test/rankOfPerUserOfDailyRun")
    public Result rankOfPerUserOfDailyRun(String deviceId) {
        EllDevice ellDevice = wisdomClassService.selDevice(deviceId);
        //查询学校
        //先从上往下查询
        List<PfDepart> departList = wisdomClassService.selSchoolsByOrgId(ellDevice.getDeptId(), true);
        //没有学校就从下往上查询
        if (departList == null || departList.size() <= 0) {
            departList = wisdomClassService.selSchoolsByOrgId(ellDevice.getDeptId(), false);
        }
        List<String> dailyIds = new ArrayList<>();
        if (departList != null && departList.size() != 0) {
            for (int i = 0; i < departList.size(); i++) {
                EllDailyExercisePlan ellDailyExercisePlan = wisdomClassService.selDailyPlan(departList.get(i).getId());
                dailyIds.add(ellDailyExercisePlan.getId());
            }
        }
        if (dailyIds.size() <= 0) {
            return new Result(HttpStatus.ERROR, "未查询到日常锻炼计划");
        }
        List<EllProRankUserDto> dtoList = wisdomClassService.selDailyRunRanking(dailyIds, 30);
        for (int i = 0; i < dtoList.size(); i++) {
            dtoList.get(i).setRank(i + 1);
        }
        //保存到redis
        redisTemplate.opsForList().rightPush("rank:individual:run", dtoList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("show", 1);
        jsonObject.put("list", dtoList);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("班级(日常)过去30日班级达标排行榜")
    @GetMapping("/test/rankOfPerClassOfDailyRun")
    public Result rankOfPerClassOfDailyRun(String deviceId) throws ParseException {
        EllDevice ellDevice = wisdomClassService.selDevice(deviceId);
        //查询学校
        //先从上往下查询
        List<PfDepart> departList = wisdomClassService.selSchoolsByOrgId(ellDevice.getDeptId(), true);
        //没有学校就从下往上查询
        if (departList == null || departList.size() <= 0) {
            departList = wisdomClassService.selSchoolsByOrgId(ellDevice.getDeptId(), false);
        }
        List<String> dailyIds = new ArrayList<>();
        TreeMap<Integer, PfDepart> treeMap = new TreeMap<>();
        //几月
        int moth = Integer.parseInt(CreateTime.timeDayMoth());
        if (departList != null && departList.size() != 0) {
            for (int i = 0; i < departList.size(); i++) {
                //查询班级
                List<PfDepart> pfDepartList = wisdomClassService.selClass(departList.get(i).getId());
                //查询日常计划
                EllDailyExercisePlan ellDailyExercisePlan = wisdomClassService.selDailyPlan(departList.get(i).getId());
                for (int j = 0; j < pfDepartList.size(); j++) {
                    if (moth >= 7) {
                        int passNum = wisdomClassService.selPassNumBy30DayClass(pfDepartList.get(j).getId(), ellDailyExercisePlan.getSecondMonth());
                        treeMap.put(passNum, pfDepartList.get(j));
                    } else {
                        int passNum = wisdomClassService.selPassNumBy30DayClass(pfDepartList.get(j).getId(), ellDailyExercisePlan.getFirstMonth());
                        treeMap.put(passNum, pfDepartList.get(j));
                    }
                }
                dailyIds.add(ellDailyExercisePlan.getId());
            }
        }
        List<EllClassRankDto> classRankDtos = new ArrayList<>();
        //排名
        int rank = 0;
        for (Integer key : treeMap.keySet()) {
            EllClassRankDto rankDto = new EllClassRankDto(rank++, 0, treeMap.get(key).getDeptName(), key);
            classRankDtos.add(rankDto);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("show", 1);
        jsonObject.put("list", classRankDtos);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @PostMapping("/test/deviceInfo")
    @ApiOperation("设备（大屏、一体机）状态信息")
    public Result deviceInfoState(@RequestBody EllDeviceInfoStateDto stateDto) throws Exception {
      /*  EllDeviceInfoStateDto deviceInfoStateDto = wisdomClassService.getDeviceInfoState(stateDto.getDeviceId());
        if (deviceInfoStateDto != null) {
            //如果有绑定继续绑定
            stateDto.setHeldBy(deviceInfoStateDto.getHeldBy());
            stateDto.setHeldByTeacherName(deviceInfoStateDto.getHeldByTeacherName());
        }*/
        //保存
        wisdomClassService.deviceInfoState(stateDto);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd", "deviceInfo");
        jsonObject.put("data", stateDto);
        //通知所有设备
        tcpServerRemoteService.sendToAll(jsonObject.toJSONString());
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @PostMapping("/test/switchMode")
    @ApiOperation("切换模式")
    public Result switchMode(@RequestBody EllChangeDeviceModeDto deviceModeDto) throws Exception {
        String[] deviceIds = deviceModeDto.getToDevices().split(",");
        String occupyDevice = "";
        for (int i = 0; i < deviceIds.length; i++) {
            EllDeviceInfoStateDto deviceInfoStateDto = wisdomClassService.getDeviceInfoState(deviceIds[i]);
            //抢占的不用了
      /*      if (deviceInfoStateDto != null && (deviceInfoStateDto.getHeldBy().equals(deviceModeDto.getTeacherId()) || deviceInfoStateDto.getHeldBy().equals(""))) {
                //日常锻炼模式代表没人绑定
                if (deviceModeDto.getMode() != 1) {
                    //绑定
                    deviceInfoStateDto.setHeldBy(deviceModeDto.getTeacherId());
                    deviceInfoStateDto.setHeldByTeacherName(deviceModeDto.getTeacherName());
                } else {
                    //解绑
                    deviceInfoStateDto.setHeldBy("");
                    deviceInfoStateDto.setHeldByTeacherName("");
                }*/
                //绑定
                deviceInfoStateDto.setHeldBy(deviceModeDto.getTeacherId());
                deviceInfoStateDto.setHeldByTeacherName(deviceModeDto.getTeacherName());
                deviceInfoStateDto.setMode(deviceModeDto.getMode());
                //更新
                wisdomClassService.deviceInfoState(deviceInfoStateDto);
                EllChangeDeviceModeDto ellChangeDeviceModeDto = deviceModeDto;
                ellChangeDeviceModeDto.setToDevices(deviceIds[i]);
                JSONObject occupyJson = new JSONObject();
                occupyJson.put("cmd", "deviceInfo");
                occupyJson.put("data", deviceInfoStateDto);
                //通知所有设备此设备被占用
                tcpServerRemoteService.sendToAll(occupyJson.toJSONString());
                JSONObject switchModeJson = new JSONObject();
                switchModeJson.put("cmd", "switchMode");
                switchModeJson.put("data", ellChangeDeviceModeDto);
                //通知设备切换模式
                tcpServerRemoteService.sendTo(switchModeJson.toJSONString(), deviceIds[i]);
//            } else {
//                occupyDevice = occupyDevice + deviceInfoStateDto.getDeviceName() + "/";
//            }
        }
        if (!occupyDevice.equals("")) {
            return new Result(HttpStatus.DEVICE_OCCUPY, occupyDevice + "被占用");
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @PostMapping("/test/playbackTestVideo")
    @ApiOperation("播放测试视频")
    public Result playbackTestVideo(@RequestBody EllTestVideoCmdDto ellTestVideoCmdDto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd", "playbackTestVideo");
        jsonObject.put("data", ellTestVideoCmdDto);
        tcpServerRemoteService.sendTo(ellTestVideoCmdDto.getToDevice(), jsonObject.toJSONString());
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("查询学校每个年级配置的规则")
    @GetMapping("/getSchoolGradeRule")
    public Result getSchoolGradeRule(Integer schoolId) {
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.selConstitutionRules(schoolId));
    }

    @ApiOperation("添加和修改每个年级的配置规则")
    @PostMapping("/addGradeRules")
    public Result addGradeRules(@RequestBody List<EllConstitutionRules> constitutionRules) {
        wisdomClassService.delConstitutionRules(constitutionRules.get(0).getSchoolId());
        return new Result(HttpStatus.SUCCESS, "成功", wisdomClassService.addConstitutionRules(constitutionRules));
    }

    @ApiOperation("查询所有设备状态")
    @GetMapping("/tes/allDeviceInfo")
    public Result allDeviceInfo(String deviceId) {
        //模糊查询key
        List<String> keyString = new ArrayList<String>(redisTemplate.keys("deviceState:*"));
        List<EllDeviceInfoStateDto> deviceInfoStateDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(keyString)) {
            for (int i = 0; i < keyString.size(); i++) {
                EllDeviceInfoStateDto deviceInfoStateDto = (EllDeviceInfoStateDto) redisTemplate.opsForValue().get(keyString.get(i));
                deviceInfoStateDtoList.add(deviceInfoStateDto);
            }
        }
        return new Result(HttpStatus.SUCCESS, "成功", deviceInfoStateDtoList);
    }
}
