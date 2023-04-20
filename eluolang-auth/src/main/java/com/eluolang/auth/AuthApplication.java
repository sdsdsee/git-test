package com.eluolang.auth;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 验证授权中心
 *
 * @author suziwei
 * @date 2020/9/2
 */

@Slf4j
@SpringBootApplication
@EnableCustomSwagger2
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.eluolang.common.security.service.feign","com.eluolang.auth.service.feign","com.eluolang.common.log"})
public class AuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AuthApplication.class,args);
    }
}
