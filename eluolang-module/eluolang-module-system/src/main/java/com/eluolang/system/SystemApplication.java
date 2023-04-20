package com.eluolang.system;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 设备微服务启动类
 */
@SpringBootApplication
@EnableCustomSwagger2
@EnableFeignClients(basePackages = {"com.eluolang.system","com.eluolang.common.log","com.eluolang.common.security"})
@MapperScan(basePackages = "com.eluolang.system.mapper")
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
}

