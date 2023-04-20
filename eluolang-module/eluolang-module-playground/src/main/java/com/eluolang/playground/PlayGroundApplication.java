package com.eluolang.playground;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 日常监测微服务启动类
 * @author dengrunsen
 */
@SpringBootApplication
@EnableCustomSwagger2
@EnableFeignClients(basePackages = {"com.eluolang.playground","com.eluolang.common.log","com.eluolang.common.security"})
@MapperScan(basePackages = "com.eluolang.playground.mapper")
@EnableScheduling
@EnableDiscoveryClient
public class PlayGroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayGroundApplication.class,args);
    }

}