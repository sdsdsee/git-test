package com.eluolang.app.manage.redis.impl;

import com.eluolang.app.manage.redis.SkipAppRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SkipAppRedisServiceImpl implements SkipAppRedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获得锁/加锁
     *
     * @param phone  电话号
     * @param minute 过期时间分钟
     * @return
     */
    public boolean getLock(String phone, long minute, int code) {
        //不存在就会生锁返回ture，生成成功，存在就生成失败返回false
        Boolean success = redisTemplate.opsForValue().setIfAbsent("lock:" + phone, String.valueOf(code),
                1, TimeUnit.MINUTES);
        //判断获取锁/存在就不能更改code
        if (success != null && success) {
            redisTemplate.opsForValue().set(phone, String.valueOf(code), 5, TimeUnit.MINUTES);
        }
        return success != null && success;
    }

    @Override
    public String findLockCode(String phone) {
        return redisTemplate.opsForValue().get(phone);
    }

    /**
     * 释放锁
     *
     * @param lockId key
     */
    public boolean releaseLock(String lockId) {

        return redisTemplate.delete(lockId);
    }
}
