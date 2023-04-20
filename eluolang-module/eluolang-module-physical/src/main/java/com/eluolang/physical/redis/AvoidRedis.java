package com.eluolang.physical.redis;

import com.eluolang.common.core.pojo.ImgUserRelevance;

import java.util.List;

public interface AvoidRedis {
    /**
     * 释放锁
     *
     * @param lockId key
     */
    void releaseLock(String lockId);

    /**
     * 获得锁/加锁
     *
     * @param lockId      key
     * @param millisecond 过期时间
     * @return
     */
    boolean getLock(String lockId, long millis);

    /**
     * 计算分数获得锁/加锁
     *
     * @param lockId key
     * @param millis 过期时间
     * @return
     */
    boolean getScoreLock(String lockId, long millis);

    /**
     * 计算分数释放锁
     *
     * @param lockId key
     */
    void releaseScoreLock(String lockId);

    /**
     * 存储人脸数据
     */
    void saveFaceInfo(String orgId, List<ImgUserRelevance> imgUserRelevanceList);

    /**
     * 获取人脸数据
     */
    List<ImgUserRelevance> getFaceInfo(String orgId);

    /**
     * 删除keys
     */
    void deleteKey(List<String> key);

    /**
     * 模糊查询keys
     */
    List<String> getKeys(String key);

    /**
     * 增加正在测试人数
     */
    void addTestNum(int orgId);
    /**
     * 减少正在测试的人数
     */
    void reduceTestNum(int orgId);
}
