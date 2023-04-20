package com.eluolang.playground.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.hardware.dto.EllDeviceInfoStateDto;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.playground.dto.*;
import com.eluolang.playground.mapper.WisdomClassMapper;
import com.eluolang.playground.service.WisdomClassService;
import com.eluolang.playground.util.CreateTime;
import com.eluolang.playground.util.PathGeneration;
import com.eluolang.playground.util.VideoChangeUtil;
import com.eluolang.playground.util.WordCloudHelper;
import com.eluolang.playground.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class WisdomClassServiceImpl implements WisdomClassService {
    @Autowired
    private WisdomClassMapper wisdomClassMapper;
    //锻炼模式下默认课程id
    private static String defaultCourseId = "default";
    //词云存储文件夹名称
    public static String wordCloudFileName = "WordCloudFile";
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    //别人要求
    public int addHisClass(List<EllClassHistoryVo> histories) {
        List<EllClassHistory> historieList = new ArrayList<>();
        //多个成绩的情况下
        for (int i = 0; i < histories.size(); i++) {
            //判断是否重测
            int isRetest = selHisClass(new EllSelClassHistory(histories.get(i).getUserId(), histories.get(i).getProId(), histories.get(i).getType())).size() > 0 ? 1 : 2;
            for (int j = 0; j < histories.get(i).getChances().size(); j++) {
                EllClassHistory ellClassHistory = new EllClassHistory();
                ellClassHistory.setCourseId(histories.get(i).getCourseId());
                if (ellClassHistory.getType() == 1) {
                    ellClassHistory.setCourseId(defaultCourseId);
                }
                //将拓展单条数据变为数组
                List<String> extraDataList = new ArrayList<>();
                extraDataList.add(histories.get(i).getExtraData());
                ellClassHistory.setExtraData(JSONArray.toJSON(extraDataList).toString());
                ellClassHistory.setCreateTime(CreateTime.getTime());
                ellClassHistory.setGrade(histories.get(i).getGrade());
                ellClassHistory.setTeacherId(histories.get(i).getTeacherId());
                ellClassHistory.setCreateByDevice(histories.get(i).getCreateByDevice());
                ellClassHistory.setProId(histories.get(i).getProId());
                ellClassHistory.setOrgId(histories.get(i).getOrgId());
                ellClassHistory.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellClassHistory.setIsRetest(isRetest);
                ellClassHistory.setUpdateTime(CreateTime.getTime());
                ellClassHistory.setUserSex(histories.get(i).getUserSex());
                ellClassHistory.setType(histories.get(i).getType());
                ellClassHistory.setProName(histories.get(i).getProName());
                ellClassHistory.setUserId(histories.get(i).getUserId());
                ellClassHistory.setEndTimestamp(histories.get(i).getEndTimestamp());
                ellClassHistory.setCheckInTimestamp(histories.get(i).getCheckInTimestamp());
                ellClassHistory.setTimeSpent(ellClassHistory.getEndTimestamp() - ellClassHistory.getCheckInTimestamp());
                String data = null;
                for (int k = 0; k < histories.get(i).getChances().get(j).getScores().size(); k++) {
                    if (data == null || data.equals("")) {
                        data = String.valueOf(histories.get(i).getChances().get(j).getScores().get(k));
                    } else {
                        data = data + "," + histories.get(i).getChances().get(j).getScores().get(k);
                    }
                }
                ellClassHistory.setData(data);
                historieList.add(ellClassHistory);
            }
        }
        return wisdomClassMapper.addHisClass(historieList);
    }

    @Override
    public List<EllSelClassHistory> selHisClass(EllSelClassHistory history) {
        return wisdomClassMapper.selHisClass(history);
    }


    @Override
    public int addVideo(EllClassVideoVo classVideo, MultipartFile video) throws IOException {
        if (!video.isEmpty()) {
            String[] type = video.getContentType().toString().split("/");
            //把文件写入到目标位置
            String fileName = UUID.randomUUID().toString();
            //把文件写入到目标位置
            String path = "";
            //如果是avi的视频
            String pathAvi = "";
            //锻炼模式
            if (classVideo.getMode() == 1) {
                classVideo.setCourseId(defaultCourseId);
            }
            String[] userIds = new String[]{classVideo.getUserIds()};
            //判断是否是多人，多人的情况用逗号隔开
            if (classVideo.getUserIds().contains(",")) {
                userIds = classVideo.getUserIds().split(",");
            }
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.videoGrade + "courseClass\\" + classVideo.getCourseId() + "\\" + userIds[0] + "\\" + classVideo.getProjectId() + "\\" + fileName + ".mp4";
                pathAvi = WindowsSite.videoGrade + "courseClass\\" + classVideo.getCourseId() + "\\" + userIds[0] + "\\" + classVideo.getProjectId() + "\\" + fileName + ".avi";
            } else {
                path = LinuxSite.videoGrade + "courseClass/" + classVideo.getCourseId() + "/" + userIds[0] + "/" + classVideo.getProjectId() + "/" + fileName + ".mp4";
                pathAvi = LinuxSite.videoGrade + "courseClass/" + classVideo.getCourseId() + "/" + userIds[0] + "/" + classVideo.getProjectId() + "/" + fileName + ".avi";
            }
            //判断是否有文件路径并生成路径
            PathGeneration.createPath(path);
            File file = new File(path);
            video.transferTo(file);//把文件写入目标文件地址
            //判断是否为MP4
            String videoType = video.getContentType().split("/")[1];
            if (videoType.equals("AVI") || videoType.equals("avi")) {
                VideoChangeUtil videoChangeUtil = new VideoChangeUtil(pathAvi, path);
                Thread inThread = new Thread(videoChangeUtil);
                inThread.start();
            }
            //把文件信息存入数据库
            //把nginx访问图片地址存入数据库
            String url = "";
            url = "/grade/" + "courseClass/" + classVideo.getCourseId() + "/" + userIds[0] + "/" + classVideo.getProjectId() + "/" + fileName + ".mp4";
            List<EllClassVideo> classVideos = new ArrayList<>();
            //多人共用同一个视频时候保存同一个地址
            for (int i = 0; i < userIds.length; i++) {
                EllClassVideo ellClassVideo = new EllClassVideo();
                //锻炼模式没有课程id默认设置一个
                if (classVideo.getMode() == 1) {
                    ellClassVideo.setCourseId(defaultCourseId);
                }
                ellClassVideo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellClassVideo.setVideoPath(url);
                ellClassVideo.setCreateTime(CreateTime.getTime());
                ellClassVideo.setProId(classVideo.getProjectId());
                ellClassVideo.setUserId(userIds[i]);
                ellClassVideo.setMode(classVideo.getMode());
                ellClassVideo.setCourseId(classVideo.getCourseId());
                classVideos.add(ellClassVideo);
            }
            return wisdomClassMapper.addVideo(classVideos);
        }
        return 0;
    }


    @Override
    public List<EllClassVideo> selVideo(EllClassVideo classVideo) {
        //判断是否为锻炼模式
        if (classVideo.getMode() == 1) {
            classVideo.setCourseId(defaultCourseId);
        }
        return wisdomClassMapper.selVideo(classVideo);
    }

    @Override
    public int addClassTime(List<EllSmartClassTime> classTimes) {
        //删除原有的课程时间
        wisdomClassMapper.delClasTime(classTimes.get(0).getSchoolId());
        classTimes.stream().forEach(classTime -> {
            classTime.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            classTime.setCreateTime(CreateTime.getTime());
            classTime.setTimeStampEnd(CreateTime.hourTransitionTimestampMinutes(classTime.getTimeEnd()));
            classTime.setTimeStampStart(CreateTime.hourTransitionTimestampMinutes(classTime.getTimeStart()));
        });
        return wisdomClassMapper.addClassTime(classTimes);
    }

    @Override
    public EllSmartClassTime selClassTimeById(String Id) {
        return wisdomClassMapper.selClassTimeById(Id);
    }

    @Override
    public int addClassSchedule(EllSmartClassScheduleVo classScheduleVo) throws Exception {
        //查询学校id
        PfDepart school = wisdomClassMapper.selSchoolByClassId(classScheduleVo.getClassId());
        //查询课堂时间
        List<EllSmartClassTime> smartClassTimeList = wisdomClassMapper.selClassTime(school.getId());
        //获取起始日期直接的所有日期
        List<String> dayTimes = CreateTime.findEveryDay(classScheduleVo.getConcreteStartDate(), classScheduleVo.getConcreteEndDate());
        //获取相应的日期是周几
        Map<Integer, List<String>> weeksDate = CreateTime.getDayOfTheWeek(dayTimes);
        Map<String, EllSmartClassTime> stringEllSmartClassTimeMap = new HashMap<>();
        List<EllSmartClassSchedule> classSchedules = new ArrayList<>();
        for (int i = 0; i < smartClassTimeList.size(); i++) {
            stringEllSmartClassTimeMap.put(smartClassTimeList.get(i).getId(), smartClassTimeList.get(i));
        }
        if (weeksDate.get(classScheduleVo.getWeek() - 1).size() == 0) {
            return 0;
        }
        for (int i = 0; i < weeksDate.get(classScheduleVo.getWeek() - 1).size(); i++) {
            EllSmartClassSchedule classSchedule = new EllSmartClassSchedule();
            classSchedule.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            classSchedule.setClassName(classScheduleVo.getClassName());
            classSchedule.setTeacherId(classScheduleVo.getTeacherId());
            classSchedule.setClassTimeId(classScheduleVo.getClassTimeId());
            //将日期和时间拼接
            classSchedule.setConcreteStart(weeksDate.get(classScheduleVo.getWeek() - 1).get(i) + " " + stringEllSmartClassTimeMap.get(classScheduleVo.getClassTimeId()).getTimeStart());
            classSchedule.setConcreteEnd(weeksDate.get(classScheduleVo.getWeek() - 1).get(i) + " " + stringEllSmartClassTimeMap.get(classScheduleVo.getClassTimeId()).getTimeEnd());
            //将具体时间转为时间戳
            classSchedule.setConcreteStampEnd(CreateTime.dateTransitionTimestamp(classSchedule.getConcreteStart()));
            classSchedule.setConcreteStampStart(CreateTime.dateTransitionTimestamp(classSchedule.getConcreteEnd()));
            classSchedule.setCreateBy(classScheduleVo.getTeacherId());
            classSchedule.setClassId(classScheduleVo.getClassId());
            classSchedule.setSchoolId(school.getId());
            classSchedule.setCreateTime(CreateTime.getTime());
            classSchedule.setUpdateTime(CreateTime.getTime());
            classSchedule.setDateTime(weeksDate.get(classScheduleVo.getWeek() - 1).get(i));
            classSchedule.setLocation(classScheduleVo.getLocation());
            classSchedule.setPhone(classScheduleVo.getPhone());
            classSchedule.setWeek(classScheduleVo.getWeek());
            classSchedules.add(classSchedule);
        }
        return wisdomClassMapper.addClassSchedule(classSchedules);
    }

    @Override
    public int delCourseById(List<String> courseId) {
        return wisdomClassMapper.delCourseById(courseId);
    }

    @Override
    public int updateCourse(EllSmartClassScheduleVo classScheduleVo) throws Exception {
        //删除相应时间的课程
        wisdomClassMapper.delCourseByTime(CreateTime.getDayTransitionTimestamp(classScheduleVo.getConcreteStartDate()), CreateTime.getDayTransitionTimestamp(classScheduleVo.getConcreteEndDate()), classScheduleVo.getTeacherId());
        return addClassSchedule(classScheduleVo);
    }

    @Override
    public int updateCourseById(EllSmartClassSchedule classSchedule) {
        return wisdomClassMapper.updateCourseById(classSchedule);
    }

    @Override
    public List<EllSmartClassScheduleDto> selSmartClassWeek(Long startTimeStamp, Long endTimeStamp, int accountId, Integer weekDay, String courseName) throws ParseException {

        return wisdomClassMapper.selSmartClassWeek(startTimeStamp, endTimeStamp, accountId, weekDay, courseName);
    }

    @Override
    public List<EllSmartClassTime> selClassTime(Integer schoolId) {
        return wisdomClassMapper.selClassTime(schoolId);
    }

    @Override
    public List<PfDepart> selSchools(int accountId, Boolean type) {
        return wisdomClassMapper.selSchools(accountId, type);
    }

    @Override
    public List<PfDepart> selSchoolsByOrgId(int orgId, Boolean type) {
        return wisdomClassMapper.selSchoolsByOrgId(orgId, type);
    }

    @Override
    public EllDailyExercisePlan selDailyPlan(Integer schoolId) {
        return wisdomClassMapper.selDailyPlan(schoolId);
    }

    @Override
    public List<PfOperator> selTeacher(Integer schoolId, Integer accountId) {
        return wisdomClassMapper.selTeacher(schoolId, accountId);
    }

    @Override
    public int addCourseware(MultipartFile file, EllCourseware courseware) throws ParseException, IOException {
        if (!file.isEmpty()) {
            String[] type = file.getOriginalFilename().split("\\.");
            //把文件写入到目标位置
            String fileName = UUID.randomUUID().toString();
            //把文件写入到目标位置
            String path = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.IMG_PATH + courseware.getTeacherId() + "\\" + CreateTime.getYear() + "\\" + fileName + "." + type[1];
            } else {
                path = LinuxSite.CoursewarePath + courseware.getTeacherId() + "/" + CreateTime.getYear() + "/" + fileName + "." + type[1];
            }
            //判断是否有文件路径并生成路径
            PathGeneration.createPath(path);
            File file1 = new File(path);
            file.transferTo(file1);//把文件写入目标文件地址
            String url = "/" + courseware.getTeacherId() + "/" + CreateTime.timeDay() + "/" + fileName + type[1];
            courseware.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            courseware.setCreateTime(CreateTime.getTime());
            courseware.setFileUrl(url);
            return wisdomClassMapper.addCourseware(courseware);
        }
        return 0;
    }

    @Override
    public List<EllCourseware> selCourse(String coursewareName, int accountId) {
        return wisdomClassMapper.selCourse(coursewareName, accountId);
    }

    @Override
    public int delCourseware(List<String> coursewareId) {
        return wisdomClassMapper.delCourseware(coursewareId);
    }

    @Override
    public List<EllSmartClassScheduleDto> selSmartClassIng(Long nowTimeStamp, int accountId, String courseName) {
        return wisdomClassMapper.selSmartClassIng(nowTimeStamp, accountId, courseName);
    }

    @Override
    public List<EllSmartClassTime> selCourseTimeByClassId(Integer orgId) {
        return wisdomClassMapper.selCourseTimeByClassId(orgId);
    }

    @Override
    public List<EllSmartClassTimeDto> selCourseTimeByCourseId(Integer orgId) {
        return wisdomClassMapper.selCourseTimeByCourseId(orgId);
    }

    @Override
    public List<EllSmartGradeDto> selGradeAvgAndMax(Integer classId, String courseId) {
        return wisdomClassMapper.selGradeAvgAndMax(classId, courseId);
    }

    @Override
    public EllUserCourseDto selUserAndCourse(String courseId, String userId) {
        return wisdomClassMapper.selUserAndCourse(courseId, userId);
    }

    @Override
    public List<EllStudentGradeDto> selUserGrade(String userId, String courseId) {
        return wisdomClassMapper.selUserGrade(userId, courseId);
    }

    @Override
    public List<EllClassVideo> selVideoByUserId(String userId, Integer proId, String courseId) {
        return wisdomClassMapper.selVideoByUserId(userId, proId, courseId);
    }

    @Override
    public List<EllGetUserByCourseIdDto> selUserByCourseId(String courseId, String userName) {
        return wisdomClassMapper.selUserByCourseId(courseId, userName);
    }

    @Override
    public List<EllFlatPlatePlanDto> selPlanFlatPlate(int isExam, int accountOrgId, Integer status) {
        return wisdomClassMapper.selPlanFlatPlate(isExam, accountOrgId, status);
    }

    @Override
    public List<EllPlanStatusNumVo> selPlanStatusNum(Integer isExam, int accountOrgId, Integer status) {
        return wisdomClassMapper.selPlanStatusNum(isExam, accountOrgId, status);
    }

    @Override
    public List<EllProScoreDto> selProScore(Integer orgId, String proId, Integer type) {
        return wisdomClassMapper.selProScore(orgId, proId, type);
    }

    @Override
    public EllDevice selDevice(String deviceId) {
        return wisdomClassMapper.selDevice(deviceId);
    }

    @Override
    public int selTestPeoNum(String day, Integer orgId) {
        return wisdomClassMapper.selTestPeoNum(day, orgId);
    }

    @Override
    public List<EllProTestNumDto> selProTestNum(String day, Integer orgId) {
        return wisdomClassMapper.selProTestNum(day, orgId);
    }

    @Override
    public List<EllProTestNumDto> selProTestNumDay(String day, Integer orgId) {
        return wisdomClassMapper.selProTestNumDay(day, orgId);
    }

    @Override
    public Integer selProSexNumDay(String day, Integer orgId, Integer sex) {
        return wisdomClassMapper.selProSexNumDay(day, orgId, sex);
    }

    @Override
    public String createWordCloud(String deviceId) {
        EllDevice ellDevice = wisdomClassMapper.selDevice(deviceId);
        if (ellDevice == null) {
            return null;
        }
        List<String> proNames = wisdomClassMapper.selTestProName(ellDevice.getDeptId());
        //初始化
//        StringBuffer stringBuffer = new StringBuffer(proNames.get(0));
//        for (int i = 1; i < proNames.size(); i++) {
//            stringBuffer.append("   " + proNames.get(i) + "    ");
//        }
        String wordCloudFile = null;
        String fileName = new Date().getTime() + IdUtils.fastSimpleUUID();
        //词云形状模板图片，无底的图片大小控制在800*600左右
        String templatePath = null;
        if (proNames.size() > 0) {
            if (FileUploadUtil.isLinux() == false) {
                wordCloudFile = WindowsSite.IMG_PATH + wordCloudFileName + "\\" + ellDevice.getDeptId() + "\\" + fileName + ".png";
                templatePath = WindowsSite.WORD_CLOUD_TEMPLATE;
            } else {
                wordCloudFile = LinuxSite.IMG_PATH + wordCloudFileName + "/" + ellDevice.getDeptId() + "/" + fileName + ".png";
                templatePath = LinuxSite.WORD_CLOUD_TEMPLATE;
            }
            PathGeneration.createPath(wordCloudFile);
            //生成词云
            WordCloudHelper.wordCloudList(proNames, wordCloudFile, templatePath);
            String url = "/" + wordCloudFileName + "/" + ellDevice.getDeptId() + "/" + fileName + ".png";
            return url;
        }
        return null;
    }

    @Override
    public Integer selSchoolNum(Integer orgId, Integer sex) {
        return wisdomClassMapper.selSchoolNum(orgId, sex);
    }

    @Override
    public Integer selDailyRunMileage(String dailyPlanId) {
        return wisdomClassMapper.selDailyRunMileage(dailyPlanId);
    }

    @Override
    public Integer selSmartTestNum(int orgId) {
        return wisdomClassMapper.selSmartTestNum(orgId);
    }

    @Override
    public Integer selSmartTestTimes(int orgId) {
        return wisdomClassMapper.selSmartTestTimes(orgId);
    }

    @Override
    public List<String> selClassId(int orgId) {
        return wisdomClassMapper.selClassId(orgId);
    }

    @Override
    public List<PfDepart> selClass(int orgId) {
        return wisdomClassMapper.selClass(orgId);
    }

    @Override
    public List<EllDeviceInfoDto> selInfoDevice(String deviceId, String type) {
        return wisdomClassMapper.selInfoDevice(deviceId, type);
    }

    @Override
    public List<String> selInfoFile(String infoId) {
        return wisdomClassMapper.selInfoFile(infoId);
    }

    @Override
    public List<EllProRankUserDto> selDailyRunRanking(List<String> dailyIds, int dayNum) {
        return wisdomClassMapper.selDailyRunRanking(dailyIds, dayNum);
    }

    @Override
    public Integer selPassNumBy30DayClass(int classId, Double passNum) {
        return wisdomClassMapper.selPassNumBy30DayClass(classId, passNum);
    }

    @Override
    public void deviceInfoState(EllDeviceInfoStateDto ellDeviceInfoStateDto) {
        redisTemplate.opsForValue().set("deviceState:" + ellDeviceInfoStateDto.getDeviceId(), ellDeviceInfoStateDto/*, 45, TimeUnit.MINUTES*/);
    }

    @Override
    public EllDeviceInfoStateDto getDeviceInfoState(String deviceId) {
        return (EllDeviceInfoStateDto) redisTemplate.opsForValue().get("deviceState:" + deviceId);
    }

    @Override
    public PfDepart selSchoolByClassId(int classId) {
        return wisdomClassMapper.selSchoolByClassId(classId);
    }

    @Override
    public List<EllConstitutionRulesDto> selConstitutionRules(Integer schoolId) {
        return wisdomClassMapper.selConstitutionRules(schoolId);
    }

    @Override
    public Integer addConstitutionRules(List<EllConstitutionRules> constitutionRules) {
        return wisdomClassMapper.addConstitutionRules(constitutionRules);
    }

    @Override
    public Integer delConstitutionRules(Integer schoolId) {
        return wisdomClassMapper.delConstitutionRules(schoolId);
    }
}
