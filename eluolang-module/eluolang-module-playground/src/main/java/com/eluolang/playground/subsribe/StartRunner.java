//package com.eluolang.playground.subsribe;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.Map;
//
///**
// * @author dengrunsen
// * @date 2022年11月02日 16:11
// */
//@Component
//@Order(value = 1)
//public class StartRunner implements CommandLineRunner {
//
//    @Autowired
//    private MQTTSubsribe mqttSubsribe;
//
//    private static MQTTSubsribe mqtt;
//
//    @Autowired
//    private MQTTConfig mqttConfig;
//    @PostConstruct
//    public void init() {
//        mqtt= mqttSubsribe;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        Map<String,String> macList = mqttConfig.getDefault_topic();
//        for (String key:macList.keySet() ) {
//            String number = macList.get(key);
//            mqtt.init("/upload/"+number+"/wristband/data",0);
//        }
//        System.out.println(">>>服务启动第一个开始执行的任务<<<<");
//    }
//}
