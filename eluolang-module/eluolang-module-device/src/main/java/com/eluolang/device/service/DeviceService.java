package com.eluolang.device.service;

import com.eluolang.common.core.pojo.*;
import com.eluolang.device.dto.EllBodyErrorScreenshotDto;
import com.eluolang.device.dto.EllDeviceDto;
import com.eluolang.device.dto.EllDeviceInfoDto;
import com.eluolang.device.dto.EllDeviceInfodDto;
import com.eluolang.device.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 定义业务层的设备接口方法
 *
 * @author dengrunsen
 */
public interface DeviceService {
    /**
     * 添加成績視頻
     */
    int addGradeVideo(List<EllGradeVideoVo> ellGradeVideoVoList);

    /**
     * 查询设备是否注册
     *
     * @param deviceId
     * @return
     */
    int selDeviceRegister(String deviceId);

    /**
     * 根据设备id查询日常锻炼计划详情
     * @param deviceId
     * @return
     */
    EllDailyExercisePlan selExercisePlan(String deviceId);

    /**
     * 添加设备
     *
     * @param ellDevice
     * @return
     */
    int insertDevice(EllDevice ellDevice);

    /**
     * 根据设备id删除设备
     *
     * @param deviceId
     * @return
     */
    int deleteDevice(String deviceId);

    /**
     * 更新设备信息
     *
     * @param ellDevice
     * @return
     */
    int updateDevice(EllDevice ellDevice);

    /**
     * 按条件查询设备信息
     *
     * @param ellDeviceDto
     * @return
     */
    List<EllDeviceInfodDto> selectDevice(EllDeviceDto ellDeviceDto);

    /**
     * 根据设备id查询发布中的体测计划
     *
     * @param deviceId
     * @return
     */
    List<EllPlanVo> selPlanByDeviceId(String deviceId);

    /**
     * 根据计划id查询计划
     *
     * @param planId
     * @return
     */
    EllPlanVo selPlanById(String planId);

    /**
     * 查询所有体测项目
     *
     * @return
     */
    List<TestProject> selAllTestProject();

    /**
     * 根据计划id查询其使用部门
     *
     * @param planId
     * @return
     */
    List<EllUsePlanDepartment> selPlanDepartmentByPlanId(String planId);

    /**
     * 根据规则组id查询规则组信息
     *
     * @param departments
     * @return
     */
    List<EllTestRules> selTestRuleByIds(List<EllUsePlanDepartment> departments);

    /**
     * 根据父级id查询计分规则
     *
     * @param ellTestRules
     * @return
     */
    List<EllTestRulesScore> selRuleScoreByParentId(List<EllTestRules> ellTestRules);

    /**
     * 根据部门id集合查询其下的学生信息
     *
     * @param departmentList
     * @return
     */
    List<EllUserVo> selEllUserByDeptId(List<EllUsePlanDepartment> departmentList);

    /**
     * 查询学生人数信息
     *
     * @param departmentList
     * @return
     */
    UserNumberVo selUserNumber(@Param("departmentLists") List<EllUsePlanDepartment> departmentList);

    /**
     * 查询计划总数
     */
    int selPlanNum(List<EllUsePlanDepartment> departments, int sex);

    /**
     * 根据计划id查询项目测试次数
     *
     * @param planId
     * @return
     */
    List<EllPlanProjectChanceVo> selProjectChanceById(String planId);

    /**
     * 根据计划id查询已测人数
     *
     * @param planId
     * @return
     */
    int selTestHistoryByPlanId(String planId, Integer proId);

    /**
     * 按类型查询数据字典
     *
     * @return
     */
    List<SysDataDictionary> selSysDataDictionaryByType(Integer type);

    /**
     * 上传肢体错误截图
     */
    int addBodyErrorScreenshot(EllBodyErrorScreenshotDto bodyErrorScreenshotDto, MultipartFile image);

    /**
     * 添加成績視頻
     */
    int addGradeVideo(EllGradeVideoVo ellGradeVideo, MultipartFile videoFile);

    /**
     * 图片采集
     */
    int imageGather(MultipartFile imageList) throws IOException;

    /**
     * 文件储存
     *
     * @param fileName
     * @param size
     * @param type
     * @param fileUrl
     * @param uploadTime
     * @param id
     * @return
     */
    int uploadImage(String fileName, String size, String type, String fileUrl,
                    String uploadTime, String id);

    /**
     * 根据deviceId查询设备的rstp
     */
    String selDeviceRtsp(String deviceId);

    /**
     * 查询视频是否为重测视频
     */
    int selGradeVideo(String planId, String userId, int proId);

    /**
     * 根据部门id查询学校
     *
     * @param deptId
     * @return
     */
    String selSchoolNameById(Integer deptId);

    /**
     * 查询此学生是否在此计划下
     *
     * @param planId
     * @param studentCode
     * @return
     */
    int selIsExistsUserByPlan(String planId, String studentCode);

    /**
     * 添加签到检录记录
     *
     * @param ellSign
     * @return
     */
    int insertSignData(EllSign ellSign);

    /**
     * 根据部门id查询其学生总人数
     *
     * @param deptId
     * @param planId
     * @return
     */
    int selUserCountByDeptId(@Param("deptId") Integer deptId, @Param("planId") String planId);

    /**
     * 根据部门id和计划id查询当日签到人数
     *
     * @param deptId
     * @param planId
     * @return
     */
    int selSignCount(@Param("deptId") Integer deptId, @Param("planId") String planId, @Param("date") String date);

    /**
     * 根据计划id查询当日签到人数
     *
     * @param planId
     * @return
     */
    int selSignCountByPlanId(String planId);

    /**
     * 根据学籍号查询学生信息
     *
     * @param studentCode
     * @return
     */
    SignUserVo selSignUserByCode(String studentCode);

    /**
     * 根据部门id查询所属学校信息
     *
     * @param deptId
     * @return
     */
    PfDepart selSchoolDataById(Integer deptId);

    /**
     * 按天查询学生签到信息
     *
     * @param planId
     * @param time
     * @return
     */
    List<SignExcelVo> exportSignUserData(String planId, String time, Integer deptId);

    /**
     * 查询此计划下的所有学校信息
     *
     * @param planId
     * @return
     */
    List<PfDepart> selSchoolByPlanId(String planId);

    /**
     * 根据计划id查询所有学校当日签到信息
     *
     * @param planId
     * @return
     */
    List<SignDataVo> selAllSignDataByPlanId(String planId);

    /**
     * 上传程序版本
     */
    String uploadVersion(MultipartFile device) throws IOException;

    /**
     * 创建项目版本
     */
    int addDeviceVersionsLabel(EllDeviceVersionsLabel deviceVersions);

    /**
     * 添加版本
     */
    int addDeviceVersions(EllDeviceVersions versions);

    /**
     * 查询版本库
     */
    List<EllDeviceVersionsLabel> selDeviceVersionsLabel();

    /**
     * 查询版本通过版本库id查询
     */
    List<EllDeviceVersions> selDeviceVersion(String parentId);

    /**
     * 删除包版本
     */
    int delDeviceVersion(String id);

    /**
     * 删除包版本库
     */
    int delDeviceVersionLabel(String id);

    /**
     * 按库删除包版本
     */
    int delAllVersionLabel(String parentId);

    /**
     * 查询软件下载地址
     */
    List<EllDeviceVersions> selVersionUrl(String label);

    /**
     * 通过版本库标签查询id
     */
    Integer selLabelId(String label);

    /**
     * 查询是否有版本号相同的软件
     */
    EllDeviceVersions selSameVersion(String version, int parentId);

    /**
     * 按条件查询设备信息
     *
     * @param deviceId
     * @return
     */
    EllDeviceInfoDto selectDeviceById(String deviceId);
}
