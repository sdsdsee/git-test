package com.eluolang.physical.service;

import com.eluolang.common.core.pojo.*;
import com.eluolang.physical.dto.DepartTreeDto;
import com.eluolang.physical.dto.EllScoreHistoryDto;
import com.eluolang.physical.dto.OneUserScoreDto;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ScoreService {
    /**
     * 上传分数
     *
     * @param ellHistoryVo
     * @return
     */
    int addStudentScoreMapper(EllHistoryVo ellHistoryVo);

    /**
     * 条件查询成绩
     */
    List<EllReturnScoreVo> findUserHistory(FindScoreConditionVo findScoreConditionVo, List<EllReturnScoreVo> findUser);

    /**
     * 查询用户已测试项目个数
     */
    int selTestProNum(String userId, String planId);

    /**
     * 设备检录时候判断是否是重测成绩
     */
    List<EllTestHistory> findScore(EllHistoryVo ellHistoryVo);

    String calculateGrade(String enTime, int enGrade);

    /**
     * 导出成绩成绩 与查询成绩一样
     */
    List<EllReturnScoreVo> importHistory(FindScoreConditionVo findScoreConditionVo);

    /**
     * 测试人数
     */
    int findScoreNum(int proId, String planId);

    /**
     * 根据学生id和计划id查询学生各项测试平均分
     *
     * @param userId
     * @param planId
     * @return
     */
    String selUserAvg(String userId, String planId);

    /**
     * 根据学生id和计划id查询学生所有项目体测分数
     *
     * @param userId
     * @param planId
     * @return
     */
    List<TestHistoryModel> selUserTestScore(String userId, String planId);

    /**
     * 添加成绩核实
     */
    int resultsVerify(EllScoreVerifyVo ellScoreVerifyVo);

    /**
     * 设备添加成绩核实
     */
    int resultsVerifyFacility(EllScoreVerifyVo ellScoreVerifyVo, MultipartFile imageName, MultipartFile imageFace);

    /**
     * 查询肢体错误截图
     */
    List<ErrorScreenShotVo> findErrorImage(String planId, String userId);

    /**
     * 查询成绩核实
     */
    EllScoreVerify findVerify(String userId, String planId);

    /**
     * 核实成绩查询
     *
     * @param userId
     * @param planId
     * @return
     */
    EllReturnScoreVo findResultsVerify(String userId, String planId);

    /**
     * 查询有成绩的学生id
     */
    List<EllReturnScoreVo> findUserReturn(FindScoreConditionVo findScoreConditionVo, List<String> userIds);

    /**
     * 查询视力另存
     */
    List<EllEyesightParticulars> findEyesight(String planId, String userId);

    /**
     * 查询视力子项目
     */
    List<EllPlanProjectChance> findEyesightProject(String planId, int parentId);

    /**
     * 添加最终成绩
     *
     * @param findScoreConditionVo
     * @param findUser
     */
    public void createEndScore(FindScoreConditionVo findScoreConditionVo, List<EllReturnScoreVo> findUser);

    /**
     * 修改单个项目最终成绩
     */
    int updateScore(EllChangeScoreVo ellChangeScoreVo);

    /**
     * 查询考试录像
     */
    List<EllGradeVideoVo> selGradeVideo(String planId, String userId, int proId);

    /**
     * 查询该计划免测人的userId
     */
    List<String> selFindAvoid(String planId);

    /**
     * 查询列表学生信息
     */
    List<EllScoreListDto> selUserList(EllScoreListVo ellScoreListVo);

    /**
     * 查询学生是否储存的有最终成绩
     */
    EllPlanEndScore findPlanEndScore(String planId, String userId);

    /**
     * 查询成绩列表
     */
    List<EllScoreListGradeDto> selGradeList(String userId, String planId);

    //计算分数
    List<EllReturnScoreVo> judgeProComplete(String planId, List<EllReturnScoreVo> ellReturnScoreVos);

    /**
     * 查询学生
     */
    List<EllUser> selUser(String orgId, String userSex);

    /**
     * 查询计划项目
     */
    List<EllTestProject> selPro(String planId, String useSex);

    /**
     * 查询计划项目ID
     */
    List<String> selProId(String planId, String useSex);

    /**
     * 查询计划自选项目ID
     */
    List<String> selOptionProId(String planId, String userId);

    /**
     * 查询某人单项成绩
     */
    List<EllTestHistory> selHistoryById(String planId, String userId, String proId);

    /**
     * 查询计划低于60分的人
     */
    List<EllUser> selScoreless60(String planId, String userSex);

    /**
     * 查询单个项目成绩
     */
    List<OneUserScoreDto> getOneUserScore(int account, String orgId, String planId, int proId, String userName, String userId);

    /**
     * 保存文件生成地址
     */
    Map<String, String> imageSaveFiles(MultipartFile faceImage, MultipartFile signatureImage) throws IOException;

    /**
     * 根据用户id查询用户应测项目ID
     */
    List<String> selUserTestPro(String planId, String userId);

    /**
     * 查询所有应测项目id
     */
    List<FindScoreVo> selAllProHistory(String planId, String userId, List<String> proId);

    /**
     * 带项目名称/最终分数的查询成绩
     */
    List<EllScoreHistoryDto> selEndAndProNameScore(String planId, String accountId, String orgId);

    /**
     * 通过计划id查询有成绩的学生id
     */
    List<EllTestHistory> selHistory(String planId);


    /**
     * 查询成绩核实的照片签名和拍照
     */
    EllScoreVerify selVerifyByUserId(@Param("planId") String planId, @Param("userId") String userId);
}
