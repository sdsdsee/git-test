package com.eluolang.device.service.feign;

import com.eluolang.common.core.pojo.EllElectricQuantity;
import com.eluolang.common.core.web.Result;
import com.eluolang.device.factory.RemotePlatformFallBackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 远程调用platform服务的service层
 *
 * @author dengrunsen
 */
@FeignClient(value = "ELL-platform", fallbackFactory = RemotePlatformFallBackFactory.class)
public interface PlatformRemoteService {
    /**
     * 添加电量
     *
     * @param ellElectricQuantity
     * @return
     */
    @ApiOperation(value = "添加电量", notes = "添加电量")
    @RequestMapping(value = "/addElectricity", method = RequestMethod.POST)
    Result addElectricity(@RequestBody EllElectricQuantity ellElectricQuantity);

}
