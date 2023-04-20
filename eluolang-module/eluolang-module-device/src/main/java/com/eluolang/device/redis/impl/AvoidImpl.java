package com.eluolang.device.redis.impl;

import com.eluolang.device.redis.AvoidRedis;
import com.eluolang.device.util.CreateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AvoidImpl implements AvoidRedis {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获得锁/加锁
     *
     * @param lockId      key
     * @param millisecond 过期时间
     * @return
     */
    public boolean getLock(String lockId, long millisecond) {
        //不存在就会生锁返回ture，生成成功，存在就生成失败返回false
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockId, "avoid",
                millisecond, TimeUnit.MILLISECONDS);
        return success != null && success;
    }

    @Override
    public boolean getVideoLock(String lockId, long millis) {
        //不存在就会生锁返回ture，生成成功，存在就生成失败返回false
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockId, CreateTime.getTime(),
                millis, TimeUnit.MINUTES);
        return success != null && success ? true : false;
    }

    @Override
    public void releaseVideoLock(String lockId) {
        redisTemplate.delete(lockId);
    }

    /**
     * 释放锁
     *
     * @param lockId key
     */
    public void releaseLock(String lockId) {
        redisTemplate.delete(lockId);
    }
}
