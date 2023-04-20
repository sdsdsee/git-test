package com.eluolang.physical.mapper;

import com.eluolang.common.core.pojo.EllSkipContest;
import com.eluolang.common.core.pojo.EllSkipHomework;
import com.eluolang.common.core.pojo.EllSkipJoinContest;
import com.eluolang.common.core.pojo.EllSkipUser;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkipContestMapper {
    /**
     * 创建比赛
     */
    int addContest(@Param("ellSkipContest") EllSkipContest ellSkipContest);

    /**
     * 修改比赛
     */
    int updateContest(@Param("ellUpdateSkipContestVo") EllUpdateSkipContestVo ellUpdateSkipContestVo);

    /**
     * 查询比赛
     */
    List<EllSkipContestVo> selContest(@Param("ellSelContestVo") EllSelConditionContestVo ellSelContestVo);

    /**
     * 查询参赛人员
     */
    List<EllFindSkipJoinContestUserVo> findContestUser(@Param("ellSkipFindContestUserVo") EllSkipFindContestUserVo ellSkipFindContestUserVo);

    /**
     * 定时更改到期的比赛状态
     */
    int timingUpdateContestState(String dateNow);

    /**
     * 删除比赛
     */
    int deleteContest(String conTestId);

    /**
     * 添加作业
     */
    int addSkipHomework(@Param("ellSkipHomeworkList") List<EllSkipHomework> ellSkipHomeworkList);

    /**
     * 查询作业
     */
    List<EllSkipHomeworkVo> findHomework(@Param("ellSkipHomeworkVo") EllSkipHomeworkVo ellSkipHomeworkVo);

    /**
     * 删除作业
     *
     * @param homeworkId
     * @return
     */
    int delHomework(String homeworkId);

    /**
     * 修改作业
     */
    int upHomework(@Param("ellSkipHomework") EllSkipHomework ellSkipHomework);

    /***
     * 作业完成查询
     */
    List<EllFindHomeworkVo> selHomeworkAccomplish(@Param("ellFindHomeworkVo") EllFindHomeworkVo ellFindHomeworkVo);
}
