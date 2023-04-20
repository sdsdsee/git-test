package com.eluolang.module.sys.user;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 用户管理启动类
 *
 *@author  dengrunsen
 *@createDate  2020/8/25
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCustomSwagger2
@EnableScheduling
@EnableFeignClients(basePackages = {"com.eluolang.common.security.service","com.eluolang.common.log","com.eluolang.module.sys.user"})
public class SysUserApplication {
    public static void main(String[] args){
        SpringApplication.run(SysUserApplication.class,args);
    }
}
