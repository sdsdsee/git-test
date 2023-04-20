package com.eluolang.device.redis;

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
     * 拉取视频获得锁/加锁
     *
     * @param lockId      key
     * @param millis 过期时间
     * @return
     */
    boolean getVideoLock(String lockId, long millis);
    /**
     * 拉去视频释放锁
     *
     * @param lockId key
     */
    void releaseVideoLock(String lockId);

}
