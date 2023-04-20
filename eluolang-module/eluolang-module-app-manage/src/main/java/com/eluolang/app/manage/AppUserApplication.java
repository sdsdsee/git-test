package com.eluolang.app.manage;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 小程序微服务启动类
 * @author dengrunsen
 */
@SpringBootApplication
@EnableCustomSwagger2
@EnableFeignClients(basePackages = {"com.eluolang.app.manage","com.eluolang.common.log","com.eluolang.common.security"})
@MapperScan(basePackages = "com.eluolang.app.manage.mapper")
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
public class AppUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppUserApplication.class,args);
    }

}