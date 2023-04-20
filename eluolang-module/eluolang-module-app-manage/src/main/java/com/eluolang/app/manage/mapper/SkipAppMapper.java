package com.eluolang.app.manage.mapper;

import com.eluolang.app.manage.dto.EllUserVo;
import com.eluolang.app.manage.vo.*;
import com.eluolang.common.core.pojo.EllSkipDevice;
import com.eluolang.common.core.pojo.EllSkipJoinContest;
import com.eluolang.common.core.pojo.EllUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkipAppMapper {
    /**
     * 查询用户信息
     */
    EllUserVo findUser(String wxOpenId);

    /**
     * 通过身份证标识查找相同标识的用户id
     */
    List<String> findUserIdByIdentification(String identification);

    /**
     * 通过id查找是否拥有这个用户
     */
    EllUser findUserById(String userId);

    /**
     * 通过userid查询跳绳用户
     */
    EllUserVo findTourist(@Param("skipUserId") String skipUserId, @Param("role") int role);

    /**
     * 跳绳用户注册
     */
    int addSkipUserWx(@Param("ellSkipUserVo") EllSkipUserVo ellSkipUserVo);

    /**
     * 生成判断身份证数据库标识
     */
    int addIdentification(@Param("skipUserId") String skipUserId, @Param("identification") String identification);

    /**
     * 查询此用户的学校名称
     */
    String findUserSchoolName(String orgId);

    /**
     * 查询此用户的某个角色是否被绑定
     *
     * @return
     */
    int findUserRoleIsHas(@Param("ellSkipUserVo") EllSkipUserVo ellSkipUserVo);

    /**
     * 上传成绩个数
     */
    int addSkipNum(@Param("ellSkipNumberVo") EllSkipNumberVo ellSkipNumberVo);

    /**
     * 查询本人排名
     */
    int findRanking(@Param("conTestId") String conTestId, @Param("skipAvg") int skipAvg);

    /**
     * 查询一天总个数
     */
    List<EllSkipDayNum> findSkipDayNum(@Param("skipUserId") String skipUserId, @Param("dayList") List<String> dayList);

    /**
     * 查询近一段时间的跳绳记录
     */
    List<EllSkipNumberVo> findSkipTimeQuantum(@Param("skipUserId") String skipUserId, @Param("dayList") List<String> dayList, @Param("isWork") int isWork);

    /**
     * 查询用户和设备是否相互绑定，或者用户绑定了其他的设备，设备绑定了其他的用户
     */
    List<EllSkipDevice> findDeviceCoding(@Param("userId") String userId, @Param("deviceCoding") String deviceCoding);

    /**
     * 跳绳设备绑定
     */
    int addUserSkipDevice(@Param("id") String id, @Param("userId") String userId, @Param("deviceCoding") String deviceCoding);

    /**
     * 查询比赛
     */
    List<EllSkipContestVo> findEllSkipContest(@Param("ellSkipContestVo") EllSkipContestVo ellSkipContestVo);

    /**
     * 查询比赛内容
     */
    int findConContent(@Param("contestId") String contestId, @Param("conContent") int conContent);

    /**
     * 修改比赛成绩
     */
    int updateGrade(@Param("avg") Double avg, @Param("updateTime") String updateTime, @Param("conTestId") String conTestId, @Param("skipNumId") String skipNumId, @Param("skipUserId") String skipUserId, @Param("videoFileId") String videoFileId);

    /**
     * 修改排名
     */
    int updateRanking(@Param("conTestId") String conTestId, @Param("avg") Double avg, @Param("skipUserId") String skipUserId);

    /**
     * 参加比赛
     */
    int addContest(@Param("ellSkipJoinContest") EllSkipJoinContest ellSkipJoinContest);

    /**
     * 文件地址存储
     *
     * @param fileName
     * @param size
     * @param fileUrl
     * @param uploadTime
     * @param id
     * @return
     */
    int uploadImage(@Param("fileName") String fileName, @Param("size") String size, @Param("type") String type, @Param("fileUrl") String fileUrl,
                    @Param("uploadTime") String uploadTime, @Param("id") String id);

    /**
     * 查询所有跳绳总个数和总时间
     */
    EllSkipAllNumAndTime findSkipAllNumAndTime(String skipUserId);

    /**
     * 查询比赛排名
     */
    List<EllSkipRankingVo> findSkipRanking(@Param("conTestId") String conTestId, @Param("userId") String userId);

    /**
     * 查询后台用户的头像id
     */
    String findUserImgId(String userId);

    /**
     * 查询字典个数
     */
    List<Integer> findNum();
    /**
     * 查询作业
     */
    List<EllSkipHomeworkVo> findHomework(@Param("ellSkipHomeworkVo") EllSkipHomeworkVo ellSkipHomeworkVo);
}
