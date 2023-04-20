package com.eluolang.platform;

import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import com.eluolang.platform.util.VoiceUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

@EnableCustomSwagger2
@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = {"com.eluolang.platform", "com.eluolang.common.log", "com.eluolang.common.security"})
@MapperScan(basePackages = "com.eluolang.platform.mapper")
public class PlatformApplication {
    public static void main(String[] args) throws LineUnavailableException, IOException, InterruptedException {
        SpringApplication.run(PlatformApplication.class, args);
  /*      System.out.println("启动成功!");
        System.out.println("启动udp服务");
        VoiceUtil.sss();*/
    }
}
