package com.eluolang.common.core.exception;

import com.eluolang.common.core.constant.Constants;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 *
 * @author suziwei
 * @date 2020/9/9
 */
@Slf4j
@RestControllerAdvice
public class MyGlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request, Exception e) {
        String eStr = String.valueOf(e);
        /** 记录报错信息 */
        log.error(eStr);
        /** 记录报错位置 */
        log.error(String.valueOf(e.getStackTrace()[0]));
        /** 判断是否为程序错误 */
        if (eStr.startsWith(Constants.PACKAGE_PREFIX)) {
            return new Result(500, e.getMessage());
        } else {
            //windows系统（线下）就是系统内部错误，linux系统线上为请先确认参数是否正确
            if (FileUploadUtil.isLinux() == false) {
                return new Result(500, "系统内部错误！");
            } else {
                return new Result(500, "请先确认参数是否正确！");
            }
        }
    }
}