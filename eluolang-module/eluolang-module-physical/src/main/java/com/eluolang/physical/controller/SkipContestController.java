package com.eluolang.physical.controller;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllSkipContest;
import com.eluolang.common.core.pojo.EllSkipHomework;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.SkipContestService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "跳绳管理")
@RestController
public class SkipContestController {
    @Autowired
    private SkipContestService skipContestService;

    @ApiOperation("创建比赛")
    @PostMapping("/addContest")
    @Transactional
    public Result addContest(@RequestBody EllSkipContest ellSkipContest) {
        //判断比赛开始时间小于结束时间，报名时间也是如此
        if (ellSkipContest.getConBeginTime().compareTo(ellSkipContest.getConEndTime()) < 0) {
            int i = skipContestService.addContest(ellSkipContest);
            if (i > 0) {
                return new Result(HttpStatus.SUCCESS, "成功");
            } else {
                return new Result(HttpStatus.NOT_IMPLEMENTED, "创建失败");
            }
        }
        return new Result(HttpStatus.ERROR, "报名开始时间或比赛开始时间大于结束时间");
    }

    @ApiOperation("修改比赛")
    @PostMapping("/updateContest")
    @Transactional
    public Result updateContest(@RequestBody EllUpdateSkipContestVo ellUpdateSkipContestVo) {
        //判断比赛开始时间小于结束时间，报名时间也是如此

        try {
            if (ellUpdateSkipContestVo.getRegBeginTime().compareTo(ellUpdateSkipContestVo.getRegEndTime()) > 0 && ellUpdateSkipContestVo.getConBeginTime().compareTo(ellUpdateSkipContestVo.getConEndTime()) > 0) {
                return new Result(HttpStatus.ERROR, "报名开始时间或比赛开始时间大于结束时间");
            }
        } catch (NullPointerException e) {

        }
        int i = skipContestService.updateContest(ellUpdateSkipContestVo);
        if (i > 0) {
            return new Result(HttpStatus.SUCCESS, "成功");
        }
        return new Result(HttpStatus.NOT_IMPLEMENTED, "修改失败");
    }

    @ApiOperation("修改比赛状态")
    @PostMapping("/updateContestState")
    @Transactional
    public Result updateContestState(int state, String contestId) {
        int i = skipContestService.updateContestState(state, contestId);
        if (i > 0) {
            return new Result(HttpStatus.SUCCESS, "成功");
        }
        return new Result(HttpStatus.NOT_IMPLEMENTED, "修改失败");
    }

    @ApiOperation("查询比赛(后台)")
    @PostMapping("/selContest")
    @Transactional
    public Result selContest(@RequestBody EllSelConditionContestVo ellSelContestVo) {
        PageHelper.startPage(ellSelContestVo.getPage(), 15);
        List<EllSkipContestVo> ellSkipContestVo = skipContestService.selContest(ellSelContestVo);
        PageInfo info = new PageInfo(ellSkipContestVo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", info.getTotal());
        jsonObject.put("skipContest", ellSkipContestVo);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("查询参数人员")
    @PostMapping("/selContestUser")
    @Transactional
    public Result selContestUser(@RequestBody EllSkipFindContestUserVo ellSkipFindContestUserVo) {
        PageHelper.startPage(ellSkipFindContestUserVo.getPage(), 15);
        List<EllFindSkipJoinContestUserVo> ellSkipContestVo = skipContestService.findContestUser(ellSkipFindContestUserVo);
        PageInfo info = new PageInfo(ellSkipContestVo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", info.getTotal());
        jsonObject.put("skipContestUser", ellSkipContestVo);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("删除比赛")
    @PostMapping("/deleteConTest")
    public Result deleteConTest(String conTestId) {
        return new Result(HttpStatus.SUCCESS, "删除比赛", skipContestService.deleteContest(conTestId));
    }

    @ApiOperation("添加作业")
    @PostMapping("/addSkipHomework")
    public Result addSkipHomework(@RequestBody EllSkipHomeworkVo ellSkipHomeworkVo) {
        return new Result(HttpStatus.SUCCESS, "添加成功", skipContestService.addSkipHomework(ellSkipHomeworkVo));
    }

    @ApiOperation("查询作业")
    @PostMapping("/selSkipHomework")
    public Result selSkipHomework(@RequestBody EllSkipHomeworkVo ellSkipHomeworkVo) {
        PageHelper.startPage(ellSkipHomeworkVo.getPage(), 15);
        List<EllSkipHomeworkVo> skipHomeworkVoList = skipContestService.findHomework(ellSkipHomeworkVo);
        PageInfo info = new PageInfo<>(skipHomeworkVoList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("skipHomeworkVoList", skipHomeworkVoList);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("删除作业")
    @PostMapping("/delHomework")
    public Result delHomework(String homeworkId) {
        return new Result(HttpStatus.SUCCESS, "查询成功", skipContestService.delHomework(homeworkId));
    }

    @ApiOperation("/修改作业")
    @PostMapping("/upHomework")
    public Result upHomework(@RequestBody EllSkipHomework ellSkipHomework) {
        return new Result(HttpStatus.SUCCESS, "查询成功", skipContestService.upHomework(ellSkipHomework));
    }

    @ApiOperation("/发布/结束/作业")
    @PostMapping("/putHomework")
    public Result putHomework(int state, String homeworkId, String createBy) {
        EllSkipHomework ellSkipHomework = new EllSkipHomework();
        ellSkipHomework.setState(state);
        ellSkipHomework.setId(homeworkId);
        ellSkipHomework.setCreateBy(createBy);
        return new Result(HttpStatus.SUCCESS, "查询成功", skipContestService.upHomework(ellSkipHomework));
    }

    @ApiOperation("作业完成度")
    @PostMapping("/selAccomplish")
    public Result selHomeworkAccomplish(@RequestBody EllFindHomeworkVo ellFindHomeworkVo) {
        PageHelper.startPage(ellFindHomeworkVo.getPage(),15 );
        List<EllFindHomeworkVo> ellFindHomeworkVoList = skipContestService.selHomeworkAccomplish(ellFindHomeworkVo);
        PageInfo info=new PageInfo<>(ellFindHomeworkVoList);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("homeworkAccomplishList",ellFindHomeworkVoList);
        jsonObject.put("size",info.getTotal());
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }


}
