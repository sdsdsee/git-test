package com.eluolang.physical.service.impl;

import com.eluolang.common.core.pojo.EllSkipContest;
import com.eluolang.common.core.pojo.EllSkipHomework;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.physical.mapper.SkipContestMapper;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.SkipContestService;
import com.eluolang.physical.util.CreateTime;
import com.eluolang.physical.util.SkipContestContentUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SkipContestImpl implements SkipContestService {
    @Autowired
    private SkipContestMapper skipContestMapper;

    @Override
    public int addContest(EllSkipContest ellSkipContest) {
        ellSkipContest.setCreateTime(CreateTime.getTime());
        ellSkipContest.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        ellSkipContest.setContentUnit(SkipContestContentUtil.contentUnit(ellSkipContest.getConPattern()));
        return skipContestMapper.addContest(ellSkipContest);
    }

    @Override
    public int updateContest(EllUpdateSkipContestVo ellUpdateSkipContestVo) {
        ellUpdateSkipContestVo.setUpdateTime(CreateTime.getTime());
        return skipContestMapper.updateContest(ellUpdateSkipContestVo);
    }

    @Override
    public int updateContestState(int state, String contestId) {
        EllUpdateSkipContestVo ellUpdateSkipContestVo = new EllUpdateSkipContestVo();
        ellUpdateSkipContestVo.setUpdateTime(CreateTime.getTime());
        ellUpdateSkipContestVo.setConState(state);
        ellUpdateSkipContestVo.setId(contestId);
        return skipContestMapper.updateContest(ellUpdateSkipContestVo);
    }

    @Override
    public List<EllSkipContestVo> selContest(EllSelConditionContestVo ellSelContestVo) {
        // List<EllSkipContestVo> ellSkipContestVo = skipContestMapper.selContest(ellSelContestVo);
        return skipContestMapper.selContest(ellSelContestVo);
    }

    @Override
    public List<EllFindSkipJoinContestUserVo> findContestUser(EllSkipFindContestUserVo ellSkipFindContestUserVo) {
        return skipContestMapper.findContestUser(ellSkipFindContestUserVo);
    }

    @Scheduled(cron = "0 * * * * *")
    @Override
    public int timingUpdateContestState() {
        return skipContestMapper.timingUpdateContestState(CreateTime.getTime());
    }

    @Override
    public int deleteContest(String conTestId) {
        return skipContestMapper.deleteContest(conTestId);
    }

    @Override
    public int addSkipHomework(EllSkipHomeworkVo ellSkipHomeworkVo) {
        List<EllSkipHomework> ellSkipHomeworkList = new ArrayList<>();
        EllSkipHomework ellSkipHomework = new EllSkipHomework();
        ellSkipHomework.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        ellSkipHomework.setCreateBy(ellSkipHomeworkVo.getCreateBy());
        ellSkipHomework.setFinishModality(ellSkipHomeworkVo.getFinishModality());
        ellSkipHomework.setWorkPattern(ellSkipHomeworkVo.getWorkPattern());
        ellSkipHomework.setUseOrgId(ellSkipHomeworkVo.getUseOrgId());
        ellSkipHomework.setTeacherName(ellSkipHomeworkVo.getTeacherName());
        ellSkipHomework.setWorkPlace(ellSkipHomeworkVo.getWorkPlace());
        ellSkipHomework.setWorkSetting(ellSkipHomeworkVo.getWorkSetting());
        ellSkipHomework.setWorkName(ellSkipHomeworkVo.getWorkName());
        ellSkipHomework.setWorkContent(ellSkipHomeworkVo.getWorkContent());
        ellSkipHomework.setCreateTime(CreateTime.getTime());
        ellSkipHomework.setBeginTime(ellSkipHomeworkVo.getBeginTime());
        ellSkipHomework.setEndTime(ellSkipHomeworkVo.getEndTime());
        ellSkipHomework.setState(ellSkipHomeworkVo.getState());
        ellSkipHomeworkList.add(ellSkipHomework);
        return skipContestMapper.addSkipHomework(ellSkipHomeworkList);
    }

    @Override
    public List<EllSkipHomeworkVo> findHomework(EllSkipHomeworkVo ellSkipHomeworkVo) {
        return skipContestMapper.findHomework(ellSkipHomeworkVo);
    }

    @Override
    public int delHomework(String homeworkId) {
        return skipContestMapper.delHomework(homeworkId);
    }

    @Override
    public int upHomework(EllSkipHomework ellSkipHomework) {
        ellSkipHomework.setCreateTime(CreateTime.getTime());
        return skipContestMapper.upHomework(ellSkipHomework);
    }

    @Override
    public List<EllFindHomeworkVo> selHomeworkAccomplish(EllFindHomeworkVo ellFindHomeworkVo) {
        return skipContestMapper.selHomeworkAccomplish(ellFindHomeworkVo);
    }
}
