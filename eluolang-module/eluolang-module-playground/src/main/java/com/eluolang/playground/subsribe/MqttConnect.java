//package com.eluolang.playground.subsribe;
//
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
///**
// * 创建MqttConnectOptions连接对象
// */
//@Component
//public class MqttConnect {
//
//    @Autowired
//    private MQTTConfig config;
//
//    public MqttConnect(MQTTConfig config) {
//        this.config = config;
//    }
//    //生成配置对象，用户名，密码等
//    public MqttConnectOptions getOptions() {
//        MqttConnectOptions options = new MqttConnectOptions();
//        /**
//         * 如果设置为true，那么当客户端离线后服务端就会清理对应客户端的session，当客户端再次上线后，服务端不会给它发送离线后的消息了
//         */
//        options.setCleanSession(config.isCleansession());
//        options.setUserName(config.getUsername());
//        options.setPassword(config.getPassword().toCharArray());
//        options.setConnectionTimeout(config.getConnectionTimeout());
//        //设置心跳
//        options.setKeepAliveInterval(config.getKeepalive());
//        return options;
//    }
//
//    public MqttConnectOptions getOptions(MqttConnectOptions options) {
//
//        options.setCleanSession(options.isCleanSession());
//        options.setUserName(options.getUserName());
//        options.setPassword(options.getPassword());
//        options.setConnectionTimeout(options.getConnectionTimeout());
//        options.setKeepAliveInterval(options.getKeepAliveInterval());
//        return options;
//    }
//}
