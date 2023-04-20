package com.eluolang.common.core.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 打印日志
 *
 * @author suziwei
 * @date 2020/9/10
 */
@Slf4j
public class LogUtils {
    public static void printLog(Exception e){
        log.error(String.valueOf(e.getMessage()));
        log.error(String.valueOf(e.getStackTrace()[0]));
    }
}
