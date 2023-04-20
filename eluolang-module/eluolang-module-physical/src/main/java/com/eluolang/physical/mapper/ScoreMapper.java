package com.eluolang.physical.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.physical.dto.EllScoreHistoryDto;
import com.eluolang.physical.dto.OneUserScoreDto;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface ScoreMapper {
    /**
     * 添加成绩
     *
     * @param ellHistoryVo
     * @return
     */
    int addStudentScoreMapper(@Param("ellHistoryVo") EllHistoryVo ellHistoryVo);

    /**
     * 条件查询成绩
     */
    List<EllTestHistory> findUserHistory(@Param("ellHistoryVo") EllHistoryVo ellHistoryVo);

    /**
     * 查询用户已测试项目个数
     */
    Integer selTestProNum(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 测试人数
     */
    int findScoreNum(@Param("proId") int proId, @Param("planId") String planId);

    /**
     * 条件查询成绩前端使用
     */
    List<FindScoreVo> findUserScore(@Param("findScoreConditionVo") FindScoreConditionVo findScoreConditionVo);

    /**
     * 条件查询成绩前端使用
     */
    List<FindScoreVo> importHistory(@Param("findScoreConditionVo") FindScoreConditionVo findScoreConditionVo);

    /**
     * 查询有成绩的学生id
     */
    List<EllReturnScoreVo> findUserReturn(@Param("findScoreConditionVo") FindScoreConditionVo findScoreConditionVo, @Param("userIds") List<String> userIds);

    /**
     * 查询该计划免测人的userId
     */
    List<String> selFindAvoid(String planId);

    /**
     * 修改成绩
     */
    int updateScore(@Param("updateScoreVo") UpdateScoreVo updateScoreVo, @Param("time") String time);

    /**
     * 根据学生id和计划id查询学生各项测试平均分
     *
     * @param userId
     * @param planId
     * @return
     */
    String selUserAvg(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 根据学生id和计划id查询学生所有项目体测分数
     *
     * @param userId
     * @param planId
     * @return
     */
    List<TestHistoryModel> selUserTestScore(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 查询成绩核实
     */
    EllScoreVerify findVerify(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 纸质添加成绩核实
     */
    int resultsVerify(@Param("id") String id, @Param("ellScoreVerifyVo") EllScoreVerifyVo ellScoreVerifyVo);

    /**
     * 设备添加成绩核实
     */
    int resultsVerifyFacility(@Param("id") String id, @Param("ellScoreVerifyVo") EllScoreVerifyVo ellScoreVerifyVo, @Param("fileNameId") String fileId, @Param("fileFaceId") String fileFaceName);

    /**
     * 查询有多少个项目的成绩
     */
    int findProHistory(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 将学生的最终分数进行存储
     */
    int addPlanEndScore(@Param("planId") String planId, @Param("userId") String userId, @Param("endScore") Double endScore, @Param("endComment") String endComment);

    /**
     * 查询学生是否储存的有最终成绩
     */
    EllPlanEndScore findPlanEndScore(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 查询成绩核实的照片签名和拍照
     */
    EllScoreVerify selVerifyByUserId(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 查询分数占比
     */
    List<EllPlanScoresOf> findPlanScoresOf(@Param("planId") String planId);

    /**
     * 查询肢体错误截图
     */
    List<ErrorScreenShotVo> findErrorImage(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 查询本计划必考是否含有视力
     */
    int findHasVision(String planId);

    /**
     * 查询本计划自选是否含有视力
     *
     * @param planId
     * @param userId
     * @return
     */
    int findHasVisionOptional(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 视力单独存储
     */
    int addEyesight(@Param("ellEyesightParticulars") EllEyesightParticulars ellEyesightParticulars);

    /**
     * 查询视力另存
     */
    List<EllEyesightParticulars> findEyesight(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 视力存储修改
     */
    int updataEyesight(@Param("ellEyesightParticulars") EllEyesightParticulars ellEyesightParticulars);

    /**
     * 查询视力子项目
     */
    List<EllPlanProjectChance> findSubsetProject(@Param("planId") String planId, @Param("parentId") int parentId);

    /**
     * 另存身高体重
     */
    int createBmi(@Param("ellBmiParticulars") EllBmiParticulars ellBmiParticulars);

    /**
     * 查询某计划历史身高体重另存
     */
    List<EllBmiParticulars> findBmi(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 修改身高体重另存
     */
    int updataBmi(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 修改最终成绩
     */
    int updataEndScore(@Param("planId") String planId, @Param("userId") String userId, @Param("endScore") Double endScore, @Param("endComment") String endComment);

    /**
     * 查询测试过多少次体重
     */
    int contBmi(String userId);

    /**
     * 通过user_id查询免测申请
     */
    List<EllAvoidProject> selAvoidPro(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 查询考试录像
     */
    List<EllGradeVideoVo> selGradeVideo(@Param("planId") String planId, @Param("userId") String userId, @Param("proId") int proId);

    /**
     * 查询列表学生信息
     */
    List<EllScoreListDto> selUserList(@Param("ellScoreListVo") EllScoreListVo ellScoreListVo);

    /**
     * 查询成绩列表
     */
    List<EllScoreListGradeDto> selGradeList(@Param("userId") String userId, @Param("planId") String planId);

    /**
     * 查询学生
     */
    List<EllUser> selUser(@Param("orgId") String orgId, @Param("userSex") String userSex);

    /**
     * 查询计划项目
     */
    List<EllTestProject> selPro(@Param("planId") String planId, @Param("useSex") String useSex);

    /**
     * 查询计划必考项目ID
     */
    List<String> selProId(@Param("planId") String planId, @Param("useSex") String useSex);

    /**
     * 查询计划自选项目ID
     */
    List<String> selOptionProId(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 查询某人单项成绩
     */
    List<EllTestHistory> selHistoryById(@Param("planId") String planId, @Param("userId") String userId, @Param("proId") String proId);

    /**
     * 查询计划低于60分的人
     */
    List<EllUser> selScoreless60(@Param("planId") String planId, @Param("userSex") String userSex);

    /**
     * 查询单个项目成绩
     */
    List<OneUserScoreDto> getOneUserScore(@Param("account") int account, @Param("orgId") String orgId, @Param("planId") String planId, @Param("proId") int proId, @Param("userName") String userName, @Param("userId") String userId);

    /**
     * 根据用户id查询用户应测项目ID
     */
    List<String> selUserTestPro(@Param("planId") String planId, @Param("userId") String userId);

    /**
     * 查询所有应测项目id
     */
    List<FindScoreVo> selAllProHistory(@Param("planId") String planId, @Param("userId") String userId, @Param("proId") List<String> proId);

    /**
     * 查询当前项目分数占比
     */
    String selProGdp(@Param("planId") String planId, @Param("proId") String proId);

    /**
     * 带项目名称/最终分数的查询成绩
     */
    List<EllScoreHistoryDto> selEndAndProNameScore(@Param("planId") String planId, @Param("accountId") String accountId, @Param("orgId") String orgId);

    /**
     * 通过计划id查询有成绩的学生id
     */
    List<EllTestHistory> selHistory(String planId);
}

