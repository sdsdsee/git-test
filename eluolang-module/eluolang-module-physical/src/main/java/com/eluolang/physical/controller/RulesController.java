package com.eluolang.physical.controller;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllTestRules;
import com.eluolang.common.core.pojo.EllTestRulesScore;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.RulesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "规则管理")
@RestController
@Transactional
public class RulesController {
    @Autowired
    private RulesService rulesService;

    /**
     * 具体每条规则
     */
    @ApiOperation("添加规则")
    @PostMapping("/creatRule")
    public Result creatRule(@RequestBody List<RulesVo> rulesVo) throws Exception {
        List<EllTestRulesScore> ruleList = rulesService.selScoreRule(Integer.parseInt(rulesVo.get(0).getSex()), rulesVo.get(0).getParentId());
        if (ruleList.size() > 0) {
            return new Result(HttpStatus.ERROR, "已经存在该性别的该项目的规则");
        }
        int i = rulesService.addRules(rulesVo);
        if (i > 0) {
            System.out.println("添加成功");
            return Result.SUCCESS();
        } else {
            System.out.println("添加失败");
            return Result.ERROR();
        }
    }


    @ApiOperation("修改规则")
    @PutMapping("/updateRule")
    public Result updateRule(@RequestBody List<RulesVo> rulesVo) throws Exception {
        //把原先的规则进行删除重新添加
        rulesService.delScoreRule(rulesVo.get(0).getParentId(), Integer.parseInt(rulesVo.get(0).getSex()));
        int i = rulesService.addRules(rulesVo);
        if (i > 0) {
            System.out.println("添加成功");
            return Result.SUCCESS();
        } else {
            System.out.println("添加失败");
            return Result.ERROR();
        }
    }

    @ApiOperation("查找规则管理员")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", dataType = "int", required = false), @ApiImplicitParam(name = "ruleName", value = "规则名称", dataType = "String", required = false), @ApiImplicitParam(name = "orgId", value = "创建的部门id", dataType = "String", required = false), @ApiImplicitParam(name = "proId", value = "项目id", dataType = "String", required = false), @ApiImplicitParam(name = "sex", value = "性别", dataType = "String", required = false),})
    @GetMapping("/findRuleSys")
    public Result findRuleSys(String id, String orgId, String ruleName, String proId, String parentId, String sex) throws Exception {
        List<EllTestRulesScore> rules = rulesService.findRulesSys(id, orgId, proId, ruleName, parentId, sex);
        System.out.println("管理员" + rules.get(0).getCreateTime());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rules", rules);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("查找规则用户")
    @GetMapping("/findRule")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", dataType = "int", required = false), @ApiImplicitParam(name = "ruleName", value = "规则名称", dataType = "String", required = false), @ApiImplicitParam(name = "orgId", value = "创建的部门id", dataType = "String", required = false), @ApiImplicitParam(name = "proId", value = "项目id", dataType = "String", required = false), @ApiImplicitParam(name = "parentId", value = "父id", dataType = "String", required = false), @ApiImplicitParam(name = "sex", value = "性别", dataType = "String", required = false), @ApiImplicitParam(name = "page", value = "页码", dataType = "int", required = true),})
    public Result findRule(String id, String orgId, String ruleName, String proId, int page, String parentId, String sex) throws Exception {
        PageHelper.startPage(page, 10);
        List<EllTestRulesScore> projectRules = rulesService.findRules(id, orgId, proId, ruleName, parentId, sex);
        List<RulesVo> rulesVos = projectRules.stream().map(projectRule -> {
            RulesVo rulesVo = new RulesVo();
            rulesVo.setId(projectRule.getId());
            rulesVo.setRule(projectRule.getRule());
            rulesVo.setOrgId(projectRule.getOrgId());
            rulesVo.setRuleScore(projectRule.getRuleScore());
            rulesVo.setRuleName(projectRule.getRuleName());
            rulesVo.setProjectName(projectRule.getProjectName());
            rulesVo.setProjectId(projectRule.getProjectId());
            rulesVo.setSex(projectRule.getSex());
            rulesVo.setGrade(projectRule.getGrade());
            rulesVo.setParentId(projectRule.getParentId());
            rulesVo.setRemark(projectRule.getRemark());
            return rulesVo;
        }).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(projectRules);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rules", rulesVos);
        jsonObject.put("size", pageInfo.getTotal());
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("删除规则")
    @DeleteMapping("/deleteRule")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", dataType = "int", required = true),})
    public Result deleteRule(int id) throws Exception {
        int i = rulesService.deleteRuleSys(id);
        if (i > 0) {
            System.out.println("成功");
            return Result.SUCCESS();
        } else {
            System.out.println("失败");
            return Result.ERROR();
        }
    }


    //规则组
    @ApiOperation("添加规则组")
    @PostMapping("/createRuleGroup")
 /*   @ApiImplicitParams({
            @ApiImplicitParam(name = "ruleName", value = "规则组名", dataType = "STRING", required = true),
            @ApiImplicitParam(name = "parentId", value = "属于那个规则", dataType = "STRING", required = false),
            @ApiImplicitParam(name = "parentId", value = "属于那个规则", dataType = "STRING", required = false),
    })*/ public Result createRuleGroup(@RequestBody CreateRuleVo ruleVo) throws Exception {
        int isUse = rulesService.findGroup(ruleVo.getRuleName());
        if (isUse > 0) {
            return new Result(HttpStatus.ALREADY_EXISTS, "名称已经重复请进行跟换！");
        }
        int i = rulesService.createRule(ruleVo);
        System.out.println("1111111" + ruleVo.getId());
        if (i == HttpStatus.ALREADY_EXISTS) {
            return new Result(i, "此规则已经存在！");
        }
        if (i > 0) {
            System.out.println("成功");
            return Result.SUCCESS();
        } else {
            System.out.println("失败");
            return Result.ERROR();
        }
    }

    //查询规则组
    @ApiOperation("查询规则组")
    @GetMapping("/findRuleGroup")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "规则id", dataType = "STRING", required = false),})
    public Result findRuleGroup(String id) throws Exception {
        List<EllTestRulesVo> ellRule = rulesService.findEllRule(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ruleGroup", ellRule);
        return new Result(HttpStatus.ERROR, "", jsonObject);
    }

    //查询规则组
    @ApiOperation("规则格式化")
    @PostMapping("/ruleFormatting")
    public Result ruleormatting(@RequestBody EllRuleFormattingVo ellRuleFormattingVo) throws Exception {
        List<EllTestRulesScore> ruleList = rulesService.selScoreRule(ellRuleFormattingVo.getSex(), ellRuleFormattingVo.getRulesId());
        if (ruleList.size()==0){
            ellRuleFormattingVo.setSymbol(1);
            return new Result(HttpStatus.SUCCESS, "成功", ellRuleFormattingVo);
        }
        //左右截取的个数
        int cutOutNumLeft = 0;
        int cutOutNumRight = 0;
        System.out.println(ruleList.get(0).getRule().contains(">="));
        //判断规则的符号
        if (ruleList != null && ruleList.size() > 0 && ruleList.get(0).getRule().contains(">=")==true) {
            cutOutNumLeft = 5;
            cutOutNumRight = 4;
            ellRuleFormattingVo.setSymbol(1);
        } else {
            cutOutNumLeft = 4;
            cutOutNumRight = 5;
            ellRuleFormattingVo.setSymbol(2);
        }
        List<EllRuleFormVo> ruleForm = new ArrayList<>();
        for (int i = 0; i < ruleList.size(); i++) {
            String rule[] = ruleList.get(i).getRule().split("&&");
            EllRuleFormVo ellRuleFormVo = new EllRuleFormVo();
            ellRuleFormVo.setRemark(ruleList.get(i).getRemark());
            ellRuleFormVo.setRuleScore(ruleList.get(i).getRuleScore());
            ellRuleFormVo.setMin(Double.parseDouble(rule[0].substring(cutOutNumLeft)));
            ellRuleFormVo.setMax(Double.parseDouble(rule[1].substring(cutOutNumRight)));
            ruleForm.add(ellRuleFormVo);
        }
        //排序
        ruleForm.sort(Comparator.comparing(EllRuleFormVo::getMax));
        ellRuleFormattingVo.setRuleForm(ruleForm);
        return new Result(HttpStatus.SUCCESS, "成功", ellRuleFormattingVo);
    }

}
