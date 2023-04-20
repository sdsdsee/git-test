package com.eluolang.device.service.feign;

import com.eluolang.device.factory.RemoteTcpServerFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用tcp服务的service层
 * @author renzhixing
 */
@FeignClient(value = "ELL-socket-server",fallbackFactory = RemoteTcpServerFallBackFactory.class)
public interface TcpServerRemoteService {

}
