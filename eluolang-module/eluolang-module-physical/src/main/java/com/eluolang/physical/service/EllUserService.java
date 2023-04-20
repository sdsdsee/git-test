package com.eluolang.physical.service;

import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.GroupingUtil;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.dto.EllExamineeDto;
import com.eluolang.physical.dto.EllSexNumDto;
import com.eluolang.physical.dto.EllTotalScoreDto;
import com.eluolang.physical.dto.GroupingDto;
import com.eluolang.physical.model.*;
import com.eluolang.physical.model.DepartVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;

public interface EllUserService {
    /**
     * 添加
     *
     * @param ellUserVo
     * @return
     */
    int createUser(EllUserVo ellUserVo) throws ParseException;

    /**
     * 测试
     * 查询学生更加手机号或id或学号，身份证号
     *
     * @param findEllUserVo
     * @return
     */
    List<EllUser> findUsers(FindEllUserVo findEllUserVo);

    /**
     * 修改信息
     *
     * @param updateUserVo
     * @return
     */
    int updateUser(UpdateUserVo updateUserVo);

    /**
     * 查询学生更加手机号或id或学号，身份证号
     *
     * @param findEllUserVo
     * @return
     */
    List<UserInfoVo> findUser(FindEllUserVo findEllUserVo);

    /**
     * 查询学生更加手机号或id或学号，身份证号
     *
     * @param findEllUserVo
     * @return
     */
    List<UserInfoVo> findOrgUsers(FindEllUserVo findEllUserVo);

    /**
     * 查询学生个数
     *
     * @param findEllUserVo
     * @return
     */
    int findOrgUsersNum(FindEllUserVo findEllUserVo);

    /**
     * 查询未绑定微信的人数
     *
     * @return
     */
    int findNoRegisterNum(String ordId);

    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    int deleteUser(String id);

    /**
     * 查询是否有部门下级
     *
     * @param orgId
     * @return
     */
    List<DepartVo> findDepart(String orgId);

    /**
     * 查询是否有部门下级,测试使用
     *
     * @param orgId
     * @return
     */
    List<DepartVo> findDeparts(String orgId);

    /**
     * 查询所有名族
     */
    List<Ethnic> findEthnic();

    /**
     * 查询名族
     *
     * @param name
     * @return
     */
    Ethnic findByNameEthnic(String name);

    /**
     * 导入学生
     *
     * @param ellUserList
     * @return
     */
    int importUser(List<EllUser> ellUserList, List<ELLSymmetry> ellSymmetryList, List<EllIdentification> identificationList) throws ParseException;

    int createImageFace(MultipartFile multipartFile, String userId);

    //人脸批量上传
    String batchFaceAuthentication(MultipartFile multipartFile);

    /**
     * 头像未认证查询
     *
     * @return
     */
    int findNotAuthenticated(String orgId);

    List<EllUser> findExaminee(String planId);

    /**
     * 添加考生分组
     *
     * @param ellExamineeGroup
     * @return
     */
    int insertExamineeGroup(EllExamineeGroup ellExamineeGroup);

    /**
     * 批量添加分组下的考生
     *
     * @return
     */
    int insertExamineeUser(List<EllExamineeUser> ellExamineeUserList);

    /**
     * 学生按男女生分组
     *
     * @param ellUsers
     * @param groupingDto
     * @param sex
     * @return
     */
    boolean ExamineeGroupBySex(List<List<EllUser>> ellUsers, GroupingDto groupingDto, Integer sex);

    /**
     * 学生打乱分组
     *
     * @param ellUsers
     * @param groupingDto
     * @return
     */
    boolean upsetGroup(List<List<EllUser>> ellUsers, GroupingDto groupingDto);


    /**
     * 根据部门id查询学生
     *
     * @param pfDepartList
     * @return
     */
    List<EllUser> selEllUserByDeptId(List<PfDepart> pfDepartList);

    /**
     * 根据用户id查询
     *
     * @param id
     * @return
     */
    List<EllReturnScoreVo> findUserById(String id);

    /**
     * 根据用户身份证查询用户
     *
     * @param idCard
     * @return
     */
    List<EllReturnScoreVo> findUserByIdCard(String idCard);

    /**
     * 查询部门名称
     */
    String findDeptNameById(int id);

    /**
     * 根据用户id或学号查询用户
     *
     * @param identification
     * @return
     */
    List<EllReturnScoreVo> findUserBy(String identification, int orgId);

    /**
     * 查询所有考生分组
     *
     * @return
     */
    List<EllExamineeGroup> selectExamineeGroupAll(String planId);

    /**
     * 查询该计划是否将考生分组
     *
     * @param planId
     * @return
     */
    int selExamineeGroupByPlanId(String planId);

    /**
     * 按条件查询考生信息
     *
     * @param ellExamineeDto
     * @return
     */
    List<EllExamineeVo> selectExaminee(EllExamineeDto ellExamineeDto);

    /**
     * 根据体测计划id删除考生分组
     *
     * @param planId
     * @return
     */
    int deleteExamineeByPlanId(String planId);

    /**
     * 导出个人档案
     * <p>
     * Title:
     * </p>
     *
     * @param userId
     * @param request
     * @param response
     * @return
     */
    Result expPersonHis(String planId, String userId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 个人体质档案批量导出zip
     * <p>
     * Title:
     * </p>
     *
     * @param userIdList
     * @param request
     * @param response
     * @return
     */
    Result expPersonHisZip(String planId, List<String> userIdList, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 根据部门id修改部门状态
     *
     * @param deptIdList
     * @return
     */
    int updatePfDepartStatus(List<Integer> deptIdList);

    /**
     * 根据学生id集合修改学生状态
     *
     * @param userIdList
     * @return
     */
    int updateUserStatusList(List<EllUser> userIdList);

    /**
     * 根据部门id集合查询学生信息
     *
     * @param deptIdList
     * @return
     */
    List<EllUser> selUserByDeptIdList(List<Integer> deptIdList);

    /**
     * 新增重测组
     *
     * @param ellRetestGroupList
     * @return
     */
    int insertRetestGroup(List<EllRetestGroup> ellRetestGroupList);

    /**
     * 检录时检查是否为重测组人员
     */
    int findIsRetestGroupUser(String planId, String userId);

    /**
     * 查找是否有其他前后缀相同的数据
     */
    List<String> findIdentification(String identification);

    /**
     * 根据用户id查询用户身份证
     *
     * @param id
     * @return
     */
    String findIdCardById(String id);

    /**
     * 查询部门人数
     */
    int findOrgUserSize(int accountOrgId, String orgId);

    /**
     * 查询虹软人脸特征值并进行比对
     */
    String findFeature(String planId, String image);

    /**
     * 查询考生已经考试信息
     *
     * @param planId
     * @param userId
     * @return
     */
    EllAdmissionTicketPrintingVo AdmissionTicketPrinting(String planId, String userId);

    /**
     * 查询人脸照片
     */
    String findUserImage(String userId);

    /**
     * 查询部门 1是学校0不是学校
     */
    String findOrgName(String orgId, int orgType, String userId);

    List<EllUser> findUsers(String name, String phone, String studentId, String id, String orgId, String ethnic);

    /**
     * 批量生成准考证
     *
     * @param planId
     * @param orgId
     * @return
     */
    String batchAdmissionTicketPrinting(String planId, String orgId);

    /**
     * 查询头像地址
     */
    String findImageUrl(String userId);

    /**
     * 判断该用户需要测试该项目
     */
    int hasTestProject(FindAvoidVo findAvoidVo);

    /**
     * 查询学校name
     */
    String selSchoolName(int orgId);

    /**
     * 判断学籍号唯一
     *
     * @param studentCode
     * @return
     */
    int findStudentCode(String studentCode);


    /**
     * 查询计划使用用户
     */
    List<EllUser> selPlanUsers(String planId);

    /**
     * 查询本计划参与的学校/班级
     */
    List<PfDepart> selPlanSchool(String planId, int type, String classOrgId);

    /**
     * 查询计划选择的部门
     */
    List<EllUserPlanDepartment> selUsePlanDep(String planId);

    /**
     * 查询学校下面参加此计划的学生
     */
    List<EllUser> selPlanSchoolUser(String planId, String orgId);

    /**
     * 查询学生选择的项目
     */
    List<EllOptionalUserProVo> selOptionalUserPro(String planId, String orgId);

    /**
     * 批量添加考生分组
     *
     * @param ellExamineeGroup
     * @return
     */
    int insertBacthExamineeGroup(List<EllExamineeGroup> ellExamineeGroup);

    /**
     * 查询考试分组的学生
     */
    List<EllExamineeUser> selGroupUser(String groupId);

    /**
     * 通过id查询用户
     *
     * @param userId
     * @return
     */
    EllUser selUserById(String userId);

    /**
     * 查询部门男女人数
     */
    List<EllSexNumDto> selSexUser(FindEllUserVo findEllUserVo);
}
