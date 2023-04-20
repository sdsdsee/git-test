package com.eluolang.app.manage.redis;

public interface SkipAppRedisService {
    /**
     *
     *
     * @param phone 电话
     */
    boolean releaseLock(String phone);

    /**
     *
     *
     * @param phone       电话
     * @param minute      过期时间分钟
     * @param code        验证码
     * @return
     */
    boolean getLock(String phone, long minute, int code);

    /**
     * 获取验证码
     */
    String findLockCode(String phone);
}

