package com.eluolang.physical.controller;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllTestProject;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.model.AddProjectVo;
import com.eluolang.physical.model.FindProjectVo;
import com.eluolang.physical.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "项目管理")
@RestController
@Transactional
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @ApiOperation("添加项目")
    @PostMapping("/addProject")
    public Result addProject(@RequestBody AddProjectVo addProjectVo) {
        int i = projectService.addProject(addProjectVo);
        if (i > 0) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @ApiOperation("修改项目")
    @PostMapping("/updataProject")
    public Result updataProject(@RequestBody AddProjectVo addProjectVo) throws Exception {
        int i = projectService.updataProject(addProjectVo);
        if (i > 0) {
            return Result.SUCCESS();
        } else {
            return Result.ERROR();
        }
    }

    @ApiOperation("查询项目(管理员)")
    @GetMapping("/findProjectSys")
    public <T> Result findProjectSys() {
        List<EllTestProject> testProjectList = projectService.findProjectSys();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("project", testProjectList);
        return new Result(HttpStatus.SUCCESS, "查询成功！", jsonObject);
    }

    @ApiOperation("查询项目(管理员)")
    @GetMapping("/selProjectSys")
    public <T> Result selProjectSys() {
        List<EllTestProject> testProjectList = projectService.findProjectSys();
        return new Result(HttpStatus.SUCCESS, "查询成功！", testProjectList);
    }
    @ApiOperation("查询项目(用户)")
    @GetMapping("/findProject")
    public <T> Result findProject() {
        List<FindProjectVo> testProjectList = projectService.findProject();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("project", testProjectList);
        return new Result(HttpStatus.SUCCESS, "查询成功！", jsonObject);
    }

    @ApiOperation("物理删除")
    @DeleteMapping("/deleteProjectSys")
    public <T> Result deleteProjectSys(int id) {
        int i = projectService.deleteProjectSys(id);
        if (i > 0) {
            return Result.SUCCESS();
        }
        return Result.ERROR();
    }

    @ApiOperation("逻辑删除")
    @DeleteMapping("/deleteProject")
    public <T> Result deleteProject(int id) {
        int i = projectService.deleteProject(id);
        if (i > 0) {
            return Result.SUCCESS();
        }
        return Result.ERROR();
    }
}
