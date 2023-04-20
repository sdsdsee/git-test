package com.eluolang.app.manage.service;

import com.eluolang.app.manage.dto.EllUserVo;
import com.eluolang.app.manage.vo.*;
import com.eluolang.common.core.pojo.EllSkipDevice;
import com.eluolang.common.core.pojo.EllSkipJoinContest;
import com.eluolang.common.core.pojo.EllUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface SkipAppService {
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
     * 通过id查询跳绳用户
     */
    EllUserVo findTourist(String skipUserId,int role);

    /**
     * 跳绳用户注册
     */
    int addSkipUserWx(EllSkipUserVo ellSkipUserVo);

    /**
     * 查询此用户的学校名称
     */
    String findUserSchoolName(String orgId);

    /**
     * 查询此用户的某个角色是否被绑定
     *
     * @return
     */
    int findUserRoleIsHas(EllSkipUserVo ellSkipUserVo);

    /**
     * 上传个数
     */
    int addSkipNum(EllSkipNumberVo ellSkipNumberVo);

    /**
     * 比赛上传个数
     */
    int addSkipContestNum(EllSkipNumberVo ellSkipNumberVo, MultipartFile file) throws IOException;

    /**
     * 查询一天总个数
     */
    List<EllSkipDayNum> findSkipDayNum(String skipUserId) ;

    /**
     * 查询近一段时间的跳绳记录
     */
    List<EllSkipNumberVo> findSkipTimeQuantum(String skipUserId, int dayNum, int isWork);

    /**
     * 查询用户设备绑定
     */
    List<EllSkipDevice> findDeviceCoding(String userId, String deviceCoding);

    /**
     * 跳绳设备绑定
     */
    int addUserSkipDevice(String id, String userId, String deviceCoding);

    /**
     * 查询比赛
     */
    List<EllSkipContestVo> findEllSkipContest(EllSkipContestVo ellSkipContestVo);

    /**
     * 参加比赛
     */
    int addContest(EllSkipJoinContest ellSkipJoinContest) throws Exception;
    /**
     * 查询所有跳绳总个数和总时间
     */
    EllSkipAllNumAndTime findSkipAllNumAndTime(String skipUserId);
    /**
     * 查询后台用户的头像id
     */
    String findUserImgId(String userId);
    /**
     * 查询比赛排名
     */
    List<EllSkipRankingVo> findSkipRanking(String conTestId,String userId);
    /**
     * 查询字典个数
     */
    List<Integer> findNum();
    /**
     * 查询作业
     */
    List<EllSkipHomeworkVo> findHomework(@Param("ellSkipHomeworkVo") EllSkipHomeworkVo ellSkipHomeworkVo);
}
