package com.eluolang.module.report;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 报表管理启动类
 *
 * @author dengrunsen
 * @createDate 2022-3-3
 */

@SpringBootApplication
@EnableScheduling
@EnableCustomSwagger2
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.eluolang.module.report","com.eluolang.common.log","com.eluolang.common.security.service.feign"})
public class ReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class,args);
    }
}
