package com.eluolang.device.service.impl;

import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.device.dto.EllBodyErrorScreenshotDto;
import com.eluolang.device.dto.EllDeviceDto;
import com.eluolang.device.dto.EllDeviceInfoDto;
import com.eluolang.device.dto.EllDeviceInfodDto;
import com.eluolang.device.mapper.DeviceMapper;
import com.eluolang.device.service.DeviceService;
import com.eluolang.device.util.CreateTime;
import com.eluolang.device.util.PathGeneration;
import com.eluolang.device.util.VideoChangeUtil;
import com.eluolang.device.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.print.PathGraphics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 设备业务逻辑层
 *
 * @author dengrunsen
 */
@Transactional
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;


    @Override
    public int addGradeVideo(List<EllGradeVideoVo> ellGradeVideoVoList) {
        return deviceMapper.addGradeVideo(ellGradeVideoVoList);
    }

    @Override
    public int selDeviceRegister(String deviceId) {
        return deviceMapper.selDeviceRegister(deviceId);
    }

    @Override
    public EllDailyExercisePlan selExercisePlan(String deviceId) {
        return deviceMapper.selExercisePlan(deviceId);
    }

    @Override
    public int insertDevice(EllDevice ellDevice) {
        return deviceMapper.insertDevice(ellDevice);
    }

    @Override
    public int deleteDevice(String deviceId) {
        return deviceMapper.deleteDevice(deviceId);
    }

    @Override
    public int updateDevice(EllDevice ellDevice) {
        return deviceMapper.updateDevice(ellDevice);
    }

    @Override
    public List<EllDeviceInfodDto> selectDevice(EllDeviceDto ellDeviceDto) {
        return deviceMapper.selectDevice(ellDeviceDto);
    }

    @Override
    public List<EllPlanVo> selPlanByDeviceId(String deviceId) {
        return deviceMapper.selPlanByDeviceId(deviceId);
    }

    @Override
    public EllPlanVo selPlanById(String planId) {
        return deviceMapper.selPlanById(planId);
    }

    @Override
    public List<TestProject> selAllTestProject() {
        return deviceMapper.selAllTestProject();
    }

    @Override
    public List<EllUsePlanDepartment> selPlanDepartmentByPlanId(String planId) {
        return deviceMapper.selPlanDepartmentByPlanId(planId);
    }

    @Override
    public List<EllTestRules> selTestRuleByIds(List<EllUsePlanDepartment> departments) {
        return deviceMapper.selTestRuleByIds(departments);
    }

    @Override
    public List<EllTestRulesScore> selRuleScoreByParentId(List<EllTestRules> ellTestRules) {
        return deviceMapper.selRuleScoreByParentId(ellTestRules);
    }

    @Override
    public List<EllUserVo> selEllUserByDeptId(List<EllUsePlanDepartment> departmentList) {
        return deviceMapper.selEllUserByDeptId(departmentList);
    }

    @Override
    public UserNumberVo selUserNumber(List<EllUsePlanDepartment> departmentList) {
        return deviceMapper.selUserNumber(departmentList);
    }

    @Override
    public int selPlanNum(List<EllUsePlanDepartment> departments, int sex) {
        return deviceMapper.selPlanNum(departments, sex);
    }

    @Override
    public List<EllPlanProjectChanceVo> selProjectChanceById(String planId) {
        return deviceMapper.selProjectChanceById(planId);
    }

    @Override
    public int selTestHistoryByPlanId(String planId, Integer proId) {
        return deviceMapper.selTestHistoryByPlanId(planId, proId);
    }

    @Override
    public List<SysDataDictionary> selSysDataDictionaryByType(Integer type) {
        return deviceMapper.selSysDataDictionaryByType(type);
    }

    @Override
    public int addBodyErrorScreenshot(EllBodyErrorScreenshotDto bodyErrorScreenshotDto, MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                String imgId = IdUtils.fastSimpleUUID();
                String[] type = image.getContentType().toString().split("/");
                //把文件写入到目标位置
                String fileName = UUID.randomUUID().toString();
                String path = "";
                if (FileUploadUtil.isLinux() == false) {
                    path = WindowsSite.IMG_PATH_BodyErrorScreenshot + fileName + "." + type[1];
                } else {
                    path = LinuxSite.IMG_PATH_BodyErrorScreenshot + fileName + "." + type[1];
                }
                File file = new File(path);
                PathGeneration.createPath(path);
                image.transferTo(file);//把文件写入目标文件地址
                //把文件信息存入数据库
                //把nginx访问图片地址存入数据库
                String url = "";
                if (FileUploadUtil.isLinux() == false) {
                    url = WindowsSite.IMG_NginxPATH_BodyErrorScreenshot + fileName + "." + type[1];
                } else {
                    url = LinuxSite.IMG_NginxPATH_BodyErrorScreenshot + fileName + "." + type[1];
                }
                int i = deviceMapper.uploadImage(fileName, String.valueOf(image.getSize()), "1", url, CreateTime.getTime(), imgId);
                if (i > 0) {
                    int isHas = deviceMapper.findIsHaveHistory(bodyErrorScreenshotDto.getPlanId(), String.valueOf(bodyErrorScreenshotDto.getProjectId()), bodyErrorScreenshotDto.getUserId());
                    return deviceMapper.addBodyErrorScreenshot(imgId, bodyErrorScreenshotDto, url, isHas + 1);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }

    @Override
    public int addGradeVideo(EllGradeVideoVo ellGradeVideo, MultipartFile videoFile) {
        List<EllGradeVideoVo> list = new ArrayList<>();
        //avi视频地址
        String aviPath = "";
        //转换后MP4地址
        String mp4Path = "";
        File file = null;
        //多人项目
        if (ellGradeVideo.getUserIds().contains(",")) {
            String userId[] = ellGradeVideo.getUserIds().split(",");
            for (int i = 0; i < userId.length; i++) {
                EllGradeVideoVo ellGradeVideoVo = (EllGradeVideoVo) ellGradeVideo.clone();
                ellGradeVideoVo.setUserIds(userId[i]);
                ellGradeVideoVo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellGradeVideoVo.setBeginTime(CreateTime.TimestampTransitionDate(ellGradeVideo.getBeginTime()));
                ellGradeVideoVo.setEndTime(CreateTime.TimestampTransitionDate(ellGradeVideo.getEndTime()));
                list.add(ellGradeVideoVo);
            }
        } else {
            ellGradeVideo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
//            ellGradeVideo.setBeginTime(CreateTime.TimestampTransitionDate(ellGradeVideo.getBeginTime()));
//            ellGradeVideo.setEndTime(CreateTime.TimestampTransitionDate(ellGradeVideo.getEndTime()));
            list.add(ellGradeVideo);
        }
        if (!videoFile.isEmpty()) {
            try {
                String videoId = new Date().getTime() + IdUtils.fastSimpleUUID();
                String[] type = videoFile.getContentType().toString().split("/");
                //把文件写入到目标位置
                String fileName = new Date().getTime() + UUID.randomUUID().toString();
                //avi视频格式转换MP4前的地址
                //通过不同的计划和不同的人进行存储如果是长跑的话几个人就用同一个人的地址
                if (FileUploadUtil.isLinux() == false) {
                    aviPath = WindowsSite.videoGrade + ellGradeVideo.getPlanId() + "\\" + list.get(0).getUserIds() + "\\" + ellGradeVideo.getProjectId() + "\\" + fileName + "." + type[1];
                    mp4Path = WindowsSite.videoGrade + ellGradeVideo.getPlanId() + "\\" + list.get(0).getUserIds() + "\\" + ellGradeVideo.getProjectId() + "\\" + fileName + ".mp4";
                } else {
                    aviPath = LinuxSite.videoGrade + ellGradeVideo.getPlanId() + "/" + list.get(0).getUserIds() + "/" + ellGradeVideo.getProjectId() + "/" + fileName + "." + type[1];
                    mp4Path = LinuxSite.videoGrade + ellGradeVideo.getPlanId() + "/" + list.get(0).getUserIds() + "/" + ellGradeVideo.getProjectId() + "/" + fileName + ".mp4";
                }
                file = new File(aviPath);
                PathGeneration.createPath(aviPath);
                videoFile.transferTo(file);//把文件写入目标文件地址
                //判断是否为MP4
                String videoType = videoFile.getContentType().split("/")[1];
                if (videoType.equals("AVI") || videoType.equals("avi")) {
                    VideoChangeUtil videoChangeUtil = new VideoChangeUtil(aviPath, mp4Path);
                    Thread inThread = new Thread(videoChangeUtil);
                    inThread.start();
                }
                String url = "/grade/" + ellGradeVideo.getPlanId() + "/" + list.get(0).getUserIds() + "/" + ellGradeVideo.getProjectId() + "/" + fileName + ".mp4";
                int i = deviceMapper.uploadImage(fileName, String.valueOf(videoFile.getSize()), "1", url, CreateTime.getTime(), videoId);
                if (i > 0) {
                    //用户与视频做绑定
                    list.stream().forEach(l -> l.setFileId(videoId));

                    return deviceMapper.addGradeVideo(list);
                }
            } catch (Exception e) {
                file.delete();
                System.out.println(e);
            }
        }
        return 0;
    }

    @Override
    public int imageGather(MultipartFile imageList) throws IOException {

        if (!imageList.isEmpty()) {
            String[] type = imageList.getContentType().toString().split("/");
            //把文件写入到目标位置
            String fileName = UUID.randomUUID().toString();
            String path = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.ImageGather + fileName + "." + type[1];
            } else {
                path = LinuxSite.ImageGather + fileName + "." + type[1];
            }
            File file = new File(path);
            PathGeneration.createPath(path);
            imageList.transferTo(file);//把文件写入目标文件地址
        }
        return 1;
    }

    @Override
    public int uploadImage(String fileName, String size, String type, String fileUrl, String uploadTime, String id) {
        return deviceMapper.uploadImage(fileName, size, type, fileUrl, uploadTime, id);
    }

    @Override
    public String selDeviceRtsp(String deviceId) {
        return deviceMapper.selDeviceRtsp(deviceId);
    }

    @Override
    public int selGradeVideo(String planId, String userId, int proId) {
        return deviceMapper.selGradeVideo(planId, userId, proId);
    }

    @Override
    public String selSchoolNameById(Integer deptId) {
        return deviceMapper.selSchoolNameById(deptId);
    }

    @Override
    public int selIsExistsUserByPlan(String planId, String studentCode) {
        return deviceMapper.selIsExistsUserByPlan(planId, studentCode);
    }

    @Override
    public int insertSignData(EllSign ellSign) {
        return deviceMapper.insertSignData(ellSign);
    }

    @Override
    public int selUserCountByDeptId(Integer deptId, String planId) {
        return deviceMapper.selUserCountByDeptId(deptId, planId);
    }

    @Override
    public int selSignCount(Integer deptId, String planId, String date) {
        return deviceMapper.selSignCount(deptId, planId, date);
    }

    @Override
    public int selSignCountByPlanId(String planId) {
        return deviceMapper.selSignCountByPlanId(planId);
    }

    @Override
    public SignUserVo selSignUserByCode(String studentCode) {
        return deviceMapper.selSignUserByCode(studentCode);
    }

    @Override
    public PfDepart selSchoolDataById(Integer deptId) {
        return deviceMapper.selSchoolDataById(deptId);
    }

    @Override
    public List<SignExcelVo> exportSignUserData(String planId, String time, Integer deptId) {
        return deviceMapper.exportSignUserData(planId, time, deptId);
    }

    @Override
    public List<PfDepart> selSchoolByPlanId(String planId) {
        return deviceMapper.selSchoolByPlanId(planId);
    }

    @Override
    public List<SignDataVo> selAllSignDataByPlanId(String planId) {
        return deviceMapper.selAllSignDataByPlanId(planId);
    }

    @Override
    public String uploadVersion(MultipartFile device) throws IOException {
        if (!device.isEmpty()) {
            String type = device.getOriginalFilename().substring(device.getOriginalFilename().lastIndexOf("."));
            //把文件写入到目标位置
            String fileName = UUID.randomUUID().toString();
            String path = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.DeviceVersion + fileName + type;
            } else {
                path = LinuxSite.DeviceVersion + fileName + type;
            }
            //判断是否有文件路径并生成路径
            PathGeneration.createPath(path);
            File file = new File(path);
            device.transferTo(file);//把文件写入目标文件地址
            String url = "/" + fileName + type;
            return url;
        }
        return null;
    }

    @Override
    public int addDeviceVersionsLabel(EllDeviceVersionsLabel deviceVersions) {
        return deviceMapper.addDeviceVersionsLabel(deviceVersions);
    }

    @Override
    public int addDeviceVersions(EllDeviceVersions versions) {
        return deviceMapper.addDeviceVersions(versions);
    }

    @Override
    public List<EllDeviceVersionsLabel> selDeviceVersionsLabel() {
        return deviceMapper.selDeviceVersionsLabel();
    }

    @Override
    public List<EllDeviceVersions> selDeviceVersion(String parentId) {
        return deviceMapper.selDeviceVersion(parentId);
    }

    @Override
    public int delDeviceVersion(String id) {
        return deviceMapper.delDeviceVersion(id);
    }

    @Override
    public int delDeviceVersionLabel(String id) {
        return deviceMapper.delDeviceVersionLabel(id);
    }

    @Override
    public int delAllVersionLabel(String parentId) {
        return deviceMapper.delDeviceVersionLabel(parentId);
    }

    @Override
    public List<EllDeviceVersions> selVersionUrl(String label) {
        Integer parentId = deviceMapper.selLabelId(label);
        if (parentId == null) {
            return null;
        }
        return deviceMapper.selVersionUrl(parentId);
    }

    @Override
    public Integer selLabelId(String label) {
        return deviceMapper.selLabelId(label);
    }

    @Override
    public EllDeviceVersions selSameVersion(String version, int parentId) {
        return deviceMapper.selSameVersion(version, parentId);
    }

    @Override
    public EllDeviceInfoDto selectDeviceById(String deviceId) {
        return deviceMapper.selectDeviceById(deviceId);
    }
}

