package com.eluolang.physical.controller;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllDevice;
import com.eluolang.common.core.pojo.EllExaminer;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.PageHelperUtil;
import com.eluolang.common.core.util.RandomNumberGenerator;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.physical.dto.EllExaminerDto;
import com.eluolang.physical.dto.ExaminerLoginDto;
import com.eluolang.physical.service.ExaminerService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "监考员控制器")
@RestController
@Transactional
public class ExaminerController {
    @Autowired
    ExaminerService examinerService;

    /**
     * 监考官通过验证码登录设备
     *
     * @return Result
     */
    @ApiOperation(value = "监考官通过验证码登录设备")
//    @Log(title = "监考官通过验证码登录设备",operType = OperType.LOGIN,content = "登录")
    @PostMapping(value = "/examinerLoginDevice")
    public Result examinerLoginDevice(@RequestBody ExaminerLoginDto examinerLoginDto) {
        EllExaminer examiner = examinerService.selExaminerByLoginCode(examinerLoginDto.getCode());
        if (examiner != null) {
            EllDevice count = examinerService.examinerLoginDevice(examinerLoginDto.getDeviceId(), examiner.getDeptId());
            if (count != null) {
                return new Result(HttpStatus.SUCCESS, "登录成功", examiner);
            }
            return new Result(HttpStatus.ERROR, "该监考员无法登录该设备", null);
        }
        return new Result(HttpStatus.ERROR, "该监考员不存在", null);
    }


    /**
     * 添加监考员
     *
     * @return Result
     */
    @ApiOperation(value = "添加监考员")
    @Log(title = "添加监考员",operType = OperType.INSERT,content = "添加监考员")
    @PostMapping(value = "/insertExaminer")
    public Result insertExaminer(@RequestBody EllExaminer ellExaminer) {
        //写入uuid
        ellExaminer.setId(IdUtils.fastSimpleUUID());
        String random;
        //循环判断生成的监考员登录码是否存在，存在则重新生成
        while (true){
            String num = RandomNumberGenerator.generateNumber();
            EllExaminer examiner = examinerService.selExaminerByLoginCode(num);
            //如果登录码不存在将值写入random，结束循环
            if (examiner == null){
                random = num;
                break;
            }
        }
        ellExaminer.setLoginCode(random);
        int count = examinerService.insertExaminer(ellExaminer);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"添加成功",count);
        }
        return new Result(HttpStatus.ERROR,"添加失败",count);
    }

    /**
     * 根据id删除监考员
     *
     * @return Result
     */
    @ApiOperation(value = "根据id删除监考员")
    @Log(title = "根据id删除监考员",operType = OperType.DELETE,content = "根据id删除监考员")
    @DeleteMapping(value = "/deleteExaminer")
    public Result deleteExaminer(String id) {
        int count = examinerService.deleteExaminer(id);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"删除成功",count);
        }
        return new Result(HttpStatus.ERROR,"删除失败",count);
    }

    /**
     * 根据id修改监考员信息
     *
     * @return Result
     */
    @ApiOperation(value = "根据id修改监考员信息")
    @Log(title = "根据id修改监考员信息",operType = OperType.UPDATE,content = "根据id修改监考员信息")
    @PostMapping(value = "/updateExaminer")
    public Result updateExaminer(@RequestBody EllExaminer ellExaminer) {
        int count = examinerService.updateExaminer(ellExaminer);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"修改成功",count);
        }
        return new Result(HttpStatus.ERROR,"修改失败",count);
    }

    /**
     * 按条件查询监考员信息
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询监考员信息")
    @GetMapping(value = "/selectExaminer")
    public Result selectExaminer(EllExaminerDto ellExaminerDto) {
        PageHelperUtil.startPage(ellExaminerDto.getPageNum(), ellExaminerDto.getPageSize());
        List<EllExaminer> ellExaminerList = examinerService.selectExaminer(ellExaminerDto);
        //将监考员数据进行分页
        PageInfo<EllExaminer> pageInfo = new PageInfo<>(ellExaminerList);
        return new Result(HttpStatus.SUCCESS,"查询成功",pageInfo);
    }
}
