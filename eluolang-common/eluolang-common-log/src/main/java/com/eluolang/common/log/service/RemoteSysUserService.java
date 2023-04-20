package com.eluolang.common.log.service;

import com.eluolang.common.core.constant.ServiceNameConstants;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ZengXiaoQian
 * @createDate 2020-11-14
 */
@FeignClient(value = ServiceNameConstants.SYS_USER_MANAGE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteSysUserService {


    /**
     * 根据用户名查询用户姓名
     * @param
     * @return
     */
    @GetMapping(value = "/sysUser/manage/getRealNameByName")
    public Result getRealNameByName(@RequestParam("name") String name);
}
