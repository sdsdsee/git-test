package com.eluolang.system.controller;

import com.alibaba.fastjson.JSON;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllDevice;
import com.eluolang.common.core.pojo.FileMgr;
import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.ExportUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.PageHelperUtil;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperObj;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.system.dto.EllSportConfigDto;
import com.eluolang.system.dto.EllSportConfigRuleDto;
import com.eluolang.system.dto.OperLogDto;
import com.eluolang.system.dto.SportRuleDto;
import com.eluolang.system.excel.OperLogExcel;
import com.eluolang.system.service.SportConfigService;
import com.eluolang.system.service.SystemService;
import com.eluolang.system.vo.EllSportConfigRuleVo;
import com.eluolang.system.vo.EllSportConfigVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "个人报告导出配置")
@RestController
@RequestMapping("/sport")
public class SportConfigController {
    @Autowired
    SportConfigService sportConfigService;

    /**
     * 新增运动指南配置
     *
     * @param sportConfigDto
     * @return
     */
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "id", dataType = "string", required = false),
//            @ApiImplicitParam(name = "name", value = "运动指南名称", dataType = "string", required = true),
//            @ApiImplicitParam(name = "proIds", value = "项目id集合(xx,xx,xx)", dataType = "string", required = true),
//            @ApiImplicitParam(name = "createTime", value = "创建时间", dataType = "string", required = false),
//            @ApiImplicitParam(name = "isDelete", value = "是否删除标识(0否,1是)", dataType = "int", required = false)
//
//    })
    @ApiOperation(value = "新增运动指南配置", notes = "新增运动指南配置")
    @RequestMapping(value = "/insertSportConfig", method = RequestMethod.POST)
    @Log(title = "新增",operType = OperType.INSERT,content = "新增运动指南配置")
    public Result insertSportConfig(@RequestBody EllSportConfigDto sportConfigDto) {
        sportConfigDto.setId(IdUtils.fastSimpleUUID());
        Integer count = sportConfigService.insertSportConfig(sportConfigDto);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"添加成功",count);
        }
        return new Result(HttpStatus.ERROR,"添加失败",count);
    }

    /**
     * 删除运动指南配置
     * @param id
     * @return
     */
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "id", dataType = "string", required = true)
//    })
    @ApiOperation(value = "删除运动指南配置", notes = "删除运动指南配置")
    @RequestMapping(value = "/deleteSportConfig", method = RequestMethod.DELETE)
    @Log(title = "删除",operType = OperType.DELETE,content = "删除运动指南配置")
    public Result deleteSportConfig(String id) {
        Integer count = sportConfigService.deleteSportConfig(id);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"删除成功",count);
        }
        return new Result(HttpStatus.ERROR,"删除失败",count);
    }

    /**
     * 修改运动指南配置
     * @param sportConfigDto
     * @return
     */
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "id", dataType = "string", required = true),
//            @ApiImplicitParam(name = "name", value = "运动指南名称", dataType = "string", required = true),
//            @ApiImplicitParam(name = "proIds", value = "项目id集合(xx,xx,xx)", dataType = "string", required = true),
//            @ApiImplicitParam(name = "createTime", value = "创建时间", dataType = "string", required = false),
//            @ApiImplicitParam(name = "isDelete", value = "是否删除标识(0否,1是)", dataType = "int", required = false)
//
//    })
    @ApiOperation(value = "修改运动指南配置", notes = "修改运动指南配置")
    @RequestMapping(value = "/updateSportConfig", method = RequestMethod.POST)
    @Log(title = "修改",operType = OperType.UPDATE,content = "修改运动指南配置")
    public Result updateSportConfig(@RequestBody EllSportConfigDto sportConfigDto) {
        Integer count = sportConfigService.updateSportConfig(sportConfigDto);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"修改成功",count);
        }
        return new Result(HttpStatus.ERROR,"修改失败",count);
    }

    @ApiOperation(value = "查询所有运动指南配置", notes = "查询所有运动指南配置")
    @RequestMapping(value = "/selAllSportConfig", method = RequestMethod.GET)
    public Result selAllSportConfig() {
        List<EllSportConfigVo> sportConfigVoList = sportConfigService.selAllSportConfig();
        return new Result(HttpStatus.SUCCESS,"修改成功",sportConfigVoList);
    }

    /**
     * 新增运动指南配置规则
     *
     * @param sportConfigRuleDto
     * @return
     */
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "id", dataType = "string", required = false),
//            @ApiImplicitParam(name = "isDelete", value = "是否删除标识(0否,1是)", dataType = "int", required = false),
//            @ApiImplicitParam(name = "sex", value = "性别：1男；2女", dataType = "int", required = true),
//            @ApiImplicitParam(name = "comment", value = "评语(优秀，良好等)", dataType = "string", required = true),
//            @ApiImplicitParam(name = "guide", value = "运动建议", dataType = "string", required = true),
//            @ApiImplicitParam(name = "createTime", value = "创建时间", dataType = "string", required = false),
//            @ApiImplicitParam(name = "proId", value = "项目id", dataType = "int", required = true),
//            @ApiImplicitParam(name = "sportConfigId", value = "运动指南ID", dataType = "string", required = true)
//    })
    @ApiOperation(value = "新增运动指南配置规则", notes = "新增运动指南配置规则")
    @RequestMapping(value = "/insertSportConfigRule", method = RequestMethod.POST)
    @Log(title = "新增",operType = OperType.INSERT,content = "新增运动指南配置规则")
    public Result insertSportConfigRule(@RequestBody EllSportConfigRuleDto sportConfigRuleDto) {
        sportConfigRuleDto.setId(IdUtils.fastSimpleUUID());
        Integer count = sportConfigService.insertSportConfigRule(sportConfigRuleDto);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"添加成功",count);
        }
        return new Result(HttpStatus.ERROR,"添加失败",count);
    }

    /**
     * 删除运动指南配置规则
     * @param id
     * @return
     */
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "id", dataType = "string", required = true)
//    })
    @ApiOperation(value = "删除运动指南配置规则", notes = "删除运动指南配置规则")
    @RequestMapping(value = "/deleteSportConfigRule", method = RequestMethod.DELETE)
    @Log(title = "删除",operType = OperType.DELETE,content = "删除运动指南配置规则")
    public Result deleteSportConfigRule(String id) {
        Integer count = sportConfigService.deleteSportConfigRule(id);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"删除成功",count);
        }
        return new Result(HttpStatus.ERROR,"删除失败",count);
    }

    @ApiOperation(value = "修改运动指南配置规则", notes = "修改运动指南配置规则")
    @RequestMapping(value = "/updateSportConfigRule", method = RequestMethod.POST)
    @Log(title = "修改",operType = OperType.UPDATE,content = "修改运动指南配置规则")
    public Result updateSportConfigRule(@RequestBody EllSportConfigRuleDto sportConfigRuleDto) {
        Integer count = sportConfigService.updateSportConfigRule(sportConfigRuleDto);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"修改成功",count);
        }
        return new Result(HttpStatus.ERROR,"修改失败",count);
    }

    /**
     * 按条件查询运动指南配置规则
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询运动指南配置规则")
    @GetMapping(value = "/selSportConfigRule")
    public Result selSportConfigRule(SportRuleDto sportRuleDto) {
        PageHelperUtil.startPage(sportRuleDto.getPageNum(), sportRuleDto.getPageSize());
        List<EllSportConfigRuleVo> sportConfigRuleVoList = sportConfigService.selSportConfigRule(sportRuleDto);
        //将设备数据进行分页
        PageInfo<EllSportConfigRuleVo> pageInfo = new PageInfo<>(sportConfigRuleVoList);
        return new Result(HttpStatus.SUCCESS, "查询成功", pageInfo);
    }

    @ApiOperation(value = "根据id查询运动指南配置规则", notes = "根据id查询运动指南配置规则")
    @RequestMapping(value = "/selSportConfigRuleById", method = RequestMethod.GET)
    public Result selSportConfigRuleById(String id) {
        EllSportConfigRuleVo sportConfigRuleVo = sportConfigService.selSportConfigRuleById(id);
        return new Result(HttpStatus.SUCCESS,"查询成功",sportConfigRuleVo);
    }

}
