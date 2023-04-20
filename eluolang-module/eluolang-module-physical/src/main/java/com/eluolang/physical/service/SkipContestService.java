package com.eluolang.physical.service;

import com.eluolang.common.core.pojo.EllSkipContest;
import com.eluolang.common.core.pojo.EllSkipHomework;
import com.eluolang.physical.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkipContestService {
    /**
     * 创建比赛
     */
    int addContest(EllSkipContest ellSkipContest);

    /**
     * 修改比赛
     */
    int updateContest(EllUpdateSkipContestVo ellUpdateSkipContestVo);

    /**
     * 修改比赛状态
     */
    int updateContestState(int state, String contestId);

    /**
     * 查询比赛
     */
    List<EllSkipContestVo> selContest(EllSelConditionContestVo ellSelContestVo);

    /**
     * 查询参赛人员
     */
    List<EllFindSkipJoinContestUserVo> findContestUser(EllSkipFindContestUserVo ellSkipFindContestUserVo);

    /**
     * 定时更改比赛时间到了的状态
     */
    int timingUpdateContestState();

    /**
     * 删除比赛
     */
    int deleteContest(String conTestId);

    /**
     * 添加作业
     */
    int addSkipHomework(EllSkipHomeworkVo ellSkipHomeworkVo);

    /**
     * 查询作业
     */
    List<EllSkipHomeworkVo> findHomework(EllSkipHomeworkVo ellSkipHomeworkVo);

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
    int upHomework(EllSkipHomework ellSkipHomework);

    /***
     * 作业完成查询
     */
    List<EllFindHomeworkVo> selHomeworkAccomplish(EllFindHomeworkVo ellFindHomeworkVo);
}
