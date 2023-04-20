package com.eluolang.gateway.service;


import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.pojo.LoginUser;
import com.eluolang.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author suziwei
 * @date 2020/9/24
 */
@Service
public class AuthServiceImpl implements AuthService
{
    @Autowired
    private RedisService redisService;

    @Override
    public boolean isLogin(String token) {
        Integer userId= redisService.getCacheObject(CacheConstant.LOGIN_TOKEN_KEY+token);
        if (userId==null){
            return true;
        }
        return false;
    }

    @Override
    public boolean isSqueezed(String token) {
        Integer userId= redisService.getCacheObject(CacheConstant.LOGIN_TOKEN_KEY+token);
        LoginUser user=redisService.getCacheObject(CacheConstant.LOGIN_ID_KEY+userId);
        if (token.equals(user.getToken())){
            return false;
        }
        return true;
    }
}
