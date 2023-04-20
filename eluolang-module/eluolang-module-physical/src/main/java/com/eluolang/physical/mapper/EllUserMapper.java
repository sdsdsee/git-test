package com.eluolang.physical.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.physical.dto.EllExamineeDto;
import com.eluolang.physical.dto.EllSexNumDto;
import com.eluolang.physical.model.*;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EllUserMapper {
    int createUser(@Param("ellUser") EllUser ellUser);

    /**
     * 添加对称加密
     */
    int createSymmetry(@Param("ellSymmetryList") List<ELLSymmetry> ellSymmetryList);

    List<EllUser> findUsers(@Param("name") String name, @Param("phone") String phone, @Param("studentId") String studentId, @Param("id") String id, @Param("orgId") String orgId, @Param("ethnic") String ethnic);

    /**
     * 判断学籍号唯一
     *
     * @param studentCode
     * @return
     */
    int findStudentCode(String studentCode);

    int updateUser(@Param("updateUserVo") UpdateUserVo updateUserVo);

    /**
     * @param name
     * @param phone
     * @param studentId
     * @param id
     * @param orgId
     * @param ethnic
     * @param isRegister 1.已注册 2.未注册 3.已认证 4.未认证 其他为全查
     * @return
     */
    List<UserInfoVo> findUser(@Param("name") String name, @Param("phone") String phone, @Param("studentId") String studentId, @Param("id") String id, @Param("orgId") String orgId, @Param("ethnic") String ethnic, @Param("isRegister") String isRegister, @Param("accountOrgId") int accountOrgId);

    /**
     * 查询学生
     *
     * @param findEllUserVo
     * @return
     */
    List<UserInfoVo> findOrgUsers(@Param("findEllUserVo") FindEllUserVo findEllUserVo);

    /**
     * 查询学生个数
     *
     * @param findEllUserVo
     * @return
     */
    int findOrgUsersNum(@Param("findEllUserVo") FindEllUserVo findEllUserVo);

    /**
     * 根据用户id查询用户
     *
     * @param id
     * @return
     */
    List<EllReturnScoreVo> findUserById(@Param("id") String id);

    /**
     * 根据用户id或学号查询用户
     *
     * @param identification
     * @return
     */
    List<EllReturnScoreVo> findUserBy(@Param("identification") String identification, @Param("orgId") int orgId);

    /**
     * 根据用户id查询用户身份证
     *
     * @param id
     * @return
     */
    String findIdCardById(@Param("id") String id);

    /**
     * 根据用户身份证查询用户
     *
     * @param idCard
     * @return
     */
    List<EllReturnScoreVo> findUserByIdCard(@Param("idCard") String idCard);

    /**
     * 查询未绑定微信的人数
     *
     * @param orgId
     * @return
     */
    int findNoRegisterNum(String orgId);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    int deleteUser(String id);

    /**
     * 查询部门是否有下级
     */
    List<DepartVo> findDepart(String orgId);

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
     * 导入
     */
    int importUser(@Param("ellUserVo") List<EllUser> ellUserVo);

    /**
     * 创建人脸信息
     *
     * @param imageId
     * @param faceInfo
     * @param userId
     * @return
     */
    int createImageFace(@Param("imageId") String imageId, @Param("faceInfo") String faceInfo, @Param("userId") String userId, @Param("ellFace") String ellFace,@Param("KsImageId")String ksImageId);

    /**
     * 批量创建人脸信息
     *
     * @return
     */
    int createImageFaceMany(@Param("ellFaceVoList") List<EllFaceVo> ellFaceVoList);

    /**
     * 把人脸照片进行存储
     *
     * @param fileName
     * @param size
     * @param fileUrl
     * @param uploadTime
     * @param id
     * @return
     */
    int uploadImage(@Param("fileName") String fileName, @Param("size") String size, @Param("type") String type, @Param("fileUrl") String fileUrl, @Param("uploadTime") String uploadTime, @Param("id") String id);

    /**
     * 文件批量保存
     */
    int uploadImageMany(@Param("fileMgrList") List<FileMgr> fileMgrList);

    /**
     * 把人脸照片批量进行存储
     *
     * @param fileName
     * @param size
     * @param fileUrl
     * @param uploadTime
     * @param id
     * @return
     */
    int uploadBatchImage(@Param("fileName") String fileName, @Param("size") String size, @Param("type") String type, @Param("fileUrl") String fileUrl, @Param("uploadTime") String uploadTime, @Param("id") String id);

    int findUserFaceInfo(String usrId);

    /**
     * 修改人脸信息
     *
     * @param imageId
     * @param faceInfo
     * @param userId
     * @return
     */
    int updateImageFace(@Param("imageId") String imageId, @Param("faceInfo") String faceInfo, @Param("userId") String userId, @Param("ellFace") String ellFace,@Param("ksImageId")String ksImageId);

    /**
     * 删除人脸认证
     */
    int delImageFaceMany(@Param("ellFaceVoList") List<EllFaceVo> ellFaceVoList);

    /**
     * 头像未认证查询
     *
     * @return
     */
    int findNotAuthenticated(String orgId);

    /**
     * 添加考生分组
     *
     * @param ellExamineeGroup
     * @return
     */
    int insertExamineeGroup(EllExamineeGroup ellExamineeGroup);

    /**
     * 批量添加考生分组
     *
     * @param ellExamineeGroup
     * @return
     */
    int insertBacthExamineeGroup(@Param("ellExamineeGroup") List<EllExamineeGroup> ellExamineeGroup);

    /**
     * 批量添加分组下的考生
     *
     * @return
     */
    int insertExamineeUser(@Param("ellExamineeUserList") List<EllExamineeUser> ellExamineeUserList);

    /**
     * 根据部门id查询学生
     *
     * @param pfDepartList
     * @return
     */
    List<EllUser> selEllUserByDeptId(@Param("pfDepartList") List<PfDepart> pfDepartList);

    /**
     * 查询部门名称
     */
    String findDeptNameById(int orgId);

    /**
     * 生成判断身份证数据库标识
     */
    int addIdentification(@Param("userId") String userId, @Param("identification") String identification, @Param("orgId") String orgId);

    /**
     * 批量生成判断身份证数据库标识
     */
    int addBatchIdentification(@Param("identificationList") List<EllIdentification> identificationList);

    /**
     * 查找是否有其他前后缀相同的数据
     */
    List<String> findIdentification(String identification);

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
     * 根据部门id修改部门状态
     *
     * @param deptIdList
     * @return
     */
    int updatePfDepartStatus(@Param("deptIdList") List<Integer> deptIdList);

    /**
     * 根据学生id集合修改学生状态
     *
     * @param userIdList
     * @return
     */
    int updateUserStatusList(@Param("userIdList") List<EllUser> userIdList);

    /**
     * 根据部门id集合查询学生信息
     *
     * @param deptIdList
     * @return
     */
    List<EllUser> selUserByDeptIdList(@Param("idList") List<Integer> deptIdList);

    /**
     * 新增重测组
     *
     * @param ellRetestGroupList
     * @return
     */
    int insertRetestGroup(@Param("ellRetestGroupList") List<EllRetestGroup> ellRetestGroupList);

    /**
     * 检录时检查是否为重测组人员
     */
    int findIsRetestGroupUser(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 查询部门人数/可通过上级部门查询旗下所有部门的人数
     */
    int findOrgUserSize(@Param("accountOrgId") int accountOrgId, @Param("orgId") String orgId);

    /**
     * 查询虹软人脸特征值
     */
    List<EllFeature> findFeature(String planId);

    /**
     * 查询部门
     */
    String findOrgName(@Param("orgId") String orgId, @Param("orgType") int orgType, @Param("userId") String userId);

    /**
     * 查询人脸照片
     */
    String findUserImage(String userId);

    /**
     * 查询头像地址
     */
    String findImageUrl(String userId);

    /**
     * 删除标识
     */
    int deleteIdentification(String userId);

    /**
     * 删除对称加密
     */
    int deleteSymetrical(String userId);

    /**
     * 判断是否有这个部门
     */
    int hasDepartment(int orgId);

    /**
     * 判断该用户需要测试该项目
     */
    int hasTestProject(@Param("findAvoidVo") FindAvoidVo findAvoidVo);

    /**
     * 删除预约
     */
    int deletePlanUser(String planId);

    /**
     * <p>Title:查询个人体质测试运动指南建议 </p>
     *
     * @return
     */
    List<SportConfigVO> querySportConfig();

    /**
     * 查询学校name
     */
    String selSchoolName(int orgId);

    /**
     * 查询计划使用用户
     */
    List<EllUser> selPlanUsers(String planId);

    /**
     * 查询本计划参与的学校/班级   1：查班级2：向上查学校3：向下查学校4：查上级
     */
    List<PfDepart> selPlanSchool(@Param("planId") String planId, @Param("type") int type, @Param("classOrgId") String classOrgId);

    /**
     * 查询计划选择的部门
     */
    List<EllUserPlanDepartment> selUsePlanDep(String planId);

    /**
     * 查询学校下面参加此计划的学生
     */
    List<EllUser> selPlanSchoolUser(@Param("planId") String planId, @Param("orgId") String orgId);

    /**
     * 查询学生选择的项目
     */
    List<EllOptionalUserProVo> selOptionalUserPro(@Param("planId") String planId, @Param("orgId") String orgId);

    /**
     * 查询学生考号
     */
    String selTestNumber(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 查询考试分组的学生
     */
    List<EllExamineeUser> selGroupUser(@Param("groupId") String groupId);

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
    List<EllSexNumDto> selSexUser(@Param("findEllUserVo") FindEllUserVo findEllUserVo);
}
