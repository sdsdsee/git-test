package com.eluolang.playground.util;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.pojo.EllWristbandData;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.SpringUtil;
import com.eluolang.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author dengrunsen
 * @date 2022年10月09日 14:36
 */
@Component
public class MqttUtil {
    @Autowired
    private RedisService redisService;

    private static RedisService redis;
    @PostConstruct
    public void init() {
        redis= redisService;
    }

    /**
     * 手环数据存入redis
     * @param result
     */
    public static void wristbandDataToRedis(String result){
        try{
            EllWristbandData ellWristbandData = JSONObject.toJavaObject(JSONObject.parseObject(result),EllWristbandData.class);
                if (!ellWristbandData.getLocation().equals("0")  || !ellWristbandData.getLocationTime().equals("0")){
                    if (ellWristbandData.getLocation().equals("528") || ellWristbandData.getLocation().equals("576")){
                    redis.setCacheObject(
                            CacheConstant.WRISTBAND_KEY+ellWristbandData.getMac()+":"+ DateUtils.timeStamp3Date(ellWristbandData.getLocationTime(),DateUtils.YYYYMMDDHHMMSS), ellWristbandData, 1, TimeUnit.DAYS);
                    }
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
