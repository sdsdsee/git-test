package com.eluolang.gateway.service;

/**
 * @author suziwei
 * @date 2020/9/23
 */
public interface AuthService
{
    /**
     * 判断是否登录
     *
     * @param token
     * @return boolean
     */
    public boolean isLogin(String token);
    /**
     * 判断是否被其他人挤下线
     *
     * @param token
     * @return boolean
     */
    public boolean isSqueezed(String token);
}
