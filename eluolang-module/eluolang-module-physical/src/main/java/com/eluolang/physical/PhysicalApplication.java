package com.eluolang.physical;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients(basePackages = {"com.eluolang.physical","com.eluolang.common.log","com.eluolang.common.security"})
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
@MapperScan(basePackages = "com.eluolang.physical.mapper")
@SpringBootApplication
@EnableCustomSwagger2
@EnableScheduling
public class PhysicalApplication {
    public static void main(String[] args) {
        System.out.println("******PhysicalApplication开始启动！");
        SpringApplication.run(PhysicalApplication.class, args);
        System.out.println("******PhysicalApplication启动成功！");
    }
}
