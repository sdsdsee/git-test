package com.eluolang.auth.controller;

import com.eluolang.auth.form.LoginBody;
import com.eluolang.auth.service.UserService;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.LoginUser;
import com.eluolang.common.core.util.RandImageUtil;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.common.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token控制器
 *
 * @author suziwei
 * @date 2020/9/2
 */
@Api(tags = "用户验证")
@Slf4j
@RestController
public class TokenController {

    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;


    @Log(title = "登录模块",operType = OperType.LOGIN,content = "登录")
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginBody loginBody) {
        LoginUser loginUser = userService.login(loginBody.getUsername(), loginBody.getPassword());
        return new Result(HttpStatus.SUCCESS, "登录成功！",loginUser);
    }

    @ApiOperation(value = "退出")
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        userService.logout(request);
//        log.debug("用户："+request.getHeader(Constants.LOGIN_USER)+"已退出！");
        return new Result(HttpStatus.SUCCESS, "退出成功！", null);
    }
//
//    @ApiOperation(value = "生成验证码")
//    @PostMapping("/randomImage")
//    public Result randomImage(HttpServletResponse response,@RequestBody CaptchaVO data){
//        try {
//            ResponseModel m=captchaService.get(data);
//            return new Result(HttpStatus.SUCCESS, "生成验证码成功",m);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(HttpStatus.ERROR, "验证失败"+e.getMessage(),null);
//        }
//    }
//
//    @ApiOperation(value = "验证码验证")
//    @PostMapping("/checkImage")
//    public Result checkImage(HttpServletResponse response,@RequestBody CaptchaVO data){
//        try {
//            ResponseModel m=captchaService.check(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(HttpStatus.ERROR, "验证失败"+e.getMessage(),null);
//        }
//        return new Result(HttpStatus.SUCCESS, "验证成功",null);
//    }

    @ApiOperation(value = "刷新Token")
    @GetMapping("/refresh")
    public Result refresh(HttpServletRequest request) {
        userService.refresh(request);
        return new Result(HttpStatus.SUCCESS, "刷新成功！", null);
    }

    /**
     * 需具备查询住户列表的权限
     */
    @ApiOperation(value = "测试")
    @GetMapping("/test")
    @PreAuthorize(hasPermi = "access:rke:list")
    public Result test() {
        return new Result(HttpStatus.SUCCESS, "通过鉴权！", null);
    }

    /**
     * 测试超时
     */
    @ApiOperation(value = "测试1")
    @GetMapping("/test1")
    public Result test1() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return new Result(HttpStatus.SUCCESS, "！", null);
    }

    /**
     * 测试日志收集
     */
    @ApiOperation(value = "测试2")
    @GetMapping("/test2")
    public Result test2() {
//        log.info("info123");
//        log.error("error123");
//        log.warn("warn123");
        return new Result(HttpStatus.SUCCESS, "！", null);
    }
}
