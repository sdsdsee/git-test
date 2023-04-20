package com.eluolang.device;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 设备微服务启动类
 * @author dengrunsen
 */
@SpringBootApplication
@EnableCustomSwagger2
@EnableFeignClients(basePackages = {"com.eluolang.device","com.eluolang.common.log","com.eluolang.common.security"})
@MapperScan(basePackages = "com.eluolang.device.mapper")
public class DeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class,args);
    }

}