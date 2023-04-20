package com.eluolang.common.log.service;

import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.common.core.web.Result;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 异步调用日志服务
 * @author ZengXiaoQian
 * @createDate 2020-9-10
 */

@Service
public class AsyncLogService {

    @Resource
    private RemoteLogService remoteLogService;

    @Resource
    private RemoteSysUserService remoteSysUserService;

    /**
     * 保存操作日志记录
     */
    //@Async
    public Result insertLog(@RequestBody OperLog operLog)
    {
        return remoteLogService.insertLog(operLog);
    }

    /**
     * 根据用户名查询用户姓名
     */
    @Async
    public Result getRealNameByName(@RequestParam("name") String name)
    {
        return remoteSysUserService.getRealNameByName(name);
    }
}
