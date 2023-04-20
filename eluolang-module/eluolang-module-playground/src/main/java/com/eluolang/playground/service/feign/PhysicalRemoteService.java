package com.eluolang.playground.service.feign;


import com.eluolang.common.core.web.Result;
import com.eluolang.playground.factory.RemotePhysicalFallBackFactory;
import com.eluolang.playground.vo.EllHistoryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;


/**
 * 远程调用physical服务的service层
 *
 * @author dengrunsen
 */
@FeignClient(value = "ELL-physical", fallbackFactory = RemotePhysicalFallBackFactory.class)
public interface PhysicalRemoteService {

    @ApiOperation("提交上传成绩")
    @PostMapping("/submitScores")
    Result submitScores(@RequestBody EllHistoryVo ellHistoryVo) throws IOException;
}
