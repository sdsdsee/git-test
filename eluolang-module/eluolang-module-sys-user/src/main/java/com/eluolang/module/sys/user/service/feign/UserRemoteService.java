package com.eluolang.module.sys.user.service.feign;

import com.eluolang.common.core.web.Result;
import com.eluolang.module.sys.user.factory.RemoteUserFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 调用认证微服务的service层
 *
 * @author ZengXiaoQian
 * @createDate 2020-9-28
 */
@FeignClient(value = "ELL-auth",fallbackFactory = RemoteUserFallBackFactory.class)
public interface UserRemoteService {
    /**
     * 用户退出登录
     * @param
     * @return
     */
    @GetMapping("/logout")
    public Result logout(@RequestParam(value = "Authorization") HttpServletRequest request);
}
