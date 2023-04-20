package com.eluolang.device.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.hardware.dto.EllDeviceInfoStateDto;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.PageHelperUtil;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.device.dto.EllBodyErrorScreenshotDto;
import com.eluolang.device.dto.EllDeviceDto;
import com.eluolang.device.dto.EllDeviceInfoDto;
import com.eluolang.device.dto.EllDeviceInfodDto;
import com.eluolang.device.redis.AvoidRedis;
import com.eluolang.device.service.DeviceService;
import com.eluolang.device.util.*;
import com.eluolang.device.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 设备的控制器层
 *
 * @author dengrunsen
 */
@Api(tags = "设备控制器")
@RestController
@RequestMapping("/device")
public class DeviceController {

    /**
     * 存放拉取视频流的对象好进行关闭
     */
    public static Map<String, List<Process>> map = new HashMap<>();
    /**
     * 视频文件绑定list
     */
    public static Map<String, EllFileVo> ellFileVoMap = new HashMap<>();
    //对称加密秘钥
    private static String DesKey = "3de902b37c15e6d3";
    @Autowired
    DeviceService deviceService;
    @Autowired
    private RtspToMP4Util rtspToMP4;
    @Autowired
    private AvoidRedis avoidRedis;
    //服务器版本
    @Value("${serverVersion}")
    private String serverVersion;
    /*    *//**
     * 存放视频内容结束的时候再进行保存入数据库
     *//*
    public static Map<String, List<EllGradeVideoVo>> videoGradeMap = new HashMap<>();*/
    @Autowired
    private RedisTemplate redisTemplate;

    public static void main(String[] args) {
        EllFileVo ellFileVo = null;
    }

    /**
     * 查询设备是否注册
     *
     * @return Result
     */
    @ApiOperation(value = "查询设备是否注册")
    @GetMapping(value = "/selDeviceRegister")
    public Result selDeviceRegister(String deviceId) {
        int count = deviceService.selDeviceRegister(deviceId);
        if (count > 0) {
            return new Result(HttpStatus.SUCCESS, "设备已注册", null);
        }
        return new Result(HttpStatus.ERROR, "未查询到该设备", null);
    }

    /**
     * 查询日常锻炼计划详情
     *
     * @return Result
     */
    @ApiOperation(value = "查询日常锻炼计划详情")
    @GetMapping(value = "/selExercisePlan")
    public Result selExercisePlan(String deviceId) {
        EllDailyExercisePlan exercisePlan = deviceService.selExercisePlan(deviceId);
        if (exercisePlan != null) {
            return new Result(HttpStatus.SUCCESS, "查询成功", exercisePlan);
        }
        return new Result(HttpStatus.ERROR, "未查询到日常锻炼计划", null);
    }

    /**
     * 添加设备
     *
     * @return Result
     */
    @ApiOperation(value = "添加设备")
    @Log(title = "添加设备", operType = OperType.INSERT, content = "添加设备")
    @PostMapping(value = "/insertDevice")
    public Result insertDevice(@RequestBody EllDevice ellDevice) {
        //写入uuid
        ellDevice.setId(IdUtils.fastSimpleUUID());
        int count = deviceService.selDeviceRegister(ellDevice.getDeviceId());
        if (count > 0) {
            return new Result(HttpStatus.ERROR, "该设备已注册", count);
        }
        int count1 = deviceService.insertDevice(ellDevice);
        if (count1 > 0) {
            return new Result(HttpStatus.SUCCESS, "添加成功", count1);
        }
        return new Result(HttpStatus.ERROR, "添加失败", count1);
    }

    /**
     * 根据设备id删除设备
     *
     * @return Result
     */
    @ApiOperation(value = "根据设备id删除设备")
    @Log(title = "根据设备id删除设备", operType = OperType.DELETE, content = "根据设备id删除设备")
    @DeleteMapping(value = "/deleteDevice")
    public Result deleteDevice(String deviceId) {
        int count = deviceService.deleteDevice(deviceId);
        if (count > 0) {
            return new Result(HttpStatus.SUCCESS, "删除成功", count);
        }
        return new Result(HttpStatus.ERROR, "删除失败", count);
    }

    /**
     * 根据设备id更新设备
     *
     * @return Result
     */
    @ApiOperation(value = "根据设备id更新设备")
    @Log(title = "根据设备id更新设备", operType = OperType.UPDATE, content = "根据设备id更新设备")
    @PostMapping(value = "/updateDevice")
    public Result updateDevice(EllDevice ellDevice) {
        int count = deviceService.updateDevice(ellDevice);
        if (count > 0) {
            return new Result(HttpStatus.SUCCESS, "更新成功", count);
        }
        return new Result(HttpStatus.ERROR, "更新失败", count);
    }

    /**
     * 按条件查询设备信息
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询设备信息")
    @GetMapping(value = "/selectDevice")
    public Result selectDevice(EllDeviceDto ellDeviceDto) {
        PageHelperUtil.startPage(ellDeviceDto.getPageNum(), ellDeviceDto.getPageSize());

        List<EllDeviceInfodDto> ellDeviceList = deviceService.selectDevice(ellDeviceDto);


        //将设备数据进行分页
        PageInfo<EllDeviceInfodDto> pageInfo = new PageInfo<>(ellDeviceList);
        for (int i = 0; i < ellDeviceList.size(); i++) {
            ellDeviceList.get(i).setDeviceStates((Integer) redisTemplate.opsForValue().get("device:state:" + ellDeviceList.get(i).getDeviceId()));
            EllDeviceInfoStateDto ellDeviceInfoStateDto = (EllDeviceInfoStateDto) redisTemplate.opsForValue().get("deviceState:" + ellDeviceList.get(i).getDeviceId());
            if (ellDeviceInfoStateDto == null) {
                ellDeviceInfoStateDto = new EllDeviceInfoStateDto();
                ellDeviceInfoStateDto.setDeviceId(ellDeviceList.get(i).getDeviceId());
                ellDeviceInfoStateDto.setDeviceName(ellDeviceList.get(i).getDevName());
                ellDeviceInfoStateDto.setType(ellDeviceList.get(i).getDeviceType());
                ellDeviceInfoStateDto.setMode(3);
            }
            ellDeviceList.get(i).setDeviceInfo(ellDeviceInfoStateDto);
        }
        return new Result(HttpStatus.SUCCESS, "查询成功", pageInfo);
    }

    /**
     * 按条件查询设备信息
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询设备信息/后台版本")
    @GetMapping(value = "/selectDeviceById")
    public Result selectDeviceById(String deviceId) {
        EllDeviceInfoDto ellDeviceInfoDto = deviceService.selectDeviceById(deviceId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceInfo", ellDeviceInfoDto);

        jsonObject.put("serverVersion", serverVersion);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    /**
     * 查询正在进行中的体测计划
     *
     * @return Result
     */
    @ApiOperation(value = "查询正在进行中的体测计划")
    @GetMapping(value = "/selectStartPlan")
    public Result selectStartPlan(String deviceId) {
        //根据设备id查询正在进行的体测计划
        List<EllPlanVo> ellPlanVo = deviceService.selPlanByDeviceId(deviceId);
        if (ellPlanVo.size() != 0) {
            return new Result(HttpStatus.SUCCESS, "体测计划获取成功", ellPlanVo);
        }
        return new Result(HttpStatus.ERROR, "体测计划获取失败", null);
    }

    /**
     * 设备查询体测数据接口
     *
     * @return Result
     */
    @ApiOperation(value = "设备查询体测数据接口")
    @GetMapping(value = "/selectPhysicalTest")
    public Result selectPhysicalTest(String planId) {

        //根据设备id查询正在进行的体测计划
        EllPlanVo ellPlanVo = deviceService.selPlanById(planId);
        if (ellPlanVo != null) {
            //查询所有体测项目
            List<TestProject> testProjects = deviceService.selAllTestProject();
            if (testProjects.size() != 0) {
                ellPlanVo.setTestProjects(testProjects);
                //该计划项目测试次数集合
                List<EllPlanProjectChanceVo> ellPlanProjectChances = deviceService.selProjectChanceById(ellPlanVo.getId());
                if (ellPlanProjectChances.size() != 0) {
                    for (int j = 0; j < ellPlanProjectChances.size(); j++) {
                        int count = deviceService.selTestHistoryByPlanId(ellPlanVo.getId(), ellPlanProjectChances.get(j).getProId());
                        if (count < 0) {
                            return new Result(HttpStatus.ERROR, "查询已测人数失败", null);
                        }
                        ellPlanProjectChances.get(j).setMeasured(count);
                    }
                    ellPlanVo.setPlanProjectChances(ellPlanProjectChances);
                    //根据计划id查询计划与规则关联信息
                    List<EllUsePlanDepartment> departments = deviceService.selPlanDepartmentByPlanId(ellPlanVo.getId());
                    if (departments.size() != 0) {

                        ellPlanVo.setDepartments(departments);
                        //根据部门id集合查询其下的学生信息
                        List<EllUserVo> ellUserVoList = deviceService.selEllUserByDeptId(departments);
                        if (ellUserVoList.size() == 0) {
                            return new Result(HttpStatus.ERROR, "未查询到学生信息", null);
                        }
//                        UserNumberVo userNumberVo = deviceService.selUserNumber(departments);
                        //总人数
                        ellPlanVo.setTotalNumber(deviceService.selPlanNum(departments, 0));
                        ellPlanVo.setManNumber(deviceService.selPlanNum(departments, 1));
                        ellPlanVo.setWomanNumber(deviceService.selPlanNum(departments, 2));
                        ellPlanVo.setUserVoList(ellUserVoList);
                        ellPlanVo.setReasons(deviceService.selSysDataDictionaryByType(2));
                        //根据规则分组id去重
//                        List<EllUsePlanDepartment> departmentList = departments.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
//                                new TreeSet<>(Comparator.comparing(o -> o.getUseRule()))), ArrayList::new));
                        //根据规则组id查询规则组信息
//                        List<EllTestRules> ellTestRulesList = deviceService.selTestRuleByIds(departmentList);
//                        if (ellTestRulesList.size() != 0) {
//                            ellPlanVo.setEllTestRulesList(ellTestRulesList);
//                            //根据父级id查询计分规则
//                            List<EllTestRulesScore> ellTestRulesScores = deviceService.selRuleScoreByParentId(ellTestRulesList);
//                            if (ellTestRulesScores.size() != 0) {
//                                ellPlanVo.setScoreRules(ellTestRulesScores);
//                                List<SysDataDictionary> sysDataDictionaryList = deviceService.selSysDataDictionaryByType(2);
//                                ellPlanVo.setReasons(sysDataDictionaryList);
//                                return new Result(HttpStatus.SUCCESS, "查询成功", ellPlanVo);
//                            }
//
//                            return new Result(HttpStatus.ERROR, "规则获取失败", null);
//                    }
//                    return new Result(HttpStatus.ERROR, "规则分组获取失败", null);
                        return new Result(HttpStatus.SUCCESS, "查询成功", ellPlanVo);
                    }
                }
                return new Result(HttpStatus.ERROR, "计划与规则关联信息获取失败", null);
            }
            return new Result(HttpStatus.ERROR, "体测项目获取失败", null);
        }
//        return new Result(HttpStatus.ERROR, "体测项目次数获取失败", null);
//    }
        return new Result(HttpStatus.ERROR, "体测计划获取失败", null);

    }

    /**
     * 设备数据更新查询
     *
     * @return Result
     */
    @ApiOperation(value = "设备数据更新查询")
    @GetMapping(value = "/selectLoopPhysicalTest")
    public Result selectLoopPhysicalTest(String deviceId, String planId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hasPlanUpdated", false);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    /**
     * 设备上传肢体错误截图
     */
    @ApiOperation("肢体错误截图上传")
    @PostMapping("/uploadBodyScreenshot")
    public Result uploadBodyScreenshot(EllBodyErrorScreenshotDto ellBodyErrorScreenshotDto, @RequestParam("image") MultipartFile image) {
        int i = deviceService.addBodyErrorScreenshot(ellBodyErrorScreenshotDto, image);
        if (i > 0) {
            return new Result(HttpStatus.SUCCESS, "上传成功");
        }
        return new Result(HttpStatus.ERROR, "上传失败");
    }

    /**
     * 设备上传视频
     */
    @ApiOperation("设备上传视频")
    @PostMapping("/uploadTestingVideo")
    public Result addGradeVideo(EllGradeVideoVo ellGradeVideo, @RequestParam("videoFile") MultipartFile videoFile) {
        int i = deviceService.addGradeVideo(ellGradeVideo, videoFile);
        if (i > 0) {
            return new Result(HttpStatus.SUCCESS, "上传成功");
        }
        return new Result(HttpStatus.ERROR, "上传失败");
    }

    /**
     * 图片采集
     */
    @ApiOperation("图片采集")
    @PostMapping(value = "/imageGather")
    @ResponseBody
    public Result imageGather(MultipartFile imageFile) throws IOException {
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.imageGather(imageFile));
    }

    @Transactional
    @ApiOperation(value = "开始录制")
    @PostMapping(value = "/startTranscribe")
    public Result Start(@RequestBody EllGradeVideoVo ellGradeVideo) {
        //开始之前判断上一组的视频流关闭没有,关闭了就获取锁加锁
        System.out.println(ellGradeVideo.getDeviceId() + "：项目：" + ellGradeVideo.getProjectId());
        while (!avoidRedis.getVideoLock("video:" + ellGradeVideo.getPlanId() + ":" + ellGradeVideo.getDeviceId(), 30)) {
            continue;
        }
        try {
            List<EllGradeVideoVo> list = new ArrayList<>();
            //判断这是多人项目的视频吗
            if (ellGradeVideo.getUserIds().contains(",")) {
                String userId[] = ellGradeVideo.getUserIds().split(",");
                for (int i = 0; i < userId.length; i++) {
                    EllGradeVideoVo ellGradeVideoVo = (EllGradeVideoVo) ellGradeVideo.clone();
                    ellGradeVideoVo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                    ellGradeVideoVo.setUserIds(userId[i]);
                    ellGradeVideoVo.setBeginTime(CreateTime.getTime());
//                ellGradeVideoVo.setFileId(videoId);
//                ellGradeVideoVo.setEndTime(CreateTime.getTime());
                    ellGradeVideoVo.setIsRetest(String.valueOf(deviceService.selGradeVideo(ellGradeVideo.getPlanId(), userId[i], ellGradeVideo.getProjectId())));
                    list.add(ellGradeVideoVo);
                }
            } else {
                ellGradeVideo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellGradeVideo.setBeginTime(CreateTime.getTime());
//            ellGradeVideo.setFileId(videoId);
                ellGradeVideo.setIsRetest(String.valueOf(deviceService.selGradeVideo(ellGradeVideo.getPlanId(), ellGradeVideo.getUserIds(), ellGradeVideo.getProjectId())));
//            ellGradeVideo.setEndTime(CreateTime.getTime());
                list.add(ellGradeVideo);
            }
            String ffmpegPath = "ffmpeg";
//        String ffmpegPath = "/usr/local/ffmpeg/bin/ffmpeg";
            //获取设备的rtsp
//        String streamUrl = "rtsp://admin:ell85353507@192.168.3.78:554/ch0/sub/av_stream";
            String streamUrl = deviceService.selDeviceRtsp(ellGradeVideo.getDeviceId());
            //是否有多个摄像头
            List<String> stringList = new ArrayList<>();
            if (streamUrl == null || streamUrl.equals("")) {
                avoidRedis.releaseVideoLock("video:" + ellGradeVideo.getPlanId() + ":" + ellGradeVideo.getDeviceId());
                return new Result(HttpStatus.ERROR, "没有查询到设备的rtsp/请确认添加后重试");
            } else {
                if (streamUrl.contains(",")) {
                    stringList = Arrays.asList(streamUrl.split(","));
                } else {
                    stringList.add(streamUrl);
                }
            }
            EllFileVo ellFileVo = new EllFileVo();
            List<String> fileNameList = new ArrayList<>();
            List<String> FilePathList = new ArrayList<>();
            List<String> videoIdList = new ArrayList<>();
            ellFileVo.setFilePath(FilePathList);
            ellFileVo.setEllGradeVideo(ellGradeVideo);
            ellFileVo.setFIleName(fileNameList);
            ellFileVo.setVideoId(videoIdList);
            ellFileVo.setList(list);
            for (int i = 0; i < stringList.size(); i++) {
                String videoId = new Date().getTime() + IdUtils.fastSimpleUUID();
                String FIleName = new Date().getTime() + IdUtils.fastSimpleUUID() + ".MP4";
                String FilePath = "";
                if (FileUploadUtil.isLinux() == false) {
                    FilePath = WindowsSite.videoGrade + ellGradeVideo.getPlanId() + "\\" + list.get(0).getUserIds() + "\\" + ellGradeVideo.getProjectId() + "\\" + FIleName;
                } else {
                    FilePath = LinuxSite.videoGrade + ellGradeVideo.getPlanId() + "/" + list.get(0).getUserIds() + "/" + ellGradeVideo.getProjectId() + "/" + FIleName;
                }
                ellFileVo.getFilePath().add(FilePath);
                ellFileVo.getFIleName().add(FIleName);
                ellFileVo.getVideoId().add(videoId);
                //路径生成
                PathGeneration.createPath(FilePath);
                RtspToMP4Util.start start = new RtspToMP4Util.start(ffmpegPath, stringList.get(i), FilePath, ellGradeVideo.getDeviceId());
                Thread startVideo = new Thread(start);
                startVideo.start();
            }
            ellFileVoMap.put(ellGradeVideo.getDeviceId(), ellFileVo);
            System.out.println("开始录制");
        } catch (Exception e) {
            System.out.println(e);
            //如果报错释放锁
            avoidRedis.releaseVideoLock("video:" + ellGradeVideo.getPlanId() + ":" + ellGradeVideo.getDeviceId());
        }

        return new Result(HttpStatus.SUCCESS, "开始录制");
    }

    //开始与结束的间隔时间最好不要少于5-10s
    @ApiOperation(value = "结束录制")
    @GetMapping(value = "/stopTranscribe")
    public Result stop(String deviceId) {
        //获取计划id和项目id
        EllGradeVideoVo ellGradeVideo = new EllGradeVideoVo();
        try {
            if (null != map.get(deviceId) && map.get(deviceId).size() > 0) {
                for (int i = 0; i < map.get(deviceId).size(); i++) {
                    RtspToMP4Util.stop stop = new RtspToMP4Util.stop(map.get(deviceId).get(i));
                    Thread stopRecord = new Thread(stop);
                    stopRecord.start();
                    // rtspToMP4.stopRecord(processList.get(i));
                }
                EllFileVo ellFileVo = ellFileVoMap.get(deviceId);

                if (map.get(deviceId).size() > 0 && ellFileVo != null && ellFileVo.getFilePath().size() > 0) {
//                List<EllGradeVideoVo> list = videoGradeMap.get(deviceId);
                    //设置结束时间
//                list.stream().forEach(l -> l.setEndTime(CreateTime.getTime()));
                    List<String> filePathList = ellFileVoMap.get(deviceId).getFilePath();
                    for (int i = 0; i < filePathList.size(); i++) {
                        //判断是否有这个文件
                        if (!new File(filePathList.get(i)).exists()) {
                            continue;
                        }
                        //成绩视频进行绑定
                        gradeVideoBinding(ellFileVoMap.get(deviceId).getList(), ellFileVoMap.get(deviceId).getEllGradeVideo(), filePathList.get(i), ellFileVoMap.get(deviceId).getFIleName().get(i), ellFileVoMap.get(deviceId).getVideoId().get(i));
                    }
                    //获取这个计划地id以及项目
                    ellGradeVideo = ellFileVoMap.get(deviceId).getList().get(0);
                    ellFileVoMap.remove(deviceId);
//                videoGradeMap.remove(deviceId);
//                deviceService.addGradeVideo(list);
// 删除关闭后的录制
                    map.remove(deviceId);
                }
                System.out.println("删除锁");
//                System.out.println("线程状态"+startThread.get(deviceId).getStartTread().getState());
                //释放锁
                avoidRedis.releaseVideoLock("video:" + ellGradeVideo.getPlanId() + ":" + ellGradeVideo.getDeviceId());
//                System.out.println("结束");
                return new Result(HttpStatus.SUCCESS, "结束成功!");
            }
        } catch (Exception e) {
            System.out.println(e);
            //如果报错释放锁
            avoidRedis.releaseVideoLock("video:" + ellGradeVideo.getPlanId() + ":" + ellGradeVideo.getDeviceId());
        }
        //如果什么也没有也去释放一次锁
        avoidRedis.releaseVideoLock("video:" + ellGradeVideo.getPlanId() + ":" + ellGradeVideo.getDeviceId());
        return new Result(HttpStatus.ERROR, "结束失败请重试!");
    }

    public void gradeVideoBinding(List<EllGradeVideoVo> list, EllGradeVideoVo ellGradeVideo, String FilePath, String FIleName, String videoId) {
        //不管是多人还是但是都取其中一个人的id进行存储
        String url = "/grade/" + ellGradeVideo.getPlanId() + "/" + list.get(0).getUserIds() + "/" + ellGradeVideo.getProjectId() + "/" + FIleName;
        File file = new File(FilePath);
        list.stream().forEach(l -> {
            l.setFileId(videoId);
            l.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            //设置结束时间
            l.setEndTime(CreateTime.getTime());
        });
        int i = deviceService.uploadImage(FIleName, String.valueOf(file.length()), "1", url, CreateTime.getTime(), videoId);
        if (i > 0) {
            //保存结束的时候要保存入数据的内容
//            videoGradeMap.put(ellGradeVideo.getDeviceId(), list);
            deviceService.addGradeVideo(list);
        }
    }

    /**
     * 添加签到检录记录
     *
     * @return Result
     */
    @ApiOperation(value = "添加签到检录记录")
    @Log(title = "添加签到检录记录", operType = OperType.INSERT, content = "添加签到检录记录")
    @PostMapping(value = "/insertSignData")
    public Result insertSignData(@RequestBody EllSign ellSign) {
        Integer row = deviceService.selIsExistsUserByPlan(ellSign.getPlanId(), ellSign.getStudentCode());
        if (row > 0) {
            ellSign.setId(IdUtils.fastSimpleUUID());
            Integer count = deviceService.insertSignData(ellSign);
            if (count > 0) {
                return new Result(HttpStatus.SUCCESS, "添加成功", null);
            }
            return new Result(HttpStatus.ERROR, "添加失败", null);
        }
        return new Result(HttpStatus.ERROR, "此学生不在此计划中", null);
    }

    /**
     * 查询已检和未检人数
     *
     * @return Result
     */
    @ApiOperation(value = "设备查询已检和未检人数")
    @PostMapping(value = "/selectSignData")
    public Result selectSignData(@RequestParam("deptId") Integer deptId, @RequestParam("planId") String planId) {
        SignDataVo signDataVo = new SignDataVo();
        String deptName = deviceService.selSchoolNameById(deptId);
        Integer shouldCheckedNum = deviceService.selUserCountByDeptId(deptId, planId);
        String date = DateUtils.getDate();
        Integer checkedNum = deviceService.selSignCount(deptId, planId, date);
        signDataVo.setDeptId(deptId);
        signDataVo.setDeptName(deptName);
        signDataVo.setShouldCheckedNum(shouldCheckedNum);
        signDataVo.setCheckedNum(checkedNum);
        return new Result(HttpStatus.SUCCESS, "查询成功", signDataVo);
    }

    /**
     * 查询所有学校当日已检和未检人数
     *
     * @return Result
     */
    @ApiOperation(value = "查询所有学校当日已检和未检人数")
    @PostMapping(value = "/selectCheckedDataByPlanId")
    public Result selectCheckedDataByPlanId(String planId) {
        List<SignDataVo> signDataVoList = deviceService.selAllSignDataByPlanId(planId);
        if (signDataVoList.size() != 0) {
            for (int i = 0; i < signDataVoList.size(); i++) {
                Integer shouldCheckedNum = deviceService.selUserCountByDeptId(signDataVoList.get(i).getDeptId(), planId);
                signDataVoList.get(i).setShouldCheckedNum(shouldCheckedNum);
            }
            return new Result(HttpStatus.SUCCESS, "查询成功", signDataVoList);
        }
        return new Result(HttpStatus.ERROR, "未查询到当日签到信息", signDataVoList);
    }

    /**
     * 查询已检和未检人数
     *
     * @return Result
     */
    @ApiOperation(value = "平台查询已检和未检人数")
    @PostMapping(value = "/selectCheckedData")
    public Result selectCheckedData(@RequestParam("deptId") Integer deptId, @RequestParam("planId") String planId, @RequestParam("date") String date) {
        Map<String, Integer> map = new HashMap<>();
        Integer shouldCheckedNum = deviceService.selUserCountByDeptId(deptId, planId);
        Integer checkedNum = deviceService.selSignCount(deptId, planId, date);
        Integer noCheckedNum = shouldCheckedNum - checkedNum;
        map.put("shouldCheckedNum", shouldCheckedNum);
        map.put("checkedNum", checkedNum);
        map.put("noCheckedNum", noCheckedNum);
        return new Result(HttpStatus.SUCCESS, "查询成功", map);
    }

    /**
     * 当日已检人数
     *
     * @return Result
     */
    @ApiOperation(value = "当日已检人数")
    @GetMapping(value = "/selectCheckedToDay")
    public Result selectCheckedToDay(String planId) {
        Integer checkedNum = deviceService.selSignCountByPlanId(planId);
        return new Result(HttpStatus.SUCCESS, "查询成功", checkedNum);
    }

    /**
     * 根据学籍号查询学生信息
     *
     * @return Result
     */
    @ApiOperation(value = "根据学籍号查询学生信息")
    @GetMapping(value = "/selectUserDataByCode")
    public Result selectUserDataByCode(@RequestParam("studentCode") String studentCode, @RequestParam("planId") String planId) {
        SignUserVo signUserVo = deviceService.selSignUserByCode(studentCode);
        if (signUserVo != null) {
            Integer row = deviceService.selIsExistsUserByPlan(planId, studentCode);
            if (row > 0) {
                int grade = GradeCalculate.gradeNumber(signUserVo.getEnTime(), signUserVo.getEnGrade());
                PfDepart pfDepart = deviceService.selSchoolDataById(signUserVo.getClassId());
                signUserVo.setGrade(grade);
                signUserVo.setSchoolId(pfDepart.getId());
                signUserVo.setSchoolName(pfDepart.getDeptName());

                EllSign ellSign = new EllSign();
                ellSign.setId(IdUtils.fastSimpleUUID());
                ellSign.setPlanId(planId);
                ellSign.setStudentCode(studentCode);
                ellSign.setSignTime(DateUtils.nowDate());
                ellSign.setSchoolId(signUserVo.getSchoolId());
                Integer count = deviceService.insertSignData(ellSign);
                if (count > 0) {
                    return new Result(HttpStatus.SUCCESS, "查询成功", signUserVo);
                }
                return new Result(HttpStatus.ERROR, "查询失败", null);
            }
            return new Result(HttpStatus.ERROR, "此学生不在此计划中", null);
        }
        return new Result(HttpStatus.ERROR, "未查询到数据", null);
    }

    @ApiOperation("导出")
    @GetMapping("/deriveUser")
    public Result deriveUser(String planId, String datetime, Integer deptId, HttpServletResponse response) {
        List<SignExcelVo> signExcelVoList = deviceService.exportSignUserData(planId, datetime, deptId);
        List<PfDepart> pfDepartList = deviceService.selSchoolByPlanId(planId);
        for (SignExcelVo s : signExcelVoList) {
            for (PfDepart p : pfDepartList) {
                if (s.getClassPath().contains(p.getPath())) {
                    s.setSchoolId(p.getId());
                    s.setSchoolName(p.getDeptName());
                }
            }
        }
        for (int i = 0; i < signExcelVoList.size(); i++) {
            int grade = GradeCalculate.gradeNumber(signExcelVoList.get(i).getEnTime(), signExcelVoList.get(i).getEnGrade());
            signExcelVoList.get(i).setGrade(grade);
        }
        String fileName = UUID.randomUUID() + ".xlsx";
        String path = "";
        //判断是否为windows
        if (FileUploadUtil.isLinux() == false) {
            path = WindowsSite.filePath + fileName;
        } else {
            path = LinuxSite.filePath + fileName;
        }
        //判断是否有文件路径
        PathGeneration.createPath(path);
        EasyExcel.write(path, SignExcelVo.class).sheet("模板").doWrite(signExcelVoList);
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file); OutputStream outputStream = response.getOutputStream();) {
            response.reset();
            // response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(HttpStatus.SUCCESS, "导出成功");
    }

    @ApiOperation("创建项目版本库")
    @PostMapping("/addLibrary")
    public Result addLibrary(@RequestBody EllDeviceVersionsLabel deviceVersions) {
        Integer i = deviceService.selLabelId(deviceVersions.getLabel());
        if (i != null) {
            return new Result(HttpStatus.ERROR, "此标签已有版本库使用");
        }
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.addDeviceVersionsLabel(deviceVersions));
    }

    @ApiOperation("上传包版本")
    @PostMapping("/uploadVersion")
    public Result uploadVersion(@RequestParam("device") MultipartFile device) throws IOException {
        String url = deviceService.uploadVersion(device);
        if (url == null) {
            return new Result(HttpStatus.ERROR, "上传失败");
        }
        return new Result(HttpStatus.SUCCESS, "成功", url);
    }

    @ApiOperation("版本保存")
    @PostMapping("/versionSave")
    public Result versionSave(@RequestBody EllDeviceVersions versions) {
        if (deviceService.selSameVersion(versions.getDeviceVersions(), versions.getParentId()) != null) {
            return new Result(HttpStatus.ERROR, "此版本库中有相同的版本");
        }
        versions.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.addDeviceVersions(versions));
    }

    @ApiOperation("查询版本库")
    @GetMapping("/getLibraryDev")
    public Result getLibraryDev() {
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.selDeviceVersionsLabel());
    }

    @ApiOperation("查询软件版本")
    @GetMapping("/selDeviceVersion")
    public Result selDeviceVersion(String parentId, int page) {
        PageHelper.startPage(page, 10);
        List<EllDeviceVersions> list = deviceService.selDeviceVersion(parentId);
        PageInfo info = new PageInfo(list);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", info.getTotal());
        jsonObject.put("list", list);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("删除单个包版本")
    @DeleteMapping("/delDeviceVersion")
    public Result delDeviceVersion(String id) {
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.delDeviceVersion(id));
    }

    @ApiOperation("删除版本库")
    @DeleteMapping("/delDeviceVersionLabel")
    public Result delDeviceVersionLabel(String id) {
        //删除版本库相应的版本也会删除
        deviceService.delAllVersionLabel(id);
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.delDeviceVersionLabel(id));
    }

    @ApiOperation("设备软件更新查询")
    @GetMapping("/selVersionUrl")
    public Result selVersionUrl(String label) {
        List<EllDeviceVersions> list = deviceService.selVersionUrl(label);
        if (list == null && list.size() <= 0) {
            return new Result(HttpStatus.ERROR, "未查询到更新包");
        }
        return new Result(HttpStatus.SUCCESS, "成功", list.get(0));
    }

    @ApiOperation("加密激活码")
    @GetMapping("/encryption")
    public Result encryption(String txt) throws Exception {
        return new Result(HttpStatus.SUCCESS, "成功", DESUtil.encrypt(txt, DesKey.getBytes()));
    }
}
