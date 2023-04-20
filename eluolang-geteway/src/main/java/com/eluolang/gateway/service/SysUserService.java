package com.eluolang.gateway.service;

/**
 * @author ZengXiaoQian
 * @createDate 2020-11-10
 */
public interface SysUserService {
    /**
     * 从redis中取出删除用户的token
     * @param token
     * @return
     */
    public boolean isSameDeleteToken(String token);

    /**
     * 从redis中取出禁用用户的token
     * @param token
     * @return
     */
    public boolean isSameDisabledToken(String token);
}
