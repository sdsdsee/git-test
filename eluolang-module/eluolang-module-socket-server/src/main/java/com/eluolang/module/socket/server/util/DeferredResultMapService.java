package com.eluolang.module.socket.server.util;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理客户端连接通道
 * @author renzhixing
 */
public class DeferredResultMapService {

    private static Map<String, DeferredResult> deferredResultMap = new ConcurrentHashMap<String, DeferredResult>();

    public static void addDeferredResult(String key, DeferredResult deferredResult){
        deferredResultMap.put(key, deferredResult);
    }

    public static Map<String, DeferredResult> getDeferredResultMap(){
        return deferredResultMap;
    }

    public static DeferredResult getDeferredResult(String key){
        return deferredResultMap.get(key);
    }

    public static void removeDeferredResult(String key){
        deferredResultMap.remove(key);
    }

}
