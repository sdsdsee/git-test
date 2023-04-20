package com.eluolang.physical.redis.impl;

import com.eluolang.common.core.pojo.ImgUserRelevance;
import com.eluolang.physical.redis.AvoidRedis;
import com.eluolang.physical.util.CreateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AvoidImpl implements AvoidRedis {
    @Autowired
    private RedisTemplate redisTemplate;

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
    public boolean getScoreLock(String lockId, long millis) {
        //不存在就会生锁返回ture，生成成功，存在就生成失败返回false
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockId, CreateTime.getTime(),
                millis, TimeUnit.MINUTES);
        return success != null && success ? true : false;
    }

    @Override
    public void releaseScoreLock(String lockId) {
        redisTemplate.delete(lockId);
    }

    @Override
    public void saveFaceInfo(String orgId, List<ImgUserRelevance> imgUserRelevanceList) {
        redisTemplate.opsForList().rightPushAll("face:" + orgId, imgUserRelevanceList);
        redisTemplate.expire(orgId, 20, TimeUnit.SECONDS);
    }

    @Override
    public List<ImgUserRelevance> getFaceInfo(String orgId) {
        if (redisTemplate.hasKey("face:" + orgId)) {
            //获取所有
            return redisTemplate.opsForList().range("face:" + orgId, 0, -1);
        }
        return null;
    }

    @Override
    public void deleteKey(List<String> key) {
        redisTemplate.delete(key);
    }

    @Override
    public List<String> getKeys(String key) {
        return new ArrayList<String>(redisTemplate.keys(key + "*"));
    }

    @Override
    public void addTestNum(int orgId) {
        //查询人数
        int num = 0;
        if (redisTemplate.opsForValue().get("testNumber:" + orgId) != null) {
            num = (Integer) redisTemplate.opsForValue().get("testNumber:" + orgId);
        }
        redisTemplate.opsForValue().set("testNumber:" + orgId, num + 1, 60, TimeUnit.MINUTES);
    }

    @Override
    public void reduceTestNum(int orgId) {
        //查询人数
        int num = 0;
        if (redisTemplate.opsForValue().get("testNumber:" + orgId) != null) {
            num = (Integer) redisTemplate.opsForValue().get("testNumber:" + orgId);
        }
        if (num > 0) {
            redisTemplate.opsForValue().set("testNumber:" + orgId, num - 1, 60, TimeUnit.MINUTES);
        }
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
