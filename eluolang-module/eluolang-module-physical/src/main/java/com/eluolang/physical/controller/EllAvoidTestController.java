package com.eluolang.physical.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.EllAvoidProject;
import com.eluolang.common.core.pojo.EllPlan;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.PageHelpers;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.dto.EllAvoidExportDto;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.EllAvoidTestService;
import com.eluolang.physical.util.PathGeneration;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Api(tags = "免测管理")
@Transactional
public class EllAvoidTestController {
    @Autowired
    private EllAvoidTestService ellAvoidTestService;

    @ApiOperation("添加免测申请")
    @PostMapping("/addAvoidTest")
    public Result addAvoidTes(@RequestBody AvoidApplyVo avoidApply) {
        FindAvoidVo findAvoidVo = new FindAvoidVo();
        findAvoidVo.setUserId(avoidApply.getUserId());
        findAvoidVo.setPlanId(avoidApply.getPlanId());
        List<AvoidApplyVo> avoidApplyVos = ellAvoidTestService.findAvoidApply(findAvoidVo);
        if (avoidApplyVos.size() > 0) {
            return new Result(HttpStatus.ALREADY_EXISTS, "已经申请过请不要在进行申请");
        }
        int i = ellAvoidTestService.addAvoidApply(avoidApply);
        if (i > 0) {
            return Result.SUCCESS();
        }
        return Result.ERROR();
    }

    @ApiOperation("查询免测申请")
    @PostMapping("/findAvoidTest")
    public Result findAvoidTest(@RequestBody FindAvoidVo findAvoidVo) {
        PageHelpers.startPage(findAvoidVo.getPage(), 10);
        List<AvoidApplyVo> avoidApplyVos = ellAvoidTestService.findAvoidApply(findAvoidVo);
        PageInfo info = new PageInfo<>(avoidApplyVos);
        for (int i = 0; i < avoidApplyVos.size(); i++) {
            //根据用户id查询用户
            List<EllReturnScoreVo> list = ellAvoidTestService.findUserById(avoidApplyVos.get(i).getUserId());
            //查询免测申请的项目
            List<EllAvoidProject> avoidProjects = ellAvoidTestService.findAvoidProject(avoidApplyVos.get(i).getId());
            EllPlan ellPlan = ellAvoidTestService.findEllPlan(avoidApplyVos.get(i).getPlanId());
            avoidApplyVos.get(i).setPlanTitle(ellPlan.getPlanTitle());
            avoidApplyVos.get(i).setAddress(ellPlan.getPlanAddr());
            avoidApplyVos.get(i).setDateBegin(ellPlan.getDateBegin());
            avoidApplyVos.get(i).setDateEnd(ellPlan.getDateEnd());
            avoidApplyVos.get(i).setUserName(list.get(0).getUserName());
            avoidApplyVos.get(i).setEllAvoidProjectList(avoidProjects);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("avoid", avoidApplyVos);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功！", jsonObject);
    }

    @ApiOperation("审核免测申请")
    @PostMapping("/auditAvoidState")
    public Result auditAvoidState(@RequestBody EllUpdateAvoid ellUpdateAvoid) {
        int i = ellAvoidTestService.auditAvoidState(ellUpdateAvoid);
        if (i == HttpStatus.HAS_BEEN_OBTAINED) {
            return new Result(HttpStatus.HAS_BEEN_OBTAINED, "此申请其他管理员正在审核！");
        }
        return new Result(HttpStatus.SUCCESS, "成功！");
    }

    @ApiOperation("查询计划可测项目")
    @GetMapping("/findPlanProject")
    public Result findPlanProject(String planId) {
        return new Result(HttpStatus.SUCCESS, "成功！", ellAvoidTestService.findPlanProject(planId));
    }

    @Transactional
    @ApiOperation("重新上传")
    @PostMapping("/resubmit")
    public Result resubmit(@RequestBody AvoidApplyVo avoidApply) {
        avoidApply.setIsResubmit(1);
        avoidApply.setState(1);
        int i = ellAvoidTestService.updataAvoid(avoidApply);
        if (i <= 0) {
            return new Result(HttpStatus.ERROR, "失败！");
        }
        ellAvoidTestService.deleteAvoid(avoidApply.getId());
        ellAvoidTestService.addAvoidProject(avoidApply.getId(), avoidApply.getEllAvoidProjectList());
        return new Result(HttpStatus.SUCCESS, "成功！");
    }

    @Transactional
    @ApiOperation("导出免测申请")
    @PostMapping("/avoidExport")
    public Result avoidExport(@RequestBody FindAvoidVo findAvoidVo) {
        String path = "";
        String fileName = new Date().getTime() + IdUtils.fastSimpleUUID() + ".xlsx";
        String url = "";
        if (FileUploadUtil.isLinux() == false) {
            path = WindowsSite.avoidExportPath + fileName;
            url = WindowsSite.avoidExportUrl + fileName;
        } else {
            path = LinuxSite.avoidExportPath + fileName;
            url = LinuxSite.avoidExportUrl + fileName;
        }
        //判断是否有文件路径
        PathGeneration.createPath(path);
        List<EllAvoidExportDto> avoidExportDtoList = ellAvoidTestService.selAvoid(findAvoidVo);
        EasyExcel.write(path, EllAvoidExportDto.class).sheet("模板").doWrite(avoidExportDtoList);
        return new Result(HttpStatus.SUCCESS, "成功", url);
    }
}
