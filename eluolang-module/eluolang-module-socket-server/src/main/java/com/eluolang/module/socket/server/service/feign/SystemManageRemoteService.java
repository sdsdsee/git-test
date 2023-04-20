package com.eluolang.module.socket.server.service.feign;

import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.factory.RemoteSystemManageFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 调用系统管理微服务的service层
 *
 * @author renzhixing
 */
@FeignClient(value = "ELL-system", contextId = "socket-server-system", fallbackFactory = RemoteSystemManageFallbackFactory.class)
public interface SystemManageRemoteService {

//    /**
//     * 保存操作日志
//     *
//     * @param operLog
//     * @return
//     */
//    @RequestMapping(value = "/system/insertLog", method = RequestMethod.POST)
//    Result insertLog(@RequestBody OperLog operLog);

    /**
     * 获取指令标识ident
     *
     * @return
     */
    @RequestMapping(value = "/system/getIdent", method = RequestMethod.POST)
    Result getIdent();


}
