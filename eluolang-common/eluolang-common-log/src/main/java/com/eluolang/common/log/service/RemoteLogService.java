package com.eluolang.common.log.service;

import com.eluolang.common.core.constant.ServiceNameConstants;
import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 日志服务
 * @author ZengXiaoQian
 * @createDate 2020-9-10
 */

@FeignClient(value = ServiceNameConstants.SYSTEM, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {
    /**
     * 保存操作日志
     * @param operLog 日志实体
     * @return 结果
     */
    @RequestMapping(value = "system/insertLog",method = RequestMethod.POST)
    public Result insertLog(@RequestBody OperLog operLog);
    
}
