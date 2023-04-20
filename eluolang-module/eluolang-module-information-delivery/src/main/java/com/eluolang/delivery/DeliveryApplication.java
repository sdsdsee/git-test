package com.eluolang.delivery;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 信息发布微服务启动类
 * @author dengrunsen
 */
@SpringBootApplication
@EnableCustomSwagger2
@EnableFeignClients(basePackages = {"com.eluolang.delivery","com.eluolang.common.log","com.eluolang.common.security"})
@MapperScan(basePackages = "com.eluolang.delivery.mapper")
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class,args);
    }

}