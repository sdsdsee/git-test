package com.eluolang.module.socket.server;

import com.eluolang.common.core.util.ClientIPUtils;
import com.eluolang.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 启动类
 *
 * @author dengrunsen
 */
@SpringBootApplication
@EnableCustomSwagger2
@EnableFeignClients(basePackages = {"com.eluolang.common.log", "com.eluolang.module"})
//@EnableFeignClients
@EnableDiscoveryClient
public class SocketServerApplication {

    public static void main(String[] args) throws SocketException {
        SpringApplication.run(SocketServerApplication.class,args);
        //启动服务端
//        NettyServer nettyServer = new NettyServer();
//        String ipAddress = IpAddressUtils.getIpAddress().getHostAddress();
//        if (ipAddress.indexOf("/")!=-1){
//            ipAddress = ipAddress.split("/")[1];
//        }
//        System.out.println(ClientIPUtils.getInterIP2());
//        nettyServer.start(new InetSocketAddress(ClientIPUtils.getInterIP2(),9988));
    }

//    @Test
//    public void test(){
////        Map<String, SocketChannel> map = GatewayService.getChannels();
////        System.out.println(map);
////        Iterator<String> it = map.keySet().iterator();
////        while (it.hasNext()) {
////            String key = it.next();
////            SocketChannel obj = map.get(key);
////            System.out.println("channel id is: " + key);
////            System.out.println("channel: " + obj.isActive());
////            obj.writeAndFlush("hello, it is Server test header ping");
////        }
//
//    }

}
